package sorokin.dev.service.command;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Component
public class CommandDispatcher {

    private final Map<CommandType, CommandHandler> handlers;

    public CommandDispatcher(List<CommandHandler> hanlerdList) {
        this.handlers = hanlerdList.stream()
                .collect(toMap(CommandHandler::getType,
                        Function.identity()));
    }

    public void dispatch(CommandType type, Scanner scanner) {
        CommandHandler handler = handlers.get(type);
        handler.handle(scanner);
    }

    public void printCommands() {
        handlers.keySet().forEach(System.out::println);
    }
}
