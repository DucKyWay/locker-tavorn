package ku.cs.models;

import java.util.ArrayList;

public class UserList {
    private ArrayList<User> users;
    public UserList(){
        users = new ArrayList<>();
    }
    public void addUser(String username, String password,String name, String email, String telphone){
        username = username.trim();
        password = password.trim();
        name = name.trim();
        email = email.trim();
        telphone = telphone.trim();
        if(!username.isEmpty() || !password.isEmpty() || !name.isEmpty() || !email.isEmpty() || !telphone.isEmpty()){
            users.add(new User(username,password,name,email,telphone));
        }
    }
    public void addUser(String username, String password, String name, String email, String telphone, int requset_id, boolean suspend, String image) {
        username = username.trim();
        password = password.trim();
        name = name.trim();
        email = email.trim();
        telphone = telphone.trim();
        image = image.trim();
        if(!username.isEmpty() || !password.isEmpty() || !name.isEmpty() || !email.isEmpty() || !telphone.isEmpty()){
            users.add(new User(username,password,name,email,telphone,requset_id,suspend,image));
        }
    }

    public User findUserById(String username){
        for(User user : users){
            if(user.getUsername().equals(username)){
                return user;
            }
        }
        return null;
    }
    public ArrayList<User> getUsers(){
        return users;
    }


}
