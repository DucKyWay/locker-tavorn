package ku.cs.services.utils;

import ku.cs.models.account.OfficerForm;
import ku.cs.models.account.OfficerList;
import ku.cs.models.account.UserList;
import ku.cs.services.strategy.account.OfficerAccountProvider;
import ku.cs.services.strategy.account.UserAccountProvider;

import java.util.ArrayList;
import java.util.List;

public class OfficerValidator {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PHONE_REGEX = "^\\d{9,10}$";

    private final OfficerAccountProvider officersProvider = new OfficerAccountProvider();
    private final UserAccountProvider usersProvider = new UserAccountProvider();

    private OfficerList officers = officersProvider.loadCollection();
    private UserList users = usersProvider.loadCollection();

    // New Officer
    public List<String> validateNew(OfficerForm form) {
        List<String> errors = new ArrayList<>();

        if (form.username() == null || form.username().isBlank()) {
            errors.add("กรุณากรอกชื่อผู้ใช้");
        } else if (officers.canFindOfficerByUsername(form.username()) || users.canFindUserByUsername(form.username())) {
            errors.add("มีชื่อผู้ใช้นี้แล้ว");
        }
        if (form.firstname() == null || form.firstname().isBlank()) {
            errors.add("กรุณากรอกชื่อพนักงาน");
        }
        if (form.lastname() == null || form.lastname().isBlank()) {
            errors.add("กรุณากรอกนามสกุลพนักงาน");
        }
        if (form.password() == null || form.password().length() < 4) {
            errors.add("รหัสผ่านต้องมีอย่างน้อย 4 ตัวอักษร");
        }
        if (form.email() == null || form.email().isBlank()) {
            errors.add("กรุณากรอกอีเมล");
        } else if (!form.email().matches(EMAIL_REGEX)) {
            errors.add("รูปแบบอีเมลไม่ถูกต้อง");
        }
        if (form.phone() == null || form.phone().isBlank()) {
            errors.add("กรุณากรอกเบอร์มือถือ");
        } else if (!form.phone().matches(PHONE_REGEX)) {
            errors.add("เบอร์มือถือไม่ถูกต้อง (ต้องมี 9-10 หลัก และเป็นตัวเลขเท่านั้น)");
        }

        return errors;
    }

    // Edit Officer
    public List<String> validateEdit(OfficerForm form) {
        List<String> errors = new ArrayList<>();

        if (form.username() == null || form.username().isBlank()) {
            errors.add("กรุณากรอกชื่อผู้ใช้");
        } else if (officers.canFindOfficerByUsername(form.username()) || users.canFindUserByUsername(form.username())) {
            errors.add("มีชื่อผู้ใช้นี้แล้ว");
        }
        if (form.firstname() == null || form.firstname().isBlank()) {
            errors.add("กรุณากรอกชื่อพนักงาน");
        }
        if (form.lastname() == null || form.lastname().isBlank()) {
            errors.add("กรุณากรอกนามสกุลพนักงาน");
        }
        if (form.email() == null || form.email().isBlank()) {
            errors.add("กรุณากรอกอีเมล");
        } else if (!form.email().matches(EMAIL_REGEX)) {
            errors.add("รูปแบบอีเมลไม่ถูกต้อง");
        }
        if (form.phone() == null || form.phone().isBlank()) {
            errors.add("กรุณากรอกเบอร์มือถือ");
        } else if (!form.phone().matches(PHONE_REGEX)) {
            errors.add("เบอร์มือถือไม่ถูกต้อง (ต้องมี 9-10 หลัก และเป็นตัวเลขเท่านั้น)");
        }

        return errors;
    }
}
