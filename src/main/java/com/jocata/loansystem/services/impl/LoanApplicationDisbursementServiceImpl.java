package com.jocata.loansystem.services.impl;

import com.jocata.loansystem.dao.*;
import com.jocata.loansystem.entities.*;
import com.jocata.loansystem.forms.LoanDisbursementForm;
import com.jocata.loansystem.services.LoanApplicationDisbursementService;
import com.jocata.loansystem.utils.LoanDisbursedMethod;
import com.jocata.loansystem.utils.LoanStatus;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Service
public class LoanApplicationDisbursementServiceImpl implements LoanApplicationDisbursementService {

    private final RiskAssessmentDao riskAssessmentDao;
    private final LoanApplicationDao loanApplicationDao;
    private final LoanProductDao loanProductDao;
    private final LoanDetailsDao loanDetailsDao;
    private final LoanApplicationDisbursementDao loanApplicationDisbursementDao;

    public LoanApplicationDisbursementServiceImpl(RiskAssessmentDao riskAssessmentDao, LoanApplicationDao loanApplicationDao, LoanProductDao loanProductDao, LoanDetailsDao loanDetailsDao, LoanApplicationDisbursementDao loanApplicationDisbursementDao) {
        this.riskAssessmentDao = riskAssessmentDao;
        this.loanApplicationDao = loanApplicationDao;
        this.loanProductDao = loanProductDao;
        this.loanDetailsDao = loanDetailsDao;
        this.loanApplicationDisbursementDao = loanApplicationDisbursementDao;
    }

    @Override
    public LoanDisbursementForm createLoanDisbursement(String loanApplicationId) {
        LoanApplicationDetails loanApplicationDetails = loanApplicationDao.getLoanApplicationDetails(Integer.valueOf(loanApplicationId));
        RiskAssessmentDetails riskAssessmentDetails= riskAssessmentDao.getRiskAssessmentByApplicationId(Integer.valueOf(loanApplicationId));
        LoanProductDetails product = loanProductDao.getProduct(loanApplicationDetails.getProductId().getProductId());

        LoanDetails loanDetails=new LoanDetails();
        loanDetails.setLoanApplication(loanApplicationDetails);
        loanDetails.setDisbursementDate(new Date(System.currentTimeMillis()));
        loanDetails.setLoanAmount(riskAssessmentDetails.getApprovedAmount());
        loanDetails.setInterestRate(product.getInterestRate());
        loanDetails.setLoanTermMonths(riskAssessmentDetails.getApprovedTerm());
        loanDetails.setLoanBalance(riskAssessmentDetails.getApprovedAmount());
        loanDetails.setStatus(String.valueOf(LoanStatus.DISBURSED));

        LoanDetails savedLoanDetails = loanDetailsDao.createLoanDetails(loanDetails);

        LoanDisbursementDetails loanDisbursementDetails=new LoanDisbursementDetails();
        loanDisbursementDetails.setLoan(savedLoanDetails);
        loanDisbursementDetails.setDisbursementDate(new Date(System.currentTimeMillis()));
        loanDisbursementDetails.setDisbursedAmount(savedLoanDetails.getLoanAmount());
        loanDisbursementDetails.setDisbursementMethod(String.valueOf(LoanDisbursedMethod.NEFT));

        LoanDisbursementDetails loanDisbursement = loanApplicationDisbursementDao.createLoanDisbursement(loanDisbursementDetails);


        LoanDisbursementForm loanDisbursementForm=new LoanDisbursementForm();
        loanDisbursementForm.setLoanId(String.valueOf(loanDisbursement.getLoan().getLoanId()));
        loanDisbursementForm.setDisbursementMethod(loanDisbursement.getDisbursementMethod());
        loanDisbursementForm.setDisbursementDate(String.valueOf(loanDisbursement.getDisbursementDate()));
        loanDisbursementForm.setDisbursementAmount(String.valueOf(loanDisbursement.getDisbursedAmount()));

        return loanDisbursementForm;
    }
}
