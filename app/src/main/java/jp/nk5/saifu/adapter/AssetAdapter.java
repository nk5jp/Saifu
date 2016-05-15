package jp.nk5.saifu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import jp.nk5.saifu.R;
import jp.nk5.saifu.ViewUtil;
import jp.nk5.saifu.domain.Asset;

/**
 * 資産画面における資産リストアダプター
 * Created by NK5JP on 2016/05/08.
 */
public class AssetAdapter extends ArrayAdapter<Asset> {
    private LayoutInflater layoutInflater;

    public AssetAdapter(Context context, int id, List<Asset> assets) {
        super(context, id, assets);
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_textwithicon, null);
        }

        Asset asset = (Asset) getItem(position);
        TextView textView = (TextView) view.findViewById(R.id.textView2);
        textView.setText(asset.getName() +
                ": " +
                ViewUtil.getMoneyString(asset.getAmount()));
        if (asset.getAmount() < 0) {
            textView.setTextColor(Color.RED);
        } else {
            textView.setTextColor(Color.BLACK);
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        if (asset.isValid()) {
            imageView.setImageResource(R.drawable.icon_valid);
        } else {
            imageView.setImageResource(R.drawable.icon_invalid);
        }
        return view;
    }
}
