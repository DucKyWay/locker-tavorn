package ku.cs.services.accounts.strategy;

public class AccountProviderFactory {
    public static AccountProvider<?, ?> create(AccountProviderType type) {
        return switch (type) {
            case OFFICER -> new OfficerAccountProvider();
            case USER -> new UserAccountProvider();
            case ALL -> {
                CompositeAccountProvider composite = new CompositeAccountProvider();
                composite.loadAccounts();
                yield composite;
            }
        };
    }
}
