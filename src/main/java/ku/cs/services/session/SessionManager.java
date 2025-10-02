package ku.cs.services.session;

import ku.cs.models.account.*;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.accounts.strategy.OfficerAccountProvider;
import ku.cs.services.accounts.strategy.UserAccountProvider;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.PasswordUtil;

import java.io.IOException;
import java.time.LocalDateTime;

public class SessionManager {
    private final PasswordUtil passwordUtil = new PasswordUtil();
    private final OfficerAccountProvider officersProvider = new OfficerAccountProvider();
    private final UserAccountProvider usersProvider = new UserAccountProvider();

    private final AlertUtil alertUtil = new AlertUtil();

    private Account currentAccount;

    public SessionManager() {}

    public void authenticate(Account account, String rawPassword) {
        if (account == null) {
            throw new IllegalArgumentException("ไม่พบชื่อผู้ใช้นี้");
        }

        if (!account.isStatus()) {
            throw new IllegalStateException(account.getUsername() + " เป็นบัญชีที่ถูกระงับการใช้งาน\nกรุณาติดต่อผู้ดูแลระบบ");
        }

        String storedHash = account.getPassword();

        if (!passwordUtil.matches(rawPassword, storedHash)) {
            throw new IllegalArgumentException("รหัสผ่านไม่ถูกต้อง");
        }

        account.setLoginTime(LocalDateTime.now());
    }

    public void login(Account account) {
        this.currentAccount = account;
        String role = account.getRole().toString().toLowerCase();
        try {
            alertUtil.info("ยินดีต้อนรับ", "เข้าสู่ระบบสำเร็จ!");
            if(role.equals("officer")){
                FXRouter.goTo("officer-zone-list");
            }else {
                FXRouter.goTo(role + "-home");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void logout() {
        if (currentAccount == null) {
            try {
                FXRouter.goTo("home");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        String role = getCurrentAccount().getRole().toString();
        currentAccount = null;
        try {
            FXRouter.goTo(role.toLowerCase() + "-login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isAuthenticated() {
        return currentAccount != null;
    }

    public Account getCurrentAccount() {
        return currentAccount;
    }

    public Officer getOfficer() {
        if (!hasRole(Role.OFFICER)) return null;
        OfficerList officerList = officersProvider.loadCollection();
        return officerList.findOfficerByUsername(currentAccount.getUsername());
    }

    public User getUser() {
        if(!hasRole(Role.USER)) { return null; }
        UserList userList = usersProvider.loadCollection();
        return userList.findUserByUsername(currentAccount.getUsername());
    }

    public boolean hasRole(Role role) {
        return currentAccount != null && currentAccount.getRole() == role;
    }

    public void requireRole(Role role, String loginRoute) {
        if (!hasRole(role)) {
            try {
                FXRouter.goTo(loginRoute);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void requireAdminLogin()   { requireRole(Role.ADMIN, "admin-login"); }
    public void requireOfficerLogin() { requireRole(Role.OFFICER, "officer-login"); }
    public void requireUserLogin()    { requireRole(Role.USER, "user-login"); }

    public void requireAdminOrOfficerLogin() {
        if(!(hasRole(Role.ADMIN) || hasRole(Role.OFFICER))) {
            requireRole(Role.OFFICER, "officer-login");
        }
    }

    public void logoutTestHelper() {
        currentAccount = null;
    }
}