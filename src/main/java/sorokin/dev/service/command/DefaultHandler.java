package sorokin.dev.service.command;

import org.springframework.stereotype.Component;
import sorokin.dev.config.MessageConfig;
import sorokin.dev.dto.CommandType;

import java.util.Scanner;

@Component
public class DefaultHandler implements CommandHandler {

    private final MessageConfig messageConfig;

    public DefaultHandler(MessageConfig messageConfig) {
        this.messageConfig = messageConfig;
    }

    @Override
    public void handle(Scanner scanner) {
        System.out.println(messageConfig.getFirstMessage());
    }

    @Override
    public CommandType getType() {
        return CommandType.NO_USE;
    }
}
