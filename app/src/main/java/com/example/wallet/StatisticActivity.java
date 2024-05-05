package com.example.wallet;

import android.os.Bundle;
import android.graphics.Color;

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
    private PieChart pieChartExpenses;
    private ListOperations listOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        listOperations = ListOperations.getSingleton(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        pieChartExpenses = findViewById(R.id.pieChartExpenses);
        setupPieChart();
        loadPieChartData();
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
}
