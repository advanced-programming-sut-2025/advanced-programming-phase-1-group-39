package services;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class LoginPersistence {
    private static final String FILE_PATH = "src/main/data/users/loggedInUser.json";
    private static final Gson gson = new Gson();

    public static void saveLoggedInUser(String username) {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(Collections.singletonMap("username", username), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String loadLoggedInUsername() {
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Map<?, ?> data = gson.fromJson(reader, Map.class);
            return (String) data.get("username");
        } catch (IOException e) {
            return null;
        }
    }

    public static void clearLoggedInUser() {
        new File(FILE_PATH).delete();
    }
}

