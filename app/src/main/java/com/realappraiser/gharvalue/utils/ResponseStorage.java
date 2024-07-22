package com.realappraiser.gharvalue.utils;

import com.realappraiser.gharvalue.communicator.ShowFSUIResponse;

public class ResponseStorage {
    private static ResponseStorage instance;
    private ShowFSUIResponse savedResponse;

    private ResponseStorage() {
        // Private constructor to prevent instantiation
    }

    public static ResponseStorage getInstance() {
        if (instance == null) {
            instance = new ResponseStorage();
        }
        return instance;
    }

    public ShowFSUIResponse getSavedResponse() {
        return savedResponse;
    }

    public void setSavedResponse(ShowFSUIResponse response) {
        savedResponse = response;
    }
}
