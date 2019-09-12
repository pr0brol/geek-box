package server.Auth;

import server.User;

public interface AuthService {
    boolean authUser(User user);
}
