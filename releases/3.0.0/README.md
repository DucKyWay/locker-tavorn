# Release Update
| Release Version | Download                                                                                   | Youtube Link                        |
|-----------------|--------------------------------------------------------------------------------------------|-------------------------------------|
| v3.0.0          | [github.com](https://github.com/CS211-681-Project/project681-rod-f-211/releases/tag/3.0.0) | [youtube.com](https://youtu.be/Fqrffv638pY) |

# Release Notes
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

### สิ่งที่พัฒนาโดยสมาชิกแต่ละคน (Version 3.0.0)

## 1. **นายมนัส เตชะพัตราภรณ์ (6710405451)**
- **Models**
    - Account
        - เพิ่ม LocalDateTime
    - ZoneList - ไว้เก็บ Zone
    - KeyLocker, KeyList - จัดการกุญแจ
    - Request, RequestList - จัดการคำขอจาก User
- **Controllers**
    - Officer
        - เพิ่มปุ่มกดเพิ่ม Locker ใน Zone ของตนเอง
        - เพิ่มหน้า Request Table ของ Officer
        - เพิ่มหน้า Request Table ของ User
        - เพิ่มหน้าเลือก Zone ที่จะบริการ (Officer)
        - สร้าง Locker Table หลังจากเลือก Zone (User)
        - เพิ่ม Locker Dialog ของ Officer (ดูข้อมูล, เปลี่ยนสถานะได้)
    - User
        - เพิ่มการสร้าง Request เพื่อส่งคำขอ (User)
- **Services**
    - JsonListFileDatasource - ใช้เป็นตัวกลางในการเขียน/อ่านไฟล์ JSON
    - ZoneListService - อัปเดต id Zone เมื่อ Zone ถูกลบ
    - RequestService - อัปเดตสถานะ Request ตามเวลาปัจจุบัน
- **รายละเอียดเพิ่มเติม**
    - Comparator สำหรับ Account/Request เพื่อ Sort จากการเข้าถึงล่าสุด
    - Dialog สำหรับตั้ง Password ของ Locker (Digital)

---

## 2. **นางสาวสุภาวิณี ฉัตรอัศวปรีดา (6710405541)**
- **Models**
    - Account
        - เพิ่มนามสกุล
        - เพิ่ม `getFullName`
    - Officer
        - เก็บ ZoneUids (แทน zoneId, zone)
        - เพิ่ม/ลบ ZoneUid ที่ดูแลได้
        - Constructor รองรับ ZoneUids
        - รองรับการลบ Account
    - OfficerList - ทำให้รองรับ Officer แบบใหม่
    - User - สามารถ `toggleSuspend` ได้
    - UserList - รองรับการลบ User
    - Zone
        - ใช้ Unique Identity
        - เพิ่มการ `toggleStatusZone`
    - ZoneList - ใช้ Unique Identity
    - ZoneStatus - เพิ่ม Enum Status
    - LockerList
        - สามารถ getZoneStatus ได้จาก Enum
        - สามารถหา Locker ตามสถานะได้
- **Controllers**
    - Admin
        - AdminDisplayOfficerZonesController - แสดง Zone ของ Officer
        - AdminManageNewOfficerController - เพิ่ม Officer ใหม่
        - AdminManageOfficerDetailsController - แก้ไข Officer + Zone
        - AdminManageOfficersController
            - ลบ Officer ได้
            - กดเข้ารายละเอียด Officer ได้
            - เพิ่ม Column Profile
        - AdminManageUsersController
            - ลบ User ได้
            - Toggle สถานะ User ได้
            - เพิ่ม Column Profile
        - AdminManageZonesController
            - เปลี่ยนสถานะ Zone ได้
            - แก้ไขชื่อ Zone ได้
            - ลบ Zone ได้
    - Officer
        - OfficerFirstLoginController - ตรวจสอบ Default Password ให้เปลี่ยนรหัสผ่านทันที
    - Components
        - AddNewZonePopup - เพิ่ม Zone
        - EditZoneNamePopup - แก้ไข Zone
        - AdminNavbarController - Template Navbar ของ Admin
        - UserNavbarController - Template Navbar ของ User
        - HeaderController - Template Header
- **Services**
    - AlertUtil - รองรับ Headless (Test)
    - AccountServices - เพิ่ม `changePasswordFirstOfficer`
    - SessionManager
        - getOfficer/getUser คืนเป็น Model
        - Authenticate รองรับ Suspended User, ตรวจรหัสผ่าน, บันทึก Login Time
    - ZoneServices - เพิ่มการ getZoneUids
- **Views**
    - เพิ่ม CSS ให้ TableView
    - Template ใหม่: admin-navbar, user-navbar, header, footer, admin-template, user-template
    - View ใหม่:
        - admin-display-officer-zones-view
        - admin-manage-new-officer-view
        - admin-manage-officer-details-view
        - admin-manage-officers-view
        - admin-manage-users-view
        - admin-manage-zones-view
        - officer-first-login-view
    - View ปรับปรุง: user-zone, zone-list-view

---

## 3. **นายอธิรุจ แก้วสีสุก (6710405559)**
- **Models / Enums**
    - Theme - Enum (LIGHT, DARK)
    - Icons - เพิ่ม Icons ใหม่
- **Classes**
    - ThemeProvider - Singleton สำหรับสลับ Theme
    - IconButton
    - FXRouter
        - เพิ่มฟังก์ชัน Dialog Pane (`loadDialogStage`, `loadNewDialogRoute`, `getCurrentRouteLabel`)
    - SceneLoader - รองรับ ThemeProvider
    - StyleMasker - รองรับ Properties เพิ่มเติม
- **Controllers**
    - LockerDialogController - Dialog จัดการ Locker
    - LockerReserveDialogController - Dialog จอง Locker
    - UserLoginController - รองรับ UI ใหม่
    - UserHomeController - รองรับ UI ใหม่ + ใช้ LockerDialogController
    - UserZoneController - Setup Controller
    - UserHistoryController - Setup Controller
- **FXML**
    - locker-dialog-view
    - locker-reserve-dialog-view
    - footer
    - toggle-theme-button
    - ปรับปรุง: user-home, user-zone, user-history, user-login, officer-login
- **CSS**
    - light-theme.css
    - dark-theme.css
    - button-style.css - ปรับ IconButton
    - global.css - ปรับปรุง

---
