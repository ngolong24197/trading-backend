package com.DrakeN.trading.Service;

import com.DrakeN.trading.Enitty.Asset;
import com.DrakeN.trading.Enitty.CryptoEntity;
import com.DrakeN.trading.Enitty.User;

import java.util.List;

public interface AssetService {

    Asset createAsset(CryptoEntity coin, User user, double quantity);
    Asset getAssetById(Long assetId) throws Exception;
    Asset getAssetByUserIdAndId(Long userId, Long assetId);
    List<Asset> getUserAssets(Long userId);
    Asset updateAsset(Long assetId, double quantity) throws Exception;

    Asset findAssetByUserIdAndCoinId(Long userId, String coinId);
    void deleteAsset(Long assetId);

}
