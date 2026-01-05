package sorokin.dev.service.command;

import org.springframework.stereotype.Component;
import sorokin.dev.config.AccountConfig;
import sorokin.dev.dto.Account;
import sorokin.dev.dto.CommandType;
import sorokin.dev.dto.User;
import sorokin.dev.service.AccountService;
import sorokin.dev.service.UserService;

import java.util.Scanner;

import static sorokin.dev.dto.CommandType.USER_CREATE;

@Component
public class UserCreateHandler implements CommandHandler {

    private final UserService userService;
    private final AccountService accountService;
    private final AccountConfig accountConfig;

    public UserCreateHandler(UserService userService, AccountService accountService, AccountConfig accountConfig) {
        this.userService = userService;
        this.accountService = accountService;
        this.accountConfig = accountConfig;
    }

    @Override
    public void handle(Scanner scanner) {
        System.out.print("Enter user login (5-20 symbols):");
        System.out.print("> ");
        String login = scanner.nextLine();
        if (!isLoginValid(login)) {
            System.out.print("login not valid, return to enter one of operation...");
            return;
        }
        if (userService.findUserByLogin(login).isPresent()) {
            System.out.print("login already exist, return to enter one of operation...");
            return;
        }
        User newUser = userService.create(login);
        Account newAccount = accountService.create(newUser.getId(), accountConfig.getDefaultAmount());
        newUser.addAccount(newAccount);
        System.out.println("User created:" + newUser);
    }

    @Override
    public CommandType getType() {
        return USER_CREATE;
    }

    private boolean isLoginValid(String value) {
        return (value != null)
                && !value.isBlank()
                && value.length() > 4
                && value.length() < 21;
    }
}
