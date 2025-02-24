package com.jocata.loansystem.controllers;

import com.jocata.loansystem.forms.RePaymentScheduleForm;
import com.jocata.loansystem.services.RePaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/repayment")
public class RePaymentController {

    private final RePaymentService rePaymentService;

    public RePaymentController(RePaymentService rePaymentService) {
        this.rePaymentService = rePaymentService;
    }

    @GetMapping("/{loanApplicationId}")
    public List<RePaymentScheduleForm> showRepayMentDetails(@PathVariable String loanApplicationId){
        return rePaymentService.showRepayMentDetails(loanApplicationId);
    }

}
