import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.scene.layout.HBox;

public class AdminController {

    @FXML
    private TableView<UserTableModel> userTable;

    @FXML
    private TableColumn<UserTableModel, String> tcColumn;

    @FXML
    private TableColumn<UserTableModel, Void> actionColumn;

    private final ObservableList<UserTableModel> userList = FXCollections.observableArrayList();
    private Admin admin;

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    @FXML
    public void initialize() {
        tcColumn.setCellValueFactory(new PropertyValueFactory<>("tc"));

        loadUsers();
        addActionButtons();
    }

    private void loadUsers() {
        userList.clear();
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement ps = conn.prepareStatement("SELECT tc, password FROM users");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                userList.add(new UserTableModel(rs.getString("tc"), rs.getString("password")));
            }

            userTable.setItems(userList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addActionButtons() {
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteBtn = new Button("Sil");
            private final Button changePassBtn = new Button("Şifre Değiştir");

            {
                deleteBtn.setOnAction(e -> {
                    UserTableModel user = getTableView().getItems().get(getIndex());
                    deleteUser(user.getTc());
                });

                changePassBtn.setOnAction(e -> {
                    UserTableModel user = getTableView().getItems().get(getIndex());
                    TextInputDialog dialog = new TextInputDialog();
                    dialog.setTitle("Şifre Değiştir");
                    dialog.setHeaderText("Yeni şifre girin:");
                    dialog.setContentText("Şifre:");

                    dialog.showAndWait().ifPresent(newPassword -> {
                        updatePassword(user.getTc(), newPassword);
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(10, deleteBtn, changePassBtn);
                    setGraphic(box);
                }
            }
        });
    }

    private void deleteUser(String tc) {
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE tc = ?")) {
            ps.setString(1, tc);
            ps.executeUpdate();
            loadUsers(); // tabloyu yenile
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updatePassword(String tc, String newPassword) {
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement ps = conn.prepareStatement("UPDATE users SET password = ? WHERE tc = ?")) {
            ps.setString(1, newPassword);
            ps.setString(2, tc);
            ps.executeUpdate();
            loadUsers(); // tabloyu yenile
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) userTable.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("FidanBank Giriş");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
