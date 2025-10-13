package ku.cs.models.dialog;

import ku.cs.models.account.Account;
import ku.cs.models.request.Request;

public class DialogData {
    private Request request;
    private Account account;

    public DialogData(Request request, Account account) {
        this.request = request;
        this.account = account;
    }

    public Request getRequest() {
        return request;
    }

    public Account getAccount() {
        return account;
    }
}
