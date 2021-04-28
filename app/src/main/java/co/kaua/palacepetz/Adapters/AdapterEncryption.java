package co.kaua.palacepetz.Adapters;

import android.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AdapterEncryption {

    private static final String ALGORITHM = "AES";
    private static final String MODE = "AES/ECB/PKCS5PADDING";
    //private static final String MODE = "Blowfish/CBC/PKCS5Padding";
    //private static final String IV = "abcdefgh";
    private static final String IV = "bsxnWolsAyO7kCfWuyrnqg==";
    private static final String KEY= "AXe8YwuIn1zxt3FPWTZFlAa14EHdPAdN9FaZ9RQWihc=";

    public static  String encrypt(String value ) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(MODE);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(IV.getBytes()));
        byte[] values = cipher.doFinal(value.getBytes());
        return Base64.encodeToString(values, Base64.DEFAULT);
    }

    public static  String decrypt(String value) throws Exception {
        byte[] values = Base64.decode(value, Base64.DEFAULT);
        SecretKeySpec secretKeySpec = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(MODE);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, new IvParameterSpec(IV.getBytes()));
        return new String(cipher.doFinal(values));
    }
}