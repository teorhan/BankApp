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
        boolean isVisible = sidebar.isVisible();
        sidebar.setVisible(!isVisible);
        sidebar.setManaged(!isVisible); // ← BU ÖNEMLİ
    }
    private void hideSidebar() {
        sidebar.setVisible(false);
        sidebar.setManaged(false);
    }


    @FXML
    private void showBalanceView() {
        double balance = DatabaseHelper.getBalance(currentCustomer.getTc());
        double gold = DatabaseHelper.getGold(currentCustomer.getTc());

        Label balanceLabel = new Label(String.format("Bakiyeniz: ₺%.2f", balance));

        balanceLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #1B5E20; -fx-font-weight: bold;");

        Label goldLabel = new Label("Altın Miktarınız: " + String.format("%.2f", gold) + " gr");
        goldLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #1B5E20; -fx-font-weight: bold;");

        VBox box = new VBox(10, balanceLabel, goldLabel);
        box.setStyle("-fx-background-color: white; -fx-padding: 30; -fx-alignment: center;");

        contentArea.getChildren().setAll(box);
        hideSidebar();
    }


    @FXML
    private void showDepositWithdrawView() {
        VBox layout = new VBox(15);
        layout.setStyle("-fx-background-color: white; -fx-padding: 30; -fx-alignment: center;");

        double balance = DatabaseHelper.getBalance(currentCustomer.getTc());

        // ✅ Güncel bakiye etiketi
        Label balanceLabel = new Label(String.format("Mevcut Bakiye: ₺%.2f", balance));
        balanceLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #1B5E20; -fx-font-weight: bold;");

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

                double updatedBalance = DatabaseHelper.getBalance(currentCustomer.getTc());
                balanceLabel.setText(String.format("Mevcut Bakiye: ₺%.2f", updatedBalance)); // ✅ güncelle
                info.setText("✅ Para yatırıldı.");
            } catch (Exception ex) {
                info.setText("❌ Geçerli bir tutar girin!");
            }
        });

        withdrawBtn.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                currentCustomer.withdraw(amount);

                double updatedBalance = DatabaseHelper.getBalance(currentCustomer.getTc());
                balanceLabel.setText(String.format("Mevcut Bakiye: ₺%.2f", updatedBalance)); // ✅ güncelle
                info.setText("✅ Para çekildi.");
            } catch (Exception ex) {
                info.setText("❌ Geçerli bir tutar girin!");
            }
        });


        HBox buttons = new HBox(10, depositBtn, withdrawBtn);
        buttons.setStyle("-fx-alignment: center;");

        layout.getChildren().addAll(balanceLabel, title, amountField, buttons, info);
        contentArea.getChildren().setAll(layout);
        hideSidebar();
    }

    @FXML
    private void showTransferView() {
        VBox layout = new VBox(15);
        layout.setStyle("-fx-background-color: white; -fx-padding: 30; -fx-alignment: center;");

        double balance = DatabaseHelper.getBalance(currentCustomer.getTc());

        // ✅ Güncel bakiye etiketi
        Label balanceLabel = new Label(String.format("Mevcut Bakiye: ₺%.2f", balance));
        balanceLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #1B5E20; -fx-font-weight: bold;");

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

                    double updatedBalance = DatabaseHelper.getBalance(currentCustomer.getTc());
                    balanceLabel.setText(String.format("Mevcut Bakiye: ₺%.2f", updatedBalance)); // ✅ güncelle
                    info.setText("✅ Transfer başarılı!");
                } else {
                    info.setText("❌ Geçersiz işlem.");
                }
            } catch (Exception ex) {
                info.setText("❌ Geçerli bir tutar girin.");
            }
        });


        layout.getChildren().addAll(balanceLabel, title, targetField, amountField, sendBtn, info);
        contentArea.getChildren().setAll(layout);
        hideSidebar();
    }


    @FXML
    private void showGoldTradeView() {
        VBox layout = new VBox(15);
        layout.setStyle("-fx-background-color: white; -fx-padding: 30; -fx-alignment: center;");

        double balance = DatabaseHelper.getBalance(currentCustomer.getTc());
        double gold = DatabaseHelper.getGold(currentCustomer.getTc());

        double alisFiyat = GoldRateFetcher.getGramAltinAlis();
        double satisFiyat = GoldRateFetcher.getGramAltinSatis();

        Label balanceLabel = new Label(String.format("Bakiyeniz: ₺%.2f", balance));
        balanceLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #1B5E20; -fx-font-weight: bold;");

        Label goldLabel = new Label("Mevcut Gram Altın: " + gold + " gr");
        goldLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #1B5E20; -fx-font-weight: bold;");

        Label fiyatLabel = new Label("Alış: ₺" + alisFiyat + "   Satış: ₺" + satisFiyat);
        fiyatLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #1B5E20;");

        TextField gramField = new TextField();
        gramField.setPromptText("Alınacak/Satılacak Gram");
        gramField.setStyle("-fx-background-color: #1B5E20; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-size: 14px; -fx-pref-width: 250;");

        Button buyBtn = new Button("Altın Al");
        buyBtn.setStyle("-fx-background-color: #1B5E20; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10;");

        Button sellBtn = new Button("Altın Sat");
        sellBtn.setStyle("-fx-background-color: #1B5E20; -fx-text-fill: white; -fx-font-size: 14px; -fx-background-radius: 10;");

        Label resultLabel = new Label();
        resultLabel.setStyle("-fx-text-fill: #1B5E20; -fx-font-size: 12px;");

        buyBtn.setOnAction(e -> {
            try {
                double miktar = Double.parseDouble(gramField.getText());
                double toplamTutar = miktar * alisFiyat;

                if (balance >= toplamTutar) {
                    DatabaseHelper.updateBalance(currentCustomer.getTc(), balance - toplamTutar);
                    DatabaseHelper.updateGold(currentCustomer.getTc(), gold + miktar);
                    DatabaseHelper.logTransaction(currentCustomer.getTc(), "Altın Alım", toplamTutar, "Gram: " + miktar);
                    resultLabel.setText("✅ " + miktar + " gr altın alındı.");
                    showGoldTradeView(); // ekranı güncelle
                } else {
                    resultLabel.setText("❌ Yetersiz bakiye.");
                }
            } catch (Exception ex) {
                resultLabel.setText("❌ Geçerli bir gram girin.");
            }
        });

        sellBtn.setOnAction(e -> {
            try {
                double miktar = Double.parseDouble(gramField.getText());
                if (gold >= miktar) {
                    double gelir = miktar * satisFiyat;
                    DatabaseHelper.updateBalance(currentCustomer.getTc(), balance + gelir);
                    DatabaseHelper.updateGold(currentCustomer.getTc(), gold - miktar);
                    DatabaseHelper.logTransaction(currentCustomer.getTc(), "Altın Satım", gelir, "Gram: " + miktar);
                    resultLabel.setText("✅ " + miktar + " gr altın satıldı.");
                    showGoldTradeView(); // ekranı güncelle
                } else {
                    resultLabel.setText("❌ Yetersiz gram altın.");
                }
            } catch (Exception ex) {
                resultLabel.setText("❌ Geçerli bir gram girin.");
            }
        });

        layout.getChildren().addAll(balanceLabel, goldLabel, fiyatLabel, gramField, buyBtn, sellBtn, resultLabel);
        contentArea.getChildren().setAll(layout);
        hideSidebar();
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