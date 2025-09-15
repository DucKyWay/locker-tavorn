package ku.cs.services.datasources;

import java.io.FileNotFoundException;
import java.io.IOException;

//T: Generic Type
public interface Datasource <T>{
    T readData() ;
    void writeData(T data);
}
