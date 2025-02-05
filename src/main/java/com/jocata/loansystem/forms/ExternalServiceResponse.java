package com.jocata.loansystem.forms;

public class ExternalServiceResponse<T> {

    private Integer statusCode;
    private String statusMessage;
    private Integer errorCode;
    private String errorDescription;
    private String txnId;
    private T data;

    public ExternalServiceResponse() {
    }

    public ExternalServiceResponse(Integer statusCode, String statusMessage, String txnId, T data) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.txnId = txnId;
        this.data = data;
    }

    public ExternalServiceResponse(Integer errorCode, String errorDescription, String txnId) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
        this.txnId = txnId;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
