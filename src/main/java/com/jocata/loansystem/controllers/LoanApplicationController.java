package com.jocata.loansystem.controllers;

import com.jocata.loansystem.forms.LoanApplicationRequestForm;
import com.jocata.loansystem.services.LoanApplicationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/loan-app")
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;

    public LoanApplicationController(LoanApplicationService loanApplicationService) {
        this.loanApplicationService = loanApplicationService;
    }
    @PostMapping("/generate")
    public String createLoanApplication(@RequestBody LoanApplicationRequestForm loanApplicationRequestForm){
        if((loanApplicationRequestForm.getAadharNumber()!=null || loanApplicationRequestForm.getAadharNumber().isEmpty())
                && (loanApplicationRequestForm.getPhoneNumber()!=null || loanApplicationRequestForm.getPhoneNumber().isEmpty())){
            return  loanApplicationService.createLoanApplication(loanApplicationRequestForm);
        }
        if(loanApplicationRequestForm.getPanNumber()!=null || loanApplicationRequestForm.getPanNumber().isEmpty()
                && (loanApplicationRequestForm.getPhoneNumber()!=null || loanApplicationRequestForm.getPhoneNumber().isEmpty())){
            return  loanApplicationService.createLoanApplication(loanApplicationRequestForm);
        }
        return "Fill the required fields";
    }

}
