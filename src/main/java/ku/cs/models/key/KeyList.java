package ku.cs.models.key;

import ku.cs.models.locker.KeyType;

import java.util.ArrayList;

public class KeyList {
    private ArrayList<KeyLocker> keyLockers;
    public KeyList(){
        keyLockers = new ArrayList<>();}
    public void addKey(KeyLocker keyLocker){
        keyLockers.add(keyLocker);
    }
    public void addkey(KeyType keyType,String zone){
        keyLockers.add(new KeyLocker(keyType,zone));
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
    public void resetKeyList(){
        keyLockers.clear();
    }
    public ArrayList<KeyLocker> getKeys(){return keyLockers;}
}
