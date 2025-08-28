package ku.cs.services.datasources;
import ku.cs.models.account.User;
import ku.cs.models.account.UserList;
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
    public void writeData(UserList data) {
        String filePath = directoryName + File.separator + fileName;
        Jsonb jsonb = JsonbBuilder.create();
        String result = jsonb.toJson(data.getUsers());
        result = result.replace("},", "},\n");
        try (PrintWriter out = new PrintWriter(new FileWriter(filePath))) {
            out.print(result);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
