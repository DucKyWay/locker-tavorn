package ku.cs.models.request;

import java.util.ArrayList;

public class RequestList {
    ArrayList<Request> requestList = new ArrayList<>();
    public void addRequest(Request request){
        requestList.add(request);
    }
    public ArrayList<Request> getRequestList(){
        return requestList;
    }
    public Request findRequestbyIdLocker(String uuid){
        for(Request request : requestList){
            if(request.getUuidLocker().equals(uuid)){
                return request;
            }
        }
        return null;
    }
}
