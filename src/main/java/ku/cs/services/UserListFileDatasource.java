package ku.cs.services;
import jakarta.json.*;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonReader;
import jakarta.json.bind.JsonbConfig;
import ku.cs.models.User;
import ku.cs.models.UserList;
import java.lang.reflect.Type;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class UserListFileDatasource implements Datasource<UserList> {
    private String directoryName;
    private String fileName;

    public UserListFileDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
    }
    private void checkFileIsExisted() {
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdir();
        }
        String filePath = directoryName + File.separator +fileName;
        file = new File(filePath);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    public UserList readData() {
        UserList userList = new UserList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        if (!file.exists() || file.length() == 0) {
            System.out.println("ไฟล์ว่างหรือไม่มีอยู่");
            return userList;
        }

        Jsonb jsonb = JsonbBuilder.create();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line); // อ่านทั้งไฟล์มาต่อกัน
            }

            // แปลงเป็น List<User>
            List<User> users = jsonb.fromJson(sb.toString(), new ArrayList<User>(){}.getClass().getGenericSuperclass());
            for(User user : users){
                userList.addUser(user);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return userList;
    }



    @Override
    public void writeData(UserList data) throws IOException {
        String filePath = directoryName + File.separator + fileName;
        Jsonb jsonb = JsonbBuilder.create();
        String result = jsonb.toJson(data.getUsers());
        result = result.replace("},", "},\n");
        try (PrintWriter out = new PrintWriter(new FileWriter(filePath))) {
            out.print(result);
        }
    }


}
