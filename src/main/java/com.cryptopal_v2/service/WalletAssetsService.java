package com.cryptopal_v2.service;

import com.cryptopal_v2.model.WalletAddress;
import com.cryptopal_v2.model.WalletAssets;
import com.cryptopal_v2.repository.WalletAssetsRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;

@Service
public class WalletAssetsService {

    @Autowired
    private WalletAssetsRepository walletAssetsRepository;

    private static final String API_KEY = "f1af3d83-6af7-4830-ba7e-fef3a348532a"; // Replace with your actual API key
    private static final String API_URL = "https://www.oklink.com/api/v5/explorer/address/token-balance";


    public void fetchAndSaveAssets(WalletAddress walletAddress) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            // Set up headers with the hardcoded API key
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + API_KEY);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Build the URL with the wallet address parameter
            String url = API_URL + "?address=" + walletAddress.getWalletAddress();
            String jsonResponse = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();

            if (jsonResponse != null) {
                parseAndSaveAssets(walletAddress, jsonResponse);
            }
        } catch (HttpClientErrorException e) {
            System.err.println("HTTP error occurred: " + e.getStatusCode());
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    private void parseAndSaveAssets(WalletAddress walletAddress, String jsonResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponse);

            JsonNode tokenList = rootNode.path("data").get(0).path("tokenList");

            for (JsonNode tokenNode : tokenList) {
                WalletAssets asset = new WalletAssets();
                asset.setWalletAddress(walletAddress);
                asset.setSymbol(tokenNode.path("symbol").asText());
                asset.setTokenContractAddress(tokenNode.path("tokenContractAddress").asText());
                asset.setTokenType(tokenNode.path("tokenType").asText());
                asset.setHoldingAmount(parseBigDecimal(tokenNode.path("holdingAmount")));
                asset.setPriceUsd(parseBigDecimal(tokenNode.path("priceUsd")));
                asset.setValueUsd(parseBigDecimal(tokenNode.path("valueUsd")));
                asset.setTokenId(tokenNode.path("tokenId").asText());

                // Save the asset to the database
                walletAssetsRepository.save(asset);
            }
        } catch (Exception e) {
            System.err.println("Error parsing and saving assets: " + e.getMessage());
        }
    }

    private BigDecimal parseBigDecimal(JsonNode node) {
        return node.isTextual() ? new BigDecimal(node.asText()) : null;
    }
}
