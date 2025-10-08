package ku.cs.models.request;

import jakarta.json.bind.annotation.JsonbPropertyOrder;
import ku.cs.models.locker.LockerType;
import ku.cs.services.utils.UuidUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;

//@JsonbVisibility(FieldAccessStrategy.class)
@JsonbPropertyOrder({"requestUid", "requestType", "lockerUid", "lockerKeyUid","zoneUid", "startDate", "endDate", "officerUsername", "userUsername", "imagePath", "message", "requestTime"})
public class Request {
    private String requestUid;
    private RequestType requestType;
    private String lockerUid;
    private String lockerKeyUid = "";
    private String zoneUid;
    private LocalDate startDate;
    private LocalDate endDate;
    private String officerUsername;
    private String userUsername;
    private String imagePath;
    private String message = "";
    private LocalDateTime requestTime;
    public Request(){

    }

    public Request(String requestUid, RequestType requestType, String lockerUid, LocalDate startDate, LocalDate endDate, String officerUsername, String userUsername, String zoneUid, String imagePath, String message, LocalDateTime requestTime, String lockerKeyUid) {
        this.requestUid = requestUid;
        this.requestType = requestType;
        this.lockerUid = lockerUid;
        this.startDate = startDate;
        this.endDate = endDate;
        this.officerUsername = officerUsername;
        this.userUsername = userUsername;
        this.zoneUid = zoneUid;
        this.imagePath = imagePath;
        this.message = message;
        this.requestTime = requestTime;
        this.lockerKeyUid = lockerKeyUid;
    }
    public Request(String lockerUid, LocalDate startDate, LocalDate endDate, String userUsername, String zoneUid, String imagePath, LocalDateTime requestTime) {
        this(new UuidUtil().generateShort(), RequestType.PENDING, lockerUid, startDate, endDate, "", userUsername, zoneUid, imagePath, "",requestTime,"");
    }

    public String getLockerKeyUid() {
        return lockerKeyUid;
    }

    public void setLockerKeyUid(String lockerKeyUid) {
        this.lockerKeyUid = lockerKeyUid;
    }

    public void setRequestUid(String requestUid) {
        this.requestUid = requestUid;
    }
    public String getRequestUid() {
        return requestUid;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public String getLockerUid() {
        return lockerUid;
    }

    public void setLockerUid(String lockerUid) {
        this.lockerUid = lockerUid;
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

    public String getOfficerUsername() {
        return officerUsername;
    }

    public void setOfficerUsername(String officerUsername) {
        this.officerUsername = officerUsername;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public void setZoneUid(String zoneUid) {
        this.zoneUid = zoneUid;
    }

    public String getZoneUid() {
        return zoneUid;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(LocalDateTime requestTime) {
        this.requestTime = requestTime;
    }

}
