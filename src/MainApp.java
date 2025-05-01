import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("FidanBank Giriş");
        stage.show();
    }

    public static void main(String[] args) {
        // ADIM 4 — Veritabanı tablosunu uygulama başlamadan önce oluştur
        DatabaseHelper.createUsersTable();
        DatabaseHelper.createTransactionsTable(); // users tablosunun altına ekle


        launch(args); // JavaFX uygulamasını başlat
    }
}
