package sorokin.dev.service.command;

import org.springframework.stereotype.Component;
import sorokin.dev.config.AccountConfig;
import sorokin.dev.dto.CommandType;
import sorokin.dev.dto.User;
import sorokin.dev.service.AccountService;
import sorokin.dev.service.UserService;

import java.util.List;
import java.util.Scanner;

import static sorokin.dev.dto.CommandType.SHOW_ALL_USERS;

@Component
public class ShowAllHandler implements CommandHandler {

    private final UserService userService;
    private final AccountService accountService;

    public ShowAllHandler(UserService userService, AccountService accountService, AccountConfig accountConfig) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @Override
    public CommandType getType() {
        return SHOW_ALL_USERS;
    }

    @Override
    public void handle(Scanner scanner) {
        System.out.println("List of all users:");
        List<User> users = userService.getUsers();
        users.forEach(System.out::println);

    }
}
