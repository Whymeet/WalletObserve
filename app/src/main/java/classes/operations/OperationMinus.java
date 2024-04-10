package classes.operations;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;

public class OperationMinus implements IOperation{
    private double amountMoney;
    private LocalDate operationDate;
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
    public LocalDate getDate() {
        return this.operationDate;
    }
    public void setDate(LocalDate date){
        this.operationDate = date;
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
