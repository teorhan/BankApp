// FACTORY PATTERN
public class UserFactory {
    public static User createUser(String tc, String password) {
        if ("admin".equals(tc)) {
            return new Admin(tc, password);
        } else {
            return new Customer(tc, password);
        }
    }
}