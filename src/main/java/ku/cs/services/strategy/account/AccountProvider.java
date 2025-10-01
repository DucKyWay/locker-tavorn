package ku.cs.services.strategy.account;

import ku.cs.models.account.Account;

import java.util.List;

public interface AccountProvider<T extends Account, C> {
    List<T> loadAccounts();
    C loadCollection();

    void saveCollection(C accounts);
}
