package sorokin.dev.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Пользователь.
 */
public class User {

    /**
     * Идентификатор.
     */
    private final Long id;

    /**
     * Логин.
     */
    private String login;

    /**
     * Список счетов.
     */
    private List<Account> accounts = new ArrayList<>();

    public User(Long id, String login, List<Account> accounts) {
        this.id = id;
        this.login = login;
        this.accounts = accounts;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", accounts=" + accounts +
                '}';
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
