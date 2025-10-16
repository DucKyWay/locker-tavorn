# Release Update
| Release Version | Download                                                                                   | Youtube Link |
|-----------------|--------------------------------------------------------------------------------------------|--------------|
| v3.0.0          | [github.com](https://github.com/CS211-681-Project/project681-rod-f-211/releases/tag/4.0.0) | -            |

# Release Notes
## [4.0.0] - 2025-10-16
### Added
+ เพิ่มระบบการจัดการ **Zone, Locker, Key และ Request** ครบวงจร
+ เพิ่มระบบ **Manual (คู่มือการใช้งาน)** สำหรับทุกบทบาท (User / Officer / Admin)
+ เพิ่มระบบ **Theme (Light/Dark)**, **IconButton**, และ **Footer**
+ เพิ่ม Template Components สำหรับ **Admin, User, Officer**
+ เพิ่มระบบตรวจสอบ **Default Password** ของ Officer
+ เพิ่มการแสดง **Profile** และการ **ลบ / เปลี่ยนสถานะ User, Officer** ในตาราง Admin
+ เพิ่ม Popup สำหรับ **เพิ่ม / แก้ไข Zone** (Add/Edit Zone Dialog)
+ เพิ่มระบบ **Dialog สำหรับ Locker** ทั้งฝั่ง Officer / User
+ เพิ่มระบบ **Request Table / Service / Comparator** สำหรับการจัดการคำขอ
+ เพิ่มระบบ **Filter & Sort Locker** ตามสถานะ, ประเภท, และขนาด
+ เพิ่มระบบ **PDF Export** และ **QR Code Integration** สำหรับ Locker
+ เพิ่ม **ReadPDFService** และ **PDFCreatorService** สำหรับอ่านและสร้างไฟล์ PDF ภายในระบบ
+ เพิ่มระบบจัดการ **KeyLocker / KeyList** สำหรับการจัดเก็บกุญแจ Locker
+ เพิ่มระบบแสดง **สถานะ Locker (Pending / Reject / Success / Late)**
+ เพิ่ม Template CSS, Theme Provider และ Style ElevatedButton

### Changed
* ปรับ Model **Zone, Officer, User, Locker, Request** ให้รองรับการลบ, Suspend, และ Unique Identity
* ปรับโครงสร้าง Model รองรับ **Provider-Based Architecture** และแยก **Datasource → Provider**
* ปรับ Account รองรับ **นามสกุล (Full Name)** และบันทึกเวลา **LocalDateTime**
* ปรับ `FXRouter`, `SceneLoader`, และ `StyleMasker` ให้รองรับ ThemeProvider และ Dialog Pane
* ปรับ `SessionManager` ให้รองรับการตรวจสอบ Suspended User, การบันทึก Login Time และรหัสผ่านแบบ BCrypt
* ปรับปรุง `AccountService`, `RequestService`, `ZoneService`, และ `LockerService` รองรับการเชื่อมโยงข้อมูล JSON ใหม่
* ปรับ TableView เพิ่ม **Column Profile**, **Status**, และระบบ Comparator สำหรับเรียงเวลา
* ปรับ UI ของทุกหน้าให้เป็น Template เดียวกัน (Header / Navbar / Footer)
* ปรับ View ของ User/Admin/Officer ให้รองรับภาษาไทยและสไตล์เดียวกัน
* ปรับ CSS (global, button-style, tableview) รองรับ Theme ใหม่ และเพิ่มความชัดของ Font/Scale
* ปรับ Dialog ให้เป็น `DialogPane` แทน Popup เดิม พร้อมรองรับภาษาไทยเต็มรูปแบบ
* ปรับระบบ Request Logic ให้ตรวจสอบ Overlap, สร้างคำร้องอัตโนมัติ และคำนวณค่าปรับ

### Fixed
- แก้บั๊ก **NullPointerException** เมื่อเปิด Dialog บางหน้า
- แก้ TableView ไม่แสดงข้อมูลในบางกรณี
- แก้ QR Code ไม่ถูกสร้างในหน้า Officer
- แก้ปัญหา **Officer First Login Loop**
- แก้ Error “ไม่สามารถเปลี่ยน Zone ได้”
- แก้ปุ่ม Back และ Navigation บางหน้าที่ไม่ทำงาน
- แก้ปัญหา Dialog Officer เมื่อ Locker ถูกลบในคำร้องค้าง
- แก้บั๊ก `Locker.setAvailable()`, `findLatestRequestByLockerUid()`
- แก้ Merge Conflict ระหว่าง `feature/clean-code`, `feature/qr-code`, `feature/filter-locker` และ `develop`
- แก้ไขการ Import Comparator และ Refactor โครงสร้าง Package ให้เป็นระบบ

### Refactor
* แยก Service ออกจาก UI และใช้หลัก **Single Responsibility**
* รวมโค้ดสร้าง Column ใน `TableColumnFactory` ให้เป็นที่เดียว
* ใช้ **BaseController**, **BaseUserController** ลดโค้ดซ้ำ
* ย้าย Dialog ออกจาก Controller ไปไว้ใน `/views/dialog`
* ปรับโครงสร้าง Provider, Comparator, และ Strategy Pattern สำหรับ Sort
* ทำ Clean Code ทั่วทั้งระบบ และปรับการเรียงเวลาผ่าน **LocalTime**
* รวม Branch หลักทั้งหมด (`feature/add-locker-uid-not-repeat`, `feature/filter-locker`, `feature/add-locker-size-field`, ฯลฯ) และทดสอบระบบหลัง Merge เสร็จสมบูรณ์


---

### สิ่งที่พัฒนาโดยสมาชิกแต่ละคน (Version 4.0.0)

## 1. **นายมนัส เตชะพัตราภรณ์ (6710405451)**
- **Manual (คู่มือการใช้งาน)**
    - เพิ่ม `UserManual.fxml`, `OfficerManual.fxml`, `AdminManual.fxml` พร้อม Controller สำหรับเปิดอ่านไฟล์ PDF ภายในระบบ
    - เพิ่มปุ่ม **“อ่านคู่มือ”** ใน Navbar ทุกบทบาท
    - เพิ่ม `ReadPDFService` สำหรับอ่าน PDF และ `PDFCreatorService` สำหรับสร้าง/แปลงไฟล์ PDF

- **Locker UID ไม่ซ้ำ**
    - เพิ่ม `LockerService` เพื่อดูแลการสร้างและตรวจสอบ UID ของ Locker ให้ไม่ซ้ำ
    - ปรับโครงสร้างข้อมูล `Locker` และตรวจสอบก่อนสร้างทุกครั้ง

- **Request & Service Logic**
    - `RequestService`
        - เมื่อเปลี่ยนสถานะหรือลบ Locker จะ **สร้างคำร้อง (Request) อัตโนมัติ**
        - ตรวจสอบสถานะซ้อนทับ (overlap) ของการจอง Locker และ **reject อัตโนมัติ**
        - เพิ่มการคำนวณ **ค่าปรับ (fine)** และแสดงผลบน FXML
        - ซิงก์กับเวลาปัจจุบันและอัปเดตสถานะอัตโนมัติ

- **Officer Dialog & Locker Detail**
    - สร้าง Dialog แสดงข้อมูล Locker สำหรับ Officer (`OfficerLockerDialogController`)
    - เพิ่มรูปภาพ Locker, **ขนาดตู้ (`LockerSizeType`)**, และ **ประเภท (`LockerType`)**
    - จัดการกุญแจใน Locker ได้ (Digital / Manual / Chain)
    - Dialog รองรับสถานะ **Late, Reject, Pending, Success**

- **Request Table (User / Officer)**
    - เพิ่มตาราง Request ทั้งฝั่ง User และ Officer
    - แสดงรายละเอียดคำร้อง, **ราคา**, **รูปภาพ**, และ **สถานะคำขอ**
    - เพิ่มตัวจัดเรียง (Comparator) ตาม **เวลาล่าสุด**
    - เพิ่มปุ่ม **“ดูรายละเอียด”** ในหน้า Officer Request History

- **การจัดการ Key Locker**
    - เพิ่ม `KeyLocker`, `KeyList` และหน้าสำหรับเพิ่ม/จัดการกุญแจ
    - เพิ่มปุ่ม **ลบกุญแจ** ในตาราง Locker

- **Filter & Sort Locker**
    - ผู้ใช้สามารถ **กรอง Locker** ตาม **สถานะ**, **ประเภท**, และ **ขนาด**
    - เพิ่มแพ็กเกจ `comparator` สำหรับตัวเปรียบเทียบเฉพาะทาง
    - เพิ่ม **Strategy Pattern** สำหรับการ **Sort Locker** และ **Sort Request**

- **แสดงสถานะ & ข้อมูล Locker ตามประเภท**
    - **Digital** → แสดง TextField ตั้งรหัสครั้งแรก
    - **Manual / Chain** → แสดงรหัสกุญแจและ **UUID** ของกุญแจ
    - **Reject** → แสดงเหตุผลการปฏิเสธ
    - **Pending** → แสดงสถานะคำขอแบบชัดเจน

- **FXML + Controller**
    - ตรวจสอบและอัปเดต UI ทั้งหมดให้สอดคล้องกับโครงสร้างใหม่
    - เพิ่ม/เชื่อมโยง FXML กับ Controller ที่เกี่ยวข้อง:
        - `UserHomeController`, `OfficerManualController`, `AdminManualController`, `OfficerLockerDialogController`

- **Models / Controllers / Services ที่อัปเดต**
    - **Model:** `Locker`, `LockerService`, `KeyLocker`, `KeyList`, `Request`, `RequestList`, `Officer`, `Zone`, `ZoneList`
    - **Controller:** `OfficerLockerDialogController`, `UserHomeController`, `OfficerManualController`, `AdminManualController`
    - **Service:** `LockerService`, `RequestService`, `ReadPDFService`, `PDFCreatorService`
    - **Comparator:** เพิ่มคลาสจัดการ **เวลา**, **ขนาด Locker**, **สถานะ**, และ **Request order**

- **Fix & Refactor**
    - แก้บั๊ก Dialog ของ Officer เมื่อ Locker ถูกลบในคำร้องค้าง (**pending request**)
    - แก้ปัญหา Request หา Zone ไม่เจอในบางกรณี
    - แก้การ **disable ปุ่มย้ายกุญแจ** ออกจาก Locker
    - แก้บั๊ก `Locker.setAvailable()`
    - แก้บั๊ก `findLatestRequestByLockerUid()`
    - แก้การ import comparator และ **refactor โครงสร้างแพ็กเกจ**
    - ปรับโค้ดให้ตรวจสอบข้อมูลเข้มขึ้น และเรียงเวลาด้วย **LocalTime** อย่างสม่ำเสมอ

- **Integration & Merge**
    - รวมสาขาหลักอย่างต่อเนื่อง:
        - `feature/add-locker-uid-not-repeat`
        - `feature/add-button-read-doc-navbar`
        - `feature/add-data`
        - `feature/add-field-fxml-request`
        - `feature/fix-locker-dialog-officer`
        - `feature/filter-locker`
        - `feature/price-request`
        - `feature/add-locker-size-field`
    - แก้ไข **merge conflicts** หลายจุด และทดสอบการทำงานหลังรวมเสร็จ

- **Mock Data**
    - ปรับ/เพิ่มข้อมูลทดสอบสำหรับ **Admin, User, Officer**

- **Overall Description**
    - ปรับปรุงระบบจัดการ **Locker** และ **Request** ของ Officer และ User ครั้งใหญ่
    - รองรับการใช้งาน Locker แบบ **Digital / Manual / Chain** ครบวงจร
    - เพิ่มระบบ **แสดงภาพ**, **เอกสารคู่มือ**, **ค่าปรับ**, และ **ตัวกรองข้อมูล**
    - พร้อมทั้ง **refactor โค้ด**, ปรับโครงสร้าง **service**, และรวมฟีเจอร์จากหลายสาขาหลักให้ทำงานร่วมกันได้อย่างสมบูรณ์

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
- **FXML + Controller**
    - ตรวจสอบและอัปเดต UI ทั้งหมดให้สอดคล้องกับโครงสร้างใหม่
    - ปรับการเชื่อมโยงระหว่าง FXML และ Controller ให้ถูกต้องตาม MVC

- **User**
    - `user-home`
        - ปรับ UI ให้เรียบง่ายและโหลดข้อมูลแบบ Async
        - เพิ่มฟังก์ชันค้นหาด้วย QR Code / Text
    - `user-my-locker`
        - แสดง Locker ของผู้ใช้ พร้อมสถานะและ QR Code
        - รองรับการเปิด Locker โดยสแกน QR Code
    - `user-history`
        - เพิ่มตารางแสดงประวัติการใช้งาน Locker
        - ปรับรูปแบบวันที่และเวลาให้เข้าใจง่าย
    - `user-zone`
        - ปรับหน้าการเลือก Zone และสถานะการใช้งาน Locker
    - **User Select Locker Page (Extra 27b)**
        - ปรับปรุงการแสดงผลหน้า Locker
        - เพิ่มสี / Icon แสดงสถานะ (ว่าง, ถูกจอง, ใช้งานอยู่)
        - เพิ่มปุ่มเปิด Locker ผ่าน QR Code

- **Officer**
    - `officer-zone-list`
        - แสดงรายการ Zone ที่เจ้าหน้าที่ดูแล
        - เพิ่มฟังก์ชันคลิกเพื่อเข้าสู่รายละเอียด Zone
    - `officer-home`
        - เพิ่ม Dashboard แสดงภาพรวม Locker / Request ของ Zone
        - ปรับการแสดงผลให้สอดคล้องกับ Template ใหม่
    - `officer-manage-locker`
        - จัดการ Locker ภายใน Zone ได้
        - Export QR Code เป็น PDF
    - `officer-manage-key`
        - จัดการกุญแจและรหัสผ่าน Locker
        - เพิ่ม TableColumnFactory สำหรับสถานะกุญแจ
    - `officer-zone-request`
        - แสดงคำขอทั้งหมดของ Zone
        - เพิ่มการกรองคำขอตามสถานะ (รออนุมัติ / สำเร็จ / ยกเลิก)
    - `officer-request-history`
        - แสดงประวัติคำขอทั้งหมด
        - ปรับ Layout ให้เหมาะกับภาษาไทย
    - `officer-first-login`
        - ตรวจสอบ Default Password และบังคับให้เปลี่ยนรหัสผ่าน
        - เพิ่มการตรวจสอบ Validation ใหม่

- **Admin**
    - `admin-home`
        - เพิ่ม Comparator แสดงข้อมูลตาม Timestamp ล่าสุด
        - ปรับ Layout รวมถึง Header / Footer ให้เป็น Template เดียวกัน
    - `admin-manage-zones`
        - เพิ่มฟังก์ชันแก้ไขชื่อ / ลบ / เปลี่ยนสถานะ Zone
    - `admin-manage-users-view`
        - Toggle สถานะผู้ใช้งานได้ (Active / Inactive)
        - เพิ่มคอลัมน์โปรไฟล์
    - `admin-manage-officers-view`
        - ลบ / แก้ไข / ดูรายละเอียด Officer ได้
        - เพิ่มคอลัมน์โปรไฟล์
    - `admin-display-officer-zones`
        - แสดง Zone ทั้งหมดที่เจ้าหน้าที่รับผิดชอบ
        - ใช้ TableColumnFactory แสดงสถานะและชื่อ Zone
    - `admin-add-new-zone-dialog`
        - Dialog สำหรับเพิ่ม Zone ใหม่
        - มีการตรวจสอบชื่อซ้ำก่อนเพิ่ม
    - `admin-edit-zone-name-dialog`
        - แก้ไขชื่อ Zone ได้โดยไม่ต้องรีเฟรชหน้า

- **Locker**
    - `locker-reserve-dialog-view`
        - Dialog สำหรับจอง Locker
        - แสดงรหัส Locker และ QR Code
    - `locker-dialog-view`
        - Dialog สำหรับเปิด Locker
        - เพิ่มระบบ Error Handling เมื่อ Locker ไม่พร้อมใช้งาน
    - `locker-change-password`
        - หน้าเปลี่ยนรหัส Locker
        - ตรวจสอบรหัสเก่าก่อนเปลี่ยน
    - `locker-change-password-chain`
        - หน้าเปลี่ยนรหัส Locker แบบต่อเนื่อง (Chain Mode)
        - ใช้โดยเจ้าหน้าที่สำหรับหลาย Locker

- **Component**
    - ปรับปรุง Template Navbar:
        - `admin-navbar`, `user-navbar`, `officer-navbar`
        - เพิ่ม Icon, Label และ Route ให้เข้ากับหน้าใหม่ทั้งหมด
    - ปรับ `header`
        - เพิ่ม RouteLabel แสดงชื่อหน้าแบบ Dynamic
    - เพิ่ม **ElevatedButton Style**
        - สำหรับปุ่มที่ต้องการเน้น Route หรือ Label พิเศษ

- **Theme**
    - ปรับ Font และ Scale ทั่วทั้งระบบ (Extra)
    - เพิ่มขนาดตัวอักษรและระยะห่างให้เหมาะกับภาษาไทย
    - ใช้โทนสีเดียวกับ Design Theme หลักของโปรเจกต์