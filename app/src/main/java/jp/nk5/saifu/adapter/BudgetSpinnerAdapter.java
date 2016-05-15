package jp.nk5.saifu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import jp.nk5.saifu.domain.Budget;

/**
 * 予算のスピナー用アダプタ
 * Created by NK5JP on 2016/05/15.
 */
public class BudgetSpinnerAdapter extends ArrayAdapter<Budget> {

    private LayoutInflater layoutInflater;

    public BudgetSpinnerAdapter(Context context, int id, List<Budget> budgets) {
        super(context, id, budgets);
        this.layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        if (view == null)
        {
            view = layoutInflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
        }

        Budget budget = (Budget) getItem(position);
        TextView textView = (TextView) view.findViewById(android.R.id.text1);
        textView.setText(budget.getName() + "(" + budget.getYear() + "/" + budget.getMonth() + ")");
        return view;
    }

    @Override
    public View getDropDownView(int position,
                                View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = layoutInflater.inflate(android.R.layout.simple_dropdown_item_1line, null);
        }
        Budget budget = (Budget) getItem(position);
        String name = budget.getName() + "(" + budget.getYear() + "/" + budget.getMonth() + ")";
        TextView view = (TextView)convertView.findViewById(android.R.id.text1);
        view.setText(name);

        return convertView;
    }
}
