import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleLogin() {
        String tc = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (tc.isEmpty() || password.isEmpty()) {
            errorLabel.setText("TC ve şifre boş olamaz.");
            return;
        }

        // Admin özel durumu kontrol edilmeye devam ediliyor
        if (tc.equals("admin") && password.equals("admin")) {
            User admin = UserFactory.createUser(tc, password);
            openAdminDashboard((Admin) admin);
            return;
        }

        // Veritabanında kullanıcı kontrolü
        String sql = "SELECT * FROM users WHERE tc = ? AND password = ?";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tc);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                User user = UserFactory.createUser(tc, password);
                if (user instanceof Customer) {
                    openDashboard((Customer) user);
                } else {
                    errorLabel.setText("Geçersiz kullanıcı rolü.");
                }
            } else {
                errorLabel.setText("Hatalı TC veya şifre.");
            }

        } catch (Exception e) {
            errorLabel.setText("Bağlantı hatası: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void openDashboard(Customer customer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashboardLayout.fxml"));
            Scene scene = new Scene(loader.load());

            DashboardController controller = loader.getController();
            controller.setUserTc(customer.getTc());

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("FidanBank Dashboard");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openAdminDashboard(Admin admin) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin.fxml"));
            Scene scene = new Scene(loader.load());

            AdminController controller = loader.getController();
            controller.setAdmin(admin);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("FidanBank - Admin Paneli");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleForgotPassword() {
        errorLabel.setText("Şifre sıfırlama için banka ile iletişime geçiniz.");
    }

    @FXML
    private void handleGoToRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/register.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("FidanBank - Hesap Oluştur");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔽 Inner Factory Class
    private static class UserFactory {
        public static User createUser(String tc, String password) {
            if ("admin".equals(tc)) {
                return new Admin(tc, password);
            } else {
                return new Customer(tc, password);
            }
        }
    }
}
