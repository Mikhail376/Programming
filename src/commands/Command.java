package commands;
/** Интерфейс команды, определяющий выполнение и описание команды */
public interface Command {
    /** Выполняет команду
     * @param argument аргумент команды
     */
    void execute(String argument);
    /** @return описание команды */
    String getDescription();
}