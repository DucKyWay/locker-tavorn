package ku.cs.models.request;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import jakarta.json.bind.annotation.JsonbVisibility;
import ku.cs.models.account.Officer;
import ku.cs.models.account.User;
import ku.cs.services.utils.UuidUtil;
import org.eclipse.yasson.FieldAccessStrategy;

import java.time.LocalDate;
import java.time.LocalDateTime;

//@JsonbVisibility(FieldAccessStrategy.class)
@JsonbPropertyOrder({"uuid", "requestType", "uuidLocker","startDate", "endDate","officerName","zone","imagePath","messenger","requestTime"})
public class Request {
    private String uuid;
    private RequestType requestType;
    private String uuidLocker;
    private LocalDate startDate;
    private LocalDate endDate;
    private String officerName;
    private String userName;
    private String zone;
    private String imagePath;
    private String messenger="";
    private LocalDateTime requestTime;

    public Request(){

    }

    public Request(String uuid, RequestType requestType, String uuidLocker, LocalDate startDate, LocalDate endDate, String officerName, String userName, String zone, String imagePath,String messenger,LocalDateTime requestTime) {
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
        this.requestTime = requestTime;
    }
    public Request(String uuidLocker, LocalDate startDate, LocalDate endDate, String userName, String zone, String imagePath,LocalDateTime requestTime) {
        this(UuidUtil.generateShort(), null, uuidLocker, startDate, endDate, "", userName, zone, imagePath, "",requestTime);
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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
    public LocalDateTime getRequestTime() {
        return requestTime;
    }
    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

}
