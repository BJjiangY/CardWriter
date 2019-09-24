package me.zhouzhuo810.cardwriter.nfc;

public class StringUtil {

    public static String bytesToHexString(byte[] paramArrayOfByte) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (paramArrayOfByte == null || paramArrayOfByte.length <= 0)
            return null;
        for (byte b = 0;; b++) {
            if (b >= paramArrayOfByte.length)
                return stringBuilder.toString();
            String str = Integer.toHexString(paramArrayOfByte[b] & 0xFF);
            if (str.length() < 2)
                stringBuilder.append(0);
            stringBuilder.append(str);
        }
    }

}
