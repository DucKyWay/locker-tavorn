package ku.cs.services;

import ku.cs.models.User;
import ku.cs.models.UserList;

import java.io.*;

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
        String filePath = directoryName + File.separator +fileName;
        File file = new File(filePath);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream =new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
        BufferedReader buffer = new BufferedReader(inputStreamReader);
        String line = "";
        try{
            while(true){
                if (!((line = buffer.readLine()) != null)) break;
                if (line.equals("")) break;
                String[] lineArray = line.split(",");
                String username = lineArray[0].trim();
                String password = lineArray[1].trim();
                String name = lineArray[2].trim();
                String email = lineArray[3].trim();
                String telphone = lineArray[4].trim();
                int requset_id = Integer.parseInt(lineArray[5].trim());
                String image = lineArray[6].trim();
                boolean suspend = Boolean.parseBoolean(lineArray[7].trim());
                //Alikato,211225,Manus,manus@ku.th,0903999647, 0,null,false
                userList.addUser(username,password,name,email,telphone,requset_id,suspend,image);
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        return userList;
    }

    @Override
    public void writeData(UserList data) {
        String filePath = directoryName + File.separator +fileName;
        File file = new File(filePath);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        BufferedWriter buffer = new BufferedWriter(outputStreamWriter);
        try{
            for(User user : data.getUsers()){
                String line = user.getUsername()+','+user.getPassword()+','+user.getName()+','+user.getEmail()+','+user.getTelphone()+','+user.getRequset_id()+','+user.getImage()+','+user.getSuspend();
                buffer.append(line);
                buffer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                buffer.flush();
                buffer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
