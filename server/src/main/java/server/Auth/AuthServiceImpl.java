package server.Auth;

import server.User;

import java.util.HashMap;
import java.util.Map;

public class AuthServiceImpl implements AuthService {

    public Map<String, String> users = new HashMap<>();

    public AuthServiceImpl(){
        users.put("ivan", "123");
        users.put("petr", "456");
        users.put("jul", "789");
    }
    @Override
    public boolean authUser(User user){
        String pwd = users.get(user.getLogin());
        return pwd !=null && pwd.equals(user.getPassword());
    }
}