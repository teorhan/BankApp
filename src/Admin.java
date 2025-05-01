import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Admin extends User {

    public Admin(String tc, String password) {
        super(tc, password);
    }

    @Override
    public String getRole() {
        return "Admin";
    }

    // Admin'e özel işlemler
    public void resetUserPassword(String targetTc, String newPassword) {
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement ps = conn.prepareStatement("UPDATE users SET password = ? WHERE tc = ?")) {
            ps.setString(1, newPassword);
            ps.setString(2, targetTc);
            ps.executeUpdate();
            System.out.println("Şifre sıfırlandı.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteUser(String targetTc) {
        try (Connection conn = DatabaseHelper.connect();
             PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE tc = ?")) {
            ps.setString(1, targetTc);
            ps.executeUpdate();
            System.out.println("Kullanıcı silindi.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
