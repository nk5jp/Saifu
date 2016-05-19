package jp.nk5.saifu;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import jp.nk5.saifu.adapter.AssetSpinnerAdapter;
import jp.nk5.saifu.adapter.BudgetSpinnerAdapter;
import jp.nk5.saifu.adapter.ReceiptDetailAdapter;
import jp.nk5.saifu.app.ReceiptApplication;
import jp.nk5.saifu.controller.ReceiptController;
import jp.nk5.saifu.domain.Asset;
import jp.nk5.saifu.domain.AssetRepository;
import jp.nk5.saifu.domain.Budget;
import jp.nk5.saifu.domain.BudgetRepository;
import jp.nk5.saifu.domain.Receipt;
import jp.nk5.saifu.domain.ReceiptDetail;
import jp.nk5.saifu.domain.ReceiptRepository;
import jp.nk5.saifu.form.ReceiptForm;
import jp.nk5.saifu.form.UpdateViewListener;
import jp.nk5.saifu.infra.AssetRepositoryImpl;
import jp.nk5.saifu.infra.BudgetRepositoryImpl;
import jp.nk5.saifu.infra.ReceiptRepositoryImpl;

public class ReceiptActivity extends AppCompatActivity implements UpdateViewListener, ListView.OnItemLongClickListener {

    private ReceiptController controller;
    private ReceiptApplication application;
    private ReceiptForm form;
    private ReceiptRepository receiptRepository;
    private AssetRepository assetRepository;
    private BudgetRepository budgetRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            int id = getIntent().getIntExtra("id", -1);
            int year = getIntent().getIntExtra("year", -1);
            int month = getIntent().getIntExtra("month", -1);
            int day = getIntent().getIntExtra("day", -1);
            boolean isCreateMode = (id == -1);
            assetRepository = new AssetRepositoryImpl(this);
            budgetRepository = new BudgetRepositoryImpl(this);
            receiptRepository = new ReceiptRepositoryImpl(this, assetRepository, budgetRepository, year, month);
            application = new ReceiptApplication(receiptRepository, assetRepository, budgetRepository);
            Receipt receipt;
            if (isCreateMode) {
                receipt = new Receipt(-1, year, month, day, null, new ArrayList<ReceiptDetail>());
            } else {
                receipt = receiptRepository.readReceiptById(id);
            }
            form = new ReceiptForm(isCreateMode, null, null, receipt, 0);
            controller = new ReceiptController(application, form, this);
            controller.initializeForm(year, month);
            initializeUI();
        } catch (Exception e) {
            this.finish();
        }
    }

    /**
     * 追加ボタン押下時のイベント処理．
     * @param view
     */
    public void onClickAddButton(View view) {
        if (!form.isCreateMode()) {
            return;
        }
        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        Budget budget = (Budget) spinner.getSelectedItem();
        int amount = getIntFromView(R.id.editText3, 0);
        if (amount == 0 || budget == null) {
            return;
        }
        controller.addDetail(budget, amount);
    }

    /**
     * 追加削除ボタン押下時のイベントハンドラ．
     * @param view
     */
    public void onClickCreateDeleteButton(View view) {
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        Asset asset = (Asset) spinner.getSelectedItem();
        if (asset == null || form.getReceipt().getDetails().size() == 0) {
            return;
        }
        controller.createDeleteReceipt(asset);
        this.finish();
    }

    public void onClickTaxButton(View view) {
        if (!form.isCreateMode()) {
            return;
        }
        if (form.getReceipt().getDetails().size() == 0) {
            return;
        }
        controller.calcTax();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        controller.removeDetail(position);
        return true;
    }

    @Override
    public void updateUI() {
        ListView listView = (ListView) findViewById(R.id.listView3);
        ReceiptDetailAdapter detailAdapter = new ReceiptDetailAdapter(this, android.R.layout.simple_list_item_1, form.getReceipt().getDetails());
        listView.setAdapter(detailAdapter);
        TextView textView = (TextView) findViewById(R.id.textView6);
        textView.setText(getString(R.string.str_total) + ": " + ViewUtil.getMoneyString(form.getTotal()));
        ((EditText) findViewById(R.id.editText3)).getEditableText().clear();
    }

    @Override
    public void showError(String errorMsg) {

    }

    private void initializeUI(){
        AssetSpinnerAdapter assetAdapter = new AssetSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, form.getAssets());
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(assetAdapter);
        BudgetSpinnerAdapter budgetAdapter = new BudgetSpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, form.getBudgets());
        spinner = (Spinner) findViewById(R.id.spinner2);
        spinner.setAdapter(budgetAdapter);
        TextView textView = (TextView) findViewById(R.id.textView6);
        textView.setText(getString(R.string.str_total) + ": " + form.getTotal());
        if (form.isCreateMode()) {
            ListView listView = (ListView) findViewById(R.id.listView3);
            listView.setOnItemLongClickListener(this);
        } else {
            Button button = (Button) findViewById(R.id.button3);
            button.setText(getString(R.string.str_delete));
        }
        updateUI();
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

}
