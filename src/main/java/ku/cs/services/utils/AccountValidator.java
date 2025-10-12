package ku.cs.services.utils;

import ku.cs.models.account.*;
import ku.cs.services.accounts.strategy.OfficerAccountProvider;
import ku.cs.services.accounts.strategy.UserAccountProvider;

import java.util.ArrayList;
import java.util.List;

public class AccountValidator {

    /** Regular expression for validating email format. */
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    /** Regular expression for validating phone numbers (9–10 digits). */
    private static final String PHONE_REGEX = "^\\d{9,10}$";

    private final OfficerAccountProvider officersProvider = new OfficerAccountProvider();
    private final UserAccountProvider usersProvider = new UserAccountProvider();

    private final OfficerList officers = officersProvider.loadCollection();
    private final UserList users = usersProvider.loadCollection();

    // =====================================================================
    // Validation for New Officer
    // =====================================================================

    /**
     * Validate all fields for create new {@link Officer}
     * @param form input form containing officer registration data
     * @return list of error massages
     */
    public List<String> validateNewOfficer(OfficerForm form) {
        List<String> errors = new ArrayList<>();

        if (form.username() == null || form.username().isBlank()) {
            errors.add("กรุณากรอกชื่อผู้ใช้");
        } else if (officers.canFindByUsername(form.username()) || users.canFindByUsername(form.username())) {
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

    // =====================================================================
    // Validation for New User
    // =====================================================================

    /**
     * Validate all fields for create new {@link User}
     * @param form input form containing user registration data
     * @return list of error massages
     */
    public List<String> validateNewUser(UserForm form) {
        List<String> errors = new ArrayList<>();

        if(form.username() == null || form.username().isBlank()) {
            errors.add("กรุณากรอกชื่อผู้ใช้");
        } else if(officers.canFindByUsername(form.username()) || users.canFindByUsername(form.username())) {
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

    // =====================================================================
    // Validation for Editing Officer
    // =====================================================================

    /**
     * Validate all fields for edit {@link Officer}
     * @param form input form containing officer edit data
     * @param oldOfficer Officer before edit
     * @return list of error massages
     */
    public List<String> validateEditOfficer(OfficerForm form, Officer oldOfficer) {
        List<String> errors = new ArrayList<>();

        if (form.username() == null || form.username().isBlank()) {
            errors.add("กรุณากรอกชื่อผู้ใช้");
        } else if (!form.username().equals(oldOfficer.getUsername()) &&
                officers.canFindByUsername(form.username()) || users.canFindByUsername(form.username())) {
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

    // =====================================================================
    // Single Field Validations
    // =====================================================================

    /**
     * Validate username.
     *
     * @param username the username to validate
     * @return error message if invalid, others {@code null}
     */
    public String validateUsername(String username) {
        if(username == null || username.isBlank()) {
            return ("กรุณากรอกชื่อผู้ใช้");
        } else if(officers.canFindByUsername(username) || users.canFindByUsername(username)) {
            return ("มีชื่อผู้ใช้นี้แล้ว");
        } else return null;
    }

    /**
     * Validate firstname.
     *
     * @param firstname the firstname to validate
     * @return error message if invalid, others {@code null}
     */
    public String validateFirstname(String firstname) {
        if (firstname == null || firstname.isBlank()) {
            return ("กรุณากรอกชื่อจริง");
        } else return null;
    }

    /**
     * Validate lastname.
     *
     * @param lastname the lastname to validate
     * @return error message if invalid, others {@code null}
     */
    public String validateLastname(String lastname) {
        if (lastname == null || lastname.isBlank()) {
            return ("กรุณากรอกนามสกุล");
        } else return null;
    }

    /**
     * Validate password.
     *
     * @param password the password to validate
     * @return error message if invalid, others {@code null}
     */
    public String validatePassword(String password) {
        if (password == null || password.length() < 4) {
            return ("รหัสผ่านต้องมีอย่างน้อย 4 ตัวอักษร");
        } else return null;
    }

    /**
     * Validate email.
     *
     * @param email the email to validate
     * @return error message if invalid, others {@code null}
     */
    public String validateEmail(String email) {
        if (email == null || email.isBlank()) {
            return ("กรุณากรอกอีเมล");
        } else if (!email.matches(EMAIL_REGEX)) {
            return ("รูปแบบอีเมลไม่ถูกต้อง");
        } else return null;
    }

    /**
     * Validate phone.
     *
     * @param phone the phone to validate
     * @return error message if invalid, others {@code null}
     */
    public String validatePhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return ("กรุณากรอกเบอร์มือถือ");
        } else if (!phone.matches(PHONE_REGEX)) {
            return ("เบอร์มือถือไม่ถูกต้อง (ต้องมี 9-10 หลัก และเป็นตัวเลขเท่านั้น)");
        } else return null;
    }
}
