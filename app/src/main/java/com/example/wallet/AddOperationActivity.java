package com.example.wallet;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import classes.ListOperations;
import classes.OperationsAdapter;
import classes.operations.*;

public class AddOperationActivity extends AppCompatActivity {

    private EditText editTextAmount;
    private EditText editTextDate;
    private EditText editTextRemark;
    private Button buttonCreateOperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_operation);

        editTextAmount = findViewById(R.id.editTextAmount);
        editTextDate = findViewById(R.id.editTextDate);
        editTextRemark = findViewById(R.id.editTextRemark);
        buttonCreateOperation = findViewById(R.id.buttonCreateOperation);

        final boolean isIncome = getIntent().getBooleanExtra("isIncome", true);

        buttonCreateOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Здесь логика для создания и добавления операции в список
                // Используйте isIncome для определения типа операции
            }
        });
    }
}
