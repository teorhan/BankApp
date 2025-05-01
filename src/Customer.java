public class Customer extends User implements Transactable {

    public Customer(String tc, String password) {
        super(tc, password);
    }

    @Override
    public String getRole() {
        return "Customer";
    }

    @Override
    public void deposit(double amount) {
        double current = DatabaseHelper.getBalance(tc);
        DatabaseHelper.updateBalance(tc, current + amount);
        DatabaseHelper.logTransaction(tc, "Yatırma", amount, null);

    }

    @Override
    public void withdraw(double amount) {
        double current = DatabaseHelper.getBalance(tc);
        if (current >= amount) {
            DatabaseHelper.updateBalance(tc, current - amount);
            DatabaseHelper.logTransaction(tc, "Çekme", amount, null);

        } else {
            System.out.println("Yetersiz bakiye!");
        }
    }
}
