package classes;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.wallet.R;

import java.util.List;
import classes.operations.IOperation;



import classes.operations.IOperation;
import classes.operations.OperationMinus;
import classes.operations.OperationPlus;

public class OperationsAdapter extends ArrayAdapter<IOperation> {
    public OperationsAdapter(Context context, List<IOperation> operations) {
        super(context, 0, operations);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IOperation operation = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.operation_item, parent, false);
        }

        TextView textViewAmount = convertView.findViewById(R.id.textViewAmount);
        TextView textViewDate = convertView.findViewById(R.id.textViewDate);

        textViewAmount.setText(String.valueOf(operation.getAmountMoney()));


        textViewDate.setText(operation.getDate().toString());

        // Установка цвета в зависимости от типа операции
        if (operation instanceof OperationPlus) {
            textViewAmount.setTextColor(Color.GREEN);
        } else if (operation instanceof OperationMinus) {
            textViewAmount.setTextColor(Color.RED);
        }

        return convertView;
    }
}