package jp.nk5.saifu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;

import jp.nk5.saifu.adapter.PlanActualAdapter;
import jp.nk5.saifu.app.BudgetApplication;
import jp.nk5.saifu.controller.BudgetController;
import jp.nk5.saifu.domain.AssetRepository;
import jp.nk5.saifu.domain.Budget;
import jp.nk5.saifu.domain.BudgetRepository;
import jp.nk5.saifu.domain.ReceiptRepository;
import jp.nk5.saifu.form.BudgetForm;
import jp.nk5.saifu.form.PlanActualDTO;
import jp.nk5.saifu.form.UpdateViewListener;
import jp.nk5.saifu.infra.AssetRepositoryImpl;
import jp.nk5.saifu.infra.BudgetRepositoryImpl;
import jp.nk5.saifu.infra.ReceiptRepositoryImpl;

public class BudgetActivity extends Activity implements UpdateViewListener, ListView.OnItemClickListener {

    private BudgetController controller;
    private BudgetApplication application;
    private BudgetForm form;
    private ReceiptRepository receiptRepository;
    private BudgetRepository budgetRepository;
    private AssetRepository assetRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        ListView listView = (ListView) findViewById(R.id.listView4);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            form = new BudgetForm(-1, year, month, null, "", 0, true);
            assetRepository = new AssetRepositoryImpl(this);
            budgetRepository = new BudgetRepositoryImpl(this);
            receiptRepository = new ReceiptRepositoryImpl(this, assetRepository, budgetRepository, year, month);
            application = new BudgetApplication(receiptRepository, budgetRepository);
            controller = new BudgetController(application, form, this);
            controller.updatePlanActualList(year, month);
        } catch (Exception e) {
            this.finish();
        }
    }

    public void onClickReadButton(View view) {
        int year = getIntFromView(R.id.editText4, -1);
        int month = getIntFromView(R.id.editText5, -1);
        if (year >= 0 && month >= 0) {
            controller.updatePlanActualList(year, month);
        }
    }

    public void onClickCreateButton(View view) {
        int year = getIntFromView(R.id.editText4, -1);
        int month = getIntFromView(R.id.editText5, -1);
        int amount = getIntFromView(R.id.editText7, -1);
        String name = getStringFromView(R.id.editText6, "");
        boolean isValid = getCheckedFromView(R.id.checkBox);
        controller.updateBudget(year, month, name, amount, isValid);
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Budget budget = ((PlanActualDTO) parent.getItemAtPosition(position)).getBudget();
        int selectedId = budget.getId();
        String name = budget.getName();
        int amount = budget.getAmount();
        boolean isValid = budget.isValid();
        controller.clearBudget(selectedId, name, amount, isValid);
    }

    public void onClickClearButton(View view) {
        controller.clearBudget(-1, "", 0, true);
    }

    public void updateUI() {
        setIntToView(R.id.editText4, form.getYear());
        setIntToView(R.id.editText5, form.getMonth());
        setIntToView(R.id.editText7, form.getAmount());
        setStringToView(R.id.editText6, form.getName());
        setCheckedToView(R.id.checkBox);
        PlanActualAdapter adapter = new PlanActualAdapter(this, R.layout.list_textwithicon, form.getDTOs());
        ListView listView = (ListView)this.findViewById(R.id.listView4);
        listView.setAdapter(adapter);
        Button button = (Button) findViewById(R.id.button6);
        EditText editText = (EditText) findViewById(R.id.editText6);
        if (form.getSelectedId() < 0) {
            button.setText(getString(R.string.str_create));
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
        } else {
            button.setText(getString(R.string.str_update));
            editText.setFocusable(false);
            editText.setFocusableInTouchMode(false);
        }
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

    private String getStringFromView(int id, String errorName) {
        EditText editText = (EditText) findViewById(id);
        if (!editText.getText().toString().equals("")) {
            return editText.getText().toString();
        } else {
            return errorName;
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

    private void setStringToView(int id, String string) {
        EditText editText = (EditText) findViewById(id);
        editText.setText(string);
    }

    private boolean getCheckedFromView(int id) {
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
        return checkBox.isChecked();
    }

    private void setCheckedToView(int id) {
        CheckBox checkBox = (CheckBox) findViewById(R.id.checkBox);
        checkBox.setChecked(form.isValid());
    }

}
