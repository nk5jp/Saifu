package jp.nk5.saifu;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import jp.nk5.saifu.domain.Asset;
import jp.nk5.saifu.domain.Budget;
import jp.nk5.saifu.domain.Receipt;
import jp.nk5.saifu.domain.ReceiptDetail;

import static junit.framework.Assert.assertEquals;

/**
 * ドメイン層（リポジトリ除く）周りのUT．．
 * Created by NK5JP on 2016/04/13.
 */
@RunWith(AndroidJUnit4.class)
public class DomainTest {

    Receipt receipt;
    Asset asset;
    Budget budget1;
    Budget budget2;

    /**
     * 試験に先立ち，各種エンティティを初期化する．
     */
    @Before
    public void BeforeTest() {
        asset = new Asset(1, "財布", 10000, true);
        budget1 = new Budget(1, "食費", 2016, 4, 10000, true);
        budget2 = new Budget(2, "交通費", 2016, 4, 5000, true);
        receipt = new Receipt(1, 2016, 4, 17, asset, new ArrayList<ReceiptDetail>());
        receipt.getDetails().add(new ReceiptDetail(1, receipt, budget1, 1000));
        receipt.getDetails().add(new ReceiptDetail(2, receipt, budget2, 500));
    }

    /**
     * Receiptの各種ゲッター確認テスト．
     * @throws Exception
     */
    @Test
    public void ReceiptGetterTest() throws Exception {
        assertEquals(1, receipt.getId());
        assertEquals(2016, receipt.getYear());
        assertEquals(4, receipt.getMonth());
        assertEquals(17, receipt.getDay());
    }

    /**
     * レシート明細の各種ゲッター確認テスト．
     * @throws Exception
     */
    @Test
    public void ReceiptDetailGetterTest() throws Exception {
        assertEquals(1, receipt.getDetails().get(0).getId());
        assertEquals("食費", receipt.getDetails().get(0).getBudget().getName());
        assertEquals(1000, receipt.getDetails().get(0).getAmount());
    }

    /**
     * 資産の各種ゲッターセッター確認テスト
     * @throws Exception
     */
    @Test
    public void AssetGetterSetterTest() throws Exception {
        assertEquals(1, asset.getId());
        assertEquals("財布", asset.getName());
        assertEquals(10000, asset.getAmount());
        assertEquals(true, asset.isValid());
        asset.setAmount(5000);
        assertEquals(5000, asset.getAmount());
        asset.setValid(false);
        assertEquals(false, asset.isValid());
    }

    /**
     * 予算の各種ゲッターセッター確認テスト
     * @throws Exception
     */
    @Test
    public void BudgetGetterSetterTest() throws Exception {
        assertEquals(1, budget1.getId());
        assertEquals("食費", budget1.getName());
        assertEquals(2016, budget1.getYear());
        assertEquals(4, budget1.getMonth());
        assertEquals(10000, budget1.getAmount());
        assertEquals(true, budget1.isValid());
        budget1.setAmount(20000);
        assertEquals(20000, budget1.getAmount());
        budget1.setValid(false);
        assertEquals(false, budget1.isValid());
    }

    /**
     * 資産エンティティの永続化可否判定の妥当性テスト．
     * @throws Exception
     */
    @Test
    public void AssetCanPersistentTest() throws Exception {
        Asset okAsset = new Asset(1, "ok", 100, true);
        assertEquals(true, okAsset.canPersistent());
        okAsset = new Asset(-1, "ok", 100, true);
        assertEquals(true, okAsset.canPersistent());
        okAsset = new Asset(1, "ok", -100, true);
        assertEquals(true, okAsset.canPersistent());
        okAsset = new Asset(1, "ok", 100, false);
        assertEquals(true, okAsset.canPersistent());
        Asset ngAsset = new Asset(1, "", 100, true);
        assertEquals(false, ngAsset.canPersistent());
    }

    /**
     * 予算エンティティの永続化可否判定の妥当性テスト．
     * @throws Exception
     */
    @Test
    public void BudgetCanPersistentTest() throws Exception {
        Budget okBudget = new Budget(1, "test", 2016, 4, 10000, true);
        assertEquals(true, okBudget.canPersistent());
        okBudget = new Budget(-1, "test", 2016, 4, 10000, true);
        assertEquals(true, okBudget.canPersistent());
        okBudget = new Budget(1, "test", 2016, 4, 10000, false);
        assertEquals(true, okBudget.canPersistent());
        Budget ngBudget = new Budget(1, "", 2016, 4, 10000, true);
        assertEquals(false, ngBudget.canPersistent());
        ngBudget = new Budget(1, "test", -2016, 4, 10000, true);
        assertEquals(false, ngBudget.canPersistent());
        ngBudget = new Budget(1, "test", 2016, -4, 10000, true);
        assertEquals(false, ngBudget.canPersistent());
        ngBudget = new Budget(1, "test", 2016, 4, -10000, true);
        assertEquals(false, ngBudget.canPersistent());
    }
}
