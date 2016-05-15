package jp.nk5.saifu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;

import jp.nk5.saifu.adapter.ReceiptAdapter;
import jp.nk5.saifu.app.CalendarApplication;
import jp.nk5.saifu.controller.CalendarController;
import jp.nk5.saifu.domain.AssetRepository;
import jp.nk5.saifu.domain.BudgetRepository;
import jp.nk5.saifu.domain.Receipt;
import jp.nk5.saifu.domain.ReceiptRepository;
import jp.nk5.saifu.form.CalendarForm;
import jp.nk5.saifu.form.UpdateViewListener;
import jp.nk5.saifu.infra.AssetRepositoryImpl;
import jp.nk5.saifu.infra.BudgetRepositoryImpl;
import jp.nk5.saifu.infra.ReceiptRepositoryImpl;

public class CalendarActivity extends Activity implements UpdateViewListener, CalendarView.OnDateChangeListener, ListView.OnItemClickListener {

    private CalendarForm form;
    private ReceiptRepository receiptRepository;
    private AssetRepository assetRepository;
    private BudgetRepository budgetRepository;
    private CalendarApplication application;
    private CalendarController controller;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //リスナーのDI（このActivityで実装する）
        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(this);
        ListView listView = (ListView) findViewById(R.id.listView2);
        listView.setOnItemClickListener(this);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        form = new CalendarForm(year, month, day, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            assetRepository = new AssetRepositoryImpl(this);
            budgetRepository = new BudgetRepositoryImpl(this);
            receiptRepository = new ReceiptRepositoryImpl(this, assetRepository, budgetRepository, form.getYear(), form.getMonth());
            application = new CalendarApplication(receiptRepository);
            controller = new CalendarController(application, form, this);
            controller.updateReceiptList(form.getYear(), form.getMonth(), form.getDay());
        } catch (Exception e) {
            this.finish();
        }
    }

    public void onClickCreateButton(View view) {
        Intent intent = new Intent(this, ReceiptActivity.class);
        intent.putExtra("id", -1);
        intent.putExtra("year", form.getYear());
        intent.putExtra("month", form.getMonth());
        intent.putExtra("day", form.getDay());
        startActivity(intent);
    }

    public void updateUI() {
        ReceiptAdapter adapter = new ReceiptAdapter(this, android.R.layout.simple_list_item_1, form.getReceipts());
        ListView listView = (ListView)this.findViewById(R.id.listView2);
        listView.setAdapter(adapter);
    }

    public void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    /**
     * 日付選択時のイベント処理．
     * @param view 対象となるカレンダービュー．
     * @param year 選択年．
     * @param month 0～11である点に注意．5月は4となる．
     * @param day 選択日．
     */
    public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
        controller.updateReceiptList(year, month + 1, day);
    }

    /**
     * ListView内のアイテム選択時のイベント処理．
     * @param parent ListView
     * @param view ListView内の各要素
     * @param position 選択位置
     * @param id 選択したビューのid
     */
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Receipt receipt = (Receipt)((ListView) parent).getItemAtPosition(position);
        Intent intent = new Intent(this, ReceiptActivity.class);
        intent.putExtra("id", receipt.getId());
        intent.putExtra("year", form.getYear());
        intent.putExtra("month", form.getMonth());
        intent.putExtra("day", form.getDay());
        startActivity(intent);
    }

}
