package com.jocata.loansystem.dao;

import com.jocata.loansystem.entities.RiskAssessmentDetails;

public interface RiskAssessmentDao {
    RiskAssessmentDetails saveRiskAssessmentDetails(RiskAssessmentDetails riskAssessmentDetails);

    RiskAssessmentDetails getRiskAssessmentByApplicationId(Integer applicationId);
}
