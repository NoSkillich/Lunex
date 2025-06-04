// FriendStatusManager.java
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class FriendStatusManager {
    public enum Status { ONLINE, OFFLINE }
    private static class Info {
        Status status;
        LocalDateTime lastSeen;
        Info(Status s, LocalDateTime t) { status = s; lastSeen = t; }
    }
    private final Map<String, Info> map = new HashMap<>();

    public FriendStatusManager() {
        // Пример начальных данных
        LocalDateTime now = LocalDateTime.now();
        map.put("Alice", new Info(Status.ONLINE,  now));
        map.put("Bob",   new Info(Status.OFFLINE, now.minusHours(3)));
        map.put("Carol", new Info(Status.OFFLINE, now.minusDays(1)));
        map.put("Dave",  new Info(Status.ONLINE,  now));
    }

    public void setStatus(String friend, Status st) {
        map.put(friend, new Info(st, LocalDateTime.now()));
    }

    public Status getStatus(String friend) {
        Info i = map.get(friend);
        return i==null ? Status.OFFLINE : i.status;
    }

    public String getStatusText(String friend) {
        Info i = map.get(friend);
        if (i == null) return "Unknown";
        if (i.status == Status.ONLINE) {
            return "Online";
        }
        Duration d = Duration.between(i.lastSeen, LocalDateTime.now());
        if (d.toDays()>0)   return "Last seen " + d.toDays()  + "d ago";
        if (d.toHours()>0)  return "Last seen " + d.toHours() + "h ago";
        if (d.toMinutes()>0)return "Last seen " + d.toMinutes() + "m ago";
        return "Last seen just now";
    }
}
