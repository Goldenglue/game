package database.services;

import pojos.User;

public interface UserService {
    int addNewUser(String username, String password);

    User getByUsername(String username);

    int getRating(String sessionId);

    User getBySession(String sessionId);

    int updateRatingOnWin(int userId);

    int updateRatingOnLose(int userId);
}
