package jp.nk5.saifu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import jp.nk5.saifu.adapter.AssetAdapter;
import jp.nk5.saifu.app.AssetApplication;
import jp.nk5.saifu.controller.TransferController;
import jp.nk5.saifu.domain.Asset;
import jp.nk5.saifu.domain.AssetRepository;
import jp.nk5.saifu.form.TransferForm;
import jp.nk5.saifu.form.UpdateViewListener;
import jp.nk5.saifu.infra.AssetRepositoryImpl;

public class TransferActivity extends Activity implements UpdateViewListener, ListView.OnItemClickListener {

    private TransferController controller;
    private AssetApplication application;
    private TransferForm form;
    private AssetRepository assetRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        ListView listView = (ListView) findViewById(R.id.listView6);
        listView.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            assetRepository = new AssetRepositoryImpl(this);
            form = new TransferForm(null, null, 0, new ArrayList<Asset>());
            application = new AssetApplication(assetRepository);
            controller = new TransferController(application, form, this);
            controller.updateAssetList();
        } catch (Exception e) {
            this.finish();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Asset asset = (Asset) parent.getItemAtPosition(position);
        controller.setAssetForTransfer(asset);
    }

    @Override
    public void updateUI() {
        setAssetName(R.id.textView4, form.getToAsset(), getString(R.string.str_to));
        setAssetName(R.id.textView5, form.getFromAsset(), getString(R.string.str_from));
        setIntToView(R.id.editText9, form.getAmount());
        AssetAdapter adapter = new AssetAdapter(this, R.layout.list_textwithicon, form.getAssets());
        ListView listView = (ListView)this.findViewById(R.id.listView6);
        listView.setAdapter(adapter);
    }

    @Override
    public void showError(String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    /**
     * バックボタン押下時のイベント処理
     * @param view
     */
    public void onClickBackButton(View view) {
        controller.removeAssetForTransfer();
    }

    /**
     * 更新ボタン押下時のイベント処理
     * @param view
     */
    public void onClickUpdateButton(View view) {
        controller.transferMoney(getIntFromView(R.id.editText9, 0));
    }

    /**
     * 資産名をセットする．
     * @param id セット先のViewのId
     * @param asset セットする資産
     * @param prefix 接頭語
     */
    private void setAssetName(int id, Asset asset, String prefix) {
        String name = prefix + ": ";
        if (asset != null) {
            name = name + asset.getName();
        }
        TextView textView = (TextView) findViewById(id);
        textView.setText(name);
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

}
