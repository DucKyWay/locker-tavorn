package ku.cs.services.datasources;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ZoneListFileDatasource implements Datasource<ZoneList> {
    private String directoryName;
    private String fileName;
    public ZoneListFileDatasource(String directoryName, String fileName){
        this.directoryName = directoryName;
        this.fileName = fileName;
        checkFileIsExisted();
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
    public ZoneList readData() throws FileNotFoundException {
        ZoneList zoneList = new ZoneList();
        String filePath = directoryName + File.separator + fileName;
        File file = new File(filePath);

        if(!file.exists()||file.length()==0) {
            return zoneList;
        }
        Jsonb jsonb = JsonbBuilder.create();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line); // อ่านทั้งไฟล์มาต่อกัน
            }

            // แปลงเป็น List<User>
            List<Zone> zones = jsonb.fromJson(sb.toString(), new ArrayList<Zone>(){}.getClass().getGenericSuperclass());
            for(Zone zone : zones){
                zoneList.addZone(zone);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return zoneList;
    }

    @Override
    public void writeData(ZoneList data) throws IOException {
        String filePath = directoryName + File.separator + fileName;
        Jsonb jsonb = JsonbBuilder.create();
        String result = jsonb.toJson(data.getZones());
        result = result.replace("},", "},\n");
        try (PrintWriter out = new PrintWriter(new FileWriter(filePath))) {
            out.print(result);
        }
    }
}
