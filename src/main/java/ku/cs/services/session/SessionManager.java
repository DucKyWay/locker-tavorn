package ku.cs.services.session;

import ku.cs.models.account.*;
import ku.cs.services.accounts.strategy.AdminAccountProvider;
import ku.cs.services.ui.FXRouter;
import ku.cs.services.accounts.strategy.OfficerAccountProvider;
import ku.cs.services.accounts.strategy.UserAccountProvider;
import ku.cs.services.utils.AlertUtil;
import ku.cs.services.utils.PasswordUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;

public class SessionManager {
    private final PasswordUtil passwordUtil = new PasswordUtil();
    private final OfficerAccountProvider officersProvider = new OfficerAccountProvider();
    private final UserAccountProvider usersProvider = new UserAccountProvider();

    private final OfficerList officers;
    private final UserList users;

    private Account currentAccount;

    public SessionManager() {
        officers = officersProvider.loadCollection();
        users = usersProvider.loadCollection();
    }

    public void authenticate(Account account, String rawPassword) {
        if (account == null) {
            throw new IllegalStateException("ไม่พบผู้ใช้งานในระบบ");
        } else if (account.getUsername() == null) {
            throw new IllegalStateException("ไม่พบผู้ใช้งาน");
        } else if (!account.isStatus()) {
            throw new IllegalStateException(account.getUsername() + " เป็นบัญชีที่ถูกระงับการใช้งาน\nกรุณาติดต่อผู้ดูแลระบบ");
        } else {
            String storedHash = account.getPassword();

            if (!passwordUtil.matches(rawPassword, storedHash)) {
                throw new IllegalArgumentException("รหัสผ่านไม่ถูกต้อง");
            } else {
                login(account);
            }
        }
    }

    public void login(Account account) {
        this.currentAccount = account;
        try {
            new AlertUtil().info("ยินดีต้อนรับ", "เข้าสู่ระบบสำเร็จ!");

            switch(account.getRole()) {
                case Role.ADMIN:
                    account.setLoginTime(LocalDateTime.now());
                    new AdminAccountProvider().saveAccount(account);

                    FXRouter.goTo("admin-home");
                    break;

                case Role.OFFICER:
                    Officer officer = officers.findByUsername(account.getUsername());
                    officer.setLoginTime(LocalDateTime.now());
                    officersProvider.saveCollection(officers);

                    if(officer.isFirstTime()) {
                        FXRouter.goTo("officer-first-login");
                        return;
                    }
                    FXRouter.goTo("officer-select-zone");
                    break;

                case Role.USER:
                    User user = users.findByUsername(account.getUsername());
                    user.setLoginTime(LocalDateTime.now());
                    usersProvider.saveCollection(users);

                    FXRouter.goTo("user-home");
                    break;
            }
            System.out.println("[LOGIN SUCCESS] " + account.getUsername() + " (" + account.getRole() + ") at " + LocalDateTime.now());
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
        return officers.findByUsername(currentAccount.getUsername());
    }

    public User getUser() {
        if(!hasRole(Role.USER)) { return null; }
        return users.findByUsername(currentAccount.getUsername());
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