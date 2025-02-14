package com.jocata.loansystem.controllers;

import com.jocata.loansystem.forms.CreditAssessmentForm;
import com.jocata.loansystem.services.CreditAssessmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/credit-assessment")
public class CreditAssessmentController {

    private final CreditAssessmentService creditAssessmentService;

    public CreditAssessmentController(CreditAssessmentService creditAssessmentService) {
        this.creditAssessmentService = creditAssessmentService;
    }

    @GetMapping("/details")
    public String getCreditAssessmentDetails(@RequestBody CreditAssessmentForm creditAssessmentForm){
        return creditAssessmentService.getCreditAssessmentDetails(creditAssessmentForm);
    }

}
