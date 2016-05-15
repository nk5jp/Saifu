package jp.nk5.saifu.controller;

import jp.nk5.saifu.app.AssetApplication;
import jp.nk5.saifu.domain.Asset;
import jp.nk5.saifu.domain.DomainException;
import jp.nk5.saifu.form.AssetForm;
import jp.nk5.saifu.form.UpdateViewListener;

/**
 * 資産画面のコントローラ
 * Created by NK5JP on 2016/05/08.
 */
public class AssetController {

    private AssetApplication application;
    private AssetForm form;
    private UpdateViewListener listener;

    public AssetController (AssetApplication application, AssetForm form, UpdateViewListener listener) {
        this.application = application;
        this.form = form;
        this.listener = listener;
    }

    /**
     * リストおよび資産合計値を更新し，UIに反映する．
     */
    public void updateAssetList() {
        try {
            createAssetList();
            calcTotal();
            updateUI();
        } catch (DomainException e) {
            showErrorMessage(e.getLayerName());
        }
    }

    /**
     * リストを更新する．例外は呼び出し元でキャッチする．
     * @throws DomainException
     */
    private void createAssetList() throws DomainException {
        form.setAssets(application.createAssetList());
    }

    /**
     * 資産合計値を算出する．
     */
    private void calcTotal() {
        form.setTotal(application.calcTotal(form.getAssets()));
    }

    /**
     * 有効無効を反転し，リストを更新した上でUIに反映する．
     * @param asset
     */
    public void switchAssetValidation(Asset asset) {
        try {
            application.switchAssetValidation(asset);
            createAssetList();
            updateUI();
        } catch (DomainException e) {
            showErrorMessage(e.getLayerName());
        }
    }

    /**
     * 資産を作成し，リストを更新した上でUIに反映する．
     * @param name
     */
    public void updateAsset(String name) {
        try {
            application.updateAsset(-1, name, 0, true);
            createAssetList();
            updateUI();
        } catch (DomainException e) {
            showErrorMessage(e.getLayerName());
        }
    }

    private void updateUI() {listener.updateUI();}
    private void showErrorMessage(String error) {listener.showError(error);}

}
