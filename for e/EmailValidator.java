import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.Hashtable;
import java.util.regex.Pattern;

public class EmailValidator {
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[\\w.+\\-]+@([\\w\\-]+\\.)+[A-Za-z]{2,}$");

    /**
     * Проверяет базовый формат email.
     */
    public static boolean isFormatValid(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean domainExists(String email) {
        String[] parts = email.split("@", 2);
        if (parts.length != 2) return false;
        String domain = parts[1];
        try {
            Hashtable<String, String> env = new Hashtable<>();
            env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
            DirContext ctx = new InitialDirContext(env);
            // Сначала MX
            Attributes attrs = ctx.getAttributes(domain, new String[]{"MX"});
            Attribute mx = attrs.get("MX");
            if (mx != null && mx.size() > 0) return true;
            // fallback на A-запись
            attrs = ctx.getAttributes(domain, new String[]{"A"});
            Attribute a = attrs.get("A");
            return (a != null && a.size() > 0);
        } catch (NamingException ex) {
            return false;
        }
    }
}
