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

    public long addOperation(IOperation operation) {
        long newId = dbHelper.addOperation(operation);
        operation.setId((newId));
        this.listOperation.add(operation);
        return newId;
    }
    public void removeOperation(int operationId) {
        IOperation toRemove = null;
        for (IOperation operation : listOperation) {
            if (operation.getId() == operationId) {
                toRemove = operation;
                break;
            }
        }
        if (toRemove != null) {
            listOperation.remove(toRemove);
            dbHelper.deleteOperationById((int) toRemove.getId()); // Удаление операции из базы данных
        }
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

    public IOperation getOperationById(long operationId) {
        for(IOperation operation : listOperation){
            if(operation.getId() == operationId){
                return operation;
            }
        }
        throw new IllegalArgumentException("Операция с id" + operationId + " не найдена");
    }
}
