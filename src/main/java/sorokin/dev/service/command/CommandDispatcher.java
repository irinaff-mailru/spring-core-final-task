package sorokin.dev.service.command;

import org.springframework.stereotype.Component;
import sorokin.dev.dto.CommandType;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;
import static sorokin.dev.dto.CommandType.NO_USE;

@Component
public class CommandDispatcher {

    private final Map<CommandType, CommandHandler> handlers;
    private final CommandHandler defaultHandler;

    public CommandDispatcher(List<CommandHandler> hanlerdList) {
        this.handlers = hanlerdList.stream()
                .collect(toMap(CommandHandler::getType,
                        Function.identity()));
        this.defaultHandler = hanlerdList.stream()
                .filter(h -> NO_USE == h.getType())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Default handler not found"));
    }

    public void dispatch(CommandType type, Scanner scanner) {
        CommandHandler handler = handlers.getOrDefault(type, defaultHandler);
        handler.handle(scanner);
    }
}
