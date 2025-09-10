# Release Update
| Release Version | Download                                                                                         | Youtube Link                        |
|-----------------|--------------------------------------------------------------------------------------------------|-------------------------------------|
| v2.0.0          | [github.com](https://github.com/CS211-681-Project/project681-rod-f-211/releases/tag/2.0.0) | [youtube.com](https://youtu.be/rBWc2zr-5f8) |

# Release Notes
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
### สิ่งที่พัฒนาโดยสมาชิกแต่ละคน (Version 2.0.0)

1. **นายมนัส เตชะพัตราภรณ์ (6710405451)**
    - Model: `UserList`, `UserReservedStatus`, `UserReservedStatusList`, `Zone`, `ZoneList`
    - Fix: `Officer.java`
    - Controller: `OfficerLoginController` (Read, Write Function)
    - Service: `UserListFileDatasource`, `ZonelistFileDatasource`, `OfficerListFileDatasource`
    - รายละเอียด:
        - สร้าง `UserList` และ `UserReservedStatus`
        - สร้าง `Zone` และ `ZoneList`
        - พัฒนา ListFileDatasource ของแต่ละคลาสสำหรับอ่าน, เขียนไฟล์ (เตรียมทำเป็น interface ในอนาคต)
        - เชื่อมโยงกับไฟล์ `officer-home.fxml`

2. **นางสาวสุภาวิณี ฉัตรอัศวปรีดา (6710405541)**
    - Model: `Account`, `Officer`, `Role`, `Locker`, `KeyType`, `LockerList`
    - Controller: `AdminHomeController`, `AdminLoginController`, `UserHomeController`, `UserLoginController`, `UserRegisterController`, `LockerTableController`, `SettingDropdownController`, `ChangePasswordPopup`, `UploadProfilePopup`
    - Service: `AdminFileDatasource`, `PasswordUtil`, `LockerListHardcodeDatasource`, `AlertUtil`, `ImageUploadUtil`, `UuidUtil`, `AccountService`, `SessionManager`
    - รายละเอียด:
        - สร้างหน้าหลักของ Admin, Officer, User
        - ระบบล็อกอินของ Admin และ User
        - ระบบสร้างบัญชี Users
        - ระบบสร้าง Default Profile Picture
        - ระบบ Dropdown Setting (เปลี่ยนโปรไฟล์, เปลี่ยนรหัสผ่าน, ออกจากระบบ)
        - สร้าง Model `Locker`, `LockerList`
        - ทำ Datasource: `AdminFileDatasource`
        - ระบบ one-way hash password, เปลี่ยนรหัสผ่าน
        - ระบบจัดการ Session
        - Template ของ Alert
        - ระบบ Upload รูป
        - ระบบ Generate UUID แบบสั้น
        - นำโครง `User` ของมนัสมา เปลี่ยนเป็น Super Class Account
        - FXML: `user-login.fxml`, `user-register.fxml`, `admin-view.fxml`, `admin-login-view.fxml`

3. **นายอธิรุจ แก้วสีสุก (6710405559)**
    - Enum: `LabelStyle`
    - Interface: `ReplaceableInParent` (interface สำหรับ class ที่สามารถแทนที่ได้)
    - Class:
        - `SceneLoader` (สำหรับเตรียม Scene ก่อนแสดงผล, โหลด global.css)
        - ปุ่ม Custom: `CustomButton`, `CustomButtonWithIcon`, `FilledButton`, `FilledButtonWithIcon`, `ElevatedButton`, `ElevatedButtonWithIcon`, `OutlinedButton`, `OutlinedButtonWithIcon`
    - CSS: `global.css`, `button-style.css`, `label-style.css`
    - รายละเอียด:
        - สร้าง Scene และระบบโหลด Style
        - เพิ่มชุดปุ่มพร้อม Icon และ Style ตาม Design System
        - FXML: ปุ่มและ Label Style ถูกนำไปใช้ใน `user-login.fxml`, `user-register.fxml`, `admin-login-view.fxml`  