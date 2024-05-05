package com.example.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.time.Instant;
import java.util.Date;

import classes.ListOperations;
import classes.fabric.OperationsFactory;
import classes.operations.*;
import com.example.wallet.R;


public class MainActivity extends AppCompatActivity {
    private TextView textViewBalance, textViewIncome, textViewExpense;
    private ListView listViewOperations;
    private Button buttonAddOperation;

    private ListOperations operations;
    private OperationsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        operations = ListOperations.getSingleton(getApplicationContext());

        textViewBalance = findViewById(R.id.textViewBalance);
        textViewIncome = findViewById(R.id.textViewIncome);
        textViewExpense = findViewById(R.id.textViewExpense);
        listViewOperations = findViewById(R.id.listViewOperations);
        buttonAddOperation = findViewById(R.id.buttonAddOperation);

        // Регистрация ListView для контекстного меню
        registerForContextMenu(listViewOperations);

        updateUI();
        buttonAddOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOperationTypeDialog();
            }
        });

        // Инициализация UI компонентов
        Button buttonViewStatistics = findViewById(R.id.buttonViewStatistics);
        buttonViewStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, StatisticActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
    }

    private void updateUI() {
        adapter = new OperationsAdapter(this, operations.getListOperation());
        listViewOperations.setAdapter(adapter);
        updateBalanceAndTransactions();
    }

    private void updateBalanceAndTransactions() {
        double balance = 0, income = 0, expense = 0;
        for (IOperation operation : operations.getListOperation()) {
            if (operation instanceof OperationPlus) {
                balance += operation.getAmountMoney();
                income += operation.getAmountMoney();
            } else if (operation instanceof OperationMinus) {
                balance -= operation.getAmountMoney();
                expense += operation.getAmountMoney();
            }
        }

        textViewBalance.setText("Остаток:\n" + balance + " ₽");
        textViewIncome.setText("Доходы:\n" + income + " ₽");
        textViewExpense.setText("Расходы:\n" + expense + " ₽");
    }

    private void showOperationTypeDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog);

        Button buttonIncome = bottomSheetDialog.findViewById(R.id.buttonIncome);
        Button buttonExpense = bottomSheetDialog.findViewById(R.id.buttonExpense);



        buttonIncome.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            openOperationActivity(true);
        });

        buttonExpense.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();
            openOperationActivity(false);
        });
        updateBalanceAndTransactions();

        bottomSheetDialog.show();
    }

    private void openOperationActivity(boolean isIncome) {
        Intent intent = new Intent(MainActivity.this, AddOperationActivity.class);
        intent.putExtra("isIncome", isIncome);
        startActivity(intent);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int itemId = item.getItemId();
        if (itemId == R.id.edit) {
            // Реализация редактирования операции
            editOperation(info.position);
            return true;
        } else if (itemId == R.id.delete) {
            deleteOperation(info.position);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    private void deleteOperation(int position){
        IOperation operation = adapter.getItem(position);
        operations.removeOperation((int)operation.getId());
        adapter.notifyDataSetChanged(); // Обновление адаптера после удаления
        onResume();
    }
    private void editOperation(int position) {
        IOperation operation = adapter.getItem(position);
        Intent intent = new Intent(MainActivity.this, EditOperationActivity.class);
        intent.putExtra("operationId", operation.getId());
        intent.putExtra("amount", operation.getAmountMoney());
        intent.putExtra("date", operation.getDate().toString());
        intent.putExtra("remark", operation.getRemark());
        intent.putExtra("isIncome", operation instanceof OperationPlus);
        startActivity(intent);
    }
    @Override
    protected void onResume() {
        super.onResume();
        updateUI(); // Перезагрузка данных при возвращении в активность
    }
}

