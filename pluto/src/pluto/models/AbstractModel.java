package pluto.models;

import java.security.SecureRandom;

public abstract class AbstractModel {
    protected String plutoCode;

    protected static final int PLUTO_CODE_LENGTH = 12;

    protected static final String PLUTO_CODE_CHARSET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public String getPlutoCode() {
        return plutoCode;
    }

    protected void generatePlutoCode() {
        SecureRandom rnd = new SecureRandom();

        StringBuilder sb = new StringBuilder(PLUTO_CODE_LENGTH);
        for (int i = 0; i < PLUTO_CODE_LENGTH; i++) {
            sb.append(PLUTO_CODE_CHARSET.charAt(rnd.nextInt(PLUTO_CODE_CHARSET.length())));
        }
        plutoCode = sb.toString();
    }

    public abstract int getIndex();
    protected abstract void save();
}
