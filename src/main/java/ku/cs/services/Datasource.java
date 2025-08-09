package ku.cs.services;
//T: Generic Type
public interface Datasource <T>{
    T readData();
    void writeData(T data);
}
