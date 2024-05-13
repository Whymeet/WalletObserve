package com.example.wallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
    public IOperation getItem(int position) {
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
            view.setBackgroundColor(view.getContext().getResources().getColor(R.color.orange));
        }

        return view;
    }

    public void sortByIncomeFirst(int tag) {
        Collections.sort(operations, new Comparator<IOperation>() {
            @Override
            public int compare(IOperation o1, IOperation o2) {
                if (o1 instanceof OperationPlus && o2 instanceof OperationMinus) {
                    return -1 * tag;
                } else if (o1 instanceof OperationMinus && o2 instanceof OperationPlus) {
                    return 1 * tag;
                }
                return 0;
            }
        });
        notifyDataSetChanged();
    }
}
