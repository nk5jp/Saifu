package jp.nk5.saifu.controller;

import java.util.ArrayList;
import java.util.List;

import jp.nk5.saifu.app.ReceiptApplication;
import jp.nk5.saifu.domain.Asset;
import jp.nk5.saifu.domain.Budget;
import jp.nk5.saifu.domain.DomainException;
import jp.nk5.saifu.domain.ReceiptDetail;
import jp.nk5.saifu.form.ReceiptForm;
import jp.nk5.saifu.form.UpdateViewListener;

/**
 * Created by NK5JP on 2016/05/15.
 */
public class ReceiptController {
    private ReceiptApplication application;
    private ReceiptForm form;
    private UpdateViewListener listener;

    public ReceiptController (ReceiptApplication application, ReceiptForm form, UpdateViewListener listener) {
        this.application = application;
        this.form = form;
        this.listener = listener;
    }

    /**
     * スピナーの選択肢を取得してフォームに格納する．
     * 参照モードの場合，資産スピナにはレシートに紐づく1つだけを格納し，
     * 予算スピナは空にしておく．また，同じく参照モードの場合，
     * 合計値を計算して格納しておく．
     */
    public void initializeForm(int year, int month) {
        try {
            if (!form.isCreateMode()) {
                List<Asset> assets = new ArrayList<Asset>();
                assets.add(form.getReceipt().getAsset());
                form.setAssets(assets);
                form.setTotal(application.calcTotal(form.getReceipt().getDetails()));
                form.setBudgets(new ArrayList<Budget>());
            } else {
                form.setAssets(application.createValidAssetList());
                form.setBudgets(application.createValidBudgetList(year, month));
            }
        } catch (DomainException e) {
            showErrorMessage(e.getLayerName());
        }
    }

    /**
     * 明細を追加する．
     * @param budget 明細の予算．
     * @param amount 明細の金額．
     */
    public void addDetail(Budget budget, int amount) {
        try {
            form.getReceipt().getDetails().add(new ReceiptDetail(-1, form.getReceipt(), budget, amount));
            calcTotal(form.getReceipt().getDetails());
            updateUI();
        } catch (DomainException e) {
            showErrorMessage(e.getLayerName());
        }
    }

    /**
     * レシートを作成もしくは削除する．
     * @param asset 使用する資産．作成時のみ必要．
     */
    public void createDeleteReceipt(Asset asset) {
        try {
            if (form.isCreateMode()) {
                form.getReceipt().setAsset(asset);
                application.createReceipt(form.getReceipt());
            } else {
                application.deleteReceipt(form.getReceipt());
            }
        } catch (DomainException e) {
            showErrorMessage(e.getLayerName());
        }
    }

    /**
     * 明細を削除する
     * @param position 削除する明細の位置
     */

    public void removeDetail(int position) {
        try {
            form.getReceipt().getDetails().remove(position);
            calcTotal(form.getReceipt().getDetails());
            updateUI();
        } catch (DomainException e) {
            showErrorMessage(e.getLayerName());
        }
    }

    /**
     * 税金を計算する．
     */
    public void calcTax() {
        try {
            application.calcTax(form.getReceipt().getDetails());
            calcTotal(form.getReceipt().getDetails());
            updateUI();
        } catch (DomainException e) {
            showErrorMessage(e.getLayerName());
        }
    }


    /**
     * 合計値を計算する．例外は呼び出し元でキャッチする．
     * @param details 明細の一覧．
     * @throws DomainException
     */
    private void calcTotal(List<ReceiptDetail> details) throws DomainException {
        form.setTotal(application.calcTotal(form.getReceipt().getDetails()));
    }

    private void updateUI() { listener.updateUI(); }
    private void showErrorMessage(String message) {
        listener.showError(message);
    }

}
