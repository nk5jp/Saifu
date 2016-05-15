package jp.nk5.saifu;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import jp.nk5.saifu.app.AssetApplication;
import jp.nk5.saifu.app.BudgetApplication;
import jp.nk5.saifu.app.CalendarApplication;
import jp.nk5.saifu.app.ReceiptApplication;
import jp.nk5.saifu.domain.Asset;
import jp.nk5.saifu.domain.AssetRepository;
import jp.nk5.saifu.domain.Budget;
import jp.nk5.saifu.domain.BudgetRepository;
import jp.nk5.saifu.domain.Receipt;
import jp.nk5.saifu.domain.ReceiptDetail;
import jp.nk5.saifu.domain.ReceiptRepository;
import jp.nk5.saifu.form.PlanActualDTO;
import jp.nk5.saifu.infra.AssetRepositoryImpl;
import jp.nk5.saifu.infra.BudgetRepositoryImpl;
import jp.nk5.saifu.infra.DBHelper;
import jp.nk5.saifu.infra.ReceiptRepositoryImpl;

import static junit.framework.Assert.assertEquals;

/**
 * Created by NK5JP on 2016/05/02.
 */

@RunWith(AndroidJUnit4.class)
public class AppTest {

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
     * 予算アプリケーション関連テスト
     * @throws Exception
     */
    @Test
    public void BudgetApplicationTest() throws Exception {
        AssetRepository assetRepository = new AssetRepositoryImpl(activityTestRule.getActivity());
        BudgetRepository budgetRepository = new BudgetRepositoryImpl(activityTestRule.getActivity());
        ReceiptRepository receiptRepository = new ReceiptRepositoryImpl(activityTestRule.getActivity(), assetRepository, budgetRepository, 2016, 5);
        BudgetApplication application = new BudgetApplication(receiptRepository, budgetRepository);
        application.updateBudget(-1, "testBudget1", 2016, 5, 10000, true);
        application.updateBudget(-1, "testBudget2", 2016, 5, 20000, false);
        application.updateBudget(-1, "testBudget3", 2016, 6, 30000, true);
        Asset asset1 = new Asset(-1, "testAsset1", 1000, true);
        assetRepository.updateAsset(asset1);
        Receipt receipt1 = new Receipt(-1, 2016, 5, 1, asset1, new ArrayList<ReceiptDetail>());
        Receipt receipt2 = new Receipt(-1, 2016, 6, 1, asset1, new ArrayList<ReceiptDetail>());
        receipt1.getDetails().add(new ReceiptDetail(-1, receipt1, budgetRepository.readBudgetById(1), 1000));
        receipt1.getDetails().add(new ReceiptDetail(-1, receipt1, budgetRepository.readBudgetById(1), 1000));
        receipt1.getDetails().add(new ReceiptDetail(-1, receipt1, budgetRepository.readBudgetById(2), 3000));
        receipt2.getDetails().add(new ReceiptDetail(-1, receipt2, budgetRepository.readBudgetById(3), 1000));
        receiptRepository.updateReceipt(receipt1);
        receiptRepository.updateReceipt(receipt2);
        List<PlanActualDTO> DTOs = application.createPlanActualList(2016, 5);
        int total = application.calcTotal(DTOs);
        assertEquals(5000, total);
        assertEquals(2, DTOs.size());
        assertEquals(2000, DTOs.get(0).getActualAmount());
        assertEquals(10000, DTOs.get(0).getBudget().getAmount());
        assertEquals(3000, DTOs.get(1).getActualAmount());
        assertEquals(20000, DTOs.get(1).getBudget().getAmount());
    }

    @Test
    public void CalendarApplicationTest() throws Exception {
        AssetRepository assetRepository = new AssetRepositoryImpl(activityTestRule.getActivity());
        BudgetRepository budgetRepository = new BudgetRepositoryImpl(activityTestRule.getActivity());
        ReceiptRepository receiptRepository = new ReceiptRepositoryImpl(activityTestRule.getActivity(), assetRepository, budgetRepository, 2016, 5);
        CalendarApplication application = new CalendarApplication(receiptRepository);
        Asset asset1 = new Asset(-1, "testAsset1", 10000, true);
        assetRepository.updateAsset(asset1);
        Receipt receipt1 = new Receipt(-1, 2016, 5, 6, asset1, new ArrayList<ReceiptDetail>());
        Receipt receipt2 = new Receipt(-1, 2016, 5, 7, asset1, new ArrayList<ReceiptDetail>());
        Receipt receipt3 = new Receipt(-1, 2016, 5, 6, asset1, new ArrayList<ReceiptDetail>());
        receiptRepository.updateReceipt(receipt1);
        receiptRepository.updateReceipt(receipt2);
        receiptRepository.updateReceipt(receipt3);
        List<Receipt> receipts = application.createReceiptList(2016, 5, 6);
        assertEquals(receipt1, receipts.get(0));
        assertEquals(receipt3, receipts.get(1));
    }

    @Test
    public void AssetApplicationTest() throws Exception {
        AssetRepository assetRepository = new AssetRepositoryImpl(activityTestRule.getActivity());
        AssetApplication application = new AssetApplication(assetRepository);
        application.updateAsset(-1, "testAsset1", 10000, true);
        application.updateAsset(-1, "testAsset2", 20000, true);
        List<Asset> assets = application.createAssetList();
        application.switchAssetValidation(assets.get(0));
        application.transferMoney(assets.get(0), assets.get(1), 5000);
        assertEquals(5000, assets.get(0).getAmount());
        assertEquals(25000, assets.get(1).getAmount());
        assertEquals(30000, application.calcTotal(assets));
        assertEquals(false, assets.get(0).isValid());
    }

    @Test
    public void ReceiptApplicationTest() throws Exception {
        AssetRepository assetRepository = new AssetRepositoryImpl(activityTestRule.getActivity());
        BudgetRepository budgetRepository = new BudgetRepositoryImpl(activityTestRule.getActivity());
        ReceiptRepository receiptRepository = new ReceiptRepositoryImpl(activityTestRule.getActivity(), assetRepository, budgetRepository, 2016, 5);
        ReceiptApplication application = new ReceiptApplication(receiptRepository, assetRepository, budgetRepository);
        assetRepository.updateAsset(new Asset(-1, "testAsset1", 100, true));
        assetRepository.updateAsset(new Asset(-1, "testAsset2", 100, false));
        budgetRepository.updateBudget(new Budget(-1, "testBudget1", 2016, 5, 100, true));
        budgetRepository.updateBudget(new Budget(-1, "testBudget2", 2016, 5, 100, false));
        assertEquals(1, application.createValidAssetList().size());
        assertEquals(1, application.createValidBudgetList(2016, 5).size());
        Asset asset =  assetRepository.readAssetById(1);
        Receipt receipt = new Receipt(-1, 2016, 5, 14, asset, new ArrayList<ReceiptDetail>());
        Budget budget = budgetRepository.readBudgetById(1);
        ReceiptDetail detail1 = new ReceiptDetail(-1, receipt, budget, 12);
        ReceiptDetail detail2 = new ReceiptDetail(-1, receipt, budget, 24);
        ReceiptDetail detail3 = new ReceiptDetail(-1, receipt, budget, 37);
        receipt.getDetails().add(detail1);
        receipt.getDetails().add(detail2);
        receipt.getDetails().add(detail3);
        application.calcTax(receipt.getDetails());
        assertEquals(78, application.calcTotal(receipt.getDetails()));
        assertEquals(14, detail1.getAmount());
        assertEquals(25, detail2.getAmount());
        assertEquals(39, detail3.getAmount());
        application.createReceipt(receipt);
        assertEquals(22, asset.getAmount());
        application.deleteReceipt(receipt);
        assertEquals(100, asset.getAmount());
    }
}
