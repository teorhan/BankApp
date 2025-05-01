public class TransactionModel {
    private String type;
    private double amount;
    private String targetTc;
    private String timestamp;

    public TransactionModel(String type, double amount, String targetTc, String timestamp) {
        this.type = type;
        this.amount = amount;
        this.targetTc = targetTc;
        this.timestamp = timestamp;
    }

    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getTargetTc() { return targetTc; }
    public String getTimestamp() { return timestamp; }
}
