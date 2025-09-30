package ku.cs.services.strategy.account;

public class AccountProviderFactory {
    public static AccountProvider create(AccountProviderType type) {
        return switch (type) {
            case OFFICER -> new OfficerAccountProvider();
            case USER -> new UserAccountProvider();
            case ALL -> new CompositeAccountProvider();
        };
    }
}
