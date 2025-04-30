import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RegisterController {

    @FXML
    private TextField tcField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label infoLabel;

    @FXML
    private void handleRegister() {
        String tc = tcField.getText().trim();
        String password = passwordField.getText().trim();

        if (tc.length() != 11 || !tc.matches("\\d+")) {
            infoLabel.setText("TC Kimlik No 11 haneli rakam olmalı.");
            return;
        }

        if (password.isEmpty()) {
            infoLabel.setText("Şifre boş olamaz.");
            return;
        }

        String sql = "INSERT INTO users(tc, password, balance) VALUES (?, ?, 0.0)";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tc);
            stmt.setString(2, password);
            stmt.executeUpdate();

            infoLabel.setText("Kayıt başarılı! Giriş ekranına dönülüyor...");
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    javafx.application.Platform.runLater(this::handleGoBack);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                infoLabel.setText("Bu TC numarasıyla zaten kayıt var.");
            } else {
                infoLabel.setText("Hata: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleGoBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) tcField.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("FidanBank Giriş");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
