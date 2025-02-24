package com.jocata.loansystem.services;

import com.jocata.loansystem.forms.RePaymentScheduleForm;

import java.util.List;

public interface RePaymentService {

    List<RePaymentScheduleForm> showRepayMentDetails(String loanApplicationId);

}
