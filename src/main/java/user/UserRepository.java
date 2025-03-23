package user;

public interface UserRepository {
    User findByToken(String token);
}