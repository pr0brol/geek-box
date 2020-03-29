package server.Interface;

import common.AuthMessage;

public interface AuthService {
    boolean authUser(AuthMessage am);
}
