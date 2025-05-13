package models.services;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class MissionsLoader {

    public static ArrayList<String> loadMissionsFromJson(String filePath) {
        ArrayList<String> missions = new ArrayList<>();
        try {
            // خواندن فایل JSON
            FileReader reader = new FileReader(filePath);
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(reader, JsonObject.class);

            // استخراج آرایه "missions"
            JsonArray missionsArray = jsonObject.getAsJsonArray("missions");

            // اضافه کردن جملات به آرایه لیست
            for (int i = 0; i < missionsArray.size(); i++) {
                missions.add(missionsArray.get(i).getAsString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return missions;
    }
}
