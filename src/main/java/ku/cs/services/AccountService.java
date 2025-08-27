package ku.cs.services;

import ku.cs.models.*;

import java.util.Objects;

public class AccountService {
    private final Admin admin;
    private final OfficerList officers;
    private final UserList users;

    public AccountService(Admin admin) {
        this.admin = Objects.requireNonNull(admin);
        this.officers = null;
        this.users = null;
    }

    public AccountService(OfficerList officers) {
        this.officers = Objects.requireNonNull(officers);
        this.admin = null;
        this.users = null;
    }

    public AccountService(UserList users) {
        this.users = Objects.requireNonNull(users);
        this.admin = null;
        this.officers = null;
    }

    /**
     * change pass
     * @param username
     * @param currentPassword
     * @param newPassword
     */
    public void changePassword(String username, String currentPassword, String newPassword) {
        Objects.requireNonNull(username, "username");
        Objects.requireNonNull(currentPassword, "currentPassword");
        Objects.requireNonNull(newPassword, "newPassword");

        // find target account
        PasswordAccessor target = resolveTarget(username);
        if (target == null) {
            throw new IllegalArgumentException("User not found: " + username);
        }

        // check old pass
        if (!PasswordUtil.matches(currentPassword, target.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        // new pass
        String newHash = PasswordUtil.hashPassword(newPassword);
        target.setPassword(newHash);

        System.out.println("Password changed for username=" + username);
    }

    private interface PasswordAccessor {
        String getPassword();
        void setPassword(String newHash);
    }

    private PasswordAccessor resolveTarget(String username) {
        if (admin != null) {
            if (username.equals(admin.getUsername())) {
                return new PasswordAccessor() {
                    public String getPassword() { return admin.getPassword(); }
                    public void setPassword(String newHash) { admin.setPassword(newHash); }
                };
            }
            return null;
        }

        if (officers != null) {
            Officer o = officers.findOfficerByUsername(username);
            if (o == null) return null;
            return new PasswordAccessor() {
                public String getPassword() { return o.getPassword(); }
                public void setPassword(String newHash) { o.setPassword(newHash); }
            };
        }

        if (users != null) {
            User u = users.findUserByUsername(username);
            if (u == null) return null;
            return new PasswordAccessor() {
                public String getPassword() { return u.getPassword(); }
                public void setPassword(String newHash) { u.setPassword(newHash); }
            };
        }

        return null;
    }
}
