package classes.operations;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import classes.tags.Tag;

public class OperationPlus implements IOperation{
    private double amountMoney;
    private LocalDate operationDate;
    private Tag tags;

    // Конструктор, геттеры и сеттеры
    public OperationPlus(){
        tags.setTags("доходы");
    }
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

    @Override
    public void setDate(LocalDate date) {
        this.operationDate = date;
    }

    @Override
    public List<String> getRemark() {
        return tags.getTags();
    }

    @Override
    public void setRemark(String remark) {
        tags.setTags(remark);
    }
}
