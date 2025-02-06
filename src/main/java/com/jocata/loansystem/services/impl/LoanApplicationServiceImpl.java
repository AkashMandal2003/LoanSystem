package com.jocata.loansystem.services.impl;

import com.jocata.loansystem.dao.CreditScoreDao;
import com.jocata.loansystem.dao.CustomerDao;
import com.jocata.loansystem.dao.LoanApplicationDao;
import com.jocata.loansystem.entities.CreditScoreDetails;
import com.jocata.loansystem.entities.CustomerDetails;
import com.jocata.loansystem.entities.LoanApplicationDetails;
import com.jocata.loansystem.forms.*;
import com.jocata.loansystem.services.LoanApplicationService;
import com.jocata.loansystem.utils.LoanStatus;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

@Service
public class LoanApplicationServiceImpl implements LoanApplicationService {

    private final RestTemplate restTemplate;
    private final CustomerDao customerDao;
    private final CreditScoreDao creditScoreDao;
    private final LoanApplicationDao loanApplicationDao;


    public LoanApplicationServiceImpl(RestTemplate restTemplate, CustomerDao customerDao, CreditScoreDao creditScoreDao, LoanApplicationDao loanApplicationDao) {
        this.restTemplate = restTemplate;
        this.customerDao = customerDao;
        this.creditScoreDao = creditScoreDao;
        this.loanApplicationDao = loanApplicationDao;
    }

    private static final String AADHAR_SERVICE_URL = "http://localhost:9090/externalservices/api/v1/getAadharDetails";
    private static final String PAN_SERVICE_URL = "http://localhost:9090/externalservices/api/v1/getPanDetails";
    private static final String CIBIL_SERVICE_URL = "http://localhost:9090/externalservices/api/v1/getCibilDetails";

    @Override
    public String createLoanApplication(LoanApplicationRequestForm requestForm) {

        String pan = requestForm.getPanNumber();
        String aadhar = requestForm.getAadharNumber();
        String phone = requestForm.getPhoneNumber();

        boolean isPhoneValid = phone != null && phone.matches("\\d{10}");
        boolean isPanProvided = pan != null && !pan.trim().isEmpty();
        boolean isAadharProvided = aadhar != null && !aadhar.trim().isEmpty();

        if (isPanProvided && isAadharProvided && isPhoneValid && isValidPan(pan) && isValidAadhar(aadhar)) {

                PanResponse panResponse = getPanResponse(pan);
                AadharResponse aadharResponse = getAadharResponse(aadhar);
                CibilResponse cibilResponse = getCibilResponse(pan);

                String fullNameFromAadhar = aadharResponse.getFullName();
                if (fullNameFromAadhar == null || fullNameFromAadhar.isBlank()) {
                    throw new IllegalArgumentException("Full name is missing in Aadhar response");
                }
                fullNameFromAadhar = fullNameFromAadhar.trim().replaceAll("\\s+", " ");
                String[] parts = fullNameFromAadhar.split(" ");
                String firstNameFromAadhar = parts[0];
                String lastNameFromAadhar = parts.length > 1 ? parts[parts.length - 1] : "";

                String fullNameFromPan = aadharResponse.getFullName();
                if (fullNameFromPan == null || fullNameFromPan.isBlank()) {
                    throw new IllegalArgumentException("Full name is missing in Pan response");
                }
                fullNameFromPan = fullNameFromPan.trim().replaceAll("\\s+", " ");
                String[] partsPan = fullNameFromPan.split(" ");
                String firstNameFromPan = partsPan[0];
                String lastNameFromPan = partsPan.length > 1 ? partsPan[partsPan.length - 1] : "";

                double similarity = calculateNameSimilarity(fullNameFromAadhar, fullNameFromPan);

                if (similarity < 90) {
                    throw new IllegalArgumentException("PAN and Aadhar names do not match sufficiently: " + similarity + "%");
                }

                CustomerDetails customer = new CustomerDetails();
                customer.setFirstName(!StringUtils.isEmpty(firstNameFromPan) ? firstNameFromPan : firstNameFromAadhar);
                customer.setLastName(!StringUtils.isEmpty(lastNameFromPan) ? lastNameFromPan : lastNameFromAadhar);
                customer.setEmail(aadharResponse.getEmail());
                customer.setDob(Date.valueOf(aadharResponse.getDob()));
                customer.setPhoneNumber(aadharResponse.getContactNumber());
                customer.setAddress(aadharResponse.getAddress());
                customer.setIdentityNumber(!StringUtils.isEmpty(panResponse.getPanNum()) ? panResponse.getPanNum() : aadharResponse.getAadharNum());

                customerDao.createCustomer(customer);

                CreditScoreDetails scoreDetails = new CreditScoreDetails();
                scoreDetails.setCustomer(customer);
                scoreDetails.setScoreDate(Date.valueOf(cibilResponse.getReportDate()));
                scoreDetails.setScore(Integer.valueOf(cibilResponse.getCreditScore()));
                scoreDetails.setCreditHistory(cibilResponse.getCreditHistory());
                scoreDetails.setTotalOutstandingBalance(new BigDecimal(cibilResponse.getTotalOutstandingBalance()));
                scoreDetails.setRecentCreditInquiries(cibilResponse.getRecentCreditInquiries());
                scoreDetails.setPaymentHistory(cibilResponse.getPaymentHistory());
                scoreDetails.setCreditLimit(new BigDecimal(cibilResponse.getCreditLimit()));
                scoreDetails.setStatus(cibilResponse.getStatus());

                creditScoreDao.createCreditScore(scoreDetails);

                LoanApplicationDetails applicationDetails = new LoanApplicationDetails();
                applicationDetails.setApplicationDate(new Date(System.currentTimeMillis()));
                applicationDetails.setCustomerId(customer);
                applicationDetails.setStatus(String.valueOf(LoanStatus.PENDING));

                loanApplicationDao.createLoanApplication(applicationDetails);

            }

        return null;

    }

    private CibilResponse getCibilResponse(String panNumber) {

        CibilPayLoad cibilPayLoad = new CibilPayLoad();
        cibilPayLoad.setPanNumber(panNumber);

        ExternalServiceRequest externalServiceRequest = new ExternalServiceRequest();
        externalServiceRequest.setTxnId(UUID.randomUUID().toString());
        externalServiceRequest.setCibilPayLoad(cibilPayLoad);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ExternalServiceRequest> requestHttpEntity = new HttpEntity<>(externalServiceRequest, headers);

        ResponseEntity<ExternalServiceResponse<List<CibilResponse>>> responseEntity = restTemplate.exchange(
                CIBIL_SERVICE_URL,
                HttpMethod.POST,
                requestHttpEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        return getCibilResponse(panNumber, responseEntity);
    }

    private static CibilResponse getCibilResponse(String panNumber, ResponseEntity<ExternalServiceResponse<List<CibilResponse>>> responseEntity) {
        ExternalServiceResponse<List<CibilResponse>> response = responseEntity.getBody();
        if (response == null || response.getData() == null) {
            throw new IllegalArgumentException("Pan data not found for Aadhar number: " + panNumber);
        }
        List<CibilResponse> cibilResponses = response.getData();
        CibilResponse latestCibilResponse = null;
        for (CibilResponse cibilResponse : cibilResponses) {
            if (cibilResponse.getReportDate() == null) {
                continue;
            }

            if (latestCibilResponse == null || cibilResponse.getReportDate().compareTo(latestCibilResponse.getReportDate()) > 0) {
                latestCibilResponse = cibilResponse;
            }
        }

        if (latestCibilResponse == null) {
            throw new IllegalArgumentException("No valid CIBIL report found for PAN: " + panNumber);
        }
        return latestCibilResponse;
    }

    private AadharResponse getAadharResponse(String aadharNumber) {

        AadharPayLoad aadharPayLoad = new AadharPayLoad();
        aadharPayLoad.setAadharNumber(aadharNumber);

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
                new ParameterizedTypeReference<>() {
                }
        );

        ExternalServiceResponse<AadharResponse> response = responseEntity.getBody();
        if (response == null || response.getData() == null) {
            throw new IllegalArgumentException("Aadhar data not found for Aadhar number: " + aadharNumber);
        }

        return response.getData();
    }

    private PanResponse getPanResponse(String panNumber) {

        PanPayload panPayload = new PanPayload();
        panPayload.setPanNumber(panNumber);

        ExternalServiceRequest externalServiceRequest = new ExternalServiceRequest();
        externalServiceRequest.setTxnId(UUID.randomUUID().toString());
        externalServiceRequest.setPanPayload(panPayload);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<ExternalServiceRequest> requestEntity = new HttpEntity<>(externalServiceRequest, headers);

        ResponseEntity<ExternalServiceResponse<PanResponse>> responseEntity = restTemplate.exchange(
                PAN_SERVICE_URL,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        ExternalServiceResponse<PanResponse> response = responseEntity.getBody();
        if (response == null || response.getData() == null) {
            throw new IllegalArgumentException("Pan data not found for Aadhar number: " + panNumber);
        }

        return response.getData();
    }

    private double calculateNameSimilarity(String name1, String name2) {
        LevenshteinDistance levenshtein = new LevenshteinDistance();
        int distance = levenshtein.apply(name1.toLowerCase(), name2.toLowerCase());
        int maxLength = Math.max(name1.length(), name2.length());

        return (1 - ((double) distance / maxLength)) * 100; // Convert to percentage
    }


    private boolean isValidAadhar(String aadhar) {
        return aadhar.matches("\\d{12}");
    }

    private boolean isValidPan(String pan) {
        return pan.matches("[A-Z]{5}[0-9]{4}[A-Z]{1}");
    }

}
