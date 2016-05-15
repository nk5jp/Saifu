package jp.nk5.saifu.controller;

import jp.nk5.saifu.app.AssetApplication;
import jp.nk5.saifu.domain.Asset;
import jp.nk5.saifu.domain.DomainException;
import jp.nk5.saifu.form.TransferForm;
import jp.nk5.saifu.form.UpdateViewListener;

/**
 * 振込画面のコントローラ．
 * Created by NK5JP on 2016/05/14.
 */
public class TransferController {
    private AssetApplication application;
    private TransferForm form;
    private UpdateViewListener listener;

    public TransferController(AssetApplication application, TransferForm form, UpdateViewListener listener) {
        this.application = application;
        this.form = form;
        this.listener = listener;
    }

    /**
     * 選択した資産を，FromまたはToにセットする．両方セット済みの場合は何もしない．
     * @param asset
     */
    public void setAssetForTransfer(Asset asset) {
       if (form.getToAsset() == null) {
           form.setToAsset(asset);
           updateUI();
       } else if (form.getFromAsset() == null) {
           form.setFromAsset(asset);
           updateUI();
       }
    }

    /**
     * FromまたはToから資産を取り除く．両方空の場合は何もしない．
     */
    public void removeAssetForTransfer() {
        if (form.getFromAsset() != null) {
            form.setFromAsset(null);
            updateUI();
        } else if (form.getToAsset() != null) {
            form.setToAsset(null);
            updateUI();
        }
    }

    public void updateAssetList() {
        try {
            createAssetList();
            updateUI();
        } catch (DomainException e) {
            showError(e.getLayerName());
        }
    }

    /**
     * 資産リストを更新する．例外は呼び出し元でキャッチする．
     * @throws DomainException
     */
    private void createAssetList() throws DomainException {
        form.setAssets(application.createAssetList());
    }

    /**
     * 資金をFromからToを振り込む．
     */
    public void transferMoney(int amount) {
        try {
            application.transferMoney(form.getFromAsset(), form.getToAsset(), amount);
            form.setToAsset(null);
            form.setFromAsset(null);
            updateUI();
        } catch (DomainException e) {
            showError(e.getLayerName());
        }
    }

    private void updateUI() {
        listener.updateUI();
    }

    private void showError(String errorMsg) {
        listener.showError(errorMsg);
    }

}
