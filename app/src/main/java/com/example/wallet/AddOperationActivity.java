package com.example.wallet;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.time.Instant;
import java.util.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import classes.ListOperations;
import classes.fabric.MinustCreateFabric;
import classes.fabric.OperationsFactory;
import classes.fabric.PlusCreateFabric;
import classes.operations.*;

public class AddOperationActivity extends AppCompatActivity {

    private EditText editTextAmount;
    private EditText editTextDate;
    private EditText editTextRemark;
    private Button buttonCreateOperation;
    private ListOperations listOperations = ListOperations.getSingleton();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_operation);

        editTextAmount = findViewById(R.id.editTextAmount);
        editTextDate = findViewById(R.id.editTextDate);
        editTextRemark = findViewById(R.id.editTextRemark);
        buttonCreateOperation = findViewById(R.id.buttonCreateOperation);

        final boolean isIncome = getIntent().getBooleanExtra("isIncome", true);
        OperationsFactory factory;

        if(isIncome){
            factory = new PlusCreateFabric();

        }
        else{
            factory = new MinustCreateFabric();
        }

       IOperation operation_money = factory.createOperations();
        buttonCreateOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String amountString = editTextAmount.getText().toString();
                if (!amountString.isEmpty()) {
                    try {
                        // Преобразование строки в double
                        double amount = Double.parseDouble(amountString);
                        OperationPlus op3 = new OperationPlus();
                        // Вызов метода setAmountMoney с полученным значением
                        operation_money.setAmountMoney(amount);

                    } catch (NumberFormatException e) {
                        // Обработка исключения, если строка не может быть преобразована в число
                        // Например, показать Toast с ошибкой
                        Toast.makeText(AddOperationActivity.this, "Некорректный ввод суммы", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Обработка случая пустого ввода
                    // Например, показать Toast с предупреждением
                    Toast.makeText(AddOperationActivity.this,"Введите сумму", Toast.LENGTH_SHORT).show();
                }





                String dateText = editTextDate.getText().toString();
                if (!dateText.isEmpty()) {
                    try {
                        // Создаем форматтер с нужным форматом даты
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                        // Парсим строку в LocalDate
                        LocalDate date = LocalDate.parse(dateText, formatter);
                        operation_money.setDate(date);
                    } catch (DateTimeParseException e) {
                        // Обработка ошибки, если строка не соответствует формату даты
                        Toast.makeText(AddOperationActivity.this, "Некорректный ввод даты", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Если строка с датой пуста, используем текущую дату
                    operation_money.setDate(LocalDate.now());
                }



                String remarkStr = editTextRemark.getText().toString();


                listOperations.addOperation(operation_money);
                finish();

            }
        });
    }
}
