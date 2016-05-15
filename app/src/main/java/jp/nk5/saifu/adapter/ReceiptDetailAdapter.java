package jp.nk5.saifu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import jp.nk5.saifu.ViewUtil;
import jp.nk5.saifu.domain.ReceiptDetail;

/**
 * Created by NK5JP on 2016/05/15.
 */
public class ReceiptDetailAdapter extends ArrayAdapter<ReceiptDetail> {
    private LayoutInflater layoutInflater;

    public ReceiptDetailAdapter(Context context, int id, List<ReceiptDetail> details) {
        super(context, id, details);
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        if (view == null)
        {
            view = layoutInflater.inflate(android.R.layout.simple_list_item_1, null);
        }

        ReceiptDetail detail = (ReceiptDetail) getItem(position);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(detail.getBudget().getName() +
                ": " +
                ViewUtil.getMoneyString(detail.getAmount()));
        return view;
    }

}
