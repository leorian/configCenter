package org.luotian.open.configuration.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.RandomAccess;

import sun.misc.BASE64Encoder;  
  
public class SshaEncrypt {  
      
    private static String SecretKey = "test_ssha_key_word";  
    private static BASE64Encoder enc = new BASE64Encoder();  
    private static SshaEncrypt inst = new SshaEncrypt("SHA-1");  
    private MessageDigest sha = null;  
          
    public static SshaEncrypt getInstance(){  
        return inst;  
    }  
      
    public SshaEncrypt(String alg) {  
        try {  
            sha = MessageDigest.getInstance(alg);  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        }  
    }  
      
    public String createDigest(String entity) {
        byte[] bytes = new byte[]{53,39,-1,111,33,-71,-10,41};

       // return createDigest(SecretKey.getBytes(),entity);
        return createDigest(bytes, entity);
    }  
      
    public String createDigest(String salt, String entity) {  
        return createDigest(salt.getBytes(),entity);  
    }  
      
    public String createDigest(byte[] salt, String entity) {  
        sha.reset();  
        sha.update(entity.getBytes());  
        sha.update(salt);  
        byte[] pwhash = sha.digest();  
        return new String(enc.encode(concatenate(pwhash, salt)));  
    }  
      
    private static byte[] concatenate(byte[] l, byte[] r) {  
        byte[] b = new byte[l.length + r.length];  
        System.arraycopy(l, 0, b, 0, l.length);  
        System.arraycopy(r, 0, b, l.length, r.length);  
        return b;  
    }

    public static void main(String args[]) {
        int i = 1;
        SshaEncrypt sshaEncrypt = new SshaEncrypt("SHA-1");
        while (true) {
            if (i == 12) {
                i = 1;
            }
            String randomString = getRandomString(i);
            if (sshaEncrypt.createDigest(randomString).equals("KqkfI5aQTaNRsxE/z1XTKMlxHAt1uE07qNTHoA==")) {
                System.out.println(randomString);
                System.out.println(sshaEncrypt.createDigest(getRandomString(i)).equals("KqkfI5aQTaNRsxE/z1XTKMlxHAt1uE07qNTHoA=="));
            }
            i++;
        }
    }

    public static String getRandomString(int length){
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<length;i++){
            int number=random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}  