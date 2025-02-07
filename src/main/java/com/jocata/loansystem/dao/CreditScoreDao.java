package com.jocata.loansystem.dao;

import com.jocata.loansystem.entities.CreditScoreDetails;

public interface CreditScoreDao {

    CreditScoreDetails createCreditScore(CreditScoreDetails creditScoreDetails);

    CreditScoreDetails getCustomerFromCreditScore(Integer customerId);

    void updateCreditScore(CreditScoreDetails existingScore);
}
