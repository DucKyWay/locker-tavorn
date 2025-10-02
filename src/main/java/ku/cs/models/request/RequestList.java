package ku.cs.models.request;

import ku.cs.services.utils.UuidUtil;

import java.util.ArrayList;

public class RequestList {
    ArrayList<Request> requestList = new ArrayList<>();
    public void addRequest(Request request){
        boolean duplicate;
        do {
            duplicate = false;
            for (Request r : requestList) {
                if (request.getRequestUid().equals(r.getRequestUid())) {
                    // ถ้าเจอซ้ำ สร้างใหม่แล้วเช็คอีกครั้ง
                    request.setRequestUid(new UuidUtil().generateShort());
                    duplicate = true;
                    break;
                }
            }
        } while (duplicate);
        requestList.add(request);
    }
    public ArrayList<Request> getRequestList(){
        return requestList;
    }
    public Request findRequestbyIdLocker(String uuid){
        for(Request request : requestList){
            if(request.getLockerUid().equals(uuid)){
                return request;
            }
        }
        return null;
    }

    public Request findRequestByUuid(String uuid) {
        for(Request request : requestList){
            if(request.getRequestUid().equals(uuid)){
                return request;
            }
        }
        return null;
    }
}
