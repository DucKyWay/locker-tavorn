package ku.cs.services.strategy.account;

import ku.cs.models.account.Account;

import java.util.List;

public interface AccountProvider {
    List<Account> loadAccounts();
    void save(List<Account> accounts);
}
