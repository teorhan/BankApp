import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
