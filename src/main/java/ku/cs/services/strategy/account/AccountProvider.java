package ku.cs.services.strategy.account;

import ku.cs.models.account.Account;

import java.util.List;

public interface AccountProvider<T extends Account> {
    List<T> loadAccounts();
    void saveAccounts(List<T> accounts);
}
