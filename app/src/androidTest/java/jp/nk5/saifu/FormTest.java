package jp.nk5.saifu;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import jp.nk5.saifu.domain.Asset;
import jp.nk5.saifu.domain.Budget;
import jp.nk5.saifu.domain.Receipt;
import jp.nk5.saifu.domain.ReceiptDetail;
import jp.nk5.saifu.form.AssetForm;
import jp.nk5.saifu.form.BudgetForm;
import jp.nk5.saifu.form.CalendarForm;
import jp.nk5.saifu.form.MainForm;
import jp.nk5.saifu.form.PlanActualDTO;
import jp.nk5.saifu.form.ReceiptForm;
import jp.nk5.saifu.form.TransferForm;

import static junit.framework.Assert.assertEquals;

/**
 * Created by NK5JP on 2016/05/02.
 */
@RunWith(AndroidJUnit4.class)
public class FormTest {

    /**
     * ViewUtilのテスト．
     * Formではないが，他に置き場所がなかったのでここに・・・
     * @throws Exception
     */
    @Test
    public void ViewUtilTest() throws Exception {
        assertEquals("0", ViewUtil.getMoneyString(0));
        assertEquals("999", ViewUtil.getMoneyString(999));
        assertEquals("1,000", ViewUtil.getMoneyString(1000));
        assertEquals("999,999", ViewUtil.getMoneyString(999999));
        assertEquals("1,000,000", ViewUtil.getMoneyString(1000000));
    }

    /**
     * メイン画面フォーム関連テスト．
     * @throws Exception
     */
    @Test
    public void MainFormTest() throws Exception {
        Budget budget1 = new Budget(-1, "testBudget1", 2016, 4, 10000, true);
        MainForm mainForm = new MainForm(2016, 4, 0, 0, new ArrayList<PlanActualDTO>());
        mainForm.getDTOs().add(new PlanActualDTO(budget1, 125));
        mainForm.setYear(2017);
        mainForm.setMonth(5);
        mainForm.setActualTotal(1000);
        mainForm.setPlanTotal(1200);
        assertEquals(2017, mainForm.getYear());
        assertEquals(5, mainForm.getMonth());
        assertEquals(1000, mainForm.getActualTotal());
        assertEquals(1200, mainForm.getPlanTotal());
        assertEquals(budget1, mainForm.getDTOs().get(0).getBudget());
    }

    /**
     * カレンダー画面フォーム関連テスト．
     * @throws Exception
     */
    @Test
    public void CalendarFormTest() throws Exception {
        Receipt receipt1 = new Receipt(-1, 2016, 5, 4, new Asset(-1, "testAsset", 1000, true), new ArrayList<ReceiptDetail>());
        CalendarForm calendarForm = new CalendarForm(2016, 0, 0, new ArrayList<Receipt>());
        calendarForm.getReceipts().add(receipt1);
        calendarForm.setYear(2017);
        calendarForm.setMonth(5);
        calendarForm.setDay(4);
        assertEquals(2017, calendarForm.getYear());
        assertEquals(5, calendarForm.getMonth());
        assertEquals(4, calendarForm.getDay());
        assertEquals(receipt1, calendarForm.getReceipts().get(0));
    }

    /**
     * レシート画面フォーム関連テスト
     * @throws Exception
     */
    @Test
    public void ReceiptFormTest() throws Exception {
        Receipt receipt1 = new Receipt(-1, 2016, 5, 8, new Asset(-1, "testAsset1", 0, true), new ArrayList<ReceiptDetail>());
        ReceiptForm receiptForm = new ReceiptForm(true, null, null, receipt1, 1000);
        receiptForm.setCreateMode(false);
        receiptForm.setTotal(2000);
        assertEquals(false, receiptForm.isCreateMode());
        assertEquals(2000, receiptForm.getTotal());
    }

    /**
     * 予算画面フォーム関連テスト
     */
    @Test
    public void BudgetFormTest() throws Exception {
        Budget budget1 = new Budget(-1, "testBudget1", 2016, 5, 1000, true);
        BudgetForm budgetForm = new BudgetForm(0, 2016, 5, new ArrayList<PlanActualDTO>(), "testBudget2", 100, false);
        budgetForm.getDTOs().add(new PlanActualDTO(budget1, 100));
        budgetForm.setSelectedId(1);
        budgetForm.setYear(2017);
        budgetForm.setMonth(6);
        budgetForm.setName("testBudget3");
        budgetForm.setAmount(200);
        budgetForm.setValid(true);
        assertEquals(1, budgetForm.getSelectedId());
        assertEquals(2017, budgetForm.getYear());
        assertEquals(6, budgetForm.getMonth());
        assertEquals("testBudget3", budgetForm.getName());
        assertEquals(200, budgetForm.getAmount());
        assertEquals(true, budgetForm.isValid());
        assertEquals(budget1, budgetForm.getDTOs().get(0).getBudget());
    }

    /**
     * 資産画面フォーム関連テスト
     * @throws Exception
     */
    @Test
    public void AssetFormTest() throws Exception {
        AssetForm assetForm = new AssetForm("", 10000, new ArrayList<Asset>());
        Asset asset1 = new Asset(-1, "testAsset1", 1000, true);
        assetForm.setName("test");
        assetForm.setTotal(20000);
        assetForm.getAssets().add(asset1);
        assertEquals("test", assetForm.getName());
        assertEquals(20000, assetForm.getTotal());
        assertEquals(asset1, assetForm.getAssets().get(0));
    }

    /**
     * 振込画面フォーム関連テスト
     * @throws Exception
     */
    @Test
    public void TransferFormTest() throws Exception {
        Asset asset1 = new Asset(-1, "testAsset1", 1000, true);
        Asset asset2 = new Asset(-1, "testAsset2", 2000, true);
        TransferForm transferForm = new TransferForm(null, null, 100, new ArrayList<Asset>());
        transferForm.setFromAsset(asset1);
        transferForm.setToAsset(asset2);
        transferForm.setAmount(500);
        transferForm.getAssets().add(asset1);
        assertEquals(asset1, transferForm.getFromAsset());
        assertEquals(asset2, transferForm.getToAsset());
        assertEquals(500, transferForm.getAmount());
        assertEquals(asset1, transferForm.getAssets().get(0));
    }

}
