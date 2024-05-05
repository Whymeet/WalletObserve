package classes.operations;
import java.time.LocalDate;
import java.util.Set;

public interface IOperation {
    double getAmountMoney();

    void setAmountMoney(double amountMoney);

    LocalDate getDate();

    void setDate(LocalDate date);

    String getRemark();

    void addRemark(String remark);

    Set<String> getTags();
    void setId(long id);
    long getId();
    void setRemark(String remark);
}