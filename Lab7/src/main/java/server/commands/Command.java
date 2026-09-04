package server.commands;

import common.Request;
import common.Response;

/**
 * Интерфейс для всех команд сервера.
 * Определяет контракт взаимодействия между сетевым модулем и бизнес-логикой.
 */
public interface Command {
    /**
     * Выполняет логику команды.
     * @param request объект запроса, содержащий аргументы и данные от клиента.
     * @return объект ответа с результатом выполнения.
     */
    Response execute(Request request);

    /**
     * @return Описание команды для вывода в справке (help).
     */
    String getDescription();

    /**
     * @return Имя команды (используется для идентификации и логирования).
     */
    String getName();
}