import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DashboardController {

    @FXML
    private Label balanceLabel;

    @FXML
    private TextField amountField;

    private Customer currentCustomer;
    @FXML
    private TextField targetTcField;

    @FXML
    private TextField transferAmountField;

    @FXML
    private Label transferInfoLabel;


    public void setUserTc(String tc) {
        this.currentCustomer = new Customer(tc, null);
        updateBalanceLabel();
    }

    @FXML
    private void initialize() {
        // initialize içinde işlem yapılmaz çünkü setUserTc() sonradan çağrılır
    }

    @FXML
    private void handleDeposit() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) {
                System.out.println("Geçerli bir tutar giriniz.");
                return;
            }
            currentCustomer.deposit(amount);
            updateBalanceLabel();
        } catch (NumberFormatException e) {
            System.out.println("Lütfen sayısal bir tutar girin!");
        }
    }

    @FXML
    private void handleWithdraw() {
        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) {
                System.out.println("Geçerli bir tutar giriniz.");
                return;
            }
            currentCustomer.withdraw(amount);
            updateBalanceLabel();
        } catch (NumberFormatException e) {
            System.out.println("Lütfen sayısal bir tutar girin!");
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) balanceLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Banka Girişi");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleTransfer() {
        String targetTc = targetTcField.getText().trim();
        String amountStr = transferAmountField.getText().trim();

        if (targetTc.isEmpty() || amountStr.isEmpty()) {
            transferInfoLabel.setText("Lütfen tüm alanları doldurun.");
            return;
        }

        if (targetTc.equals(currentCustomer.getTc())) {
            transferInfoLabel.setText("Kendi hesabınıza para gönderemezsiniz.");
            return;
        }

        try {
            double amount = Double.parseDouble(amountStr);
            double currentBalance = DatabaseHelper.getBalance(currentCustomer.getTc());

            if (amount <= 0) {
                transferInfoLabel.setText("Geçerli bir tutar girin.");
                return;
            }

            // 🔍 Alıcı TC veritabanında var mı kontrol et
            double targetBalance = DatabaseHelper.getBalance(targetTc);
            if (targetBalance == 0.0) {
                // Ancak alıcı gerçekten 0 TL'ye sahipse bu yanıltıcı olur.
                // O yüzden ayrıca kayıtlı mı kontrol edelim:
                if (!DatabaseHelper.userExists(targetTc)) {
                    transferInfoLabel.setText("Alıcı TC bulunamadı!");
                    return;
                }
            }

            if (currentBalance < amount) {
                transferInfoLabel.setText("Yetersiz bakiye.");
                return;
            }

            // ✅ Para transferi işlemi
            DatabaseHelper.updateBalance(currentCustomer.getTc(), currentBalance - amount);
            DatabaseHelper.updateBalance(targetTc, targetBalance + amount);

            transferInfoLabel.setText("Transfer başarılı!");
            updateBalanceLabel();

        } catch (NumberFormatException e) {
            transferInfoLabel.setText("Sayısal bir tutar girin!");
        }
    }


    private void updateBalanceLabel() {
        double balance = DatabaseHelper.getBalance(currentCustomer.getTc());
        balanceLabel.setText("₺" + balance);
    }
}
