package com.DrakeN.trading.Service;

import com.DrakeN.trading.Enitty.Asset;
import com.DrakeN.trading.Enitty.CryptoEntity;
import com.DrakeN.trading.Enitty.User;
import com.DrakeN.trading.Repository.AssetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AssetServiceImpl implements AssetService {

    @Autowired
    private AssetRepository assetRepository;


    @Override
    public Asset createAsset(CryptoEntity coin, User user, double quantity) {


        Asset asset = new Asset();
        asset.setCoin(coin);
        asset.setQuantity(quantity);
        asset.setUser(user);
        asset.setBuyPrice(coin.getCurrentPrice());


        return assetRepository.save(asset);
    }

    @Override
    public Asset getAssetById(Long assetId) throws Exception {


        return assetRepository.findById(assetId).orElseThrow(
                () -> new Exception("Asset not found")
        );
    }

    @Override
    public Asset getAssetByUserIdAndId(Long userId, Long assetId) {
        return null;
    }

    @Override
    public List<Asset> getUserAssets(Long userId) {
        return assetRepository.findByUserId(userId);
    }

    @Override
    public Asset updateAsset(Long assetId, double quantity) throws Exception {

        Asset oldAsset = getAssetById(assetId);
        oldAsset.setQuantity(quantity + oldAsset.getQuantity());

        return assetRepository.save(oldAsset);
    }

    @Override
    public Asset findAssetByUserIdAndCoinId(Long userId, String coinId) {


        return assetRepository.findByUserIdAndCoinId(userId,coinId);
    }

    @Override
    public void deleteAsset(Long assetId) {
        assetRepository.deleteById(assetId);

    }
}
