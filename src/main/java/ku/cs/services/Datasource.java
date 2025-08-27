package ku.cs.services;

import java.io.FileNotFoundException;
import java.io.IOException;

//T: Generic Type
public interface Datasource <T>{
    T readData() throws FileNotFoundException;
    void writeData(T data) throws IOException;
}
