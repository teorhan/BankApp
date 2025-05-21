public class TransactionModel {
    private String type;
    private double amount;
    private String targetTc;
    private String timestamp;
    private String gram;

    public TransactionModel(String type, double amount, String targetTc, String timestamp,String gram) {
        this.type = type;
        this.amount = amount;
        this.targetTc = targetTc;
        this.timestamp = timestamp;
        this.gram = gram;
    }

    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getTargetTc() { return targetTc; }
    public String getTimestamp() { return timestamp; }
    public String getGram() {
        return gram;
    }
}
