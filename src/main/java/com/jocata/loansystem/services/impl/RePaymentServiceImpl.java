package com.jocata.loansystem.services.impl;

import com.jocata.loansystem.dao.LoanApplicationDao;
import com.jocata.loansystem.entities.LoanApplicationDetails;
import com.jocata.loansystem.forms.RePaymentScheduleForm;
import com.jocata.loansystem.services.RePaymentService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class RePaymentServiceImpl implements RePaymentService {

    private final LoanApplicationDao loanApplicationDao;

    public RePaymentServiceImpl(LoanApplicationDao loanApplicationDao) {
        this.loanApplicationDao = loanApplicationDao;
    }

    @Override
    public List<RePaymentScheduleForm> showRepayMentDetails(String applicationId){
        LoanApplicationDetails loanApplicationDetails = loanApplicationDao.getLoanApplicationDetails(Integer.valueOf(applicationId));

        BigDecimal principal=loanApplicationDetails.getLoanAmount();
        BigDecimal annualInterestRate= loanApplicationDetails.getProductId().getInterestRate();
        Integer tenureMonths=loanApplicationDetails.getRequestedTerm();

        BigDecimal monthlyInterestRate= annualInterestRate.divide(BigDecimal.valueOf(12 * 100), 10, RoundingMode.HALF_UP);
        BigDecimal onePlusRPowerN = BigDecimal.ONE.add(monthlyInterestRate).pow(tenureMonths);
        BigDecimal numerator = principal.multiply(monthlyInterestRate).multiply(onePlusRPowerN);
        BigDecimal denominator = onePlusRPowerN.subtract(BigDecimal.ONE);
        BigDecimal emi = numerator.divide(denominator, 2, RoundingMode.HALF_UP);

        List<RePaymentScheduleForm> repaymentSchedule=new ArrayList<>();
        BigDecimal remainingBalance=principal;

        for(int month=1;month<=tenureMonths;month++){
            BigDecimal interestComponent=remainingBalance.multiply(monthlyInterestRate).setScale(0,RoundingMode.HALF_UP);
            BigDecimal principalComponent=emi.subtract(interestComponent);

            String balance = (remainingBalance.compareTo(emi) < 0) ? "0" : remainingBalance.setScale(0, RoundingMode.HALF_UP).toString();

            remainingBalance = remainingBalance.subtract(principalComponent).setScale(0, RoundingMode.HALF_UP);

            RePaymentScheduleForm repayment = new RePaymentScheduleForm();
            repayment.setRePaymentId(String.valueOf(month));
            repayment.setBalance(balance);
            repayment.setPrinciple(principalComponent.setScale(0, RoundingMode.HALF_UP).toString());
            repayment.setEmi(emi.setScale(0, RoundingMode.HALF_UP).toString());
            repayment.setInterestRate(interestComponent.toString());
            repaymentSchedule.add(repayment);
        }
        return repaymentSchedule;

    }
}
