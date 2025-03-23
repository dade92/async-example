package user;

public class RestUserRepository implements UserRepository {

    public static final String USERNAME = "dbotti";

    @Override
    public User findByToken(String token) {
        try {
            Thread.sleep(1000);
            return new User("XXX", USERNAME);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
