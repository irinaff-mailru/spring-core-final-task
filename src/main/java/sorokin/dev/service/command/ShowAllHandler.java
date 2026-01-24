package sorokin.dev.service.command;

import org.springframework.stereotype.Component;
import sorokin.dev.dto.User;
import sorokin.dev.service.UserService;

import java.util.List;
import java.util.Scanner;

import static sorokin.dev.service.command.CommandType.SHOW_ALL_USERS;

@Component
public class ShowAllHandler implements CommandHandler {

    private final UserService userService;

    public ShowAllHandler(UserService userService) {
        this.userService = userService;
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
