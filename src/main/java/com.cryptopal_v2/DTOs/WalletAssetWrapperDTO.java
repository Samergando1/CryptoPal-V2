package com.cryptopal_v2.DTOs;

import com.cryptopal_v2.responses.WalletAssetResponse;

import java.util.List;

public class WalletAssetWrapperDTO {

    // used to preserve other non data fields after parsing a json response
    private Integer nextPage; // Corresponds to "next_page"
    private String message;
    private List<WalletAssetResponse> data;

    // Getters and setters
    public Integer getNextPage() {
        return nextPage;
    }

    public void setNextPage(Integer nextPage) {
        this.nextPage = nextPage;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<WalletAssetResponse> getData() {
        return data;
    }

    public void setData(List<WalletAssetResponse> data) {
        this.data = data;
    }
}
