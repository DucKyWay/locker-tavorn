# Project CS211
> A JavaFX-based Locker Management System developed for CS211 Project, Semester 1/2025.

---

# Release Update
| Release Version | Download                                                                                                 | Youtube Link                        | 
|-----------------|----------------------------------------------------------------------------------------------------------|-------------------------------------|
| v3.0.0          | [github.com](https://github.com/CS211-681-Project/project681-rod-f-211/releases/tag/3.0.0) | [youtube.com](https://youtu.be/Fqrffv638pY) |
| v2.0.0          | [github.com](https://github.com/CS211-681-Project/project681-rod-f-211/releases/tag/2.0.0) | [youtube.com](https://youtu.be/rBWc2zr-5f8) |
| v1.0.1          | [github.com](https://github.com/CS211-681-Project/project681-rod-f-211/tree/main/releases/1.0.1) | [youtube.com](https://youtu.be/svdDql7tqvo) |
| v1.0.0          | [github.com](https://github.com/CS211-681-Project/project681-rod-f-211/tree/main/releases/1.0.0) | [youtube.com](https://youtu.be/svdDql7tqvo) |

# Release Notes

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