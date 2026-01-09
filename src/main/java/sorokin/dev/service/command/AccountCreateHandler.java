package sorokin.dev.service.command;

import org.springframework.stereotype.Component;
import sorokin.dev.config.AccountProperties;
import sorokin.dev.dto.Account;
import sorokin.dev.dto.User;
import sorokin.dev.service.AccountService;
import sorokin.dev.service.UserService;

import java.util.Scanner;

import static sorokin.dev.service.command.CommandType.ACCOUNT_CREATE;
import static sorokin.dev.service.HelperUtils.getLongValue;
import static sorokin.dev.service.HelperUtils.isUserIdValid;

@Component
public class AccountCreateHandler implements CommandHandler {

    private final UserService userService;
    private final AccountService accountService;

    public AccountCreateHandler(UserService userService, AccountService accountService, AccountProperties accountProperties) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @Override
    public CommandType getType() {
        return ACCOUNT_CREATE;
    }

    @Override
    public void handle(Scanner scanner) {
        System.out.println("Enter the user id for which to create an account:");
        System.out.println("> ");
        String value = scanner.nextLine();
        if (!isUserIdValid(value)) {
            System.out.print("userId not valid, return to enter one of operation...");
            return;
        }
        Long userId = getLongValue(value);
        var optionalUser = userService.findUserById(userId);
        if (userId < 0 || optionalUser.isEmpty()) {
            System.out.println("userId not exist, return to enter one of operation...");
            return;
        }
        Account newAccount = accountService.create(userId, false);
        User user = optionalUser.get();
        user.addAccount(newAccount);
        System.out.println("New account created with ID: %s  for user: %s".formatted(newAccount.getId(), user.getLogin()));
    }
}
