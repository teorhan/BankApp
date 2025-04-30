import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DashboardController {

    @FXML
    private Label balanceLabel;

    private int balance = 1000; // Başlangıç bakiyesi: 1000 TL

    @FXML
    private void initialize() {
        updateBalanceLabel();
    }

    @FXML
    private void handleDeposit() {
        balance += 100; // 100 TL yatır
        updateBalanceLabel();
    }

    @FXML
    private void handleWithdraw() {
        if (balance >= 100) {
            balance -= 100; // 100 TL çek
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
