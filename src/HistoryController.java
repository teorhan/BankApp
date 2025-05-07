import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableCell;

import java.sql.*;

public class HistoryController {

    @FXML
    private TableView<TransactionModel> historyTable;
    @FXML
    private TableColumn<TransactionModel, String> typeCol;
    @FXML
    private TableColumn<TransactionModel, Double> amountCol;
    @FXML
    private TableColumn<TransactionModel, String> targetCol;
    @FXML
    private TableColumn<TransactionModel, String> timeCol;

    private String currentTc;

    public void setTc(String tc) {
        this.currentTc = tc;
        loadTransactions();
    }

    private void loadTransactions() {
        ObservableList<TransactionModel> data = FXCollections.observableArrayList();
        String sql = "SELECT * FROM transactions WHERE tc = ? ORDER BY timestamp DESC";

        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, currentTc);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String type = rs.getString("type");
                String target = rs.getString("target_tc");
                double amount = rs.getDouble("amount");
                String timestamp = rs.getString("timestamp");

                // 🔍 Altın işlemi kontrolü
                if (type.contains("Altın")) {
                    String islem = type.contains("Alım") ? "Altın Alım" : "Altın Satım";

                    // İşlem türüne göre sabit fiyat belirle
                    double altinFiyat = type.contains("Alım") ? 4205.16 : 4205.64;

                    // Tutar / birim fiyat => kaç gram alındı/satıldı
                    double gramMiktari = amount / altinFiyat;

                    // Yeni gösterim
                    type = String.format("%s (%.2f gr)", islem, gramMiktari);
                    target = "-";
                }





                data.add(new TransactionModel(type, amount, target, timestamp));
            }

            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
            targetCol.setCellValueFactory(new PropertyValueFactory<>("targetTc"));
            timeCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

            // 🔸 Stil farkı eklemek isteyenler için (isteğe bağlı):
            typeCol.setCellFactory(column -> new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);
                        if (item.contains("Altın Alım")) {
                            setStyle("-fx-text-fill: goldenrod;");
                        } else if (item.contains("Altın Satım")) {
                            setStyle("-fx-text-fill: crimson;");
                        } else if (item.contains("Transfer")) {
                            setStyle("-fx-text-fill: steelblue;");
                        } else {
                            setStyle("-fx-text-fill: black;");
                        }
                    }
                }
            });

            historyTable.setItems(data);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}