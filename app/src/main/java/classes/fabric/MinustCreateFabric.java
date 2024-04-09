package classes.fabric;

import classes.operations.IOperation;
import classes.operations.OperationMinus;

public class MinustCreateFabric implements OperationsFactory{
    @Override
    public IOperation createOperations() {
        return new OperationMinus();
    }
}
