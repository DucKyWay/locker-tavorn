package ku.cs.models.key;

import ku.cs.models.locker.KeyType;

import java.util.ArrayList;

public class KeyList {
    private ArrayList<Key> keys;
    public KeyList(){keys = new ArrayList<>();}
    public void addKey(Key key){
        keys.add(key);
    }
    public void addkey(KeyType keyType,String zone){
        keys.add(new Key(keyType,zone));
    }
    public void removeKey(Key key){
        keys.remove(key);
    }
    public Key findKeybyUuid(String uuid){
        for(Key key:keys){
            if(key.getUuid().equals(uuid)){
                return key;
            }
        }
        return null;
    }
    public ArrayList<Key> getKeys(){return keys;}
}
