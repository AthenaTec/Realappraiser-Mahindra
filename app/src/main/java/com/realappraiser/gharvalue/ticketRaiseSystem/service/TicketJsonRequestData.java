package com.realappraiser.gharvalue.ticketRaiseSystem.service;

import okhttp3.RequestBody;

public class TicketJsonRequestData {

    private String initQueryUrl;

    private int TicketId;

    private String url;

    private int StatusId;

    private String comments;

    private String authToken;

    private RequestBody requestBody;

    private String response;

    private int responseCode;

    boolean isSuccessful;

    private boolean isValidUrl;

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public boolean isValidUrl() {
        return isValidUrl;
    }

    public void setValidUrl(boolean validUrl) {
        isValidUrl = validUrl;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }


    public RequestBody getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(RequestBody requestBody) {
        this.requestBody = requestBody;
    }


    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }


    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getTicketId() {
        return TicketId;
    }

    public void setTicketId(int ticketId) {
        TicketId = ticketId;
    }


    public String getInitQueryUrl() {
        return initQueryUrl;
    }

    public void setInitQueryUrl(String initQueryUrl) {
        this.initQueryUrl = initQueryUrl;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getStatusId() {
        return StatusId;
    }

    public void setStatusId(int statusId) {
        StatusId = statusId;
    }
}
