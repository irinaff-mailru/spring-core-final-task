package sorokin.dev;

import org.springframework.stereotype.Component;
import sorokin.dev.service.command.CommandType;
import sorokin.dev.service.command.CommandDispatcher;

import java.sql.SQLOutput;
import java.util.Scanner;

@Component
public class OperationConsoleListener {

    private final CommandDispatcher commandDispatcher;
    private final Scanner scanner;

    public OperationConsoleListener(CommandDispatcher commandDispatcher, Scanner scanner) {
        this.commandDispatcher = commandDispatcher;
        this.scanner = scanner;
    }

    public void start() {
        System.out.println("Start listener");
    }

    public void listenUpdates() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                System.out.println("\nPlease type next operation:");
                commandDispatcher.printCommands();
                System.out.print("> ");
                CommandType commandType = CommandType.getValue(scanner.nextLine());
                processCommand(commandType);
            } catch (IllegalArgumentException e) {
                System.out.println("No such command found");
            }
        }
    }

    private void processCommand(CommandType commandType) {
        try {
            commandDispatcher.dispatch(commandType, scanner);
        } catch (Exception e) {
            System.out.printf("Error executing command %s, error =%s%n", commandType, e.getMessage());
        }
    }

    public void close() {
        System.out.println("Closing listener");
    }
}
