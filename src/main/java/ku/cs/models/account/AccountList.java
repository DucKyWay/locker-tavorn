package ku.cs.models.account;

import java.util.ArrayList;
import java.util.List;

public abstract class AccountList<T extends Account> {
    protected List<T> accounts;

    public AccountList() {
        accounts = new ArrayList<>();
    }

    public void addAccount(T account) {
        if (account != null) {
            accounts.add(account);
        }
    }

    public void removeAccount(T account) {
        accounts.remove(account);
    }

    public boolean removeAccountByUsername(String username) {
        return accounts.removeIf(acc -> acc.getUsername().equals(username));
    }

    public boolean updateImagePath(String username, String newPath) {
        for (T account : accounts) {
            if (account.getUsername().equals(username)) {
                account.setImagePath(newPath);
                return true;
            }
        }
        return false;
    }

    public boolean canFindByUsername(String username) {
        return accounts.stream().anyMatch(acc -> acc.getUsername().equals(username));
    }

    public T findByUsername(String username) {
        return accounts.stream()
                .filter(acc -> acc.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    public List<T> getAccounts() {
        return accounts;
    }

    public int getCount() {
        return accounts.size();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{accounts=" + accounts + "}";
    }
}
