package com.bellsoft.updater.common.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

public class FileUtils {
    
    public static byte[] createCheckSum(String filename,String algorithm) throws Exception {
        InputStream fis =  new FileInputStream(filename);

        byte[] buffer = new byte[1024];
        MessageDigest complete = MessageDigest.getInstance(algorithm);
        int numRead;

        do {
            numRead = fis.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        fis.close();
        return complete.digest();
    }

    // see this How-to for a faster way to convert
    // a byte array to a HEX string
    public static String getFileHash(String filename,String algorithm) throws Exception {
        byte[] b = createCheckSum(filename,algorithm);
        String result = "";

        for (int i=0; i < b.length; i++) {
            result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }
}
