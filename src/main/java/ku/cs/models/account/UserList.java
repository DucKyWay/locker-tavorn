package ku.cs.models.account;

import java.util.ArrayList;
import java.io.Serializable;

public class UserList implements Serializable {
    private ArrayList<User> users;
    public UserList(){
        users = new ArrayList<>();
    }
    public void addUser(String username, String password, String firstname, String lastname,
                        String email, String phone) {

        if(!username.isEmpty() || !password.isEmpty() || !firstname.isEmpty() || !lastname.isEmpty() ||
                !email.isEmpty() || !phone.isEmpty()){
            users.add(new User(username, firstname, lastname, password, email, phone));
        }
    }

    public void addUser(User user){
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public boolean removeUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                users.remove(user);
                return true;
            }
        }
        return false;
    }

    public boolean canFindUserByUsername(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
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

    public int getCount() {
        return users.size();
    }
}
