package com.jocata.loansystem.services.impl;

import com.jocata.loansystem.dao.CustomerDao;
import com.jocata.loansystem.entities.CustomerDetails;
import com.jocata.loansystem.forms.*;
import com.jocata.loansystem.services.LoanApplicationService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.util.UUID;

@Service
public class LoanApplicationServiceImpl implements LoanApplicationService {

    private final RestTemplate restTemplate;
    private final CustomerDao customerDao;

    public LoanApplicationServiceImpl(RestTemplate restTemplate, CustomerDao customerDao) {
        this.restTemplate = restTemplate;
        this.customerDao = customerDao;
    }

    private static final String AADHAR_SERVICE_URL = "http://localhost:9090/externalservices/api/v1/getAadharDetails";
    private static final String PAN_SERVICE_URL = "http://localhost:9090/externalservices/api/v1/getPanDetails";

    public CustomerDetails createCustomerFromAadhar(String aadhar) {
        AadharPayLoad aadharPayLoad = new AadharPayLoad();
        aadharPayLoad.setAadharNumber(aadhar);

        ExternalServiceRequest aadharRequest = new ExternalServiceRequest();
        aadharRequest.setAadharPayLoad(aadharPayLoad);
        aadharRequest.setTxnId(UUID.randomUUID().toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ExternalServiceRequest> requestEntity = new HttpEntity<>(aadharRequest, headers);

        ResponseEntity<ExternalServiceResponse<AadharResponse>> responseEntity = restTemplate.exchange(
                AADHAR_SERVICE_URL,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        ExternalServiceResponse<AadharResponse> response = responseEntity.getBody();
        if (response == null || response.getData() == null) {
            throw new IllegalArgumentException("Aadhar data not found for Aadhar number: " + aadhar);
        }

        AadharResponse aadharData = response.getData();

        CustomerDetails customer = new CustomerDetails();

        String fullName = aadharData.getFullName();
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name is missing in Aadhar response");
        }
        fullName = fullName.trim().replaceAll("\\s+", " ");
        String[] parts = fullName.split(" ");
        String firstName = parts[0];
        String lastName = parts.length > 1 ? parts[parts.length - 1] : "";

        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(aadharData.getEmail());
        customer.setDob(Date.valueOf(aadharData.getDob()));
        customer.setPhoneNumber(aadharData.getContactNumber());
        customer.setAddress(aadharData.getAddress());
        customer.setIdentityNumber(aadharData.getAadharNum());

        return customerDao.createCustomer(customer);
    }

    private CustomerDetails createCustomerFromPan(String pan) {
        PanPayload panPayload=new PanPayload();
        panPayload.setPanNumber(pan);

        ExternalServiceRequest externalServiceRequest=new ExternalServiceRequest();
        externalServiceRequest.setTxnId(UUID.randomUUID().toString());
        externalServiceRequest.setPanPayload(panPayload);

        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ExternalServiceRequest> requestEntity = new HttpEntity<>(externalServiceRequest, headers);

        ResponseEntity<ExternalServiceResponse<PanResponse>> responseEntity = restTemplate.exchange(
                PAN_SERVICE_URL,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {}
        );

        ExternalServiceResponse<PanResponse> response = responseEntity.getBody();
        if (response == null || response.getData() == null) {
            throw new IllegalArgumentException("Pan data not found for Aadhar number: " + pan);
        }

        PanResponse panData = response.getData();

        CustomerDetails customer = new CustomerDetails();

        String fullName = panData.getFullName();
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException("Full name is missing in Aadhar response");
        }
        fullName = fullName.trim().replaceAll("\\s+", " ");
        String[] parts = fullName.split(" ");
        String firstName = parts[0];
        String lastName = parts.length > 1 ? parts[parts.length - 1] : "";

        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(panData.getEmail());
        customer.setDob(Date.valueOf(panData.getDob()));
        customer.setPhoneNumber(panData.getContactNumber());
        customer.setAddress(panData.getAddress());
        customer.setIdentityNumber(panData.getPanNum());

        return customerDao.createCustomer(customer);
    }

    @Override
    public String createLoanApplication(LoanApplicationRequestForm loanApplicationRequestForm) {
        String pan = loanApplicationRequestForm.getPanNumber();
        String aadhar = loanApplicationRequestForm.getAadharNumber();
        String phone = loanApplicationRequestForm.getPhoneNumber();

        boolean isPhoneValid = phone != null && phone.matches("\\d{10}");
        boolean isPanProvided = pan != null && !pan.trim().isEmpty();
        boolean isAadharProvided = aadhar != null && !aadhar.trim().isEmpty();

        if (isPhoneValid && isPanProvided) {
            if (!isValidPan(pan)) {
                return "Invalid PAN number";
            }
            createCustomerFromPan(pan);
            return "Loan application processed successfully with PAN";
        }
        else if (isPhoneValid && isAadharProvided) {
            if (!isValidAadhar(aadhar)) {
                return "Invalid Aadhaar number";
            }
            createCustomerFromAadhar(aadhar);
            return "Loan application processed successfully with Aadhaar";
        }
        else {
            return "Either a valid phone number and PAN or a valid phone number and Aadhaar must be provided";
        }
    }

    private boolean isValidAadhar(String aadhar) {
        return aadhar.matches("\\d{12}");
    }

    private boolean isValidPan(String pan) {
        return pan.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}");
    }

}
