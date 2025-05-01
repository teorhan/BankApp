import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class DashboardController {

    @FXML
    private Label balanceLabel;

    private Customer currentCustomer;

    // Kullanıcı TC’sini alıp Customer nesnesi oluşturur
    public void setUserTc(String tc) {
        this.currentCustomer = new Customer(tc, null); // Şifre burada gerekmediği için null
        updateBalanceLabel();
    }

    @FXML
    private void initialize() {
        // initialize içinde işlem yapılmaz çünkü setUserTc() sonradan çağrılıyor
    }

    @FXML
    private void handleDeposit() {
        currentCustomer.deposit(100); // Customer sınıfındaki işlem çağrılır
        updateBalanceLabel();
    }

    @FXML
    private void handleWithdraw() {
        currentCustomer.withdraw(100); // Customer sınıfındaki işlem çağrılır
        updateBalanceLabel();
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

    // Güncel bakiyeyi ekranda gösterir
    private void updateBalanceLabel() {
        double balance = DatabaseHelper.getBalance(currentCustomer.getTc());
        balanceLabel.setText("₺" + balance);
    }
}
