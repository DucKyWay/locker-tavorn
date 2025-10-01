//package ku.cs.models.request;
//
//import ku.cs.services.datasources.RequestListFileDatasource;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//
//public class Test {
//    public static void main(String[] args) {
//        // กำหนดชื่อโฟลเดอร์และไฟล์ JSON
//        String directory = "data/requests";   // โฟลเดอร์เก็บไฟล์
//        String fileName = "zone-0.json";      // ชื่อไฟล์ JSON
//
//        // สร้าง datasource สำหรับ RequestList
//        RequestListFileDatasource datasource = new RequestListFileDatasource(directory, fileName);
//
//        // อ่านข้อมูลจากไฟล์ JSON
//        RequestList requestList = datasource.readData();
//
//        // แสดงข้อมูล Request ที่มีอยู่
//        System.out.println("=== ข้อมูล Request ในไฟล์ ===");
//        if (requestList.getRequestList().isEmpty()) {
//            System.out.println("ยังไม่มีข้อมูลในไฟล์");
//        } else {
//            for (Request req : requestList.getRequestList()) {
//                System.out.println("UUID: " + req.getRequestUid() +
//                        " | User: " + req.getUserUsername() +
//                        " | Officer: " + req.getOfficerUsername() +
//                        " | Zone: " + req.getZoneUid() +
//                        " | Locker: " + req.getLockerUid() +
//                        " | Start: " + req.getStartDate() +
//                        " | End: " + req.getEndDate());
//            }
//        }
//
//        // เพิ่ม Request ใหม่
//        System.out.println("\n=== เพิ่ม Request ใหม่ ===");
//
//        // ใช้ constructor ใหม่ที่มี uuid สร้างอัตโนมัติ
//        Request newRequest = new Request(
//                "LOCKER123",            // uuidLocker
//                LocalDate.now(),        // startDate
//                LocalDate.now().plusDays(7), // endDate
//                "User B",               // userName
//                "Zone C",               // zone
//                "images/test.png", LocalDateTime.now()       // imagePath
//        );
//
//        // เพิ่มลงใน RequestList
//        requestList.addRequest(newRequest);
//
//        // บันทึกกลับไปยังไฟล์ JSON
//        datasource.writeData(requestList);
//
//        System.out.println("เพิ่ม Request ใหม่เรียบร้อย!");
//    }
//}
