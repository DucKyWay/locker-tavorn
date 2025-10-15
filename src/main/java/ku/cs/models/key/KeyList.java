package ku.cs.models.key;

import ku.cs.services.utils.UuidUtil;

import java.util.ArrayList;
import java.util.List;

public class KeyList {
    private List<Key> keys;
    public KeyList(){
        keys = new ArrayList<>();}
    public void addKey(Key key) {
        boolean duplicate;
        do {
            duplicate = false;
            for (Key kl : keys) {
                if (kl.getKeyUid().equals(key.getKeyUid())) {
                    // ถ้าเจอซ้ำ สร้างใหม่แล้วเช็คอีกครั้ง
                    key.setKeyUid(new UuidUtil().generateShort());
                    duplicate = true;
                    break;
                }
            }
        } while (duplicate);

        keys.add(key);
    }

    public void addKey(List<Key> keys_in) {
        for (Key key : keys_in) {
            boolean dup;
            do {
                dup = false;
                for(Key k : keys) {
                    if(k.getKeyUid().equals(key.getKeyUid())) {
                        key.setKeyUid(new UuidUtil().generateShort());
                        dup = true;
                        break;
                    }
                }
            } while (dup);
            keys.add(key);
        }
    }

    public void removeKey(Key key){
        keys.remove(key);
    }
    public void removeKeybyUid(String uid){
        for (Key key : keys) {
            if (key.getKeyUid().equals(uid)) {
                keys.remove(key);
            }
        }
    }

    public Key findKeyByUid(String uuid){
        for(Key key : keys){
            if(key.getKeyUid().equals(uuid)){
                return key;
            }
        }
        return null;
    }

    public void resetKeyList(){
        keys.clear();
    }

    public List<Key> getKeys(){return keys;}
}
