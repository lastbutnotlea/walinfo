package databuild;

public class Token {

    private final String token;
    private final int wknr;
    private final boolean used;
    private final boolean valid;

    public Token(String token, int wknr, boolean used, boolean valid) {
        this.token = token;
        this.wknr = wknr;
        this.used = used;
        this.valid = valid;
    }

    public String getToken() {
        return token;
    }

    public int getWknr() {
        return wknr;
    }

    public boolean isUsed() {
        return used;
    }

    public boolean isValid() {
        return valid;
    }
}
