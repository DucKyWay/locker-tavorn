package ku.cs.services.strategy.account;

import ku.cs.models.account.Account;

import java.util.ArrayList;
import java.util.List;

public class CompositeAccountProvider implements AccountProvider {
    private final OfficerAccountProvider officerProvider;
    private final UserAccountProvider userProvider;

    public CompositeAccountProvider() {
        this.officerProvider = new OfficerAccountProvider();
        this.userProvider = new UserAccountProvider();
    }

    @Override
    public List<Account> loadAccounts() {
        List<Account> result = new ArrayList<>();
        result.addAll(officerProvider.loadAccounts());
        result.addAll(userProvider.loadAccounts());
        return result;
    }

    @Override
    public void save(List<Account> accounts) {
        officerProvider.save(accounts);
        userProvider.save(accounts);
    }
}
