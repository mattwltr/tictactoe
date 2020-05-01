package matt.wltr.labs.tictactoe.util;

import java.util.Base64;

public class Random extends java.util.Random {

    public String nextString() {
        byte[] array = new byte[6];
        new java.util.Random().nextBytes(array);
        return Base64.getMimeEncoder().encodeToString(array).replaceAll("[^\\p{IsAlphabetic}\\p{IsDigit}]", "");
    }
}
