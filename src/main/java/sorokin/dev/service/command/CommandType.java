package sorokin.dev.service.command;

public enum CommandType {

    /**
     * Создание нового пользователя.
     */
    USER_CREATE,

    /**
     * Отображение списка всех пользователей.
     */
    SHOW_ALL_USERS,

    /**
     * Создание нового счета для пользователя.
     */
    ACCOUNT_CREATE,

    /**
     * Закрытие счета.
     */
    ACCOUNT_CLOSE,

    /**
     * Пополнение счета.
     */
    ACCOUNT_DEPOSIT,

    /**
     * Перевод средств между счетами.
     */
    ACCOUNT_TRANSFER,

    /**
     * Снятие средств со счета.
     */
    ACCOUNT_WITHDRAW,

    /**
     * Выход.
     */
    EXIT;

    public static CommandType getValue(String value) {
        if (value != null) {
            for (CommandType commandType : CommandType.values()) {
                if (commandType.name().equalsIgnoreCase(value)) {
                    return commandType;
                }
            }
        }
        throw new IllegalArgumentException("No such command found!");
    }

}
