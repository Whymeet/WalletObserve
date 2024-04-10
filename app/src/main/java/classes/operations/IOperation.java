package classes.operations;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface IOperation {
    double getAmountMoney();

    void setAmountMoney(double amountMoney);

    public LocalDate getDate();

    public void setDate(LocalDate date);

    String getRemark();

    void setRemark(String remark);

    public List<String> getTags();
}