package ku.cs.services;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import ku.cs.models.Officer;
import ku.cs.models.OfficerList;
import ku.cs.models.User;
import ku.cs.models.UserList;

import javax.xml.crypto.Data;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OfficerListFileDatasource implements Datasource<OfficerList> {
    private String directoryName;
    private String fileName;
    public OfficerListFileDatasource(String directoryName, String fileName) {
        this.directoryName = directoryName;
        this.fileName = fileName;
    }
    private void checkFileIsExisted() {
        File file = new File(directoryName);
        if (!file.exists()) {
            file.mkdir();
        }
        String filePath = directoryName + File.separator +fileName;
        file = new File(filePath);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    @Override
    public OfficerList readData() {
        OfficerList officerList = new OfficerList();
        String filePath = directoryName + File.separator +fileName;
        File file =new File(filePath);
        if(!file.exists() || file.length() == 0) {
            return officerList;
        }
        Jsonb jsonb = JsonbBuilder.create();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line); // อ่านทั้งไฟล์มาต่อกัน
            }
            // แปลงเป็น List<OfficerList>
            List<Officer> officers = jsonb.fromJson(sb.toString(), new ArrayList<Officer>(){}.getClass().getGenericSuperclass());
            for(Officer officer : officers){
                officerList.addOfficer(officer);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return officerList;

    }

    @Override
    public void writeData(OfficerList data) {
        String filePath = directoryName + File.separator + fileName;
        Jsonb jsonb = JsonbBuilder.create();
        String result = jsonb.toJson(data.getOfficers());
        result = result.replace("},", "},\n");
        try (PrintWriter out = new PrintWriter(new FileWriter(filePath))) {
            out.print(result);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
