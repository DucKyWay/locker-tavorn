package ku.cs.services.datasources;
import ku.cs.models.account.OfficerList;
import ku.cs.models.zone.ZoneList;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class OfficerListHardCodeDatasource {
    private Datasource<ZoneList> datasourceZone;
    private ZoneList zoneList;


    public  OfficerList readdata() {
        initialDatasourceZone();
        OfficerList officerList = new OfficerList();
        officerList.addOfficer(zoneList.findZoneById(0).getIdZone(),"admin00","Manus","5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5","manbome@gmail.com","0903999647",zoneList.findZoneById(0).getZone(),null, LocalDateTime.now());
        officerList.addOfficer(zoneList.findZoneById(1).getIdZone(),"admin01","Manus","5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5","manbome@gmail.com","0903999647",zoneList.findZoneById(1).getZone(),null, LocalDateTime.now());
        officerList.addOfficer(zoneList.findZoneById(2).getIdZone(),"admin02","Manus","5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5","manbome@gmail.com","0903999647",zoneList.findZoneById(2).getZone(),null, LocalDateTime.now());
        officerList.addOfficer(zoneList.findZoneById(3).getIdZone(),"admin03","Manus","5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5","manbome@gmail.com","0903999647",zoneList.findZoneById(3).getZone(),null, LocalDateTime.now());
        officerList.addOfficer(zoneList.findZoneById(0).getIdZone(),"admin04","Manus","5994471abb01112afcc18159f6cc74b4f511b99806da59b3caf5a9c173cacfc5","manbome@gmail.com","0903999647",zoneList.findZoneById(0).getZone(),null, LocalDateTime.now());
        return officerList;
    }
    public void initialDatasourceZone() {
        datasourceZone = new ZoneListFileDatasource("data", "test-zone-data.json");
        try {
            zoneList = datasourceZone.readData();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
