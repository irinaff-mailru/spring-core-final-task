package sorokin.dev.service.command;

import org.springframework.stereotype.Component;
import sorokin.dev.dto.Account;
import sorokin.dev.dto.User;
import sorokin.dev.service.AccountService;
import sorokin.dev.service.UserService;

import java.util.Scanner;

import static sorokin.dev.service.command.CommandType.USER_CREATE;
import static sorokin.dev.service.HelperUtils.isLoginValid;

@Component
public class UserCreateHandler implements CommandHandler {

    private final UserService userService;
    private final AccountService accountService;

    public UserCreateHandler(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @Override
    public void handle(Scanner scanner) {
        System.out.print("> Enter user login (3-20 symbols):");
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
        Account newAccount = accountService.create(newUser.getId(), true);
        newUser.addAccount(newAccount);
        System.out.printf("User created: %s%n", newUser);
    }

    @Override
    public CommandType getType() {
        return USER_CREATE;
    }
}
