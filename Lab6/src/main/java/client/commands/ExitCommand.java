package client.commands;

/**
 * Команда exit: завершает работу клиентского приложения.
 * Выполняется локально на стороне клиента.
 */
public class ExitCommand {

    /**
     * Завершает работу клиента.
     * Сохранение на сервере при этом не происходит (по ТЗ).
     */
    public void execute() {
        System.out.println("Завершение работы клиента. До свидания!");
        System.exit(0);
    }

    /** @return описание команды */
    public String getDescription() {
        return "завершить работу клиента (без сохранения на сервере)";
    }
}