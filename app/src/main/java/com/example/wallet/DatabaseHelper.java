package com.example.wallet;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import classes.operations.IOperation;
import classes.operations.OperationMinus;
import classes.operations.OperationPlus;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "wallet.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "operations";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TYPE = "type";
    private static final String COLUMN_REMARK = "remark";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_AMOUNT + " DOUBLE,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_TYPE + " TEXT,"
                + COLUMN_REMARK + " TEXT"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public long addOperation(IOperation operation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AMOUNT, operation.getAmountMoney());
        values.put(COLUMN_DATE, operation.getDate().toString());
        values.put(COLUMN_TYPE, (operation instanceof OperationPlus) ? "plus" : "minus");
        values.put(COLUMN_REMARK, operation.getRemark().toString());

        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public List<IOperation> getAllOperations() {
        List<IOperation> operations = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_DATE + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int columnIndexId = cursor.getColumnIndex(COLUMN_ID);
        int columnIndexAmount = cursor.getColumnIndex(COLUMN_AMOUNT);
        int columnIndexDate = cursor.getColumnIndex(COLUMN_DATE);
        int columnIndexType = cursor.getColumnIndex(COLUMN_TYPE);
        int columnIndexRemark = cursor.getColumnIndex(COLUMN_REMARK);

        if (columnIndexAmount == -1 || columnIndexDate == -1 || columnIndexType == -1 || columnIndexRemark == -1 || columnIndexId == -1) {
            cursor.close();
            db.close();
            return operations; // Return empty list if a column was not found
        }

        if (cursor.moveToFirst()) {
            do {
                IOperation operation = (cursor.getString(columnIndexType).equals("plus")) ? new OperationPlus() : new OperationMinus();
                operation.setId(cursor.getInt(columnIndexId));
                operation.setAmountMoney(cursor.getDouble(columnIndexAmount));
                operation.setDate(LocalDate.parse(cursor.getString(columnIndexDate)));
                operation.addRemark(cursor.getString(columnIndexRemark));
                operations.add(operation);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return operations;
    }


    public void addOperationsList(List<IOperation> operations) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Начало транзакции
        db.beginTransaction();
        try {
            // Очистка таблицы перед добавлением новых записей
            db.delete(TABLE_NAME, null, null);

            for (IOperation operation : operations) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_AMOUNT, operation.getAmountMoney());
                values.put(COLUMN_DATE, operation.getDate().toString());
                values.put(COLUMN_TYPE, (operation instanceof OperationPlus) ? "plus" : "minus");
                values.put(COLUMN_REMARK, operation.getRemark().toString());

                db.insert(TABLE_NAME, null, values);
            }

            db.setTransactionSuccessful(); // Помечаем транзакцию как успешную
        } finally {
            db.endTransaction(); // Завершение транзакции
            db.close(); // Закрытие базы данных
        }
    }

    public void deleteOperationById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public int updateOperation(long id, IOperation operation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AMOUNT, operation.getAmountMoney());
        values.put(COLUMN_DATE, operation.getDate().toString());
        values.put(COLUMN_TYPE, (operation instanceof OperationPlus) ? "plus" : "minus");
        values.put(COLUMN_REMARK, operation.getRemark());

        int updateStatus = db.update(TABLE_NAME, values, COLUMN_ID + " = ?", new String[] {String.valueOf(id)});
        db.close();
        return updateStatus;
    }
}
