package ku.cs.models.request;

import ku.cs.models.request.date.LockerDate;
import ku.cs.models.request.date.LockerDateList;
import ku.cs.models.request.date.DateRange;
import ku.cs.services.datasources.LockerDateListFileDatasource;

import java.time.LocalDate;
import java.util.ArrayList;

public class TestDate {
    public static void main(String[] args) {
        String directory = "data/dates";
        String fileName = "zone-0.json";

        LockerDateListFileDatasource datasource = new LockerDateListFileDatasource(directory, fileName);

        // อ่านข้อมูลที่มีอยู่แล้ว
        LockerDateList dateList = datasource.readData();
        System.out.println("=== ข้อมูลในไฟล์ก่อน ===");
        for (LockerDate d : dateList.getDateList()) {
            System.out.println("uuidLocker: " + d.getUuidLocker());
            System.out.println("DateList: " + d.getDateList());
        }

        // เพิ่มข้อมูลใหม่
        ArrayList<DateRange> ranges = new ArrayList<>();
        ranges.add(new DateRange(LocalDate.now(), LocalDate.now().plusDays(3)));

        LockerDate newDate = new LockerDate("LOCKER-123", ranges);
        newDate.addDateList(new DateRange(LocalDate.now().plusDays(5), LocalDate.now().plusDays(7)));

        dateList.addDateList(newDate);

        // เขียนข้อมูลกลับไฟล์
        datasource.writeData(dateList);

        // อ่านใหม่แล้วแสดงผล
        System.out.println("\n=== ข้อมูลในไฟล์หลังเพิ่ม ===");
        LockerDateList after = datasource.readData();
        for (LockerDate d : after.getDateList()) {
            System.out.println("uuidLocker: " + d.getUuidLocker());
            System.out.println("DateList: " + d.getDateList());
        }
    }
}