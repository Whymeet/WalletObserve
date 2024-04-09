package com.example.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import classes.ListOperations;
import classes.OperationsAdapter;
import classes.fabric.MinustCreateFabric;
import classes.fabric.OperationsFactory;
import classes.fabric.PlusCreateFabric;
import classes.operations.*;

public class MainActivity extends AppCompatActivity {
    private TextView textViewBalance;
    private ListView listViewOperations;
    private Button buttonAddOperation;

    private OperationsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("Вывод");

        textViewBalance = findViewById(R.id.textViewBalance);
        listViewOperations = findViewById(R.id.listViewOperations);
        buttonAddOperation = findViewById(R.id.buttonAddOperation);

        // Адаптер для ListView
        adapter = new OperationsAdapter(this, ListOperations.getSingleton().getListOperation());
        listViewOperations.setAdapter(adapter);

        // Обновление баланса
        updateBalance();

        buttonAddOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOperationTypeDialog();
            }
        });
    }

    private void updateBalance() {
        double balance = 0;
        for (IOperation operation : ListOperations.getSingleton().getListOperation()) {
            if (operation instanceof OperationPlus) {
                balance += operation.getAmountMoney();
            } else if (operation instanceof OperationMinus) {
                balance -= operation.getAmountMoney();
            }
        }
        textViewBalance.setText("Баланс: " + balance);
    }

    private void showOperationTypeDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);

        Button buttonIncome = bottomSheetDialog.findViewById(R.id.buttonIncome);
        Button buttonExpense = bottomSheetDialog.findViewById(R.id.buttonExpense);
        OperationsFactory factory;


        buttonIncome.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            openOperationActivity(true);
        });

        buttonExpense.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            openOperationActivity(false);
        });

        bottomSheetDialog.show();
    }

    private void openOperationActivity(boolean isIncome) {
        Intent intent = new Intent(MainActivity.this, AddOperationActivity.class);
        intent.putExtra("isIncome", isIncome);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBalance();
        System.out.println("Вывод");
        adapter.clear();
        adapter.addAll(ListOperations.getSingleton().getListOperation());
        adapter.notifyDataSetChanged();

    }
}
