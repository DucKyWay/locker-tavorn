package ku.cs;

import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.services.datasources.RequestListFileDatasource;

import java.time.LocalDate;

public class Test {
    public static void main(String[] args) {
        // กำหนดชื่อโฟลเดอร์และไฟล์ JSON ที่เก็บข้อมูล
        String directory = "data/requests"; // โฟลเดอร์เก็บไฟล์
        String fileName = "requests.json"; // ชื่อไฟล์ JSON

        // สร้าง datasource
        RequestListFileDatasource datasource = new RequestListFileDatasource(directory, fileName);

        // อ่านข้อมูลจากไฟล์ JSON
        RequestList requestList = datasource.readData();

        System.out.println("=== ข้อมูล Request ในไฟล์ ===");
        if (requestList.getRequestList().isEmpty()) {
            System.out.println("ยังไม่มีข้อมูลในไฟล์");
        } else {
            for (Request req : requestList.getRequestList()) {
                System.out.println("UUID: " + req.getUuid() +
                        " | User: " + req.getUserName() +
                        " | Officer: " + req.getOfficerName() +
                        " | Zone: " + req.getZone());
            }
        }

        // เพิ่มข้อมูลใหม่
        System.out.println("\n=== เพิ่ม Request ใหม่ ===");

        Request newRequest = new Request(
                "LOCKER123", // uuidLocker
                LocalDate.now(), // startDate
                LocalDate.now().plusDays(7), // endDate
                "Officer A", // officerName
                "User B", // userName
                "Zone C", // zone
                "images/test.png" // imagePath
        );

        // เพิ่มลงใน RequestList
        requestList.addRequest(newRequest);

        // บันทึกกลับไปยังไฟล์ JSON
        datasource.writeData(requestList);

        System.out.println("เพิ่ม Request ใหม่เรียบร้อย!");
    }
}
