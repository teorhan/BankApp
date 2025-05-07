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
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "tc TEXT NOT NULL UNIQUE," +
                "password TEXT NOT NULL," +
                "balance REAL DEFAULT 0.0" +
                ");";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
            System.out.println("✅ Kullanıcı tablosu oluşturuldu veya zaten mevcut.");

            // Altın sütununu varsa ekleme
            addGoldColumnIfMissing();

        } catch (SQLException e) {
            System.out.println("❌ Tablo oluşturulamadı: " + e.getMessage());
        }
    }

    private static void addGoldColumnIfMissing() {
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement("ALTER TABLE users ADD COLUMN gold REAL DEFAULT 0.0")) {
            ps.execute();
            System.out.println("✅ 'gold' sütunu başarıyla eklendi.");
        } catch (SQLException e) {
            if (e.getMessage().contains("duplicate column name") || e.getMessage().contains("already exists")) {
                System.out.println("ℹ️ 'gold' sütunu zaten mevcut.");
            } else {
                System.out.println("❌ 'gold' sütunu eklenemedi: " + e.getMessage());
            }
        }
    }



    public static boolean userExists(String tc) {
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM users WHERE tc = ?")) {
            ps.setString(1, tc);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // Eğer satır varsa kullanıcı vardır
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
    public static double getGold(String tc) {
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement("SELECT gold FROM users WHERE tc = ?")) {
            ps.setString(1, tc);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("gold");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public static void updateGold(String tc, double newGoldAmount) {
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement("UPDATE users SET gold = ? WHERE tc = ?")) {
            ps.setDouble(1, newGoldAmount);
            ps.setString(2, tc);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createTransactionsTable() {
        String sql = "CREATE TABLE IF NOT EXISTS transactions ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "tc TEXT NOT NULL,"
                + "type TEXT NOT NULL,"
                + "amount REAL NOT NULL,"
                + "target_tc TEXT,"
                + "timestamp DATETIME DEFAULT CURRENT_TIMESTAMP"
                + ");";

        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.execute();
            System.out.println("✅ Transaction tablosu oluşturuldu veya zaten mevcut.");
        } catch (SQLException e) {
            System.out.println("❌ Transaction tablosu oluşturulamadı: " + e.getMessage());
        }
    }
    public static void logTransaction(String tc, String type, double amount, String targetTc) {
        String sql = "INSERT INTO transactions(tc, type, amount, target_tc) VALUES (?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tc);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            ps.setString(4, targetTc);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
