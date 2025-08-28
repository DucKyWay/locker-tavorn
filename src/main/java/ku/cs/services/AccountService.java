package ku.cs.services;

import ku.cs.models.Account;
import ku.cs.models.Role;

import java.util.Objects;

public class AccountService {
    private final Account account;

    public AccountService(Account account) {
        this.account = Objects.requireNonNull(account, "account");
    }

    /**
     * change password
     *
     * @param role
     * @param username
     * @param currentPassword
     * @param newPassword
     */
    public void changePassword(Role role, String username, String currentPassword, String newPassword) {
        Objects.requireNonNull(role, "role");
        Objects.requireNonNull(username, "username");
        Objects.requireNonNull(currentPassword, "currentPassword");
        Objects.requireNonNull(newPassword, "newPassword");

        if (account.getRole() != role) {
            throw new IllegalArgumentException("Role mismatch for username=" + username);
        }

        if (!username.equals(account.getUsername())) {
            throw new IllegalArgumentException("Account not found: " + username);
        }

        if (!PasswordUtil.matches(currentPassword, account.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        account.setPassword(PasswordUtil.hashPassword(newPassword));
        System.out.println("Password changed for " + role + " username=" + username);
    }

    public void changePassword(String currentPassword, String newPassword) {
        Objects.requireNonNull(currentPassword, "currentPassword");
        Objects.requireNonNull(newPassword, "newPassword");

        if (!PasswordUtil.matches(currentPassword, account.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        account.setPassword(PasswordUtil.hashPassword(newPassword));
        System.out.println("Password changed for username=" + account.getUsername());
    }
}
