package sorokin.dev.service.command;

import org.springframework.stereotype.Component;
import sorokin.dev.config.AccountConfig;
import sorokin.dev.dto.Account;
import sorokin.dev.dto.CommandType;
import sorokin.dev.dto.User;
import sorokin.dev.service.AccountService;
import sorokin.dev.service.UserService;

import java.math.BigDecimal;
import java.util.Scanner;

import static sorokin.dev.dto.CommandType.ACCOUNT_CREATE;

@Component
public class AccountCreateHandler implements CommandHandler {

    private final UserService userService;
    private final AccountService accountService;

    public AccountCreateHandler(UserService userService, AccountService accountService, AccountConfig accountConfig) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @Override
    public void handle(Scanner scanner) {
        System.out.print("Enter the user id for which to create an account:");
        System.out.print("> ");
        String value = scanner.nextLine();
        if (!isUserIdValid(value)) {
            System.out.print("userId not valid, return to enter one of operation...");
            return;
        }
        Long userId = getLongValue(value);
        var optionalUser = userService.findUserById(userId);
        if (userId < 0 || optionalUser.isEmpty()) {
            System.out.print("userId not exist, return to enter one of operation...");
            return;
        }
        Account newAccount = accountService.create(userId, BigDecimal.ZERO);
        User user = optionalUser.get();
        user.addAccount(newAccount);
        System.out.println("New account created with ID:" + newAccount.getId() + " for user: " + user.getLogin());
    }

    @Override
    public CommandType getType() {
        return ACCOUNT_CREATE;
    }

    private boolean isUserIdValid(String value) {
        return (value != null)
                && !value.isBlank()
                && value.length() < 19
                && value.matches("\\d+");
    }

    private Long getLongValue(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            System.out.print("userId not valid, return to enter one of operation...");
            return -1L;
        }
    }
}
