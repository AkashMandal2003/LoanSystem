package com.jocata.loansystem.services;

import com.jocata.loansystem.dao.LoanDetailsDao;
import com.jocata.loansystem.dao.LoanPaymentDao;
import com.jocata.loansystem.entities.LoanApplicationDetails;
import com.jocata.loansystem.entities.LoanDetails;
import com.jocata.loansystem.entities.LoanPaymentDetails;
import com.jocata.loansystem.forms.LoanPaymentForm;
import com.jocata.loansystem.forms.RePaymentScheduleForm;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class LoanPaymentServiceImpl implements LoanPaymentService{

    private final LoanDetailsDao loanDetailsDao;
    private final RePaymentService rePaymentService;
    private final LoanPaymentDao loanPaymentDao;

    public LoanPaymentServiceImpl(LoanDetailsDao loanDetailsDao, RePaymentService rePaymentService, LoanPaymentDao loanPaymentDao) {
        this.loanDetailsDao = loanDetailsDao;
        this.rePaymentService = rePaymentService;
        this.loanPaymentDao = loanPaymentDao;
    }

    public LoanPaymentForm createLoanPayment(String loanId) {

        LoanDetails loanDetails = loanDetailsDao.getLoanDetails(Integer.valueOf(loanId));
        LoanApplicationDetails loanApplicationDetails = loanDetailsDao.getLoanApplicationDetailsByLoanId(Integer.valueOf(loanId));
        List<RePaymentScheduleForm> rePaymentScheduleForms = rePaymentService.showRepayMentDetails(String.valueOf(loanApplicationDetails.getApplicationId()));

        LoanPaymentDetails lastPayment = loanPaymentDao.getLastPaymentByLoanId(Integer.valueOf(loanId));
        Date paymentDate = Date.valueOf((lastPayment != null) ? lastPayment.getPaymentDate().toLocalDate().plusDays(30) : LocalDate.now());

        for (RePaymentScheduleForm schedule : rePaymentScheduleForms) {
            if (!loanPaymentDao.existsByLoanIdAndPaymentDate(Integer.valueOf(loanId), paymentDate)) {
                LoanPaymentDetails loanPaymentDetails = new LoanPaymentDetails();
                loanPaymentDetails.setLoan(loanDetails);
                loanPaymentDetails.setPaymentAmount(new BigDecimal(schedule.getPrinciple()));
                loanPaymentDetails.setRemainingBalance(new BigDecimal(schedule.getBalance()));
                loanPaymentDetails.setPaymentDate(paymentDate);
                loanPaymentDetails.setPaymentMethod("Bank Transfer");

                loanPaymentDao.createLoanPayment(loanPaymentDetails);
            }
            paymentDate = Date.valueOf(paymentDate.toLocalDate().plusDays(30));
        }

        return null;
    }

}
