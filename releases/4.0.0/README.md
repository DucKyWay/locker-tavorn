# Release Update
| Release Version | Download                                                                                   | Youtube Link |
|-----------------|--------------------------------------------------------------------------------------------|--------------|
| v3.0.0          | [github.com](https://github.com/CS211-681-Project/project681-rod-f-211/releases/tag/4.0.0) | -            |

# Release Notes
## [4.0.0] - 2025-10-16
### Added

### Changed

---

### สิ่งที่พัฒนาโดยสมาชิกแต่ละคน (Version 4.0.0)

## 1. **นายมนัส เตชะพัตราภรณ์ (6710405451)**

---

## 2. **นางสาวสุภาวิณี ฉัตรอัศวปรีดา (6710405541)**
- **Models**
    - **Account / Officer / User / Zone / Locker**
        - ปรับโครงสร้างให้รองรับ `Provider` และ `DatasourceProvider` แบบใหม่
        - เปลี่ยนระบบรหัสผ่านเป็น **BCrypt**
        - แก้ไขโครงสร้าง Model ให้ชื่อชัดเจน สื่อความหมายมากขึ้น (Refactor Variable Name)
        - **Locker**
            - เพิ่มการสร้าง UID ใหม่อัตโนมัติเมื่อซ้ำ
            - รองรับการเก็บ `price`
            - เพิ่ม Constructor ใหม่
        - **Zone**
            - เพิ่ม `status` และ `toggleStatus`
            - ใช้ Unique Identity (zoneUid)
        - **Request**
            - รองรับการเพิ่มหลายรายการพร้อมกัน (`List<Request>`)
        - **Key / ZoneStatus / Enum**
            - เพิ่ม Enum สำหรับสถานะ Zone และ Locker เพื่อความปลอดภัยในการใช้งาน
- **Controllers**
    - **Admin**
        - `AdminManageOfficersController`
            - ลบ / แก้ไข / ดูรายละเอียด Officer ได้
            - เพิ่มคอลัมน์โปรไฟล์
        - `AdminManageUsersController`
            - Toggle สถานะผู้ใช้ได้ (Active / Inactive)
            - เพิ่มคอลัมน์โปรไฟล์
        - `AdminManageZonesController`
            - แก้ไขชื่อ / สถานะ / ลบ Zone ได้
        - `AdminDisplayOfficerZonesController`
            - แสดง Zone ที่เจ้าหน้าที่รับผิดชอบ
        - `AdminNavbarController`
            - แปลงเป็น Template แยกออกจาก View หลัก
        - `AdminHomeController`
            - ใช้ Comparator `TimestampComparator` แสดงข้อมูลเรียงตามเวลาล่าสุด
    - **Officer**
        - `OfficerFirstLoginController`
            - ตรวจสอบรหัสผ่านเริ่มต้นและบังคับให้เปลี่ยนรหัสทันที
        - `OfficerManageLockersController`
            - ตรวจสอบ Locker ทั้งหมดใน Zone ของตน
            - สามารถดูประวัติการใช้งาน Locker ได้
            - สามารถ Export PDF แสดงรหัส Locker แต่ละ Zone ได้
        - รองรับการสแกน QR Code เพื่อเปิด Locker
    - **User**
        - `UserMyLockerController`
            - แสดง Locker ของผู้ใช้
            - รองรับ QR Code ในการเปิด / จอง Locker
        - `UserHomeController`
            - สามารถค้นหาด้วย QR Code หรือ Text ได้
            - ปรับปรุงการแสดง TableView ให้ Async
    - **Components / Utilities**
        - `TableColumnFactory`
            - รวมโค้ดการสร้าง Column ทุก TableView ให้อยู่ในที่เดียว
        - `AlertUtil`
            - ปรับให้เรียกใช้ง่ายขึ้น (Instance-based)
        - `DialogPane`
            - ปรับโครงสร้างจาก Popup เดิมเป็น Dialog Pane เหมือนโค้ดอื่น ๆ
- **Services**
    - `SessionManager`
        - เปลี่ยนจาก Singleton เป็น Normal Instance
        - รองรับการ Login / Authentication ของทุก Role (Admin, Officer, User)
        - ตรวจสอบรหัสผ่าน, Suspended Account, และบันทึกเวลาเข้าสู่ระบบ
    - `AccountService`
        - เพิ่ม `changePassword` และการตรวจสอบ Password ปัจจุบัน
    - `ZoneDatasourceProvider`, `RequestDatasourceProvider`
        - รองรับการเชื่อมโยงข้อมูล JSON ใหม่
    - `RequestService`, `ZoneService`
        - เพิ่มเมธอดค้นหา / สถานะ / การโหลดข้อมูลล่าสุด
    - `SearchService`
        - รองรับการค้นหาทั่วระบบ (User, Officer, Locker)
- **Features ใหม่**
    - ระบบ **QR Code Integration**
        - ผู้ใช้สามารถสแกน QR Code เพื่อจองหรือเปิด Locker ได้
        - เจ้าหน้าที่สามารถสร้าง QR Code ที่มีข้อมูล LockerUid + Password
        - Officer สามารถ Export PDF ที่มี QR Code ของ Locker ได้
    - ระบบ **PDF Export**
        - Export รหัส Locker แต่ละ Zone ออกเป็น PDF
    - ระบบ **Timestamp Sorting**
        - แสดงรายการล่าสุดในหน้า Admin Home
    - **Auto Zone Status**
- **Views**
   - ปรับเวอร์ชัน FXML ทั้งหมดจาก **JavaFX 23 → JavaFX 21** เพื่อความเข้ากันได้
   - เพิ่ม DialogView ใหม่ (Locker Dialog, Officer Detail Dialog, Manual Dialog)
   - ปรับ UI ทุกหน้าให้เป็นภาษาไทย (Login, Home, User, Officer, Admin)
   - เพิ่ม Template ใหม่:
       - `admin-template`, `user-template`, `admin-navbar`, `user-navbar`, `header`, `footer`
   - View ใหม่/ปรับปรุง:
       - `admin-manage-officers-view`
       - `officer-first-login-view`
       - `user-my-locker-view`
       - `locker-dialog-view`
       - `officer-manage-lockers-view`
       - `admin-display-officer-zones-view`
       - ปรับขนาด TableView, Font, Alignment ให้เหมาะกับภาษาไทย
- **Refactor**
    - เปลี่ยนระบบ Datasource → Provider ทุกรูปแบบ
    - ย้ายไฟล์ Dialog ออกจาก Controller ไปเก็บรวมใน `/views/dialog`
    - ลบโค้ดซ้ำใน Controller ด้วยการใช้ BaseController และ BaseUserController
    - Clean Code ตามหลัก Single Responsibility และแยก Service ออกจาก UI
    - ปรับโครงสร้างโปรเจกต์ให้สอดคล้องกับโมดูลใหม่ (Modularization)
- **Fix**
    - แก้ปัญหาเปิด Dialog แล้ว `NullPointerException`
    - แก้ TableView ไม่แสดงข้อมูล
    - แก้ QR Code ไม่สร้างในหน้า Officer
    - แก้ Back Button และ Navigation บางหน้าที่กดแล้วไม่ทำงาน
    - แก้ Error “ไม่สามารถเปลี่ยน Zone ได้” สำหรับ Officer
    - แก้ปัญหา Officer First Login Loop
    - แก้ไขข้อความภาษาไทย / คำผิดใน UI
    - แก้ไข Bug Merge Conflict ระหว่าง `feature/clean-code`, `feature/qr-code`, และ `develop`
- **Documentation**
    - เพิ่ม JavaDocs ใน `utils` และ `model`
    - เพิ่ม Mock Docs และคู่มือ PDF ประกอบโปรเจกต์
---

## 3. **นายอธิรุจ แก้วสีสุก (6710405559)**

---
