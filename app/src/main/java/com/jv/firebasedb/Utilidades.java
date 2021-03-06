package com.jv.firebasedb;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utilidades {

    public Utilidades(){}

    public String md5(String s) {
        try {
            if( s == null )
                return "";
            // Crea el hash y convertelo.
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));

            return hexString.toString();
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
