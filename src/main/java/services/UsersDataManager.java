package services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import models.User;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class UsersDataManager {

    private static final String FILE_PATH = "src/main/resources/data/users/users.json"; // آدرس فایل JSON

    // تابع ذخیره اطلاعات کاربران در فایل JSON
    public static void saveUsers(ArrayList<User> users) {
        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // تابع بارگذاری اطلاعات از فایل JSON
    public static ArrayList<User> loadUsers() {
        Gson gson = new Gson();
        ArrayList<User> users = new ArrayList<>();
        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type userListType = new TypeToken<ArrayList<User>>() {}.getType();
            users = gson.fromJson(reader, userListType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }
}
