package com.jocata.loansystem.services.impl;

import com.jocata.loansystem.dao.*;
import com.jocata.loansystem.entities.*;
import com.jocata.loansystem.forms.CreditAssessmentForm;
import com.jocata.loansystem.services.CreditAssessmentService;
import com.jocata.loansystem.utils.LoanStatus;
import org.springframework.stereotype.Service;

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

    public CreditAssessmentServiceImpl(CreditScoreDao creditScoreDao, CustomerDao customerDao, LoanProductDao loanProductDao, LoanApplicationDao loanApplicationDao, RiskAssessmentDao riskAssessmentDao) {
        this.creditScoreDao = creditScoreDao;
        this.customerDao = customerDao;
        this.loanProductDao = loanProductDao;
        this.loanApplicationDao = loanApplicationDao;
        this.riskAssessmentDao = riskAssessmentDao;
    }

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

            return checkLoanSlabEligibility(principalAmount, creditAssessmentForm);
        }
        return "Not Eligible for loan, check credit scores";
    }

    private String checkLoanSlabEligibility(double principalAmount, CreditAssessmentForm creditAssessmentForm) {

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

                    riskAssessmentDao.saveRiskAssessmentDetails(riskAssessmentDetails);

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
}
