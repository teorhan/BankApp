import javafx.beans.property.SimpleStringProperty;

public class UserTableModel {
    private final SimpleStringProperty tc;
    private final SimpleStringProperty password;

    public UserTableModel(String tc, String password) {
        this.tc = new SimpleStringProperty(tc);
        this.password = new SimpleStringProperty(password);
    }

    public String getTc() {
        return tc.get();
    }

    public String getPassword() {
        return password.get();
    }
}
