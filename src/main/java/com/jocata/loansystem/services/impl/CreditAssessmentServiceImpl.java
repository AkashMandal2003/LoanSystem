package com.jocata.loansystem.services.impl;

import com.jocata.loansystem.dao.*;
import com.jocata.loansystem.entities.*;
import com.jocata.loansystem.forms.*;
import com.jocata.loansystem.services.CreditAssessmentService;
import com.jocata.loansystem.utils.LoanStatus;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Service
public class CreditAssessmentServiceImpl implements CreditAssessmentService {

    private final CreditScoreDao creditScoreDao;
    private final CustomerDao customerDao;
    private final LoanProductDao loanProductDao;
    private final LoanApplicationDao loanApplicationDao;
    private final RiskAssessmentDao riskAssessmentDao;
    private final RestTemplate restTemplate;

    public CreditAssessmentServiceImpl(CreditScoreDao creditScoreDao, CustomerDao customerDao, LoanProductDao loanProductDao, LoanApplicationDao loanApplicationDao, RiskAssessmentDao riskAssessmentDao, RestTemplate restTemplate) {
        this.creditScoreDao = creditScoreDao;
        this.customerDao = customerDao;
        this.loanProductDao = loanProductDao;
        this.loanApplicationDao = loanApplicationDao;
        this.riskAssessmentDao = riskAssessmentDao;
        this.restTemplate = restTemplate;
    }

    private static final String CIBIL_SERVICE_URL = "http://localhost:9090/externalservices/api/v1/addCibilDetails";

    @Override
    public String getCreditAssessmentDetails(CreditAssessmentForm creditAssessmentForm) {

        if (isInvalidInput(creditAssessmentForm)) {
            return "Invalid Credentials";
        }

        CustomerDetails daoCustomer = customerDao.getCustomer(creditAssessmentForm.getPanNumber());
        CreditScoreDetails customerCreditScore = creditScoreDao.getCustomerFromCreditScore(daoCustomer.getCustomerId());
        LoanProductDetails productByTenure = loanProductDao.getProductByTenure(Integer.parseInt(creditAssessmentForm.getTenureInMonths()));

        BigDecimal outstandingAmount = customerCreditScore.getTotalOutstandingBalance();
        double customerEligibleAmount = Double.parseDouble(creditAssessmentForm.getMonthlyIncome()) * 0.75;
        if (outstandingAmount.doubleValue() > customerEligibleAmount) return "Not Eligible for loan";

        if ((outstandingAmount.doubleValue() < customerEligibleAmount) &&
                Integer.parseInt(customerCreditScore.getRecentCreditInquiries()) < 15 &&
                customerCreditScore.getScore() >= 750) {
            double availableEmi = customerEligibleAmount - outstandingAmount.doubleValue();

            BigDecimal interestRate = productByTenure.getInterestRate();
            Integer termMonths = productByTenure.getTermMonths();

            double monthlyInterestRate = interestRate.doubleValue() / 12 / 100;
            double powFactor = Math.pow(1 + monthlyInterestRate, termMonths);
            double principalAmount = ((availableEmi * (powFactor - 1)) / (monthlyInterestRate * powFactor));

            return checkLoanSlabEligibility( availableEmi, principalAmount, creditAssessmentForm);
        }
        return "Not Eligible for loan, check credit scores";
    }

    private String checkLoanSlabEligibility(double availableEmi, double principalAmount, CreditAssessmentForm creditAssessmentForm) {

        CustomerDetails daoCustomer = customerDao.getCustomer(creditAssessmentForm.getPanNumber());
        CreditScoreDetails customerCreditScore = creditScoreDao.getCustomerFromCreditScore(daoCustomer.getCustomerId());

        List<LoanProductDetails> allLoanProducts = loanProductDao.getAllLoanProduct();

        double requestedAmountDouble = Double.parseDouble(creditAssessmentForm.getRequiredAmount());

        for (LoanProductDetails loanProduct : allLoanProducts) {
            BigDecimal minAmount = loanProduct.getMinAmount();
            BigDecimal maxAmount = loanProduct.getMaxAmount();

            if (principalAmount >= minAmount.doubleValue() && principalAmount <= maxAmount.doubleValue()) {
                if (requestedAmountDouble >= minAmount.doubleValue() && requestedAmountDouble <= maxAmount.doubleValue()) {

                    LoanApplicationDetails loanApplication = loanApplicationDao.getLatestLoanApplicationByCustomerId(daoCustomer.getCustomerId());
                    loanApplication.setStatus(String.valueOf(LoanStatus.APPROVED));
                    loanApplication.setProductId(loanProduct);
                    loanApplication.setLoanAmount(BigDecimal.valueOf(requestedAmountDouble));
                    loanApplication.setRequestedTerm(loanProduct.getTermMonths());
                    loanApplication.setLoanPurpose("Required for Self");

                    LoanApplicationDetails updatedLoanApplication = loanApplicationDao.updateLoanApplication(loanApplication);

                    RiskAssessmentDetails riskAssessmentDetails = new RiskAssessmentDetails();
                    riskAssessmentDetails.setAssessmentDate(new Date(System.currentTimeMillis()));
                    riskAssessmentDetails.setCreditScore(customerCreditScore.getScore());
                    riskAssessmentDetails.setApprovedAmount(BigDecimal.valueOf(requestedAmountDouble));
                    riskAssessmentDetails.setApprovedTerm(loanProduct.getTermMonths());
                    riskAssessmentDetails.setApprovedStatus("VERIFIED");
                    riskAssessmentDetails.setIncome(new BigDecimal(creditAssessmentForm.getMonthlyIncome()));
                    riskAssessmentDetails.setApplication(updatedLoanApplication);

                    RiskAssessmentDetails saveRiskAssessmentDetails = riskAssessmentDao.saveRiskAssessmentDetails(riskAssessmentDetails);

                    CibilRequestForm cibilRequestForm=new CibilRequestForm();
                    cibilRequestForm.setPan(daoCustomer.getIdentityNumber());
                    cibilRequestForm.setCreditLimit(String.valueOf(customerCreditScore.getCreditLimit()));
                    cibilRequestForm.setCreditHistory(customerCreditScore.getCreditHistory());
                    cibilRequestForm.setPaymentHistory(customerCreditScore.getPaymentHistory());
                    cibilRequestForm.setRecentCreditInquiries(customerCreditScore.getRecentCreditInquiries());
                    cibilRequestForm.setCreditScore(String.valueOf(saveRiskAssessmentDetails.getCreditScore()));
                    cibilRequestForm.setReportDate(String.valueOf(new Date(System.currentTimeMillis())));
                    cibilRequestForm.setStatus(customerCreditScore.getStatus());
                    double currOutStandingBalance=availableEmi+customerCreditScore.getTotalOutstandingBalance().doubleValue();
                    cibilRequestForm.setTotalOutstandingBalance(String.valueOf(currOutStandingBalance));

                    addCibilDetails(cibilRequestForm);

                    return "You are eligible for the " + loanProduct.getProductName() + " loan slab. And Loan Application Updated Successfully";
                }
                return suggestLoanSlab(principalAmount, requestedAmountDouble, minAmount, maxAmount, loanProduct);
            }
        }

        return "Not eligible for any loan slab.";
    }

    private String suggestLoanSlab(double principalAmount, double requestedAmount, BigDecimal minAmount, BigDecimal maxAmount, LoanProductDetails loanProduct) {
        if (requestedAmount < minAmount.doubleValue()) {
            LoanProductDetails nextSlab = getNextHigherSlab(loanProduct);
            return "Please check the " + nextSlab.getProductName() + " slab for higher eligibility.";
        }

        if (requestedAmount > maxAmount.doubleValue()) {
            LoanProductDetails nextSlab = getNextLowerSlab(loanProduct);
            return "Please check the " + nextSlab.getProductName() + " slab for lower eligibility.";
        }

        if (requestedAmount > principalAmount) {
            return "Please recheck requested amount exceeds your eligible principal.";
        }

        return "Not eligible for requested loan slab.";
    }

    private LoanProductDetails getNextHigherSlab(LoanProductDetails currentSlab) {
        if (currentSlab.getProductId() == 1) {
            return loanProductDao.getProduct(2);
        } else if (currentSlab.getProductId() == 2) {
            return loanProductDao.getProduct(3);
        }
        return currentSlab;
    }

    private LoanProductDetails getNextLowerSlab(LoanProductDetails currentSlab) {
        if (currentSlab.getProductId() == 3) {
            return loanProductDao.getProduct(2);
        } else if (currentSlab.getProductId() == 2) {
            return loanProductDao.getProduct(1);
        }
        return currentSlab;
    }

    private boolean isInvalidInput(CreditAssessmentForm form) {
        return form == null || form.getPanNumber() == null || form.getMonthlyIncome() == null || form.getTenureInMonths() == null;
    }

    private CibilResponse addCibilDetails(CibilRequestForm cibilRequestForm) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CibilRequestForm> requestHttpEntity = new HttpEntity<>(cibilRequestForm, headers);

        ResponseEntity<ExternalServiceResponse<CibilResponse>> responseEntity = restTemplate.exchange(CIBIL_SERVICE_URL, HttpMethod.POST, requestHttpEntity, new ParameterizedTypeReference<>() {
        });

        ExternalServiceResponse<CibilResponse> body = responseEntity.getBody();
        if (body == null || body.getData() == null) {
            throw new IllegalArgumentException("Pan data not found for Cibil Details: " + cibilRequestForm.getPan());
        }
        return body.getData();
    }

}
