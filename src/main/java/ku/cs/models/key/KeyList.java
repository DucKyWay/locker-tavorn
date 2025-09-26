package ku.cs.models.key;

import ku.cs.models.locker.KeyType;
import ku.cs.services.utils.UuidUtil;

import java.util.ArrayList;

public class KeyList {
    private ArrayList<KeyLocker> keyLockers;
    public KeyList(){
        keyLockers = new ArrayList<>();}
    public void addKey(KeyLocker keyLocker) {
        boolean duplicate;
        do {
            duplicate = false;
            for (KeyLocker kl : keyLockers) {
                if (kl.getUuid().equals(keyLocker.getUuid())) {
                    // ถ้าเจอซ้ำ สร้างใหม่แล้วเช็คอีกครั้ง
                    keyLocker.setUuid(UuidUtil.generateShort());
                    duplicate = true;
                    break;
                }
            }
        } while (duplicate);

        keyLockers.add(keyLocker);
    }

    public void removeKey(KeyLocker keyLocker){
        keyLockers.remove(keyLocker);
    }
    public KeyLocker findKeybyUuid(String uuid){
        for(KeyLocker keyLocker : keyLockers){
            if(keyLocker.getUuid().equals(uuid)){
                return keyLocker;
            }
        }
        return null;
    }
    public void removeUnavailableKeys() {
        for (int i = keyLockers.size() - 1; i >= 0; i--) {
            if (!keyLockers.get(i).isAvailable()) {
                keyLockers.remove(i);
            }
        }
    }

    public void resetKeyList(){
        keyLockers.clear();
    }
    public ArrayList<KeyLocker> getKeys(){return keyLockers;}
}
