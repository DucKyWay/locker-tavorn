package ku.cs.models.key;

import jakarta.json.bind.annotation.JsonbProperty;
import ku.cs.models.locker.KeyType;
import ku.cs.services.utils.UuidUtil;

public class KeyLocker {
    private final String uuid; //uuid of key
    private KeyType keyType; //type of uuid
    private boolean available;
    private String uuidLocker;
    @JsonbProperty("passkey")
    private String passkey;
    private String zone; //collect zone

    public KeyLocker(){
        this.uuid = UuidUtil.generateShort();
    }
    public KeyLocker(String uuid, KeyType keyType, boolean available, String uuidLocker, String password, String zone) {
        this.uuid = uuid;
        this.keyType = keyType;
        this.available = available;
        this.uuidLocker = uuidLocker;
        this.passkey = password;
        this.zone = zone;
    }
    public KeyLocker(KeyType keyType, String zone){
        this.uuid = UuidUtil.generateShort();
        this.keyType = keyType;
        this.zone = zone;
        this.passkey = UuidUtil.generateShort();
        this.available = true;
        this.uuidLocker = UuidUtil.generateShort();
    }
    public void setToLockerId(String uuidLocker){
        this.uuidLocker = uuidLocker;
        this.available = false;
    }
    public void setOutOfLockerId(){
        this.uuidLocker = "";
        this.available = true;
    }
    public String getUuid() {
        return uuid;
    }

    public KeyType getKeyType() {
        return keyType;
    }

    public void setKeyType(KeyType keyType) {
        this.keyType = keyType;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getUuidLocker() {
        return uuidLocker;
    }

    public void setUuidLocker(String uuidLocker) {
        this.uuidLocker = uuidLocker;
    }
    @JsonbProperty("passkey")
    public String getPasskey() {
        return passkey;
    }
    @JsonbProperty("passkey")
    public void setPasskey(String passkey) {
        this.passkey = passkey;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
}
