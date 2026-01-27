package sorokin.dev.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Пользователь.
 */
//TODO в следующем задании использовать для передаци на UI
public class UserDto {

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
    private List<AccountDto> accountDtos = new ArrayList<>();

    public UserDto(Long id, String login, List<AccountDto> accountDtos) {
        this.id = id;
        this.login = login;
        this.accountDtos = accountDtos;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", accounts=" + accountDtos +
                '}';
    }

    public void addAccount(AccountDto accountDto) {
        accountDtos.add(accountDto);
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public List<AccountDto> getAccounts() {
        return accountDtos;
    }

    public void setAccounts(List<AccountDto> accountDtos) {
        this.accountDtos = accountDtos;
    }
}
