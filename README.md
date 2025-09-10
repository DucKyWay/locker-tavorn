# Project CS211
> A JavaFX-based Locker Management System developed for CS211 Project, Semester 1/2025.

---

# Release Update
| Release Version | Download                                                                                                 | Youtube Link                        | 
|-----------------|----------------------------------------------------------------------------------------------------------|-------------------------------------|
| v2.0.0          | [github.com](https://github.com/CS211-681-Project/project681-rod-f-211/releases/tag/2.0.0) | [youtube.com](https://youtu.be/rBWc2zr-5f8) |
| v1.0.1          | [github.com](https://github.com/CS211-681-Project/project681-rod-f-211/tree/main/releases/1.0.1) | [youtube.com](https://youtu.be/svdDql7tqvo) |
| v1.0.0          | [github.com](https://github.com/CS211-681-Project/project681-rod-f-211/tree/main/releases/1.0.0) | [youtube.com](https://youtu.be/svdDql7tqvo) |

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