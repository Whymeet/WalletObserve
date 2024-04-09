package classes;

import java.util.ArrayList;
import java.util.List;
import classes.operations.IOperation;

public class ListOperations {

    private static ListOperations instance;
    private List<IOperation> listOperation = new ArrayList<>();

    private static final ListOperations SINGLETON = new ListOperations();

    public static ListOperations getSingleton() {return  SINGLETON;}

    public void addOperation(IOperation operation) {
        listOperation.add(operation);
    }

    public void removeOperation(IOperation operation) {
        listOperation.remove(operation);
    }

    public List<IOperation> getListOperation() {
        return listOperation;
    }
}
