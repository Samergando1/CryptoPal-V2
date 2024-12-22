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
import java.math.RoundingMode;

@Service
public class WalletAssetsService {

    @Autowired
    private WalletAssetsRepository walletAssetsRepository;

    private static final String API_KEY = "f1af3d83-6af7-4830-ba7e-fef3a348532a"; // Replace with your actual API key
    String API_URL = "https://www.oklink.com/api/v5/explorer/address/token-balance";
    String chainShortName = "eth";  // Ensure this matches the expected blockchain short name.
    String address = "0xdac17f958d2ee523a2206206994597c13d831ec7";  // Replace with the address you need.
    String protocolType = "token_20";  // Set this to "token_20" for ERC-20 tokens.
    int limit = 1;

    // Construct the URL with parameters
    String url = API_URL + "?chainShortName=" + chainShortName + "&address=" + address + "&protocolType=" + protocolType + "&limit=" + limit;


    public void fetchAndSaveAssets(WalletAddress walletAddress) {
        RestTemplate restTemplate = new RestTemplate();

        try {
            // Set up headers with the correct API key header
            HttpHeaders headers = new HttpHeaders();
            headers.set("OK-ACCESS-KEY", API_KEY);

            HttpEntity<String> entity = new HttpEntity<>(headers);


            String jsonResponse = restTemplate.exchange(url, HttpMethod.GET, entity, String.class).getBody();

            if (jsonResponse != null) {
                parseAndSaveAssets(walletAddress, jsonResponse);
            }
        } catch (HttpClientErrorException e) {
            System.err.println("HTTP error occurred: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    private void parseAndSaveAssets(WalletAddress walletAddress, String jsonResponse) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonResponse);

            // Check if "data" field exists and is an array
            JsonNode dataNode = rootNode.path("data");
            if (!dataNode.isArray() || dataNode.isEmpty()) {
                System.err.println("Error: 'data' field is missing or empty in JSON response.");
                return;
            }

            // Check if "tokenList" exists in data[0]
            JsonNode tokenList = dataNode.get(0).path("tokenList");
            if (!tokenList.isArray() || tokenList.isEmpty()) {
                System.err.println("Error: 'tokenList' field is missing or empty.");
                return;
            }

            // Iterate through the token list and save each asset
            for (JsonNode tokenNode : tokenList) {
                WalletAssets asset = new WalletAssets();
                asset.setWalletAddress(walletAddress);
                asset.setSymbol(tokenNode.path("symbol").asText(null));  // Set default null if missing
                asset.setTokenContractAddress(tokenNode.path("tokenContractAddress").asText(null));
                asset.setTokenType(tokenNode.path("tokenType").asText(null));
                asset.setHoldingAmount(parseBigDecimal(tokenNode.path("holdingAmount")));
                asset.setPriceUsd(parseBigDecimal(tokenNode.path("priceUsd")));
                asset.setValueUsd(parseBigDecimal(tokenNode.path("valueUsd")));
                asset.setTokenId(tokenNode.path("tokenId").asText(null));

                // Save the asset to the database
                walletAssetsRepository.save(asset);
            }
        } catch (Exception e) {
            System.err.println("Error parsing and saving assets: " + e.getMessage());
        }
    }

    private BigDecimal parseBigDecimal(JsonNode node) {
        try {
            BigDecimal value = node.isTextual() ? new BigDecimal(node.asText()) : BigDecimal.ZERO;
            BigDecimal maxAllowedValue = BigDecimal.valueOf(10).pow(20).subtract(BigDecimal.valueOf(1, 18));
            return value.compareTo(maxAllowedValue) > 0 ? maxAllowedValue : value.setScale(18, RoundingMode.DOWN);
        } catch (Exception e) {
            System.err.println("Error parsing BigDecimal: " + node.asText() + " - " + e.getMessage());
            return BigDecimal.ZERO;
        }
    }





}
