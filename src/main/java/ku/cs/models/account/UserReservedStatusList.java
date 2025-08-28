package ku.cs.models.account;

import java.util.ArrayList;

public class UserReservedStatusList {
    private ArrayList<UserReservedStatus> userReservedStatusList;
    public UserReservedStatusList(){
        userReservedStatusList = new ArrayList<>();
    }
    public void addUserReservedStatus(String id, String typeKey, String zone, String status){
        userReservedStatusList.add(new UserReservedStatus(id, typeKey, zone, status));
    }
    public UserReservedStatus findUserReservedStatusById(String id){
        for(UserReservedStatus userReservedStatus : userReservedStatusList){
            if(userReservedStatus.getId().equals(id)){
                return userReservedStatus;
            }
        }
        return null;
    }
    public ArrayList<UserReservedStatus> getUserReservedStatus() {
        return userReservedStatusList;
    }
}
