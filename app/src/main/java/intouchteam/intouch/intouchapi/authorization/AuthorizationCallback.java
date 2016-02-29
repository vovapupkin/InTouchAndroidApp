package intouchteam.intouch.intouchapi.authorization;

import intouchteam.intouch.intouchapi.model.User;

public interface AuthorizationCallback {
    void onSuccess(User user);
    void inError(String error);
}
