package common;

import common.model.Dragon;
import java.io.Serializable;

/**
 * Запрос от клиента к серверу.
 * Содержит команду, аргументы и данные для авторизации.
 */
public class Request implements Serializable {
    private static final long serialVersionUID = 2L;

    private final String commandName;
    private final String commandArgument;
    private final Dragon objectArgument;
    private final String username;
    private final String passwordHash;

    public Request(String commandName, String commandArgument, Dragon objectArgument,
                   String username, String passwordHash) {
        this.commandName = commandName;
        this.commandArgument = commandArgument;
        this.objectArgument = objectArgument;
        this.username = username;
        this.passwordHash = passwordHash;
    }

    public String getCommandName() { return commandName; }
    public String getCommandArgument() { return commandArgument; }
    public Dragon getObjectArgument() { return objectArgument; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
}