package com.example.logintaller.utils;

import android.util.Log;

import com.example.logintaller.SignUpActivity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public abstract class HashTool {
    private static final String LOG_TAG= HashTool.class.getSimpleName();
    public static final String EXTRA_SALT= "28%7a^9432^&7G";
    private static byte[] encriptarSha(String cadena,String salt){
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(salt.getBytes());
            return messageDigest.digest(cadena.getBytes());
        } catch (NoSuchAlgorithmException e) {
            Log.d(LOG_TAG,"error encriptando la cadena"+e.getMessage());
            return new byte[0];
        }
    }
    private static String bytesToHex(byte[] bytes) {
        if(bytes.length==0) return "";
        char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for ( int j = 0; j < bytes.length; j++ ) {
            v = bytes[j] & 0xFF;
            hexChars[j*2] = hexArray[v/16];
            hexChars[j*2 + 1] = hexArray[v%16];
        }
        return new String(hexChars);
    }

    public static String obtenerBytesSha(String cadena,String salt){
        return Arrays.toString(encriptarSha(cadena,salt));
    }
    public static String obtenerHexSha(String cadena,String salt){
        return bytesToHex(encriptarSha(cadena,salt));
    }

    public static boolean HashEqual(String plainText,String hexEncriptado){
        return obtenerHexSha(plainText,EXTRA_SALT).equals(hexEncriptado);
    }
}
