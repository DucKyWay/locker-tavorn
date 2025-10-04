package ku.cs.services.accounts.strategy;

import ku.cs.models.account.*;
import java.util.ArrayList;
import java.util.List;

public class CompositeAccountProvider implements AccountProvider<Account, Void> {
    private final UserAccountProvider userProvider = new UserAccountProvider();
    private final OfficerAccountProvider officerProvider = new OfficerAccountProvider();
    private final AdminAccountProvider adminProvider = new AdminAccountProvider();

    @Override
    public List<Account> loadAccounts() {
        List<Account> all = new ArrayList<>();
        all.addAll(userProvider.loadAccounts());
        all.addAll(officerProvider.loadAccounts());
        all.addAll(adminProvider.loadAccount());
        return all;
    }

    @Override
    public Void loadCollection() {
        return null;
    }

    @Override
    public void saveCollection(Void ignored) {
        throw new UnsupportedOperationException("ใช้ saveUsers หรือ saveOfficers แทน");
    }
}
