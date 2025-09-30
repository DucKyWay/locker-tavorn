package ku.cs.services.strategy.account;

import ku.cs.models.account.Account;
import ku.cs.models.account.Officer;
import ku.cs.models.account.Role;
import ku.cs.models.account.User;

import java.util.ArrayList;
import java.util.List;

public class CompositeAccountProvider implements AccountProvider<Account> {
    private final List<AccountProvider<? extends Account>> providers = new ArrayList<>();

    public void addProvider(AccountProvider<? extends Account> provider) {
        providers.add(provider);
    }

    @Override
    public List<Account> loadAccounts() {
        List<Account> allAccounts = new ArrayList<>();
        for (AccountProvider<? extends Account> provider : providers) {
            allAccounts.addAll(provider.loadAccounts());
        }
        return allAccounts;
    }

    @Override
    public void saveAccounts(List<Account> accounts) {
        for (AccountProvider<? extends Account> provider : providers) {
            if (provider instanceof OfficerAccountProvider officerProvider) {
                List<Officer> officers = accounts.stream()
                        .filter(a -> a.getRole() == Role.OFFICER)
                        .map(a -> (Officer) a)
                        .toList();
                officerProvider.saveAccounts(officers);

            } else if (provider instanceof UserAccountProvider userProvider) {
                List<User> users = accounts.stream()
                        .filter(a -> a.getRole() == Role.USER)
                        .map(a -> (User) a)
                        .toList();
                userProvider.saveAccounts(users);
            }
        }
    }
}
