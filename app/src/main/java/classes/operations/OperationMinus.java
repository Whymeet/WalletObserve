package classes.operations;

import java.time.LocalDate;
import java.util.List;

import classes.tags.Tag;

public class OperationMinus implements IOperation{
    private double amountMoney;
    private LocalDate operationDate;
    private Tag teg;

    // Конструктор, геттеры и сеттеры
    public OperationMinus(){
        teg.setTags("расходы");
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
    public void setDate(LocalDate date){
        this.operationDate = date;
    }

    @Override
    public List<String> getRemark() {
        return teg.getTags();
    }

    @Override
    public void setRemark(String remark) {
        teg.setTags(remark);
    }
}
