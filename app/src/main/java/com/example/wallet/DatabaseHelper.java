package com.example.wallet;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Название базы данных
    private static final String DATABASE_NAME = "operations.db";
    // Версия базы данных. При изменении схемы увеличьте этот номер
    private static final int DATABASE_VERSION = 1;

    // Название таблицы и столбцов
    public static final String TABLE_NAME = "operations";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_REMARK = "remark";
    public static final String COLUMN_TYPE = "type"; // Для различия между OperationPlus и OperationMinus

    // SQL для создания таблицы
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_AMOUNT + " REAL, " +
                    COLUMN_DATE + " TEXT, " +
                    COLUMN_REMARK + " TEXT, " +
                    COLUMN_TYPE + " INTEGER" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Здесь вы можете обновить схему базы данных, если версия изменилась
    }

}
