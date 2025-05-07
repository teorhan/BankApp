import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GoldRateFetcher {

    private static final String API_URL = "https://finans.truncgil.com/v3/today.json";

    private static JSONObject fetchData() throws Exception {
        URL url = new URL(API_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("User-Agent", "Mozilla/5.0"); // 403 hatası için

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder jsonContent = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            jsonContent.append(line);
        }
        in.close();
        return new JSONObject(jsonContent.toString());
    }

    public static double getGramAltinAlis() {
        try {
            JSONObject data = fetchData();
            JSONObject gramAltin = data.getJSONObject("gram-altin");
            String raw = gramAltin.getString("Buying");  // Örn: "4.205,16"
            String cleaned = raw.replace(".", "").replace(",", "."); // 4205.16
            return Double.parseDouble(cleaned);
        } catch (Exception e) {
            System.out.println("❌ Alış verisi alınamadı: " + e.getMessage());
            return 0.0;
        }
    }

    public static double getGramAltinSatis() {
        try {
            JSONObject data = fetchData();
            JSONObject gramAltin = data.getJSONObject("gram-altin");
            String raw = gramAltin.getString("Selling");  // Örn: "4.205,64"
            String cleaned = raw.replace(".", "").replace(",", ".");
            return Double.parseDouble(cleaned);
        } catch (Exception e) {
            System.out.println("❌ Satış verisi alınamadı: " + e.getMessage());
            return 0.0;
        }
    }
}
