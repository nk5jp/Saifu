package jp.nk5.saifu;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;

import java.util.Calendar;

import jp.nk5.saifu.adapter.PlanActualAdapter;
import jp.nk5.saifu.app.BudgetApplication;
import jp.nk5.saifu.controller.MainController;
import jp.nk5.saifu.domain.AssetRepository;
import jp.nk5.saifu.domain.BudgetRepository;
import jp.nk5.saifu.domain.ReceiptRepository;
import jp.nk5.saifu.form.MainForm;
import jp.nk5.saifu.form.UpdateViewListener;
import jp.nk5.saifu.infra.AssetRepositoryImpl;
import jp.nk5.saifu.infra.BudgetRepositoryImpl;
import jp.nk5.saifu.infra.ReceiptRepositoryImpl;

/**
 * メイン画面のアクティビティ．
 */
public class MainActivity extends Activity implements UpdateViewListener {

    private MainController controller;
    private BudgetApplication application;
    private MainForm form;
    private ReceiptRepository receiptRepository;
    private BudgetRepository budgetRepository;
    private AssetRepository assetRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * カレンダアイコン押下時のイベント処理．
     * @param view
     */
    public void onClickCalendarButton (View view) {
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
    }

    /**
     * 予算アイコン押下時のイベント処理．
     * @param view
     */
    public void onClickBudgetButton (View view) {
        Intent intent = new Intent(this, BudgetActivity.class);
        startActivity(intent);
    }

    /**
     * 振込アイコン押下時のイベント処理．
     * @param view
     */
    public void onClickTransferButton (View view) {
        Intent intent = new Intent(this, TransferActivity.class);
        startActivity(intent);
    }

    /**
     * 資産ボタン押下時のイベント処理．
     * @param view
     */
    public void onClickAssetButton (View view) {
        Intent intent = new Intent(this, AssetActivity.class);
        startActivity(intent);
    }

    /**
     * Readボタン押下時のイベント処理．
     * @param view
     */
    public void onClickReadButton(View view) {
        int year = getIntFromView(R.id.editText, -1);
        int month = getIntFromView(R.id.editText2, -1);
        if (year >= 0 && month >= 0) {
            controller.updatePlanActualList(year, month);
        }
    }

    /**
     * フォームの内容に応じてuIを更新する．
     */
    public void updateUI() {
        setIntToView(R.id.editText, form.getYear());
        setIntToView(R.id.editText2, form.getMonth());
        PlanActualAdapter adapter = new PlanActualAdapter(this, R.layout.list_textwithicon, form.getDTOs());
        ListView listView = (ListView)this.findViewById(R.id.listView);
        listView.setAdapter(adapter);
        ((TextView) findViewById(R.id.textView)).setText(getString(R.string.str_total) + ": " + ViewUtil.getMoneyString(form.getTotal()));
    }

    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    /**
     * EditTextの値を取得してint型として返す．空白時はerrorNumberを返す．
     * @param id 対象とするEditTextのID．
     * @param errorNumber 空白時の返却整数値．
     * @return EditTextに格納されている数字．
     */
    private int getIntFromView(int id, int errorNumber) {
        EditText editText = (EditText) findViewById(id);
        if (!editText.getText().toString().equals("")) {
            return Integer.parseInt(editText.getText().toString());
        } else {
            return errorNumber;
        }
    }

    /**
     * ビューに値を格納する．
     * @param id 対象とするEditTextのID．
     * @param number 格納する数字．
     */
    private void setIntToView(int id, int number) {
        EditText editText = (EditText) findViewById(id);
        editText.setText(Integer.toString(number));
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            form = new MainForm(year, month, 0, null);
            assetRepository = new AssetRepositoryImpl(this);
            budgetRepository = new BudgetRepositoryImpl(this);
            receiptRepository = new ReceiptRepositoryImpl(this, assetRepository, budgetRepository, year, month);
            application = new BudgetApplication(receiptRepository, budgetRepository);
            controller = new MainController(application, form,  this);
            controller.updatePlanActualList(year, month);
        } catch (Exception e) {
            this.finish();
        }
    }

}
