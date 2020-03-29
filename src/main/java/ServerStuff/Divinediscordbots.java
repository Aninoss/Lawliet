package ServerStuff;

import Constants.Settings;
import General.Internet.Internet;
import General.Internet.InternetProperty;
import General.Internet.InternetResponse;
import General.SecretManager;
import javafx.util.Pair;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Divinediscordbots {

    public static boolean updateServerCount(int serverCount) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("server_count", serverCount);
            InternetProperty[] properties = new InternetProperty[]{
                    new InternetProperty("Content-Type", "application/json"),
                    new InternetProperty("Authorization", SecretManager.getString("divinediscordbots.token"))
            };
            InternetResponse internetResponse = Internet.getData(String.format("https://divinediscordbots.com/bot/%d/stats", Settings.LAWLIET_ID), jsonObject.toString(), properties).get();
            return internetResponse.getCode() == 200;
        } catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

}