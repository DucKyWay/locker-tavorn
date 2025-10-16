# Project CS211
> A JavaFX-based Locker Management System developed for CS211 Project, Semester 1/2025.

---

# Release Update
| Release Version | Download                                                                                         | Youtube Link                                | 
|-----------------|--------------------------------------------------------------------------------------------------|---------------------------------------------|
| v4.0.1          | [github.com](https://github.com/CS211-681-Project/project681-rod-f-211/releases/tag/4.0.1)       | -                                           |
| v4.0.0          | [github.com](https://github.com/CS211-681-Project/project681-rod-f-211/releases/tag/4.0.0)       | -                                           |
| v3.0.0          | [github.com](https://github.com/CS211-681-Project/project681-rod-f-211/releases/tag/3.0.0)       | [youtube.com](https://youtu.be/Fqrffv638pY) |
| v2.0.0          | [github.com](https://github.com/CS211-681-Project/project681-rod-f-211/releases/tag/2.0.0)       | [youtube.com](https://youtu.be/rBWc2zr-5f8) |
| v1.0.1          | [github.com](https://github.com/CS211-681-Project/project681-rod-f-211/tree/main/releases/1.0.1) | [youtube.com](https://youtu.be/svdDql7tqvo) |
| v1.0.0          | [github.com](https://github.com/CS211-681-Project/project681-rod-f-211/tree/main/releases/1.0.0) | [youtube.com](https://youtu.be/svdDql7tqvo) |

# Release Notes
## [4.0.1] - 2025-10-16
### Fixed
- fix bug
---
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
## [3.0.0] - 2025-09-26
### Added
+ เพิ่มระบบการจัดการ Zone, Locker, Key และ Request ครบวงจร
+ เพิ่มระบบ Theme (Light/Dark), IconButton และ Footer
+ เพิ่ม Template Components สำหรับ Admin, User
+ เพิ่มระบบตรวจสอบ Default Password ของ Officer
+ เพิ่มการแสดง Profile และการลบ User/Officer ในตาราง Admin
+ เพิ่ม Popup สำหรับเพิ่ม/แก้ไข Zone
+ เพิ่มระบบ Dialog สำหรับ Locker (Officer/User)
+ เพิ่มระบบ Request Service/Comparator สำหรับการจัดการคำขอ
+ เพิ่ม Template CSS และ Theme Provider

### Changed
* ปรับ Model Zone, Officer, User ให้รองรับการลบ, Suspend และ Unique Identity
* ปรับ Account รองรับนามสกุล และเพิ่ม LocalDateTime
* ปรับ FXRouter, SceneLoader, StyleMasker รองรับ ThemeProvider และ Dialog Pane
* ปรับ TableView เพิ่ม Column Profile และ Status
* ปรับ SessionManager รองรับ Suspended User, Login Time และตรวจสอบรหัสผ่าน
* ปรับ CSS (global, button*style, tableview) รองรับ Theme ใหม่
* ปรับ View ของ User/Admin ให้เป็น Template เดียวกัน

---
## [2.0.0] - 2025-09-10
### Added
- Login (Admin, Officer, User)
- Can create new User
- Session and Password Hash
- Upload Picture and UUID Generator
- Datasource (Admin, User, Officer, Zone, Locker)
- CustomButton with Style from global.css
- Template Alert และ Dropdown Setting
- Main FXML: `user-login.fxml`, `user-register.fxml`, `officer-home.fxml`, `admin-view.fxml`, `admin-login-view.fxml`

### Changed
- Main Controller: AdminHomeController, OfficerLoginController, UserLoginController
- Connect scene with SceneLoader
- Manage CSS (global.css, button-style.css, label-style.css)

---
## [1.0.1] - 2025-08-08
### Changed
- Updated `README.md` (documentation)
- Added missing section from v1.0.0: “สิ่งที่พัฒนาโดยสมาชิกแต่ละคน”
- No source code changes; binary is identical to v1.0.0

---
## [1.0.0] - 2025-08-05
### Added
- Home screen (`home-view.fxml`, `HomeController.java`)
- Developer screen (`developer.fxml`, `DeveloperController.java`)
- Custom components: `DefaultLabel`, `DefaultButton`
- Main Application: `HomeApplication.java`