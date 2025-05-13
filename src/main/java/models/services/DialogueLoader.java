package models.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;

public class DialogueLoader {
    public static HashMap<String, String> loadJsonToMap(String filePath) {
        Gson gson = new Gson();
        HashMap<String, String> map = new HashMap<>();

        try (FileReader reader = new FileReader(filePath)) {
            Type type = new TypeToken<HashMap<String, String>>(){}.getType();
            map = gson.fromJson(reader, type);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        return map;
    }
}
