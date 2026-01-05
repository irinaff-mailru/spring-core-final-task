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
    private Long id;

    /**
     * Логин.
     */
    private String login;

    /**
     * Список счетов.
     */
    private List<Account> accounts = new ArrayList<>();

    public User(Long id, String login) {
        this.id = id;
        this.login = login;
    }

    public void addAccount() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
