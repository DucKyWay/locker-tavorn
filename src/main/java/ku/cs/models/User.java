package ku.cs.models;

public class User {
    private String username;
    private String name;
    private String password;
    private String email;
    private String telphone;
    private int requset_id; //ไว้เก็บการrequest ของzone และ id ใน zone นั้นๆ
    private String image;
    private boolean suspend;
    //เพิ่ม locker
    public User(String username, String password,String name, String email, String telphone) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.email = email;
        this.telphone = telphone;
        this.suspend = false;
        this.image = null;
    }
    public User(String username, String password,String name, String email, String telphone,int requset_id,boolean suspend,String image) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.email = email;
        this.telphone = telphone;
        this.requset_id = requset_id;
        this.suspend =suspend;
        this.image = image;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public void setSuspend(boolean suspend) {
        this.suspend = suspend;
    }
    public void setRequset_id(int requset_id) {
        this.requset_id = requset_id;
    }
    public String getUsername() {
        return username;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
    public String getTelphone() {
        return telphone;
    }
    public int getRequset_id() {
        return requset_id;
    }
    public String getImage() {
        return image;
    }
    public boolean getSuspend() {
        return suspend;
    }
    public String getPassword() {
        return password;
    }
    public boolean isSuspend() {
        return suspend;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                "password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", telphone='" + telphone + '\'' +
                ", requset_id=" + requset_id +
                ", image='" + image + '\'' +
                ", suspend=" + suspend +
                '}';
    }
}
