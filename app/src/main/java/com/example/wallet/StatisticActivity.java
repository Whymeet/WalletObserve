package com.example.wallet;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import classes.ListOperations;
import classes.operations.IOperation;

public class StatisticActivity extends AppCompatActivity {
    private PieChart pieChartExpenses, pie2;
    private ListOperations listOperations;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        listOperations = ListOperations.getSingleton(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        pieChartExpenses = findViewById(R.id.pieChartExpenses);
        btnBack = findViewById(R.id.btnBack);
        pie2 = findViewById(R.id.pie2);
        setupPieChart();
        loadPieChartData();
        setupPieChartFor2();
        loadPieChartDataFor2();

        String url = "https://www.cbr.ru/scripts/XML_daily.asp";

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupPieChart() {
        pieChartExpenses.setDrawHoleEnabled(true);
        pieChartExpenses.setUsePercentValues(true);
        pieChartExpenses.setEntryLabelTextSize(12);
        pieChartExpenses.setEntryLabelColor(Color.BLACK);
        pieChartExpenses.setCenterText("Расходы по тегам");
        pieChartExpenses.setCenterTextSize(24);
        pieChartExpenses.getDescription().setEnabled(false);

        Legend l = pieChartExpenses.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    private void loadPieChartData() {
        // Здесь должна быть логика для загрузки данных
        // Следующий код предполагает, что у вас уже есть HashMap с данными
        HashMap<String, Double> dataMap = parseInfoReturrnHashMap("расходы");
        // Заполнение данных в dataMap

        List<PieEntry> entries = new ArrayList<>();
        for (String tag : dataMap.keySet()) {
            entries.add(new PieEntry(dataMap.get(tag).floatValue(), tag));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Теги Расходов");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChartExpenses));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChartExpenses.setData(data);
        pieChartExpenses.invalidate();
    }


    private HashMap<String, Double> parseInfoReturrnHashMap(String tagsVar){
        HashMap<String, Double> dataTags = new HashMap<>();
        double sum = 0;
        List<IOperation> operations= listOperations.getListOperation();
        for (IOperation operation : operations){
            if(operation.getTags().contains(tagsVar)) {
                for (String tag : operation.getTags()) {
                    if(operation.getTags().size() == 1) {
                        tag = "Other";
                    }
                    if (!tag.equals(tagsVar)) {
                        if (!dataTags.containsKey(tag)) {
                            dataTags.put(tag, (double) 0);
                        }
                        sum += dataTags.get(tag) + operation.getAmountMoney();
                        dataTags.put(tag, sum);
                    }
                }
            }
        }
        return dataTags;
    }


    private void setupPieChartFor2() {
        pie2.setDrawHoleEnabled(true);
        pie2.setUsePercentValues(true);
        pie2.setEntryLabelTextSize(12);
        pie2.setEntryLabelColor(Color.BLACK);
        pie2.setCenterText("Доходы по тегам");
        pie2.setCenterTextSize(24);
        pie2.getDescription().setEnabled(false);

        Legend l = pie2.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    private void loadPieChartDataFor2() {
        // Здесь должна быть логика для загрузки данных
        // Следующий код предполагает, что у вас уже есть HashMap с данными
        HashMap<String, Double> dataMap = parseInfoReturrnHashMap("доходы");
        // Заполнение данных в dataMap

        List<PieEntry> entries = new ArrayList<>();
        for (String tag : dataMap.keySet()) {
            entries.add(new PieEntry(dataMap.get(tag).floatValue(), tag));
        }

        PieDataSet dataSet = new PieDataSet(entries, "Теги Доходов");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pie2));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pie2.setData(data);
        pie2.invalidate();
    }

    private class XmlHelper extends AsyncTask<String, String, String>{
        public String eurString;
        public String usdString;
        public String cnyString;


        @Override
        protected Object doInBackground(Object[] objects) {
            return null;
        }
    }


}
