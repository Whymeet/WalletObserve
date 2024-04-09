package classes.operations;
import java.time.Instant;
import java.util.Date;
public interface IOperation {
    double getAmountMoney();
    void setAmountMoney(double amountMoney);

    Date getOperationDate();
    void setOperationDate(Date operationDate);
    Instant getDate();
    String getRemark();
    void setRemark(String remark);
}
