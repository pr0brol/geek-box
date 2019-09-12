package server.Auth;

import common.AuthMessage;

import java.util.HashMap;
import java.util.Map;

public class AuthServiceImpl {

    public static Map<String, String> users = new HashMap<>();

    public AuthServiceImpl() {
        users.put("ivan", "123");
        users.put("petr", "456");
        users.put("jul", "789");
    }

    public static Map<String, String> getUsers() {
        return users;
    }

    public static boolean authUser(AuthMessage msg) {
        String password = users.get(msg.getLogin());
        return password != null && password.equals(msg.getPassword());
    }
}