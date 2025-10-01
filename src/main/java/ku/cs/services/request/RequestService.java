package ku.cs.services.request;

import ku.cs.models.key.KeyList;
import ku.cs.models.key.Key;
import ku.cs.models.locker.Locker;
import ku.cs.models.locker.LockerList;
import ku.cs.models.locker.LockerType;
import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.models.request.RequestType;
import ku.cs.models.zone.Zone;
import ku.cs.models.zone.ZoneList;
import ku.cs.services.datasources.KeyListFileDatasource;
import ku.cs.services.datasources.LockerListFileDatasource;
import ku.cs.services.datasources.RequestListFileDatasource;
import ku.cs.services.datasources.ZoneListFileDatasource;
import ku.cs.services.session.SelectedDayService;
import ku.cs.services.utils.GenerateNumberUtil;

public class RequestService {

    private ZoneListFileDatasource zoneListDatasource;
    private ZoneList zoneList;

    private LockerListFileDatasource lockerListDatasource;
    private LockerList lockerList;

    private KeyListFileDatasource keyListFileDatasource;
    private KeyList keyList;

    private final SelectedDayService selectedDayService = new SelectedDayService();

    private RequestListFileDatasource requestListFileDatasource;
    private RequestList requestList;

    public void updateData() {
        zoneListDatasource = new ZoneListFileDatasource("data", "test-zone-data.json");
        zoneList = zoneListDatasource.readData();

        for (Zone zone : zoneList.getZones()) {
            requestListFileDatasource = new RequestListFileDatasource("data/requests",
                    "zone-" + zone.getZoneUid() + ".json");
            requestList = requestListFileDatasource.readData();
            updateRequestList(requestList, zone);
        }
    }

    public void updateRequestList(RequestList requestList, Zone zone) {
        boolean updated = false;
        for (Request request : requestList.getRequestList()) {
            System.out.println("AAAAAA:"+request.getRequestUid());
            boolean booked = selectedDayService.isBooked(request.getStartDate(), request.getEndDate());
            boolean hasImage = request.getImagePath() != null && !request.getImagePath().isEmpty();
            // ถ้าเป็น APPROVE เท่านั้น
            if (request.getRequestType().equals(RequestType.APPROVE)) {
                if (booked) {
                    if (!hasImage) {
                        request.setRequestType(RequestType.SUCCESS);
                        updated = true;
                    }
                } else {
                    request.setRequestType(RequestType.LATE);
                    updated = true;
                }
            }

            if (!booked) {
                releaseLockerAndKey(request, zone);
            }
        }
        if (updated) {
            requestListFileDatasource.writeData(requestList);
        }
    }



    private void releaseLockerAndKey(Request request, Zone zone) {
        lockerListDatasource = new LockerListFileDatasource("data/lockers",
                "zone-" + zone.getZoneUid() + ".json");
        lockerList = lockerListDatasource.readData();

        Locker locker = lockerList.findLockerByUuid(request.getLockerUid());
        if (locker == null) {
            System.err.println("⚠ Locker not found for request uuid=" + request.getLockerUid()
                    + " in zone=" + zone.getZoneUid());
            return;
        }

        if (locker.getLockerType().equals(LockerType.MANUAL)) {
            keyListFileDatasource = new KeyListFileDatasource("data/keys",
                    "zone-" + zone.getZoneUid() + ".json");
            keyList = keyListFileDatasource.readData();

            Key key = keyList.findKeyByUuid(locker.getUid());
            if (key != null) {
                key.setAvailable(true);
                keyListFileDatasource.writeData(keyList);
            } else {
                System.err.println("⚠ Key not found for locker uuid=" + locker.getUid()
                        + " in zone=" + zone.getZoneUid());
            }
        } else {
            locker.setPassword(GenerateNumberUtil.generateNumberShort());
        }

        // ปรับ locker กลับมา available
        locker.setAvailable(true);
        lockerListDatasource.writeData(lockerList);
    }

}
