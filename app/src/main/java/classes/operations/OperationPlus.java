package classes.operations;

import java.time.Instant;
import java.util.Date;

public class OperationPlus implements IOperation{
    private double amountMoney;
    private Date operationDate;
    private String remark;

    // Конструктор, геттеры и сеттеры
    @Override
    public double getAmountMoney() {
        return amountMoney;
    }

    @Override
    public void setAmountMoney(double amountMoney) {
        this.amountMoney = amountMoney;
    }

    @Override
    public Date getOperationDate() {
        return operationDate;
    }

    @Override
    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
    }

    @Override
    public Instant getDate() {
        return null;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
