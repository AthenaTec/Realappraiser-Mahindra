package com.realappraiser.gharvalue.utils.security;

import android.app.Activity;
import android.util.Base64;
import android.util.Log;

import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.safetynet.SafetyNetClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.realappraiser.gharvalue.R;
import com.realappraiser.gharvalue.model.SafeNetModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;

public class SafetyNetChecker {
    private static final String TAG = SafeNetModel.class.getSimpleName();

    public SafetyNetChecker(Activity activity,OnSuccessListener<SafetyNetApi.AttestationResponse> successListener,
                            OnFailureListener failureListener) {

        String nonceData = "Safety Net Sample: " + System.currentTimeMillis();
        byte[] nonce = getRequestNonce(nonceData);

        SafetyNetClient client = SafetyNet.getClient(activity);
        Task<SafetyNetApi.AttestationResponse> task = client.attest(Objects.requireNonNull(nonce),
                activity.getString(R.string.google_maps_key));

        task.addOnSuccessListener(activity, successListener)
                .addOnFailureListener(activity, failureListener);
    }



    public static byte[] getRequestNonce(String data) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[24];
        Random random = new Random();
        random.nextBytes(bytes);
        try {
            byteStream.write(bytes);
            byteStream.write(data.getBytes());
        } catch (IOException e) {
            return null;
        }

        return byteStream.toByteArray();
    }


    public static SafeNetModel decodeJWS(String result) {
        byte[] json = Base64.decode(result.split("[.]")[1],Base64.DEFAULT);
        String text = new String(json, StandardCharsets.UTF_8);
        Log.e(TAG, "decodeJWS: "+text );
       return new Gson().fromJson(text, SafeNetModel.class);
    }
    
    
    

}
