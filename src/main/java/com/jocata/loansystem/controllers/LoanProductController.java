package com.jocata.loansystem.controllers;

import com.jocata.loansystem.forms.LoanProductRequestForm;
import com.jocata.loansystem.forms.LoanProductResponseForm;
import com.jocata.loansystem.services.LoanProductService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/loan-product")
public class LoanProductController {

    private final LoanProductService loanProductService;

    public LoanProductController(LoanProductService loanProductService) {
        this.loanProductService = loanProductService;
    }

    @PostMapping("/generate")
    public LoanProductResponseForm createProduct(@RequestBody LoanProductRequestForm loanProductRequestForm){
        return loanProductService.createProduct(loanProductRequestForm);
    }

    @GetMapping("/{id}")
    public LoanProductResponseForm getProduct(@PathVariable(name = "id") String productId){
        return loanProductService.getProduct(productId);
    }

    @PutMapping("/update")
    public LoanProductResponseForm updateProduct(@RequestBody LoanProductResponseForm loanProductRequestForm){
        return loanProductService.updateProduct(loanProductRequestForm);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable(name = "id") String productId){
        return loanProductService.deleteProduct(productId);
    }


}
