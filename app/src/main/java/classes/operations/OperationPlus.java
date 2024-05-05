package classes.operations;

import java.time.LocalDate;
import java.util.Set;

import classes.tags.Tag;

public class OperationPlus implements IOperation{
    private double amountMoney;
    private LocalDate operationDate;
    private Tag tags;
    private long id;

    // Конструктор, геттеры и сеттеры
    public OperationPlus(){
        tags = new Tag();
        tags.addTags("доходы");
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
    public String getRemark() {
        String result = String.join(" ", tags.getTags());
        return result;

    }

    @Override
    public void addRemark(String remark) {
        tags.addTags(remark);
    }
    public void setRemark(String remark){
        tags.setTags(remark);
    }

    public Set<String> getTags(){
        return tags.getTags();
    }

    @Override
    public long getId() {

        return id;
    }
    @Override

    public void setId(long id) {
        this.id = id;
    }
}
