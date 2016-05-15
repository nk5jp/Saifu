package jp.nk5.saifu;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import jp.nk5.saifu.domain.Asset;
import jp.nk5.saifu.domain.AssetRepository;
import jp.nk5.saifu.domain.Budget;
import jp.nk5.saifu.domain.BudgetRepository;
import jp.nk5.saifu.domain.Receipt;
import jp.nk5.saifu.domain.ReceiptDetail;
import jp.nk5.saifu.domain.ReceiptRepository;
import jp.nk5.saifu.infra.AssetRepositoryImpl;
import jp.nk5.saifu.infra.BudgetRepositoryImpl;
import jp.nk5.saifu.infra.DBHelper;
import jp.nk5.saifu.infra.ReceiptRepositoryImpl;

import static junit.framework.Assert.assertEquals;

/**
 * Repository層のテスト．
 * Created by NK5JP on 2016/04/17.
 */
public class RepositoryTest {
    @Rule
    public ActivityTestRule<ReceiptActivity> activityTestRule =
            new ActivityTestRule<>(ReceiptActivity.class);

    @Before
    public void BeforeTest()
    {
        DBHelper dbHelper = DBHelper.getInstance(activityTestRule.getActivity());
        dbHelper.onUpgrade(dbHelper.getWritableDatabase(), dbHelper.getVersion(), dbHelper.getVersion());
    }

    /**
     * 資産リポジトリ関連テスト
     * @throws Exception
     */
    @Test
    public void AssetRepositoryTest() throws Exception {
        AssetRepository repository = new AssetRepositoryImpl(activityTestRule.getActivity());
        repository.updateAsset(new Asset(-1, "testAsset1", 1000, true));
        repository.updateAsset(new Asset(-1, "testAsset2", 2000, true));
        Asset asset = repository.readAssetById(1);
        asset.setAmount(3000);
        asset.setValid(false);
        repository.updateAsset(asset);
        List<Asset> assets1 = repository.readAllAsset();
        List<Asset> assets2 = repository.readAllValidAsset();
        assertEquals(assets1.get(0), repository.readAssetById(1));
        assertEquals(assets1.get(1), repository.readAssetById(2));
        assertEquals(assets2.get(0), repository.readAssetById(2));
    }

    /**
     * 予算リポジトリのテスト
     * @throws Exception
     */
    @Test
    public void BudgetRepositoryTest() throws Exception {
        BudgetRepository repository = new BudgetRepositoryImpl(activityTestRule.getActivity());
        repository.updateBudget(new Budget(-1, "testBudget1", 2016, 4, 10000, true));
        repository.updateBudget(new Budget(-1, "testBudget2", 2016, 4, 20000, true));
        repository.updateBudget(new Budget(-1, "testBudget3", 2016, 5, 30000, true));
        Budget budget = repository.readBudgetById(1);
        budget.setAmount(3000);
        budget.setValid(false);
        repository.updateBudget(budget);
        List<Budget> budgets1 = repository.readBudgetByDate(2016, 4);
        List<Budget> budgets2 = repository.readValidBudgetByDate(2016, 4);
        assertEquals(budgets1.get(0), repository.readBudgetById(1));
        assertEquals(budgets1.get(1), repository.readBudgetById(2));
        assertEquals(3000, repository.readBudgetById(1).getAmount());
        assertEquals(budgets2.get(0), repository.readBudgetById(2));
    }

    /**
     * レシートリポジトリのテスト．
     * @throws Exception
     */
    @Test
    public void ReceiptRepositoryTest() throws Exception {
        BudgetRepository budgetRepository = new BudgetRepositoryImpl(activityTestRule.getActivity());
        budgetRepository.updateBudget(new Budget(-1, "testBudget1", 2016, 4, 1000, true));
        AssetRepository assetRepository = new AssetRepositoryImpl(activityTestRule.getActivity());
        assetRepository.updateAsset(new Asset(-1, "testAsset1", 10000, true));
        ReceiptRepository repository = new ReceiptRepositoryImpl(activityTestRule.getActivity(), assetRepository, budgetRepository, 2016, 4);
        Receipt receipt1 = new Receipt(-1, 2016, 4, 29, assetRepository.readAssetById(1), new ArrayList<ReceiptDetail>());
        receipt1.getDetails().add(new ReceiptDetail(-1, receipt1, budgetRepository.readBudgetById(1), 1000));
        receipt1.getDetails().add(new ReceiptDetail(-1, receipt1, budgetRepository.readBudgetById(1), 2000));
        Receipt receipt2 = new Receipt(-1, 2016, 4, 30, assetRepository.readAssetById(1), new ArrayList<ReceiptDetail>());
        receipt2.getDetails().add(new ReceiptDetail(-1, receipt2, budgetRepository.readBudgetById(1), 3000));
        receipt2.getDetails().add(new ReceiptDetail(-1, receipt2, budgetRepository.readBudgetById(1), 4000));
        //本来は禁止している状態（4月の予算で5月のレシートを作成している）
        Receipt receipt3 = new Receipt(-1, 2016, 5, 1, assetRepository.readAssetById(1), new ArrayList<ReceiptDetail>());
        receipt3.getDetails().add(new ReceiptDetail(-1, receipt2, budgetRepository.readBudgetById(1), 5000));
        receipt3.getDetails().add(new ReceiptDetail(-1, receipt2, budgetRepository.readBudgetById(1), 6000));
        repository.updateReceipt(receipt1);
        repository.updateReceipt(receipt2);
        repository.updateReceipt(receipt3);
        List<Receipt> receipts = repository.readReceiptByDate(2016, 4);
        assertEquals(2, receipts.size());
        receipts = repository.readReceiptByDate(2016, 5);
        assertEquals(1, receipts.size());
        receipts = repository.readReceiptByDate(2016, 4, 30);
        assertEquals(1, receipts.size());
        receipt1 = repository.readReceiptByDate(2016, 4, 29).get(0);
        repository.deleteReceipt(receipt1);
        receipts = repository.readReceiptByDate(2016, 4);
        assertEquals(3000, receipts.get(0).getDetails().get(0).getAmount());
        assertEquals(4000, receipts.get(0).getDetails().get(1).getAmount());
    }

}
