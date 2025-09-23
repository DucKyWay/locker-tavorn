package ku.cs.models.account;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.io.Serializable;
public class UserList implements Serializable {
    private ArrayList<User> users;
    public UserList(){
        users = new ArrayList<>();
    }
    public void addUser(String username, String password, String name,
                        String email, String phone, LocalDateTime logintime) {
        username = username.trim();
        password = password.trim();
        name = name.trim();
        email = email.trim();
        phone = phone.trim();
        if(!username.isEmpty() || !password.isEmpty() || !name.isEmpty() || !email.isEmpty() || !phone.isEmpty()){
            users.add(new User(username,name,password,email,phone, Role.USER,logintime));
        }
    }
    public void addUser(String username, String password, String name,
                        String email, String phone, boolean suspend, String image, LocalDateTime logintime) {
        username = username.trim();
        password = password.trim();
        name = name.trim();
        email = email.trim();
        phone = phone.trim();
        if(image==null || image.isEmpty()){
            users.add(new User(username,password,name,email,phone, Role.USER,logintime));
        }else {
            image = image.trim();
            if (!username.isEmpty() || !password.isEmpty() || !name.isEmpty() || !email.isEmpty() || !phone.isEmpty()) {
                users.add(new User(username, name,password, email, phone, suspend, Role.USER,logintime));
            }
        }
    }
    public void addUser(User user){
        users.add(user);
    }

    public User findUserByUsername(String username){
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
