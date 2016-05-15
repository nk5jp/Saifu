package jp.nk5.saifu.app;

import java.util.List;

import jp.nk5.saifu.domain.Asset;
import jp.nk5.saifu.domain.AssetRepository;
import jp.nk5.saifu.domain.Budget;
import jp.nk5.saifu.domain.BudgetRepository;
import jp.nk5.saifu.domain.DomainException;
import jp.nk5.saifu.domain.Receipt;
import jp.nk5.saifu.domain.ReceiptDetail;
import jp.nk5.saifu.domain.ReceiptRepository;

/**
 * レシート画面のアプリケーション
 * Created by NK5JP on 2016/05/14.
 */
public class ReceiptApplication {
    private ReceiptRepository receiptRepository;
    private AssetRepository assetRepository;
    private BudgetRepository budgetRepository;
    private final int RATE = 108;

    public ReceiptApplication(ReceiptRepository receiptRepository, AssetRepository assetRepository, BudgetRepository budgetRepository) {
        this.receiptRepository = receiptRepository;
        this.assetRepository = assetRepository;
        this.budgetRepository = budgetRepository;
    }

    /**
     * 有効な資産リストを作成する．
     * @return 資産のリスト
     * @throws DomainException
     */
    public List<Asset> createValidAssetList() throws DomainException {
        return assetRepository.readAllValidAsset();
    }

    /**
     * 有効な予算リストを作成する．
     * @return 予算のリスト．
     * @throws DomainException
     */
    public List<Budget> createValidBudgetList (int year, int month) throws DomainException {
        return budgetRepository.readValidBudgetByDate(year, month);
    }

    /**
     * レシートを作成し，資産から合計金額分を減算する．
     * @param receipt 作成対象のレシート
     * @throws DomainException
     */
    public void createReceipt(Receipt receipt) throws DomainException {
        if (receipt.canPersistent()) {
            receiptRepository.updateReceipt(receipt);
            Asset asset = receipt.getAsset();
            asset.setAmount(asset.getAmount() - calcTotal(receipt.getDetails()));
            assetRepository.updateAsset(asset);
        } else {
            throw new DomainException("DOM:RA:CR");
        }
    }

    /**
     * 資産から合計金額分を加算し，レシートを削除する．
     * @param receipt 削除対象のレシート．
     * @throws DomainException
     */
    public void deleteReceipt(Receipt receipt) throws DomainException {
        Asset asset = receipt.getAsset();
        asset.setAmount(asset.getAmount() + calcTotal(receipt.getDetails()));
        assetRepository.updateAsset(asset);
        receiptRepository.deleteReceipt(receipt);
    }

    /**
     * レシートの合計金額を計算して返却する．
     * @param details 対象となるレシートの明細リスト
     * @return 合計金額
     */
    public int calcTotal(List<ReceiptDetail> details) {
        int total = 0;
        for (ReceiptDetail detail: details) {
            total = total + detail.getAmount();
        }
        return total;
    }

    /**
     * 消費税を計算する．差分は1つ目の明細にすべて加算する．
     * @param details
     */
    public void calcTax(List<ReceiptDetail> details) {
        int beforeTotal = calcTotal(details);
        int afterTotal = beforeTotal * RATE / 100;
        for (ReceiptDetail detail: details) {
            detail.setAmount(detail.getAmount() * RATE / 100);
        }
        int tempTotal = calcTotal(details);
        details.get(0).setAmount(details.get(0).getAmount() + afterTotal - tempTotal);
    }
}
