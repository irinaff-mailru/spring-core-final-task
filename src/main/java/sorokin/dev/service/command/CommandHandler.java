package sorokin.dev.service.command;

import java.util.Scanner;

/**
 * Общий интерфейс обработки комманд
 */
public interface CommandHandler {

    /**
     * Метод выполнения логики обработки комманды
     *
     * @param scanner
     */
    void handle(Scanner scanner);

    /**
     * Получить тип обрабатываемой команды
     *
     * @return
     */
    CommandType getType();
}
