import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.control.*;


public class DashboardController {

    @FXML private BorderPane rootPane;
    @FXML private VBox sidebar;
    @FXML private StackPane contentArea;
    @FXML private Button menuButton;

    private Customer currentCustomer;

    public void setUserTc(String tc) {
        this.currentCustomer = new Customer(tc, null);
        showBalanceView();
    }

    @FXML
    private void toggleSidebar() {
        sidebar.setVisible(!sidebar.isVisible());
    }

    @FXML
    private void showBalanceView() {
        Label label = new Label("Bakiyeniz: ₺" + DatabaseHelper.getBalance(currentCustomer.getTc()));
        label.setStyle("-fx-font-size: 24px; -fx-text-fill: #1B5E20; -fx-font-weight: bold;");

        VBox box = new VBox(label);
        box.setStyle("-fx-background-color: white; -fx-padding: 30; -fx-alignment: center;");

        contentArea.getChildren().setAll(box);
    }

    @FXML
    private void showDepositWithdrawView() {
        VBox layout = new VBox(15);
        layout.setStyle("-fx-background-color: white; -fx-padding: 30; -fx-alignment: center;");

        Label title = new Label("Para İşlemleri");
        title.setStyle("-fx-text-fill: #1B5E20; -fx-font-size: 24px; -fx-font-weight: bold;");

        TextField amountField = new TextField();
        amountField.setPromptText("Tutar (₺)");
        amountField.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-font-size: 14px; -fx-padding: 10; -fx-background-color: #1B5E20; -fx-text-fill: white; -fx-pref-width: 250;");

        Button depositBtn = new Button("Yatır");
        depositBtn.setStyle("-fx-background-color: #1B5E20; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 8 16;");

        Button withdrawBtn = new Button("Çek");
        withdrawBtn.setStyle("-fx-background-color: #1B5E20; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 8 16;");

        Label info = new Label();
        info.setStyle("-fx-text-fill: #1B5E20; -fx-font-size: 12px;");

        depositBtn.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                currentCustomer.deposit(amount);
                showBalanceView();
            } catch (Exception ex) {
                info.setText("❌ Geçerli bir tutar girin!");
            }
        });

        withdrawBtn.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                currentCustomer.withdraw(amount);
                showBalanceView();
            } catch (Exception ex) {
                info.setText("❌ Geçerli bir tutar girin!");
            }
        });

        HBox buttons = new HBox(10, depositBtn, withdrawBtn);
        buttons.setStyle("-fx-alignment: center;");

        layout.getChildren().addAll(title, amountField, buttons, info);
        contentArea.getChildren().setAll(layout);
    }

    @FXML
    private void showTransferView() {
        VBox layout = new VBox(15);
        layout.setStyle("-fx-background-color: white; -fx-padding: 30; -fx-alignment: center;");

        Label title = new Label("Para Gönder");
        title.setStyle("-fx-text-fill: #1B5E20; -fx-font-size: 24px; -fx-font-weight: bold;");

        TextField targetField = new TextField();
        targetField.setPromptText("Alıcı TC");
        targetField.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-font-size: 14px; -fx-padding: 10; -fx-background-color: #1B5E20; -fx-text-fill: white; -fx-pref-width: 250;");

        TextField amountField = new TextField();
        amountField.setPromptText("Gönderilecek Tutar (₺)");
        amountField.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-font-size: 14px; -fx-padding: 10; -fx-background-color: #1B5E20; -fx-text-fill: white; -fx-pref-width: 250;");

        Button sendBtn = new Button("Gönder");
        sendBtn.setStyle("-fx-background-color: #1B5E20; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10; -fx-cursor: hand; -fx-padding: 8 16;");

        Label info = new Label();
        info.setStyle("-fx-text-fill: #1B5E20; -fx-font-size: 12px;");

        sendBtn.setOnAction(e -> {
            String targetTc = targetField.getText().trim();
            try {
                double amount = Double.parseDouble(amountField.getText());
                double current = DatabaseHelper.getBalance(currentCustomer.getTc());
                if (DatabaseHelper.userExists(targetTc) && !targetTc.equals(currentCustomer.getTc()) && current >= amount) {
                    DatabaseHelper.updateBalance(currentCustomer.getTc(), current - amount);
                    DatabaseHelper.updateBalance(targetTc, DatabaseHelper.getBalance(targetTc) + amount);
                    DatabaseHelper.logTransaction(currentCustomer.getTc(), "Transfer (gönderici)", amount, targetTc);
                    DatabaseHelper.logTransaction(targetTc, "Transfer (alıcı)", amount, currentCustomer.getTc());
                    info.setText("✅ Transfer başarılı!");
                    showBalanceView();
                } else {
                    info.setText("❌ Geçersiz işlem.");
                }
            } catch (Exception ex) {
                info.setText("❌ Geçerli bir tutar girin.");
            }
        });

        layout.getChildren().addAll(title, targetField, amountField, sendBtn, info);
        contentArea.getChildren().setAll(layout);
    }



    @FXML
    private void handleShowHistory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/history.fxml"));
            Scene scene = new Scene(loader.load());
            HistoryController controller = loader.getController();
            controller.setTc(currentCustomer.getTc());

            Stage stage = new Stage();
            stage.setTitle("İşlem Geçmişi");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) rootPane.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("FidanBank Giriş");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}