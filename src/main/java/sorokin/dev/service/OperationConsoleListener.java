package sorokin.dev.service;

import org.springframework.stereotype.Component;
import sorokin.dev.config.MessageConfig;
import sorokin.dev.dto.CommandType;
import sorokin.dev.service.command.CommandDispatcher;

import java.util.Scanner;

import static sorokin.dev.dto.CommandType.EXIT;

@Component
public class OperationConsoleListener {

    private final MessageConfig messageConfig;
    private final CommandDispatcher commandDispatcher;

    public OperationConsoleListener(MessageConfig messageConfig, CommandDispatcher commandDispatcher) {
        this.messageConfig = messageConfig;
        this.commandDispatcher = commandDispatcher;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(messageConfig.getFirstMessage());

        while (true) {
            System.out.print("> ");
            CommandType commandType = CommandType.getValue(scanner.nextLine());

            if (EXIT == commandType) {
                System.out.println("Exit...");
                break;
            }

            commandDispatcher.dispatch(commandType, scanner);
        }
    }
}
