package com.tyagiabhinav.einvite.Util;

/**
 * Created by abhinavtyagi on 09/12/15.
 */
/**
 *
 */

import android.util.Base64;

import com.tyagiabhinav.einvite.Invite;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author Abhinav Tyagi
 *
 */
public class Encrypt {

    private static final String LOG_TAG = Encrypt.class.getSimpleName();
    private static String ENC_KEY;

    static {
        ENC_KEY = Invite.getEncKey();
    }

    /**
     *
     * @param message
     * @return
     * @throws Exception
     */
    public static String doAESEncryption(String message) throws Exception {
//        Log.d(LOG_TAG, "ENC KEY ---> " + ENC_KEY);
        byte[] encData = encryptAES(ENC_KEY, message, "ASCII", 256);
        return Base64.encodeToString(encData, Base64.NO_PADDING);
    }

    /**
     *
     * @param message
     * @return
     * @throws Exception
     */
    public static String doAESDecryption( String message) throws Exception {
//        Log.d(LOG_TAG, "ENC KEY ---> " + ENC_KEY);
        byte[] data = Base64.decode(message, Base64.NO_PADDING);
        return new String(decryptAES(ENC_KEY, data, "ASCII", 256));
    }

    /**
     * Function to encrypt string
     * @param seed
     * 		String seed
     * @param msg
     * 		String msg
     * @return
     * 		byte[] encrypted msg
     * @throws Exception
     */
    private static byte[] encryptAES(String seed, String msg, String encoder, int bit) throws Exception {
        byte[] rawKey = getRawKey(seed.getBytes(encoder),bit,"AES");
        SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        return cipher.doFinal(msg.getBytes(encoder));
    }

    /**
     * Function to decrypt encrypted string
     * @param seed
     * 		String seed
     * @param encData
     * 		String encData
     * @return
     * 		byte[] decrypted encData
     * @throws Exception
     */
    private static byte[] decryptAES(String seed, byte[] encData, String encoder, int bit) throws Exception {
        byte[] rawKey = getRawKey(seed.getBytes(encoder),bit,"AES");
        SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);
        return cipher.doFinal(encData);
    }

    /**
     * Function to return RAW key from seed
     * @param seed
     * 		byte[] seed
     * @return
     * 		byte[] raw key
     * @throws Exception
     */
    private static byte[] getRawKey(byte[] seed, int bit, String encryption) throws Exception {
        KeyGenerator kgen = KeyGenerator.getInstance(encryption);
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        sr.setSeed(seed);
        kgen.init(bit, sr);
        SecretKey skey = kgen.generateKey();
        byte[] raw = skey.getEncoded();
        return raw;
    }
}
