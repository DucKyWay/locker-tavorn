package ku.cs.models.request;

import jakarta.json.bind.annotation.JsonbVisibility;
import ku.cs.models.account.Officer;
import ku.cs.models.account.User;
import ku.cs.services.utils.UuidUtil;
import org.eclipse.yasson.FieldAccessStrategy;

import java.time.LocalDate;
@JsonbVisibility(FieldAccessStrategy.class)
public class Request {
    private final String uuid;
    private RequestType requestType;
    private String uuidLocker;
    private LocalDate startDate;
    private LocalDate endDate;
    private String officerName;
    private String userName;
    private String zone;
    private String imagePath;
    private String messenger="";

    public Request(){
        this.uuid = UuidUtil.generateShort();
    }

    public Request(String uuid, RequestType requestType, String uuidLocker, LocalDate startDate, LocalDate endDate, String officerName, String userName, String zone, String imagePath,String messenger) {
        this.uuid = uuid;
        this.requestType = requestType;
        this.uuidLocker = uuidLocker;
        this.startDate = startDate;
        this.endDate = endDate;
        this.officerName = officerName;
        this.userName = userName;
        this.zone = zone;
        this.imagePath = imagePath;
        this.messenger = messenger;
    }
    public Request(String uuidLocker,LocalDate startDate, LocalDate endDate,String officerName, String userName, String zone, String imagePath) {
        this.uuid =  UuidUtil.generateShort();
        this.uuidLocker = uuidLocker;
        this.officerName = officerName;
        this.userName = userName;
        this.zone = zone;
        this.imagePath = imagePath;
        this.startDate = startDate;
        this.endDate = endDate;
        this.messenger ="";
        this.requestType = RequestType.PENDING;
    }


    public String getUuid() {
        return uuid;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public String getUuidLocker() {
        return uuidLocker;
    }

    public void setUuidLocker(String uuidLocker) {
        this.uuidLocker = uuidLocker;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getOfficerName() {
        return officerName;
    }

    public void setOfficerName(String officerName) {
        this.officerName = officerName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getMessenger() {
        return messenger;
    }

    public void setMessenger(String messenger) {
        this.messenger = messenger;
    }
}
