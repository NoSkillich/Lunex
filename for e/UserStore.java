import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class UserStore {
    private static final List<String> registeredUsers = new ArrayList<>();
    public static void addUser(String userName) {
        if(userName != null && !userName.isEmpty()) {
            registeredUsers.add(userName);
        }
    }
    public static List<String> getUsers() {
        return Collections.unmodifiableList(new ArrayList<>(registeredUsers));
    }
}