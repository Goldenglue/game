package database.services;

public interface SessionService
{
    void add(String sessionId, int userId);

    void endSession(String sessionId);
}
