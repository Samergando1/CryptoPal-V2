package com.cryptopal_v2.service;

import ch.qos.logback.core.subst.Token;
import com.cryptopal_v2.DTOs.WalletAssetWrapperDTO;
import com.cryptopal_v2.model.WalletAddress;
import com.cryptopal_v2.model.WalletAssets;
import com.cryptopal_v2.repository.WalletAddressRepository;
import com.cryptopal_v2.repository.WalletAssetsRepository;
import com.cryptopal_v2.responses.WalletAssetResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

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
                               WebClient webClient,         // fix the web client config
                               @Value("${API_KEYy}") String apiKey){
        this.walletAssetsRepository = walletAssetsRepository;
        this.walletAddressRepository = walletAddressRepository;
        this.objectMapper = objectMapper;
        this.webClient = WebClient.builder()
                .baseUrl("https://api.chainbase.online/v1")
                .build();

        Dotenv dotenv = Dotenv.configure().load();
        this.apiKey = dotenv.get("API_KEY");
    }


    /**
     * fetchWalletAssets will call the api endpoint and fetch the corresponding json response
     * @param walletAddress
     */
    public void fetchWalletAssets(String walletAddress){



        // Safety feature - we also should make sure that the program doesn't proceed with null address

        Optional<WalletAddress> walletAddressOpt = walletAddressRepository.findByWalletAddress(walletAddress);

        if (walletAddressOpt.isEmpty()) {
            throw new RuntimeException("WalletAddress not found for: " + walletAddress);
        }
        WalletAddress existingWalletAddress = walletAddressOpt.get();

        // setting up a json request payload
        String jsonResponse = webClient.get()
            .uri(uriBuilder -> uriBuilder
                    .path("/account/tokens")
                    .queryParam("chain_id", "1")
                    .queryParam("address", walletAddress)
                    .queryParam("limit", 10)
                    .queryParam("page", 1)
                    .build())
                .header("x-api-key", apiKey)
                .retrieve()
                .bodyToMono(String.class)
                .block();


        // map the created json objects to models we can save to our database

        try{
            // now JSON data can be put into wrapper dto for further processing
            WalletAssetWrapperDTO walletAssetWrapperDTO = objectMapper.readValue(jsonResponse, WalletAssetWrapperDTO.class);
            List<WalletAssetResponse> walletAssetResponses = walletAssetWrapperDTO.getData();

            for( WalletAssetResponse walletResponse : walletAssetResponses){
                // map to a wallet model and save it to the wallet repository



            }




        } catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }


    }











}
