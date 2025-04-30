import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;


public class DatabaseHelper {

    private static final String URL = "jdbc:sqlite:fidanbank.db";

    // Veritabanına bağlan
    public static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // users tablosunu oluştur (seriNo kaldırıldı)
    public static void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "tc TEXT NOT NULL UNIQUE,"
                + "password TEXT NOT NULL,"
                + "balance REAL DEFAULT 0.0"
                + ");";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
            System.out.println("✅ Kullanıcı tablosu oluşturuldu veya zaten mevcut.");
        } catch (SQLException e) {
            System.out.println("❌ Tablo oluşturulamadı: " + e.getMessage());
        }
    }
    public static double getBalance(String tc) {
        double balance = 0.0;
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement("SELECT balance FROM users WHERE tc = ?")) {
            ps.setString(1, tc);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                balance = rs.getDouble("balance");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return balance;
    }

    public static void updateBalance(String tc, double newBalance) {
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement("UPDATE users SET balance = ? WHERE tc = ?")) {
            ps.setDouble(1, newBalance);
            ps.setString(2, tc);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
