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
import jp.nk5.saifu.form.PlanActualDTO;

/**
 * メイン画面における予実リスト用アダプター．
 * Created by NK5JP on 2016/05/01.
 */
public class PlanActualAdapter extends ArrayAdapter<PlanActualDTO> {
    private LayoutInflater layoutInflater;

    public PlanActualAdapter(Context context, int id, List<PlanActualDTO> DTOs) {
        super(context, id, DTOs);
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        if (view == null)
        {
            view = layoutInflater.inflate(R.layout.list_textwithicon, null);
        }

        PlanActualDTO dto = (PlanActualDTO) getItem(position);
        TextView textView = (TextView) view.findViewById(R.id.textView2);
        textView.setText(dto.getBudget().getName() +
                ": " +
                ViewUtil.getMoneyString(dto.getActualAmount()) +
                " / " +
                ViewUtil.getMoneyString(dto.getBudget().getAmount()));
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        if(dto.getBudget().isValid()) {
            imageView.setImageResource(R.drawable.icon_valid);
        } else {
            imageView.setImageResource(R.drawable.icon_invalid);
        }
        if (dto.getActualAmount() > dto.getBudget().getAmount()) {
            textView.setTextColor(Color.RED);
        } else {
            textView.setTextColor(Color.BLACK);
        }
        return view;
    }
}
