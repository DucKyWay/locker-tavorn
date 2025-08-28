package ku.cs.models.account;

import java.util.ArrayList;
import java.io.Serializable;
public class UserList implements Serializable {
    private ArrayList<User> users;
    public UserList(){
        users = new ArrayList<>();
    }
    public void addUser(String username, String password,String name,
                        String email, String telphone){
        username = username.trim();
        password = password.trim();
        name = name.trim();
        email = email.trim();
        telphone = telphone.trim();
        if(!username.isEmpty() || !password.isEmpty() || !name.isEmpty() || !email.isEmpty() || !telphone.isEmpty()){
            users.add(new User(username,password,name,email,telphone, Role.USER));
        }
    }
    public void addUser(String username, String password, String name,
                        String email, String telphone, int request_id, boolean suspend, String image) {
        username = username.trim();
        password = password.trim();
        name = name.trim();
        email = email.trim();
        telphone = telphone.trim();
        if(image==null || image.isEmpty()){
            users.add(new User(username,password,name,email,telphone, Role.USER));
        }else {
            image = image.trim();
            if (!username.isEmpty() || !password.isEmpty() || !name.isEmpty() || !email.isEmpty() || !telphone.isEmpty()) {
                users.add(new User(username, password, name, email, telphone, request_id, suspend, Role.USER));
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
