package com.realappraiser.gharvalue.utils.security;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.realappraiser.gharvalue.MyApplication;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;


public class EncryptionUtils {
    private static final String TAG = EncryptionUtils.class.getSimpleName();

    private static EncryptionUtils instance;

    private Context context;

    public EncryptionUtils(Context context){
        if (context != null) {
            this.context = context;
        } else {
            if (MyApplication.getAppContext() != null) {

                this.context = MyApplication.getAppContext();
            }
        }
    }

    public String encrypt( String token) {
        SecurityKey securityKey = getSecurityKey(context);
        return securityKey != null ? securityKey.encrypt(token) : null;
    }

    public String decrypt(String token) {
        SecurityKey securityKey = getSecurityKey(context);
        return securityKey != null ? securityKey.decrypt(token) : null;
    }

    private static SecurityKey getSecurityKey(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return EncryptionKeyGenerator.generateSecretKey(getKeyStore());
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            return EncryptionKeyGenerator.generateKeyPairPreM(context, getKeyStore());
        } else {
            return EncryptionKeyGenerator.generateSecretKeyPre18(context);
        }
    }

    private static KeyStore getKeyStore() {
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance(EncryptionKeyGenerator.ANDROID_KEY_STORE);
            keyStore.load(null);
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            Log.e(TAG, "clear: "+e);
        }
        return keyStore;
    }

    public static void clear() {
        KeyStore keyStore = getKeyStore();
        try {
            if (keyStore.containsAlias(EncryptionKeyGenerator.KEY_ALIAS)) {
                keyStore.deleteEntry(EncryptionKeyGenerator.KEY_ALIAS);
            }
        } catch (KeyStoreException e) {
            Log.e(TAG, "clear: "+e);

        }
    }
}
