package server.auth;

import common.AuthMessage;
import server.Interface.AuthService;

import java.util.HashMap;
import java.util.Map;

public class AuthServiceImpl implements AuthService {

    public static Map<String, String> users = new HashMap<>();

    public AuthServiceImpl() {
        users.put("ivan", "123");
        users.put("petr", "456");
        users.put("jul", "789");
    }

    public boolean authUser(AuthMessage msg) {
        String password = users.get(msg.getLogin());
        return password != null && password.equals(msg.getPassword());
    }
}