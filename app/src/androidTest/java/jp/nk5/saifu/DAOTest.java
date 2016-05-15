package jp.nk5.saifu;

import android.database.sqlite.SQLiteDatabase;
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
import jp.nk5.saifu.infra.AssetDAO;
import jp.nk5.saifu.infra.AssetRepositoryImpl;
import jp.nk5.saifu.infra.BudgetDAO;
import jp.nk5.saifu.infra.BudgetRepositoryImpl;
import jp.nk5.saifu.infra.DAOUtil;
import jp.nk5.saifu.infra.DBHelper;
import jp.nk5.saifu.infra.ReceiptDAO;
import jp.nk5.saifu.infra.ReceiptDetailDAO;

import static junit.framework.Assert.assertEquals;

/**
 * DAOテスト．
 * Created by NK5JP on 2016/04/23.
 */
public class DAOTest {
    @Rule
    public ActivityTestRule<ReceiptActivity> activityTestRule =
            new ActivityTestRule<>(ReceiptActivity.class);

    @Before
    public void BeforeTest()
    {
        DBHelper dbHelper = DBHelper.getInstance(activityTestRule.getActivity());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.onUpgrade(db, dbHelper.getVersion(), dbHelper.getVersion());
    }

    @Test
    public void DAOUtilTest() throws Exception {
        assertEquals(true, DAOUtil.returnBoolFromNum(1));
        assertEquals(false, DAOUtil.returnBoolFromNum(0));
        assertEquals(1, DAOUtil.returnNumFromBool(true));
        assertEquals(0, DAOUtil.returnNumFromBool(false));
    }

    /**
     * 資産関連DAOのテスト．
     * @throws Exception
     */
    @Test
    public void AssetDAOTest() throws Exception {
        AssetDAO assetDAO = new AssetDAO(activityTestRule.getActivity());
        Asset asset1 = new Asset(-1, "testAsset1", 1000, true);
        Asset asset2 = new Asset(-1, "testAsset2", 2000, true);
        Asset asset3 = new Asset(-1, "testAsset3", 3000, true);
        assetDAO.create(asset1);
        assetDAO.create(asset2);
        assetDAO.delete(asset1);
        assetDAO.create(asset3);
        asset2.setAmount(20000);
        asset2.setValid(false);
        assetDAO.update(asset2);
        List<Asset> assets = assetDAO.read();
        assertEquals(2, assets.get(0).getId());
        assertEquals("testAsset2", assets.get(0).getName());
        assertEquals(20000, assets.get(0).getAmount());
        assertEquals(false, assets.get(0).isValid());
        assertEquals(3, assets.get(1).getId());
        assertEquals("testAsset3", assets.get(1).getName());
        assertEquals(3000, assets.get(1).getAmount());
        assertEquals(true, assets.get(1).isValid());
        assertEquals(2, asset2.getId());
        assertEquals(3, asset3.getId());
    }

    /**
     * 予算関連DAOのテスト．
     * @throws Exception
     */
    @Test
    public void BudgetDAOTest() throws Exception {
        BudgetDAO budgetDAO = new BudgetDAO(activityTestRule.getActivity());
        Budget budget1 = new Budget(-1, "testBudget1", 2016, 4, 1000, true);
        Budget budget2 = new Budget(-1, "testBudget2", 2016, 5, 2000, true);
        Budget budget3 = new Budget(-1, "testBudget3", 2016, 6, 3000, true);
        budgetDAO.create(budget1);
        budgetDAO.create(budget2);
        budgetDAO.delete(budget1);
        budgetDAO.create(budget3);
        budget2.setAmount(20000);
        budget2.setValid(false);
        budgetDAO.update(budget2);
        List<Budget> budgets = budgetDAO.read();
        assertEquals(2, budgets.get(0).getId());
        assertEquals("testBudget2", budgets.get(0).getName());
        assertEquals(2016, budgets.get(0).getYear());
        assertEquals(5, budgets.get(0).getMonth());
        assertEquals(20000, budgets.get(0).getAmount());
        assertEquals(false, budgets.get(0).isValid());
        assertEquals(3, budgets.get(1).getId());
        assertEquals("testBudget3", budgets.get(1).getName());
        assertEquals(2016, budgets.get(1).getYear());
        assertEquals(6, budgets.get(1).getMonth());
        assertEquals(3000, budgets.get(1).getAmount());
        assertEquals(true, budgets.get(1).isValid());
        assertEquals(2, budget2.getId());
        assertEquals(3, budget3.getId());
    }

    /**
     * レシート関連のDAOテスト．
     * @throws Exception
     */
    @Test
    public void ReceiptDAOTest() throws Exception {
        AssetRepository assetRepository = new AssetRepositoryImpl(activityTestRule.getActivity());
        Asset asset1 = new Asset(-1, "testAsset1", 10000, true);
        assetRepository.updateAsset(asset1);
        ReceiptDAO receiptDAO = new ReceiptDAO(activityTestRule.getActivity(), assetRepository, 2016, 4);
        Receipt receipt1 = new Receipt(-1, 2016, 4, 29, assetRepository.readAssetById(1), new ArrayList<ReceiptDetail>());
        Receipt receipt2 = new Receipt(-1, 2016, 4, 30, assetRepository.readAssetById(1), new ArrayList<ReceiptDetail>());
        Receipt receipt3 = new Receipt(-1, 2016, 5, 1, assetRepository.readAssetById(1), new ArrayList<ReceiptDetail>());
        receiptDAO.create(receipt1);
        receiptDAO.create(receipt2);
        receiptDAO.delete(receipt1);
        receiptDAO.create(receipt3);
        List<Receipt> receipts = receiptDAO.read();
        assertEquals(2, receipts.get(0).getId());
        assertEquals(2016, receipts.get(0).getYear());
        assertEquals(4, receipts.get(0).getMonth());
        assertEquals(30, receipts.get(0).getDay());
        assertEquals(asset1, receipts.get(0).getAsset());
        receiptDAO.setYearAndMonth(2016, 5);
        receipts = receiptDAO.read();
        assertEquals(3, receipts.get(0).getId());
        assertEquals(2016, receipts.get(0).getYear());
        assertEquals(5, receipts.get(0).getMonth());
        assertEquals(1, receipts.get(0).getDay());
        assertEquals(asset1, receipts.get(0).getAsset());
    }

    @Test
    public void RepositoryDetailDAOTest() throws Exception {
        BudgetRepository budgetRepository = new BudgetRepositoryImpl(activityTestRule.getActivity());
        Budget budget1 = new Budget(-1, "testBudget1", 2016, 4, 10000, true);
        Budget budget2 = new Budget(-1, "testBudget2", 2016, 4, 20000, true);
        budgetRepository.updateBudget(budget1);
        budgetRepository.updateBudget(budget2);
        AssetRepository assetRepository = new AssetRepositoryImpl(activityTestRule.getActivity());
        Asset asset1 = new Asset(-1, "testAsset1", 10000, true);
        assetRepository.updateAsset(asset1);
        ReceiptDAO receiptDAO = new ReceiptDAO(activityTestRule.getActivity(), assetRepository, 2016, 4);
        Receipt receipt1 = new Receipt(-1, 2016, 4, 29, assetRepository.readAssetById(1), new ArrayList<ReceiptDetail>());
        Receipt receipt2 = new Receipt(-1, 2016, 4, 29, assetRepository.readAssetById(1), new ArrayList<ReceiptDetail>());
        Receipt receipt3 = new Receipt(-1, 2016, 4, 29, assetRepository.readAssetById(1), new ArrayList<ReceiptDetail>());
        receiptDAO.create(receipt1);
        receiptDAO.create(receipt2);
        receiptDAO.create(receipt3);
        ReceiptDetail detail1 = new ReceiptDetail(-1, receipt1, budget1, 10000);
        ReceiptDetail detail2 = new ReceiptDetail(-1, receipt1, budget2, 20000);
        ReceiptDetail detail3 = new ReceiptDetail(-1, receipt2, budget1, 30000);
        ReceiptDetail detail4 = new ReceiptDetail(-1, receipt2, budget2, 40000);
        ReceiptDetail detail5 = new ReceiptDetail(-1, receipt3, budget1, 50000);
        ReceiptDetail detail6 = new ReceiptDetail(-1, receipt3, budget2, 60000);
        ReceiptDetailDAO receiptDetailDAO = new ReceiptDetailDAO(activityTestRule.getActivity(), receipt1, budgetRepository);
        receiptDetailDAO.create(detail1);
        receiptDetailDAO.create(detail2);
        receiptDetailDAO.setTargetReceipt(receipt2);
        receiptDetailDAO.create(detail3);
        receiptDetailDAO.create(detail4);
        receiptDetailDAO.setTargetReceipt(receipt1);
        receiptDetailDAO.delete(detail1);
        receiptDetailDAO.setTargetReceipt(receipt3);
        receiptDetailDAO.create(detail5);
        receiptDetailDAO.create(detail6);
        List<ReceiptDetail> details = receiptDetailDAO.read();
        assertEquals(50000, details.get(0).getAmount());
        assertEquals(60000, details.get(1).getAmount());
        receiptDetailDAO.setTargetReceipt(receipt1);
        details = receiptDetailDAO.read();
        assertEquals(0, details.size());
        receiptDetailDAO.setTargetReceipt(receipt2);
        details = receiptDetailDAO.read();
        assertEquals(30000, details.get(0).getAmount());
        assertEquals(40000, details.get(1).getAmount());
    }

}
