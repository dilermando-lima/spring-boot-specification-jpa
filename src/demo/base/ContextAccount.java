package demo.base;

public class ContextAccount {

    private static final ThreadLocal<String> CURRENT_ACCOUNT = new ThreadLocal<>();

    private ContextAccount(){}

    public static String account() {
        return CURRENT_ACCOUNT.get();
    }

    public static void account(String account) {
        CURRENT_ACCOUNT.set(account);
    }

    public static void clear() {
        CURRENT_ACCOUNT.remove();
    }
}
