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
import ku.cs.services.datasources.provider.KeyDatasourceProvider;
import ku.cs.services.datasources.provider.LockerDatasourceProvider;
import ku.cs.services.datasources.provider.RequestDatasourceProvider;
import ku.cs.services.datasources.provider.ZoneDatasourceProvider;
import ku.cs.services.session.SelectedDayService;
import ku.cs.services.utils.GenerateNumberUtil;

import java.time.LocalDate;

public class RequestService {
    private final ZoneDatasourceProvider zonesProvider = new ZoneDatasourceProvider();
    private final RequestDatasourceProvider requestsProvider = new RequestDatasourceProvider();
    private final LockerDatasourceProvider lockersProvider = new LockerDatasourceProvider();
    private final KeyDatasourceProvider keysProvider = new KeyDatasourceProvider();
    private final SelectedDayService selectedDaysProvider = new SelectedDayService();
    private ZoneList zoneList;

    private LockerList lockerList;
    private KeyList keyList;
    private Locker locker;

    private final SelectedDayService selectedDayService = new SelectedDayService();

    private RequestList requestList;

    public void updateData() {
        zoneList = zonesProvider.loadCollection();
        for (Zone zone : zoneList.getZones()) {
            requestList = requestsProvider.loadCollection(zone.getZoneUid());
            updateRequestList(requestList, zone);
        }
    }

    public void updateRequestList(RequestList requestList, Zone zone) {
        boolean updated = false;
        int price;
        for (Request request : requestList.getRequestList()) {
            boolean booked = selectedDayService.isBooked(request.getStartDate(), request.getEndDate());
            // ถ้าเป็น APPROVE เท่านั้น
            if (request.getRequestType().equals(RequestType.APPROVE)) {
                if (!booked) {
                    request.setRequestType(RequestType.LATE);
                    updated = true;
                }
            }
            if(request.getRequestType().equals(RequestType.LATE)){
                lockerList = lockersProvider.loadCollection(request.getZoneUid());
                locker = lockerList.findLockerByUid(request.getLockerUid());
                price = (selectedDayService.getDaysBetween(request.getStartDate(), request.getEndDate())+1)*locker.getLockerSizeType().getPrice();
                price += selectedDayService.getDaysBetween(request.getEndDate(), LocalDate.now())*locker.getLockerSizeType().getFine();
                request.setPrice(price);
                updated = true;
            }
        }
        if (updated) {
            requestsProvider.saveCollection(zone.getZoneUid(), requestList);
        }
    }

    //check request have overlap date locker of request
    public RequestList checkIsBooked(Request request,RequestList requestlist){
        for(Request r: requestlist.getRequestList()){
            if(r.getRequestType().equals(RequestType.PENDING)
                    && r.getLockerUid().equals(request.getLockerUid())
                    && !(r.getRequestUid().equals(request.getRequestUid()))
                    && selectedDayService.isBooked(request.getStartDate(), request.getEndDate() , r.getStartDate(),r.getEndDate())) {
                r.setRequestType(RequestType.REJECT);
                r.setMessage("มีคนจองตู้ก่อนแล้ว กรุณาเลือกตู้อื่น");
            }
        }
        return requestlist;
    }

}
