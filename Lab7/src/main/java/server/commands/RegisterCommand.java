package server.commands;

import common.Request;
import common.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.manager.AuthManager;

public class RegisterCommand implements Command {
    private static final Logger logger = LoggerFactory.getLogger(RegisterCommand.class);
    private final AuthManager authManager;

    public RegisterCommand(AuthManager authManager) {
        this.authManager = authManager;
    }

    @Override
    public String getName() {
        return "register";
    }

    @Override
    public String getDescription() {
        return "зарегистрировать текущего пользователя в системе";
    }

    @Override
    public Response execute(Request request) {
        String username = request.getUsername();
        String passwordHash = request.getPasswordHash();

        if (username == null || username.isBlank() || passwordHash == null) {
            return new Response("Ошибка: не указаны логин или пароль для регистрации.");
        }

        if (authManager.register(username, passwordHash)) {
            logger.info("Зарегистрирован новый пользователь: {}", username);
            return new Response("Пользователь '" + username + "' успешно зарегистрирован! Теперь вы можете использовать команды (например, 'help' или 'insert').");
        } else {
            return new Response("Ошибка: пользователь с именем '" + username + "' уже существует.");
        }
    }
}