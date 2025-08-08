# Release Update
| Release Version | Download                                                                                         | Youtube Link                        |
|-----------------|--------------------------------------------------------------------------------------------------|-------------------------------------|
| v1.0.1          | [github.com](https://github.com/CS211-681-Project/project681-rod-f-211/tree/main/releases/1.0.1) | [youtube.com](https://youtu.be/svdDql7tqvo) |

# Release Notes
## [1.0.1] - 2025-08-08
### Changed
- Updated `README.md` (documentation)
- Added missing section from v1.0.0: “สิ่งที่พัฒนาโดยสมาชิกแต่ละคน”
- No source code changes; binary is identical to v1.0.0

---
## [1.0.0] - 2025-08-05
*(Original release – information carried over to v1.0.1 for completeness)*
- Home screen (`home-view.fxml`, `HomeController.java`)
- Developer screen (`developer.fxml`, `DeveloperController.java`)
- Custom components: `DefaultLabel`, `DefaultButton`
- Main Application: `HomeApplication.java`

### สิ่งที่พัฒนาโดยสมาชิกแต่ละคน (Version 1.0.0)
1. นายมนัส เตชะพัตราภรณ์ (6710405451)
- สร้างหน้า developer.fxml ทำให้เชื่อมโยงกับหน้าหลัก
  - จัด interface หน้า developer.fxml ให้เหมือนกับที่ออกแบบไว้ใน Figma
- เพิ่มหน้ารูปยิ้มของตนเองลงใน developer.fxml
- ออกแบบระบบฐานข้อมูล (locker, officer)
2. นางสาวสุภาวิณี ฉัตรอัศวปรีดา (6710405541)
- สร้าง Component: DefaultLabel, DefaultButton
- สร้างหน้า home-view.fxml และทำให้เชื่อมโยงกับหน้า developer.fxml
- สร้าง FontLoader เพื่อให้รองรับการใช้ Custom font
- เพิ่มหน้ารูปยิ้มของตนเองลงใน developer.fxml
- ออกแบบระบบฐานข้อมูล (locker, admin)
3. นายอธิรุจ แก้วสีสุก (6710405559)
- เพิ่มหน้ารูปยิ้มของตนเองลงใน developer.fxml
- ออกแบบ Figma หน้า developer.fxml
- ออกแบบระบบฐานข้อมูล (locker, customer)
