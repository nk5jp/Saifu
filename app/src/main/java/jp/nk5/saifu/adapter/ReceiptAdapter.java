package jp.nk5.saifu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.annimon.stream.Stream;

import java.util.List;

import jp.nk5.saifu.ViewUtil;
import jp.nk5.saifu.domain.Receipt;
import jp.nk5.saifu.domain.ReceiptDetail;
import jp.nk5.saifu.form.PlanActualDTO;

/**
 * カレンダー画面におけるレシートリスト用アダプター．
 * Created by NK5JP on 2016/05/05.
 */
public class ReceiptAdapter extends ArrayAdapter<Receipt> {
    private LayoutInflater layoutInflater;

    public ReceiptAdapter(Context context, int id, List<Receipt> receipts) {
        super(context, id, receipts);
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        if (view == null)
        {
            view = layoutInflater.inflate(android.R.layout.simple_list_item_1, null);
        }

        Receipt receipt = (Receipt) getItem(position);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        int total = 0;
        for (ReceiptDetail detail : receipt.getDetails()) {
            total = total + detail.getAmount();
        }
        textView.setText(receipt.getId() +
                ": " +
                ViewUtil.getMoneyString(total) +
                " (" +
                receipt.getAsset().getName() +
                ")");
        return view;
    }
}
