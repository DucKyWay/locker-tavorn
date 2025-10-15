package ku.cs.models.request;

import ku.cs.services.utils.UuidUtil;

import java.util.ArrayList;
import java.util.List;

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

    public void addRequest(List<Request> requests_in) {
        for (Request request : requests_in) {
            boolean duplicate;
            do {
                duplicate = false;
                for (Request r : requestList) {
                    if (r.getRequestUid().equals(request.getRequestUid())) {
                        request.setRequestUid(new UuidUtil().generateShort());
                        duplicate = true;
                        break;
                    }
                }
            } while (duplicate);
            requestList.add(request);
        }
    }

    public List<Request> getRequests() {
        return requestList;
    }

    public ArrayList<Request> getRequestList(){
        return requestList;
    }
    public Request findRequestByLockerUid(String uid){
        for(Request request : requestList){
            if(request.getLockerUid().equals(uid)){
                return request;
            }
        }
        return null;
    }

    public Request findRequestByUid(String uid) {
        for(Request request : requestList){
            if(request.getRequestUid().equals(uid)){
                return request;
            }
        }
        return null;
    }

    public Request findLatestRequestByLockerUid(String uid) {
        Request latest = null;
        for (Request r : requestList) {
            if (r.getLockerUid().equals(uid)) {
                if (r.getRequestType().equals(RequestType.LATE)) {
                    latest = r;
                    break;
                }

                if (latest == null) {
                    latest = r;
                } else if (r.getStartDate().isAfter(latest.getStartDate())) {
                    latest = r;
                }
            }
        }
        return latest;
    }


}
