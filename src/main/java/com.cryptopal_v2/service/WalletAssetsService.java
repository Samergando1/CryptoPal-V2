import com.cryptopal_v2.model.WalletAsset;
import com.cryptopal_v2.model.WalletAddress;
import com.cryptopal_v2.repository.WalletAssetsRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class WalletAssetsService {

    @Autowired
    private WalletAssetsRepository walletAssetsRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Fetches and saves assets for a given wallet address.
     * @param walletAddress The wallet address entity to which assets are associated.
     * @param jsonResponse  The JSON response from the API as a string.
     */
    public void fetchAndSaveAssets(WalletAddress walletAddress, String jsonResponse) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            List<WalletAsset> assets = parseAssetsFromJson(rootNode, walletAddress);
            walletAssetsRepository.saveAll(assets);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses the JSON data to extract wallet assets.
     * @param rootNode The root JSON node containing token information.
     * @param walletAddress The associated WalletAddress entity.
     * @return A list of WalletAsset entities.
     */
    private List<WalletAsset> parseAssetsFromJson(JsonNode rootNode, WalletAddress walletAddress) {
        List<WalletAsset> assets = new ArrayList<>();

        for (JsonNode tokenNode : rootNode) {
            WalletAsset asset = new WalletAsset();
            asset.setSymbol(tokenNode.path("symbol").asText());
            asset.setTokenContractAddress(tokenNode.path("tokenContractAddress").asText());
            asset.setTokenType(tokenNode.path("tokenType").asText());
            asset.setHoldingAmount(tokenNode.path("holdingAmount").asDouble());
            asset.setPriceUsd(tokenNode.path("priceUsd").asDouble());
            asset.setValueUsd(tokenNode.path("valueUsd").asDouble());
            asset.setTokenId(tokenNode.path("tokenId").asText());
            asset.setWalletAddress(walletAddress);

            assets.add(asset);
        }

        return assets;
    }
}
