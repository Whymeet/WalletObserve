package classes;

import android.content.Context;

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
    private static DatabaseHelper dbHelper;

    // Изменение: добавление контекста в параметры для инициализации DatabaseHelper
    public static ListOperations getSingleton(Context context) {
        if (SINGLETON == null) {
            SINGLETON = new ListOperations(context);
        }
        return SINGLETON;
    }

    // Изменение: конструктор теперь принимает Context для инициализации dbHelper
    private ListOperations(Context context) {
        dbHelper = new DatabaseHelper(context);
        loadOperationsFromDatabase(); // Загрузка операций при инициализации
    }

    private void loadOperationsFromDatabase() {
        listOperation = dbHelper.getAllOperations();
    }

    private void updateDataBase() {
        dbHelper.addOperationsList(listOperation);
    }

    public void addOperation(IOperation operation) {
        this.listOperation.add(operation);
        updateDataBase(); // Обновление базы данных после добавления операции
    }

    public void removeOperation(IOperation operation) {
        listOperation.remove(operation);
        updateDataBase(); // Обновление базы данных после удаления операции
    }

    public List<IOperation> getListOperation() {
        loadOperationsFromDatabase(); // Перезагрузка операций из базы данных перед предоставлением списка
        sortOperationsByDate();
        return listOperation;
    }

    // Метод сортировки списка операций по дате
    private void sortOperationsByDate() {
        Collections.sort(listOperation, new Comparator<IOperation>() {
            @Override
            public int compare(IOperation o1, IOperation o2) {
                // Сортировка по убыванию даты
                return o2.getDate().compareTo(o1.getDate());
            }
        });
    }
}
