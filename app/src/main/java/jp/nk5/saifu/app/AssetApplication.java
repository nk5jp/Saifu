package jp.nk5.saifu.app;

import java.util.List;

import jp.nk5.saifu.domain.Asset;
import jp.nk5.saifu.domain.AssetRepository;
import jp.nk5.saifu.domain.DomainException;

/**
 * 資産画面および振込画面のフォーム
 * Created by NK5JP on 2016/05/08.
 */
public class AssetApplication {

    private AssetRepository assetRepository;

    public AssetApplication(AssetRepository assetRepository) {
       this.assetRepository = assetRepository;
    }

    /**
     * 資産の一覧を返却する．
     * @return 資産の一覧．
     * @throws DomainException
     */
    public List<Asset> createAssetList() throws DomainException {
        return assetRepository.readAllAsset();
    }

    /**
     * 資産を更新若しくは作成する．
     * @param id 対象ID
     * @param name 資産名
     * @param amount 資産額
     * @param isValid 有効か
     * @throws DomainException
     */
    public void updateAsset(int id, String name, int amount, boolean isValid) throws DomainException {
        Asset asset;
        int tempAmount = 0;
        boolean tempValid = true;
        if (id < 0) {
            asset = new Asset(id, name, amount, isValid);
        } else {
            asset = assetRepository.readAssetById(id);
            tempAmount = asset.getAmount();
            tempValid = asset.isValid();
            asset.setAmount(amount);
            asset.setValid(isValid);
        }
        if (asset.canPersistent()) {
            assetRepository.updateAsset(asset);
        } else {
            asset.setAmount(tempAmount);
            asset.setValid(isValid);
            throw new DomainException("DOM:AA:UA");
        }
    }

    /**
     * 資産を転送する．fromのみがnullなら振込，両方インスタンスがあれば振替，さもなくば例外スロー．
     * @param fromAsset 転送元
     * @param toAsset 転送先
     * @param money 転送金額
     * @throws DomainException
     */
    public void transferMoney(Asset fromAsset, Asset toAsset, int money) throws DomainException {
        if (fromAsset == null && toAsset != null) {
            updateAsset(toAsset.getId(), toAsset.getName(), toAsset.getAmount() + money, toAsset.isValid());
        } else if (fromAsset != null && toAsset != null) {
            updateAsset(toAsset.getId(), toAsset.getName(), toAsset.getAmount() + money, toAsset.isValid());
            updateAsset(fromAsset.getId(), fromAsset.getName(), fromAsset.getAmount() - money, fromAsset.isValid());
        } else {
            throw new DomainException("DOM:AA:TM");
        }
    }

    /**
     * 有効無効を変換する
     * @param asset 対象資産
     * @throws DomainException
     */
    public void switchAssetValidation(Asset asset) throws DomainException {
        updateAsset(asset.getId(), asset.getName(), asset.getAmount(), !asset.isValid());
    }

    /**
     * 資産の合計値を算出する．
     * @param assets
     * @return
     */
    public int calcTotal(List<Asset> assets) {
        int total = 0;
        for (Asset asset : assets) {
            total = total + asset.getAmount();
        }
        return total;
    }

}
