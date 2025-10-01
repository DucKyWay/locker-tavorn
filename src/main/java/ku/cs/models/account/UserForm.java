package ku.cs.models.account;

public record UserForm(
        String username,
        String firstname,
        String lastname,
        String password,   // only create new
        String email,
        String phone
) {}
