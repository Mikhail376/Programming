package common;

import common.model.Dragon;
import java.io.Serializable;

public class Request implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String commandName;
    private final String commandArgument;
    private final Dragon objectArgument;

    public Request(String commandName, String commandArgument, Dragon objectArgument) {
        this.commandName = commandName;
        this.commandArgument = commandArgument;
        this.objectArgument = objectArgument;
    }

    public String getCommandName() { return commandName; }
    public String getCommandArgument() { return commandArgument; }
    public Dragon getObjectArgument() { return objectArgument; }
}