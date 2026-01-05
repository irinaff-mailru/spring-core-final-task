package sorokin.dev.service;

import org.springframework.stereotype.Component;
import sorokin.dev.config.MessageConfig;
import sorokin.dev.dto.CommandType;

import java.util.Scanner;

import static sorokin.dev.dto.CommandType.EXIT;
import static sorokin.dev.dto.CommandType.NO_USE;

@Component
public class OperationConsoleListener {

    public OperationConsoleListener(MessageConfig messageConfig) {
        this.messageConfig = messageConfig;
    }

    private final MessageConfig messageConfig;

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(messageConfig.getFirstMessage());

        while (true) {
            System.out.print("> ");
            CommandType command = CommandType.getValue(scanner.nextLine());

            if (EXIT == command) {
                System.out.println("Выход...");
                break;
            }

            if (NO_USE == command) {
                System.out.println("Введите команду из списка");
                continue;
            }

            processCommand(command);
        }
    }

    private void processCommand(CommandType command) {
        System.out.println("Выполнена команда: " + command);
    }
}
