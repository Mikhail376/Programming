package commands;
/** Команда exit: завершает выполнение программы */
public class ExitCommand implements Command {
    /** Выполняет команду завершения программы
     * @param argument аргумент команды (не используется)
     */
    @Override
    public void execute(String argument) {
        System.out.println("Завершение работы программы...");
        System.exit(0);
    }
    /** @return описание команды */
    @Override
    public String getDescription() {
        return "завершить программу (без сохранения в файл)";
    }
}