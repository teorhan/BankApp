import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AdminController {

    @FXML
    private TextField targetTcField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private Label infoLabel;

    private Admin admin;

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    @FXML
    private void handleResetPassword() {
        String targetTc = targetTcField.getText().trim();
        String newPass = newPasswordField.getText().trim();

        if (targetTc.isEmpty() || newPass.isEmpty()) {
            infoLabel.setText("Alanlar boş olamaz.");
            return;
        }

        admin.resetUserPassword(targetTc, newPass);
        infoLabel.setText("Şifre sıfırlandı.");
    }

    @FXML
    private void handleDeleteUser() {
        String targetTc = targetTcField.getText().trim();

        if (targetTc.isEmpty()) {
            infoLabel.setText("TC alanı boş olamaz.");
            return;
        }

        admin.deleteUser(targetTc);
        infoLabel.setText("Kullanıcı silindi.");
    }
}
