package com.realappraiser.gharvalue.utils.security;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;


public class EncryptionKeyGenerator {
    private static final String TAG = EncryptionKeyGenerator.class.getSimpleName();
    public static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    public static final String KEY_ALIAS = "realappraiser";
    private static final String KEY_STORE_FILE_NAME = "/app/realappraiser.jks";
    private static final String KEY_STORE_PASSWORD = "realappraiser";

    public static final String PROVIDER = "BC";
    public static final int SALT_LENGTH = 16;
    public static final int IV_LENGTH = 16;
    public static final int PBE_ITERATION_COUNT = 100;

    private static final String HASH_ALGORITHM = "SHA-512";
    private static final String RANDOM_ALGORITHM = "SHA1PRNG";
    private static final String PBE_ALGORITHM = "PBEWithSHA256And256BitAES-CBC-BC";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    public static final String SECRET_KEY_ALGORITHM = "AES";

    @TargetApi(Build.VERSION_CODES.M) static SecurityKey generateSecretKey(KeyStore keyStore) {
        try {
            if (!keyStore.containsAlias(KEY_ALIAS)) {
                KeyGenerator keyGenerator =
                        KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);
                keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_ALIAS,
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT).setBlockModes(
                        KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .setRandomizedEncryptionRequired(false)
                        .build());
                return new SecurityKey(keyGenerator.generateKey());
            }
        } catch (KeyStoreException | NoSuchProviderException | NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            Log.e(TAG, "generateSecretKey: "+e);
        }
        try {
            final KeyStore.SecretKeyEntry entry =
                    (KeyStore.SecretKeyEntry) keyStore.getEntry(KEY_ALIAS, null);
            return new SecurityKey(entry.getSecretKey());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableEntryException e) {
            Log.e(TAG, "generateSecretKey: "+e);
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    static SecurityKey generateKeyPairPreM(Context context, KeyStore keyStore) {
        try {
            if (!keyStore.containsAlias(KEY_ALIAS)) {
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                //1 Year validity
                end.add(Calendar.YEAR, 1);

                KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context).setAlias(KEY_ALIAS)
                        .setSubject(new X500Principal("CN=" + KEY_ALIAS))
                        .setSerialNumber(BigInteger.TEN)
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();

                KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", ANDROID_KEY_STORE);
                kpg.initialize(spec);
                kpg.generateKeyPair();
            }
        } catch (KeyStoreException | NoSuchAlgorithmException | InvalidAlgorithmParameterException | NoSuchProviderException e) {
            Log.e(TAG, "generateSecretKey: "+e);
        }

        try {
            final KeyStore.PrivateKeyEntry entry =
                    (KeyStore.PrivateKeyEntry) keyStore.getEntry(KEY_ALIAS, null);
            return new SecurityKey(
                    new KeyPair(entry.getCertificate().getPublicKey(), entry.getPrivateKey()));
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableEntryException e) {
            Log.e(TAG, "generateSecretKey: "+e);
        }
        return null;
    }

    static SecurityKey generateSecretKeyPre18(Context context) {

        try {
            KeyStore androidCAStore = KeyStore.getInstance(KeyStore.getDefaultType());

            char[] password = KEY_STORE_PASSWORD.toCharArray();

            boolean isKeyStoreLoaded = loadKeyStore(context, androidCAStore, password);
            KeyStore.ProtectionParameter protParam = new KeyStore.PasswordProtection(password);
            if (!isKeyStoreLoaded || !androidCAStore.containsAlias(KEY_ALIAS)) {
                //Create and save new secret key
                saveMyKeystore(context, androidCAStore, password, protParam);
            }

            // Fetch Secret Key
            KeyStore.SecretKeyEntry pkEntry =
                    (KeyStore.SecretKeyEntry) androidCAStore.getEntry(KEY_ALIAS, protParam);

            Log.d(TAG, "Secret Key Fetched :" + new String(pkEntry.getSecretKey().getEncoded(), "UTF-8"));
            return new SecurityKey(pkEntry.getSecretKey());
        } catch (KeyStoreException | IOException | CertificateException | NoSuchAlgorithmException | UnrecoverableEntryException e) {
            Log.e(TAG, "generateSecretKey: "+e);

        }
        return null;
    }

    private static boolean loadKeyStore(Context context, KeyStore androidCAStore, char[] password) {
        java.io.FileInputStream fis;
        try {
            fis = context.openFileInput(KEY_STORE_FILE_NAME);
        } catch (FileNotFoundException e) {
            Log.e(TAG, "generateSecretKey: "+e);

            return false;
        }
        try {
            androidCAStore.load(fis, password);
            return true;
        } catch (IOException | NoSuchAlgorithmException | CertificateException e) {
            Log.e(TAG, "generateSecretKey: "+e);

        }
        return false;
    }

    private static void saveMyKeystore(Context context, KeyStore androidCAStore, char[] password,
                                       KeyStore.ProtectionParameter protParam)
            throws NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException {

        javax.crypto.SecretKey mySecretKey = KeyGenerator.getInstance("AES").generateKey();

        KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(mySecretKey);
        androidCAStore.load(null);
        androidCAStore.setEntry(KEY_ALIAS, skEntry, protParam);
        java.io.FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(KEY_STORE_FILE_NAME, MODE_PRIVATE);

            androidCAStore.store(fos, password);
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
        Log.d(TAG, "Secret Key Saved : " + new String(mySecretKey.getEncoded(), "UTF-8"));
    }


    public static String encrypt(String key, String initVector, String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            String encoded = Base64.encodeToString(encrypted,Base64.DEFAULT);

            return encoded;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static String decrypt(String key, String initVector, String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);

            byte[] original = cipher.doFinal(Base64.decode(encrypted,Base64.DEFAULT));

            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
    
}
