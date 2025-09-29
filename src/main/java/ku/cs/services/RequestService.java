package ku.cs.services;

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
        for (Request request : requestList.getRequestList()) {

            boolean booked = selectedDayService.isBooked(request.getStartDate(), request.getEndDate());
            boolean hasImage = request.getImagePath() != null && !request.getImagePath().isEmpty();

            if (request.getRequestType().equals(RequestType.APPROVE)) {
                if (booked) {
                    // ช่วงจองอยู่ + ไม่มีรูป
                    if (!hasImage) {
                        request.setRequestType(RequestType.SUCCESS);
                    }
                } else if (!booked && hasImage) {
                    // ไม่อยู่ในช่วงจองแล้ว + มีรูป
                    request.setRequestType(RequestType.LATE);
                }
            }
            if (!booked) {
                releaseLockerAndKey(request, zone);
            }
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
