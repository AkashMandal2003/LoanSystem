package com.jocata.loansystem.controllers;

import com.jocata.loansystem.forms.LoanPaymentForm;
import com.jocata.loansystem.services.LoanPaymentService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/loan-payment")
public class LoanPaymentController {

    private final LoanPaymentService loanPaymentService;

    public LoanPaymentController(LoanPaymentService loanPaymentService) {
        this.loanPaymentService = loanPaymentService;
    }

    @PostMapping("/{loanId}")
    public LoanPaymentForm createLoanPayment(@PathVariable String loanId){
        return loanPaymentService.createLoanPayment(loanId);
    }
}
