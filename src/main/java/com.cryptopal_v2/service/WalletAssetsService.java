package com.cryptopal_v2.service;

import com.cryptopal_v2.DTOs.WalletAssetWrapperDTO;
import com.cryptopal_v2.mappers.WalletAssetsMapper;
import com.cryptopal_v2.model.WalletAddress;
import com.cryptopal_v2.model.WalletAssets;
import com.cryptopal_v2.repository.WalletAddressRepository;
import com.cryptopal_v2.repository.WalletAssetsRepository;
import com.cryptopal_v2.responses.WalletAssetResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
public class WalletAssetsService {

    // write the business logic for this service later
    private final WalletAssetsRepository walletAssetsRepository;
    private final WalletAddressRepository walletAddressRepository;
    private final ObjectMapper objectMapper;


    private final WebClient webClient;
    private final String apiKey; // API Key from .env file

    @Autowired
    public WalletAssetsService(WalletAssetsRepository walletAssetsRepository,
                               WalletAddressRepository walletAddressRepository,
                               ObjectMapper objectMapper,
                               WebClient webClient         // fix the web client config
                               ){
        this.walletAssetsRepository = walletAssetsRepository;
        this.walletAddressRepository = walletAddressRepository;
        this.objectMapper = objectMapper;
        this.webClient = webClient;

        Dotenv dotenv = Dotenv.configure().load();
        this.apiKey = dotenv.get("API_KEY"); // Load API_KEY from the .env file

        if (this.apiKey == null || this.apiKey.isEmpty()) {
            throw new IllegalStateException("API_KEY is not set in the .env file!");
        }

    }


    /**
     * fetchWalletAssets will call the api endpoint and fetch the corresponding json response
     * @param walletAddress
     */
    public void fetchWalletAssetsAndSave(String walletAddress){

        // Safety feature - we also should make sure that the program doesn't proceed with null address

        WalletAddress existingWalletAddress = walletAddressRepository.findByWalletAddress(walletAddress)
                .orElseThrow(() -> new RuntimeException("WalletAddress not found for: " + walletAddress));


        // setting up a json request payload
        String jsonResponse = webClient.get()
            .uri(uriBuilder -> uriBuilder
                    .path("/account/tokens")
                    .queryParam("chain_id", "1")
                    .queryParam("address", walletAddress)
                    .queryParam("limit", 100)
                    .queryParam("page", 1)
                    .build())
                .header("x-api-key", apiKey)
                .retrieve()
                .onStatus(
                        // need to learn more about functional programming
                        HttpStatusCode::isError, // Check if the HTTP status is an error
                        clientResponse -> Mono.error(new RuntimeException(
                                "Failed to fetch wallet assets: " + clientResponse.statusCode()))
                )
                .bodyToMono(String.class)
                .block();


        System.out.println("This is the json response i got :"  + jsonResponse);
        // map the created json objects to models we can save to our database

        try{
            // now JSON data can be put into wrapper dto for further processing
            WalletAssetWrapperDTO walletAssetWrapperDTO = objectMapper.readValue(jsonResponse, WalletAssetWrapperDTO.class);

            List<WalletAssetResponse> walletAssetResponses = walletAssetWrapperDTO.getData();
            System.out.println("Wallet Responses List : " + walletAssetResponses.get(0).getContractAddress());

            WalletAssetsMapper mapper = new WalletAssetsMapper();


            List<WalletAssets> walletAssetsList = new ArrayList<>();
            for( WalletAssetResponse walletResponse : walletAssetResponses){
                WalletAssets walletAsset = mapper.mapToEntity(walletResponse, existingWalletAddress);
                walletAssetsList.add(walletAsset);
            }
            walletAssetsRepository.saveAll(walletAssetsList);

        } catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }


    }











}
