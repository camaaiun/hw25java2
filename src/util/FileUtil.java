package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import payment.Login;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Path path;

    public FileUtil(String path) { this.path = Paths.get(path);}

    public Login[] readFile() {
        try {
            String str = Files.readString(path);
            return GSON.fromJson(str, Login[].class);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return new Login[0];
        }
    }

    public void writeFile(Login[] products) {
        String json = GSON.toJson(products);

        byte[] bytes = json.getBytes();
        try {
            Files.write(path, bytes);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
