package org.luotian.open.configuration.util;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Created by xiezg@317hu.com on 2017/1/4 0004.
 */
public class LDAPUtil {

    public static final String CONFIG_CENTER_USERNAME_PREFIX = "CONFIG_CENTER_USERNAME_PREFIX_";

    public static boolean verifySHA(String ldappw, String inputpw) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        if (ldappw.startsWith("{SSHA}") || ldappw.startsWith("{ssha}")) {
            ldappw = ldappw.substring(6);
        } else if (ldappw.startsWith("{SHA}") || ldappw.startsWith("{sha}")) {
            ldappw = ldappw.substring(5);
        }

        byte[] ldappwbyte = Base64.decode(ldappw);
        byte[] shacode;
        byte[] salt;
        if (ldappwbyte.length <= 20) {
            shacode = ldappwbyte;
            salt = new byte[0];
        } else {
            shacode = new byte[20];
            salt = new byte[ldappwbyte.length - 20];
            System.arraycopy(ldappwbyte, 0, shacode, 0, 20);
            System.arraycopy(ldappwbyte, 20, salt, 0, salt.length);
        }
        System.out.println(new String(salt));
        md.update(inputpw.getBytes());
        md.update(salt);
        byte[] inputpwbyte = md.digest();

        return MessageDigest.isEqual(shacode, inputpwbyte);
    }

    public static void main (String args[]) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        System.out.println(verifySHA("{SSHA}YzTtDNPwGccsxtvc625j7DJ1lC81J/9vIbn2KQ==","317hu@2016"));
        System.out.println(verifySHA("{ssha}YzTtDNPwGccsxtvc625j7DJ1lC81J/9vIbn2KQ==","317hu@2016"));
    }
}
