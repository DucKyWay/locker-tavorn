package ku.cs.models.account;

import java.util.List;

public record OfficerForm(
        String username,
        String firstname,
        String lastname,
        String password,   // only create new
        String email,
        String phone,
        List<String> zoneUids
) {}
