package classes.fabric;

import classes.operations.IOperation;

public interface OperationsFactory {
    IOperation createOperations();
}
