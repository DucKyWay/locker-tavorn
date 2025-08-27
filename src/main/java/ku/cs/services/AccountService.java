package ku.cs.services;

import ku.cs.models.*;

import java.util.Objects;

public class AccountService {
    public enum  Role {ADMIN, OFFICER, USER};
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
    public void changePassword(Role role,String username, String currentPassword, String newPassword) {
        Objects.requireNonNull(role, "role");
        Objects.requireNonNull(username, "username");
        Objects.requireNonNull(currentPassword, "currentPassword");
        Objects.requireNonNull(newPassword, "newPassword");

        switch (role) {
            case ADMIN: {
                if (admin == null || !username.equals(admin.getUsername()))
                    throw new IllegalArgumentException("Admin not found: " + username);

                if (!PasswordUtil.matches(currentPassword, admin.getPassword()))
                    throw new IllegalArgumentException("Current password is incorrect");

                admin.setPassword(PasswordUtil.hashPassword(newPassword));
                System.out.println("Password changed for ADMIN username=" + username);
                break;
            }
            case OFFICER: {
                if (officers == null) throw new IllegalStateException("Officer list not available");
                Officer o = officers.findOfficerByUsername(username);
                if (o == null) throw new IllegalArgumentException("Officer not found: " + username);

                if (!PasswordUtil.matches(currentPassword, o.getPassword()))
                    throw new IllegalArgumentException("Current password is incorrect");

                o.setPassword(PasswordUtil.hashPassword(newPassword));
                System.out.println("Password changed for OFFICER username=" + username);
                break;
            }
            case USER: {
                if (users == null) throw new IllegalStateException("User list not available");
                User u = users.findUserByUsername(username);
                if (u == null) throw new IllegalArgumentException("User not found: " + username);

                if (!PasswordUtil.matches(currentPassword, u.getPassword()))
                    throw new IllegalArgumentException("Current password is incorrect");

                u.setPassword(PasswordUtil.hashPassword(newPassword));
                System.out.println("Password changed for USER username=" + username);
                break;
            }
            default:
                throw new IllegalArgumentException("Unsupported realm");
        }
    }
}
