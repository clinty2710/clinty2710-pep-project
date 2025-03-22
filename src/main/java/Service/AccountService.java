package Service;

import DAO.AccountDAO;
import Model.Account;

/**
 * registration and login
 */
public class AccountService {

    private AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    // Validates registration, create new account
    public Account register(Account account) {
        if (account.getUsername() == null || account.getUsername().isBlank()) {
            return null;
        }

        if (account.getPassword() == null || account.getPassword().length() < 4) {
            return null;
        }

        if (accountDAO.usernameExists(account.getUsername())) {
            return null;
        }

        return accountDAO.createAccount(account);
    }

    // Validate login
    public Account login(String username, String password) {
        return accountDAO.getAccountByCredentials(username, password);
    }
}
