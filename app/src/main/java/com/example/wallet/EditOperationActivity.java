package com.example.wallet;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import classes.ListOperations;
import classes.operations.IOperation;
import classes.operations.OperationMinus;
import classes.operations.OperationPlus;

public class EditOperationActivity extends AppCompatActivity {

    private EditText editTextAmount;
    private EditText editTextDate;
    private EditText editTextRemark;
    private Button buttonSaveChanges;
    private ListOperations listOperations;
    private IOperation currentOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_operation);

        listOperations = ListOperations.getSingleton(getApplicationContext());

        editTextAmount = findViewById(R.id.editTextAmount);
        editTextDate = findViewById(R.id.editTextDate);
        editTextRemark = findViewById(R.id.editTextRemark);
        buttonSaveChanges = findViewById(R.id.buttonSaveChanges);

        // Загрузка данных операции для редактирования
        long operationId = getIntent().getLongExtra("operationId", -1);
        currentOperation = listOperations.getOperationById(operationId);

        if (currentOperation != null) {
            editTextAmount.setText(String.valueOf(currentOperation.getAmountMoney()));
            editTextDate.setText(currentOperation.getDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            editTextRemark.setText(currentOperation.getRemark());
        }
        buttonSaveChanges.setOnClickListener(v -> {
            try {
                double amount = Double.parseDouble(editTextAmount.getText().toString());
                LocalDate date = LocalDate.parse(editTextDate.getText().toString(), DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                String remark = editTextRemark.getText().toString();

                currentOperation.setAmountMoney(amount);
                currentOperation.setDate(date);

                if(!remark.contains("доходы") || !remark.contains("расходы")){
                    if(currentOperation instanceof OperationPlus){
                        remark = remark + " доходы";
                    }
                    else if(currentOperation instanceof OperationMinus){
                        remark = remark + " расходы";
                    }
                }
                currentOperation.setRemark(remark);

                DatabaseHelper dbHelper = new DatabaseHelper(getApplicationContext());
                dbHelper.updateOperation(currentOperation.getId(), currentOperation);

                Toast.makeText(EditOperationActivity.this, "Операция обновлена", Toast.LENGTH_SHORT).show();
                finish(); // Завершение Activity и возвращение к предыдущему экрану
            } catch (NumberFormatException | DateTimeParseException e) {
                Toast.makeText(EditOperationActivity.this, "Некорректный ввод данных", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
