package jp.nk5.saifu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import jp.nk5.saifu.adapter.AssetAdapter;
import jp.nk5.saifu.app.AssetApplication;
import jp.nk5.saifu.controller.AssetController;
import jp.nk5.saifu.domain.Asset;
import jp.nk5.saifu.domain.AssetRepository;
import jp.nk5.saifu.form.AssetForm;
import jp.nk5.saifu.form.UpdateViewListener;
import jp.nk5.saifu.infra.AssetRepositoryImpl;

public class AssetActivity extends Activity implements UpdateViewListener, ListView.OnItemLongClickListener {

    private AssetController controller;
    private AssetApplication application;
    private AssetForm form;
    private AssetRepository assetRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset);
        ListView listView = (ListView) findViewById(R.id.listView5);
        listView.setOnItemLongClickListener(this);
    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            assetRepository = new AssetRepositoryImpl(this);
            application = new AssetApplication(assetRepository);
            form = new AssetForm("", 0, null);
            controller = new AssetController(application, form, this);
            controller.updateAssetList();
        } catch (Exception e) {
            this.finish();
        }
    }

    public void onClickCreateButton(View view) {
        String name = getStringFromView(R.id.editText8, "");
        controller.updateAsset(name);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Asset asset = (Asset) parent.getItemAtPosition(position);
        controller.switchAssetValidation(asset);
        return false;
    }

    @Override
    public void updateUI() {
        setStringToView(R.id.editText8, form.getName());
        ((TextView) findViewById(R.id.textView3)).setText(getString(R.string.str_total) + ": " + ViewUtil.getMoneyString(form.getTotal()));
        AssetAdapter adapter = new AssetAdapter(this, R.layout.list_textwithicon, form.getAssets());
        ListView listView = (ListView)this.findViewById(R.id.listView5);
        listView.setAdapter(adapter);
    }

    @Override
    public void showError(String errorMsg) {
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    private String getStringFromView(int id, String errorName) {
        EditText editText = (EditText) findViewById(id);
        if (!editText.getText().toString().equals("")) {
            return editText.getText().toString();
        } else {
            return errorName;
        }
    }

    private void setStringToView(int id, String string) {
        EditText editText = (EditText) findViewById(id);
        editText.setText(string);
    }

}
