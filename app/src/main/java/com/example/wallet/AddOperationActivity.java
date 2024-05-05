package com.example.wallet;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import classes.ListOperations;
import classes.operations.IOperation;
import classes.operations.OperationMinus;
import classes.operations.OperationPlus;

public class AddOperationActivity extends AppCompatActivity {

    private EditText editTextAmount;
    private EditText editTextDate;
    private EditText editTextRemark;
    private Button buttonCreateOperation;
    private ListOperations listOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_operation);

        listOperations = ListOperations.getSingleton(getApplicationContext());

        editTextAmount = findViewById(R.id.editTextAmount);
        editTextDate = findViewById(R.id.editTextDate);
        editTextRemark = findViewById(R.id.editTextRemark);
        buttonCreateOperation = findViewById(R.id.buttonCreateOperation);

        final boolean isIncome = getIntent().getBooleanExtra("isIncome", true);

        buttonCreateOperation.setOnClickListener(v -> {
            double amount = 0;
            try {
                amount = Double.parseDouble(editTextAmount.getText().toString());
            } catch (NumberFormatException e) {
                Toast.makeText(AddOperationActivity.this, "Некорректный ввод суммы", Toast.LENGTH_SHORT).show();
                return;
            }

            LocalDate date;
            if (editTextDate.getText().toString().isEmpty()) {
                date = LocalDate.now();
            } else {
                try {
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                    date = LocalDate.parse(editTextDate.getText().toString(), formatter);
                } catch (DateTimeParseException e) {
                    Toast.makeText(AddOperationActivity.this, "Некорректный ввод даты", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            String remark = editTextRemark.getText().toString();
            IOperation operation = isIncome ? new OperationPlus() : new OperationMinus();
            operation.setAmountMoney(amount);
            operation.setDate(date);
            operation.addRemark(remark);

            // Присвоение ID после добавления в базу данных
            long newId = listOperations.addOperation(operation);
            operation.setId(newId);  // Устанавливаем ID операции

            finish();
        });

    }
}
