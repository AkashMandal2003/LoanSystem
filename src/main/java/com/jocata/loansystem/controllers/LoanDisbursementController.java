package com.jocata.loansystem.controllers;

import com.jocata.loansystem.forms.LoanDisbursementForm;
import com.jocata.loansystem.services.LoanApplicationDisbursementService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/disbursement")
public class LoanDisbursementController {

    private final LoanApplicationDisbursementService loanApplicationDisbursementService;

    public LoanDisbursementController(LoanApplicationDisbursementService loanApplicationDisbursementService) {
        this.loanApplicationDisbursementService = loanApplicationDisbursementService;
    }

    @PostMapping("/{loanApplicationId}")
    public LoanDisbursementForm createLoanDisbursement(@PathVariable String loanApplicationId){
        return loanApplicationDisbursementService.createLoanDisbursement(loanApplicationId);
    }

}
