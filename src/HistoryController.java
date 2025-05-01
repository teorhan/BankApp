import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

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
                data.add(new TransactionModel(
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getString("target_tc"),
                        rs.getString("timestamp")
                ));
            }

            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
            targetCol.setCellValueFactory(new PropertyValueFactory<>("targetTc"));
            timeCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

            historyTable.setItems(data);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
