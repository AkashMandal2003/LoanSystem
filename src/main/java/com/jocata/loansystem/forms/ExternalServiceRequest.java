package com.jocata.loansystem.forms;

import java.io.Serializable;

public class ExternalServiceRequest implements Serializable {

    private String txnId;
    private PanPayload panPayload;
    private AadharPayLoad aadharPayLoad;

    public AadharPayLoad getAadharPayLoad() {
        return aadharPayLoad;
    }

    public void setAadharPayLoad(AadharPayLoad aadharPayLoad) {
        this.aadharPayLoad = aadharPayLoad;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public PanPayload getPanPayload() {
        return panPayload;
    }

    public void setPanPayload(PanPayload panPayload) {
        this.panPayload = panPayload;
    }
}
