package ku.cs.models.key;

import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbVisibility;
import ku.cs.services.utils.GenerateNumberUtil;
import ku.cs.services.utils.UuidUtil;
import org.eclipse.yasson.FieldAccessStrategy;

@JsonbVisibility(FieldAccessStrategy.class)
public class Key {
    private String keyUid; //uuid of key
    private KeyType keyType; //type of uuid
    private boolean Available;
    @JsonbProperty("lockerUid")
    private String lockerUid;
    @JsonbProperty("passkey")
    private String passkey;
    private String zoneUid;
    private String zoneName; //collect zoneName

    public Key(){
        this.keyUid = new UuidUtil().generateShort();
    }
    public Key(String keyUid, KeyType keyType, boolean Available, String lockerUid, String password, String zoneName) {
        this.keyUid = keyUid;
        this.keyType = keyType;
        this.Available = Available;
        this.lockerUid = lockerUid;
        this.passkey = password;
        this.zoneName = zoneName;
    }
    public Key(KeyType keyType, String zoneName){
        this.keyUid = new UuidUtil().generateShort();
        this.keyType = keyType;
        this.zoneName = zoneName;
        this.passkey = GenerateNumberUtil.generateNumberShort();
        this.Available = true;
    }
    public void setToLockerId(String lockerUid){
        this.lockerUid = lockerUid;
        this.Available = false;
    }
    public void setOutOfLockerId(){
        this.lockerUid = "";
        this.Available = true;
    }

    public String getKeyUid() {
        return keyUid;
    }

    public KeyType getKeyType() {
        return keyType;
    }

    public void setKeyType(KeyType keyType) {
        this.keyType = keyType;
    }

    public boolean isAvailable() {
        return Available;
    }

    public void setAvailable(boolean Available) {
        this.Available = Available;
    }

    public String getLockerUid() {
        return lockerUid;
    }

    public void setLockerUid(String lockerUid) {
        this.lockerUid = lockerUid;
    }

    public String getPasskey() {
        return passkey;
    }

    public void setPasskey(String passkey) {
        this.passkey = passkey;
    }

    public void setKeyUid(String keyUid) {
        this.keyUid = keyUid;
    }

    public String getZoneName() {
        return zoneName;
    }

    public String getZoneUid() {
        return zoneUid;
    }

    public String setZoneUid(String zoneUid) {
        return this.zoneUid;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }
}