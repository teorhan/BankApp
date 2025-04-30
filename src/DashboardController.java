import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DashboardController {

    @FXML
    private Label balanceLabel;

    private String userTc; // Giriş yapan kişinin TC’si
    private double balance;

    public void setUserTc(String tc) {
        this.userTc = tc;
        this.balance = DatabaseHelper.getBalance(tc); // Veritabanından bakiye çek
        updateBalanceLabel();
    }

    @FXML
    private void initialize() {
        // setUserTc() çağrılmadan önce initialize çalıştığı için burada işlem yapılmaz
    }

    @FXML
    private void handleDeposit() {
        balance += 100;
        DatabaseHelper.updateBalance(userTc, balance);
        updateBalanceLabel();
    }

    @FXML
    private void handleWithdraw() {
        if (balance >= 100) {
            balance -= 100;
            DatabaseHelper.updateBalance(userTc, balance);
            updateBalanceLabel();
        } else {
            System.out.println("Yetersiz bakiye!");
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

    private void updateBalanceLabel() {
        balanceLabel.setText("₺" + balance);
    }
}
