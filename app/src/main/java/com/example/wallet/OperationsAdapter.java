package com.example.wallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import classes.operations.IOperation;
import classes.operations.OperationMinus;
import classes.operations.OperationPlus;

public class OperationsAdapter extends BaseAdapter {
    private List<IOperation> operations;
    private LayoutInflater inflater;

    public OperationsAdapter(Context context, List<IOperation> operations) {
        this.operations = operations;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return operations.size();
    }

    @Override
    public Object getItem(int position) {
        return operations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.operation_item, parent, false);
        }

        IOperation operation = (IOperation) getItem(position);

        ((TextView) view.findViewById(R.id.textViewDate)).setText(operation.getDate().toString());
        ((TextView) view.findViewById(R.id.textViewAmount)).setText(String.format("%.2f", operation.getAmountMoney()));
        ((TextView) view.findViewById(R.id.textViewRemark)).setText(operation.getRemark().toString());
        // Изменение цвета фона в зависимости от типа операции
        if (operation instanceof OperationPlus) {
            view.setBackgroundColor(view.getContext().getResources().getColor(R.color.green));
        } else if (operation instanceof OperationMinus) {
            view.setBackgroundColor(view.getContext().getResources().getColor(R.color.red));
        }

        return view;
    }
}
