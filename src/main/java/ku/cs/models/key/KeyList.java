package ku.cs.models.key;

import ku.cs.services.utils.UuidUtil;

import java.util.ArrayList;

public class KeyList {
    private ArrayList<Key> keys;
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

    public void removeKey(Key key){
        keys.remove(key);
    }
    public Key findKeyByUuid(String uuid){
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

    public ArrayList<Key> getKeys(){return keys;}
}
