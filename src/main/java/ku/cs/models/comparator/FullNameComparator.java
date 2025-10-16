package ku.cs.models.comparator;

import ku.cs.models.account.Account;

import java.util.Comparator;

public class FullNameComparator implements Comparator<Account> {
    @Override
    public int compare(Account o1, Account o2) {
        return o1.getFullName().compareTo(o2.getFullName());
    }
}
