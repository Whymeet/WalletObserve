package classes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.wallet.DatabaseHelper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import classes.operations.IOperation;
import classes.operations.OperationMinus;
import classes.operations.OperationPlus;

public class ListOperations {

    private static List<IOperation> listOperation = new ArrayList<>();
    private static ListOperations SINGLETON;

    public static ListOperations getSingleton() {
        if (SINGLETON == null) {
            SINGLETON = new ListOperations();
        }
        return SINGLETON;
    }

    public void addOperation(IOperation operation) {
        this.listOperation.add(operation);
    }

    public void removeOperation(IOperation operation) {
        listOperation.remove(operation);
    }

    public List<IOperation> getListOperation() {
        sortOperationsByDate();
        return listOperation;
    }

    // Метод сортировки списка операций по дате
    public void sortOperationsByDate() {
        Collections.sort(listOperation, new Comparator<IOperation>() {
            @Override
            public int compare(IOperation o1, IOperation o2) {
                // Сортировка по убыванию даты
                return o2.getDate().compareTo(o1.getDate());
            }
        });
    }

    public void loadOperationsFromDB(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Очистка текущего списка, чтобы избежать дублирования
        listOperation.clear();

        // Колонки, которые будут извлечены
        String[] projection = {
                DatabaseHelper.COLUMN_ID,
                DatabaseHelper.COLUMN_AMOUNT,
                DatabaseHelper.COLUMN_DATE,
                DatabaseHelper.COLUMN_REMARK,
                DatabaseHelper.COLUMN_TYPE
        };

        // Сортировка по дате в убывающем порядке (сначала новые)
        String sortOrder = DatabaseHelper.COLUMN_DATE + " DESC";

        Cursor cursor = db.query(
                DatabaseHelper.TABLE_NAME,   // Таблица для запроса
                projection,                  // Столбцы для возврата
                null,               // Столбцы для условия WHERE
                null,            // Значения для условия WHERE
                null,                // Не группировать строки
                null,                 // Не фильтровать по группам строк
                sortOrder                   // Порядок сортировки
        );

        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
            double amount = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_AMOUNT));
            String dateStr = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE));
            String remark = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_REMARK));
            int type = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TYPE));

            // Создаем экземпляр операции в зависимости от типа
            IOperation operation;
            if (type == 1) { // Предположим, что 1 означает OperationPlus
                operation = new OperationPlus();
            } else { // Иначе это OperationMinus
                operation = new OperationMinus();
            }

            operation.setAmountMoney(amount);
            operation.setDate(LocalDate.parse(dateStr)); // Убедитесь, что формат даты соответствует сохраненному
            operation.setRemark(remark);

            // Добавляем операцию в список
            listOperation.add(operation);
        }
        cursor.close();
    }
}