package demo.base;

public class ContextAccount {

    private static final ThreadLocal<String> CURRENT_ACCOUNT_ID = new ThreadLocal<>();

    private ContextAccount(){}

    public static String accountId() {
        return CURRENT_ACCOUNT_ID.get();
    }

    public static void accountId(String account) {
        CURRENT_ACCOUNT_ID.set(account);
    }

    public static void clear() {
        CURRENT_ACCOUNT_ID.remove();
    }
}
