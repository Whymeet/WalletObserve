package com.example.wallet;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.text.DecimalFormat;
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
    private TextView txtUSD, txtEUR, txtCNY, curruntBalaseTxt, maxOperation, maxOperationminus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        listOperations = ListOperations.getSingleton(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        pieChartExpenses = findViewById(R.id.pieChartExpenses);
        btnBack = findViewById(R.id.btnBack);
        pie2 = findViewById(R.id.pie2);
        txtCNY = findViewById(R.id.txtCNY);
        txtUSD = findViewById(R.id.txtUSD);
        txtEUR = findViewById(R.id.txtEUR);
        curruntBalaseTxt = findViewById(R.id.currentBalance);
        maxOperation = findViewById(R.id.maxOperation);
        maxOperationminus = findViewById(R.id.maxOperationminus);
        checkStats();
        setupPieChart();
        loadPieChartData();
        setupPieChartFor2();
        loadPieChartDataFor2();

        String url = "https://www.cbr.ru/scripts/XML_daily.asp";
        new XmlHelper().execute(url);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StatisticActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkStats(){
        List<IOperation> operations= listOperations.getListOperation();
        double mx = -1;
        double mxmin = -1;
        double curBalance = 0;
        for(IOperation operation : operations){
            if(operation.getTags().contains("доходы")){
                curBalance += operation.getAmountMoney();
                if(operation.getAmountMoney() > mx){
                    mx = operation.getAmountMoney();
                }
            }
            else if(operation.getTags().contains("расходы")){
                curBalance -= operation.getAmountMoney();
                if(operation.getAmountMoney() > mxmin){
                    mxmin = operation.getAmountMoney();
                }
            }
        }

        curruntBalaseTxt.setText("Текущий баланс: " + curBalance);
        maxOperation.setText("Максимальное поступление: " + mxmin);
        maxOperationminus.setText("Максимальная трата: " + mx );

    }


    private void setupPieChart() {
        pieChartExpenses.setDrawHoleEnabled(true);
        pieChartExpenses.setUsePercentValues(true);
        pieChartExpenses.setEntryLabelTextSize(12);
        pieChartExpenses.setEntryLabelColor(Color.BLACK);
        pieChartExpenses.setCenterText("Расходы");
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
        pie2.setCenterText("Доходы");
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

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null){
                    buffer.append(line).append("\n");

                }
                return buffer.toString();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if (connection != null){
                    connection.disconnect();
                }
                if(reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        }

        protected  void onPreExecute(){
            super.onPreExecute();
        }


        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            DecimalFormat decimalFormat = new DecimalFormat("#.00");  // Формат с двумя десятичными знаками

            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(new StringReader(result));
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG && "Valute".equals(parser.getName())) {
                        String charCode = "";
                        int innerEventType = parser.next();
                        while (!(innerEventType == XmlPullParser.END_TAG && "Valute".equals(parser.getName()))) {
                            if (innerEventType == XmlPullParser.START_TAG && "CharCode".equals(parser.getName())) {
                                parser.next();
                                charCode = parser.getText();
                                parser.next(); // Move to end tag
                            }
                            if (innerEventType == XmlPullParser.START_TAG && "Value".equals(parser.getName())) {
                                parser.next();
                                String valueText = parser.getText().replace(',', '.');  // Замените запятую на точку для корректного парсинга
                                double value = Double.parseDouble(valueText);
                                String formattedValue = decimalFormat.format(value);  // Форматирование значения
                                switch (charCode) {
                                    case "USD":
                                        txtUSD.setText("USD\n" + formattedValue);
                                        break;
                                    case "EUR":
                                        txtEUR.setText("EUR\n" + formattedValue);
                                        break;
                                    case "CNY":
                                        txtCNY.setText("CNY\n" + formattedValue);
                                        break;
                                }
                                parser.next(); // Move to end tag
                            }
                            innerEventType = parser.next();
                        }
                    }
                    eventType = parser.next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        private String parseValue(XmlPullParser parser) throws Exception {
            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();
                if ("Value".equals(name)) {
                    parser.next();
                    return parser.getText();
                } else {
                    skip(parser);
                }
            }
            return null;
        }

        private void skip(XmlPullParser parser) throws Exception {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                throw new IllegalStateException();
            }
            int depth = 1;
            while (depth != 0) {
                switch (parser.next()) {
                    case XmlPullParser.END_TAG:
                        depth--;
                        break;
                    case XmlPullParser.START_TAG:
                        depth++;
                        break;
                }
            }
        }


    }


}
