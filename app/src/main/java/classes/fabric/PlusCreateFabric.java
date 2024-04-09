package classes.fabric;

import classes.operations.IOperation;
import classes.operations.OperationPlus;

public class PlusCreateFabric implements OperationsFactory{
    @Override
    public IOperation createOperations() {
        return new OperationPlus();
    }
}
