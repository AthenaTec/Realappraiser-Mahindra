package com.realappraiser.gharvalue.ticketRaiseSystem.service;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.realappraiser.gharvalue.R;
import com.realappraiser.gharvalue.communicator.TicketTaskCompleteListener;
import com.realappraiser.gharvalue.communicator.UnsafeOkHttpClient;
import com.realappraiser.gharvalue.utils.Connectivity;
import com.realappraiser.gharvalue.utils.General;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class TicketWebserviceCommunicator extends AsyncTask<String, Void, TicketJsonRequestData> {
    private static final String TAG = "WebserviceCommunicator";
    //ProgressDialog progressBar;
    private int REQUEST_CODE;
    private TicketJsonRequestData processData;
    final static int CONN_WAIT_TIME = 30000;
    final static int CONN_DATA_WAIT_TIME = 30000;
    private Context context;
    private TicketTaskCompleteListener listener;
    private boolean progressDialogLoader = true;
    public Response response;
    public String value;

    private boolean isValidUrl;

    public TicketWebserviceCommunicator(Context ctx, TicketJsonRequestData createRequest, int Requestcode) {
        this.context = ctx;
        this.processData = createRequest;
        this.REQUEST_CODE = Requestcode;
        this.isValidUrl = createRequest.isValidUrl();
    }

    @Override
    protected TicketJsonRequestData doInBackground(String... params) {
        TicketJsonRequestData requestData = new TicketJsonRequestData();
        String jsonStr = "";
        try {
            switch (REQUEST_CODE) {
                case 1:
                    requestData = makeOkHttpGetCall(processData);
                    break;
                case 2:
                    requestData = makeOkHttpPostCall(processData);
                    break;
                case 3:
                    requestData = makeOkHttpPutCall(processData);
                    break;
                case 4:
                    requestData = makeOkHttpDeleteCall(processData);
                    break;
                case 5:
                    requestData = makeOkHttpGetWithTokenCall(processData);
                    break;
                case 6:
                    requestData = makeOkHttpPostWithTokenCall(processData);
                    break;
                case 7:
                    requestData = makeOkHttpPutWithTokenCall(processData);
                    break;
                case 8:
                    requestData = makeOkHttpDeleteWithTokenCall(processData);
                    break;
                case 9:
                    requestData = makeOkHttpDeleteDocumentCall(processData);
                    break;
            }
        } catch (Exception e) {
            General.hideloading();
            Log.e(TAG, "doInBackground: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return requestData;
    }


    @Override
    protected void onPostExecute(TicketJsonRequestData responseData) {
        super.onPostExecute(responseData);
        listener.onTaskComplete(responseData);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i(TAG, "Async task started");
    }

    public void setFetchMyData(TicketTaskCompleteListener listener) {
        this.listener = listener;
    }

    /*******
     * Post Method OkHttp3 Handler
     ********/
    public TicketJsonRequestData makeOkHttpGetCall(TicketJsonRequestData jsonRequestData) {
        TicketJsonRequestData jsonResponse = new TicketJsonRequestData();
        try {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

            OkHttpClient client = null;

            //SSL UNPIN
            client = UnsafeOkHttpClient.getUnsafeOkHttpClient()
                    .addInterceptor(interceptor)
                    .addInterceptor(provideCacheInterceptor())
                    .addInterceptor(provideOfflineCacheInterceptor(context))
                    .cache(provideCache(context))
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS).build();


            //For SSL PIN
           /* if (BuildConfig.BUILD_TYPE == "debug" || BuildConfig.DEBUG == true) {
                client = new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .addInterceptor(provideCacheInterceptor())
                        .addInterceptor(provideOfflineCacheInterceptor(context))
                        .cache(provideCache(context))
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();
            } else {
                client = new OkHttpClient.Builder()
                        .addInterceptor(provideOfflineCacheInterceptor(context))
                        .cache(provideCache(context))
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();
            }*/


            Request request = new Request.Builder()
                    .url(jsonRequestData.getUrl())
                    .build();

            Response response = client.newCall(request).execute();
            jsonResponse.setResponse(response.body().string());
            jsonResponse.setResponseCode(response.code());
            jsonResponse.setSuccessful(response.isSuccessful());


           /* if (!response.isSuccessful()) {
                DataResponse dataResponse = ResponseParser.parseCurrentServerResponse(value);
                if (!dataResponse.msg.isEmpty())
                    General.customToastLong(dataResponse.msg, context);
                else
                    General.customToastLong(context.getString(R.string.serverProblem), context);
                General.hideloading();
            }*/

            System.out.println("postJSONRequest response.body : " + value);

        } catch (Exception e) {
            General.hideloading();
            if (!isValidUrl)
                General.customToastLong(context.getString(R.string.something_wrong), context);
            Log.e(TAG, "makeOkHttpGetCall: " + e.getLocalizedMessage());
            jsonResponse.setSuccessful(false);
            e.printStackTrace();

        }
        return jsonResponse;
    }


    public TicketJsonRequestData makeOkHttpGetWithTokenCall(TicketJsonRequestData jsonRequestData) {
        TicketJsonRequestData jsonResponse = new TicketJsonRequestData();
        try {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

            OkHttpClient client = null;

            //For SSL UNPI
            client = UnsafeOkHttpClient.getUnsafeOkHttpClient()
                    .addInterceptor(interceptor)
                    .addInterceptor(provideCacheInterceptor())
                    .addInterceptor(provideOfflineCacheInterceptor(context))
                    .cache(provideCache(context))
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS).build();


            //For SSL PIN
           /* if (BuildConfig.BUILD_TYPE == "debug" || BuildConfig.DEBUG == true) {
                client = new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .addInterceptor(provideCacheInterceptor())
                        .addInterceptor(provideOfflineCacheInterceptor(context))
                        .cache(provideCache(context))
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();
            } else {
                client = new OkHttpClient.Builder()
                        .addInterceptor(provideOfflineCacheInterceptor(context))
                        .cache(provideCache(context))
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();
            }*/

            Request request = new Request.Builder()
                    .addHeader("Authorization", jsonRequestData.getAuthToken())
                    .url(jsonRequestData.getUrl())
                    .build();

            Response response = client.newCall(request).execute();
            jsonResponse.setResponse(response.body().string());
            jsonResponse.setResponseCode(response.code());
            jsonResponse.setSuccessful(response.isSuccessful());

        } catch (Exception e) {
            General.hideloading();
            General.customToastLong(context.getString(R.string.something_wrong), context);
            jsonResponse.setSuccessful(false);
            e.printStackTrace();
        }
        return jsonResponse;
    }

    /*******
     * Post Method OkHttp3 Handler
     ********/
    public TicketJsonRequestData makeOkHttpPostCall(TicketJsonRequestData jsonRequestData) {
        TicketJsonRequestData jsonResponse = new TicketJsonRequestData();
        try {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

            OkHttpClient client = null;

            client = UnsafeOkHttpClient.getUnsafeOkHttpClient()
                    .addInterceptor(interceptor)
                    .addInterceptor(provideCacheInterceptor())
                    .addInterceptor(provideOfflineCacheInterceptor(context))
                    .cache(provideCache(context))
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS).build();

           /* if (BuildConfig.BUILD_TYPE == "debug" || BuildConfig.DEBUG == true) {
                client = new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .addInterceptor(provideCacheInterceptor())
                        .addInterceptor(provideOfflineCacheInterceptor(context))
                        .cache(provideCache(context))
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();
            } else {
                client = new OkHttpClient.Builder()
                        .addInterceptor(provideOfflineCacheInterceptor(context))
                        .cache(provideCache(context))
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();
            }*/

            Request request = new Request.Builder()
                    .url(jsonRequestData.getUrl())
                    .addHeader("Content-Type", "application/json")
                    .method("POST", RequestBody.create(null, new byte[0]))
                    .post(jsonRequestData.getRequestBody())
                    .build();
            Response response = client.newCall(request).execute();
            jsonResponse.setResponse(response.body().string());
            jsonResponse.setResponseCode(response.code());
            jsonResponse.setSuccessful(response.isSuccessful());
            System.out.println("create lot details: " + value);
            /*if (!response.isSuccessful()) {
                DataResponse dataResponse = ResponseParser.parseCurrentServerResponse(value);
                if (!dataResponse.msg.isEmpty())
                    General.customToastLong(dataResponse.msg, context);
                else
                    General.customToastLong(context.getString(R.string.serverProblem), context);
                General.hideloading();
            }*/
        } catch (Exception e) {
            General.hideloading();
            General.customToastLong(context.getString(R.string.something_wrong), context);
            Log.e(TAG, "makeOkHttpGetCall: " + e.getLocalizedMessage());
            jsonResponse.setSuccessful(false);
            e.printStackTrace();
        }
        return jsonResponse;
    }

    public TicketJsonRequestData makeOkHttpPostWithTokenCall(TicketJsonRequestData jsonRequestData) {
        TicketJsonRequestData jsonResponse = new TicketJsonRequestData();
        try {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

            OkHttpClient client = null;

            client = UnsafeOkHttpClient.getUnsafeOkHttpClient()
                    .addInterceptor(interceptor)
                    .addInterceptor(provideCacheInterceptor())
                    .addInterceptor(provideOfflineCacheInterceptor(context))
                    .cache(provideCache(context))
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS).build();

           /* if (BuildConfig.BUILD_TYPE == "debug" || BuildConfig.DEBUG == true) {
                client = new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .addInterceptor(provideCacheInterceptor())
                        .addInterceptor(provideOfflineCacheInterceptor(context))
                        .cache(provideCache(context))
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();
            } else {
                client = new OkHttpClient.Builder()
                        .addInterceptor(provideOfflineCacheInterceptor(context))
                        .cache(provideCache(context))
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();
            }*/

            Request request = new Request.Builder()
                    .url(jsonRequestData.getUrl())
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", jsonRequestData.getAuthToken())
                    .method("POST", RequestBody.create(null, new byte[0]))
                    .post(jsonRequestData.getRequestBody())
                    .build();
            Response response = client.newCall(request).execute();
            jsonResponse.setResponse(response.body().string());
            jsonResponse.setResponseCode(response.code());
            jsonResponse.setSuccessful(response.isSuccessful());
          /*  if (!response.isSuccessful()) {
                DataResponse dataResponse = ResponseParser.parseCurrentServerResponse(value);
                if (!dataResponse.msg.isEmpty())
                    General.customToastLong(dataResponse.msg, context);
                else
                    General.customToastLong(context.getString(R.string.serverProblem), context);
                General.hideloading();
            }*/
        } catch (Exception e) {
            General.hideloading();
            //  General.customToastLong(context.getString(R.string.something_wrong), context);
            Log.e(TAG, "makeOkHttpGetCall: " + e.getLocalizedMessage());
            jsonResponse.setSuccessful(false);
            e.printStackTrace();
        }
        return jsonResponse;
    }


    /*******
     * Put Method OkHttp3 Handler
     ********/
    public TicketJsonRequestData makeOkHttpPutCall(TicketJsonRequestData jsonRequestData) {
        TicketJsonRequestData jsonResponse = new TicketJsonRequestData();
        try {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

            OkHttpClient client = null;

            client = UnsafeOkHttpClient.getUnsafeOkHttpClient()
                    .addInterceptor(interceptor)
                    .addInterceptor(provideCacheInterceptor())
                    .addInterceptor(provideOfflineCacheInterceptor(context))
                    .cache(provideCache(context))
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS).build();

           /* if (BuildConfig.BUILD_TYPE == "debug" || BuildConfig.DEBUG == true) {
                client = new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .addInterceptor(provideCacheInterceptor())
                        .addInterceptor(provideOfflineCacheInterceptor(context))
                        .cache(provideCache(context))
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();
            } else {
                client = new OkHttpClient.Builder()
                        .addInterceptor(provideOfflineCacheInterceptor(context))
                        .cache(provideCache(context))
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();
            }*/


            Request request = new Request.Builder()
                    .url(jsonRequestData.getUrl())
                    //.method("POST", RequestBody.create(null, new byte[0]))
                    .put(jsonRequestData.getRequestBody())
                    .build();
            Response response = client.newCall(request).execute();
            jsonResponse.setResponse(response.body().string());
            jsonResponse.setResponseCode(response.code());
            jsonResponse.setSuccessful(response.isSuccessful());
         /*   if (!response.isSuccessful()) {
                DataResponse dataResponse = ResponseParser.parseCurrentServerResponse(value);
                if (!dataResponse.msg.isEmpty())
                    General.customToastLong(dataResponse.msg, context);
                else
                    General.customToastLong(context.getString(R.string.serverProblem), context);
                General.hideloading();
            }*/

        } catch (Exception e) {
            General.hideloading();
            General.customToastLong(context.getString(R.string.something_wrong), context);
            Log.e(TAG, "makeOkHttpGetCall: " + e.getLocalizedMessage());
            jsonResponse.setSuccessful(false);
            e.printStackTrace();
        }
        return jsonResponse;
    }

    public TicketJsonRequestData makeOkHttpPutWithTokenCall(TicketJsonRequestData jsonRequestData) {
        TicketJsonRequestData jsonResponse = new TicketJsonRequestData();
        try {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

            OkHttpClient client = null;

            client = UnsafeOkHttpClient.getUnsafeOkHttpClient()
                    .addInterceptor(interceptor)
                    .addInterceptor(provideCacheInterceptor())
                    .addInterceptor(provideOfflineCacheInterceptor(context))
                    .cache(provideCache(context))
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS).build();

           /* if (BuildConfig.BUILD_TYPE == "debug" || BuildConfig.DEBUG == true) {
                client = new OkHttpClient.Builder()
                        .addInterceptor(interceptor)
                        .addInterceptor(provideCacheInterceptor())
                        .addInterceptor(provideOfflineCacheInterceptor(context))
                        .cache(provideCache(context))
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();
            } else {
                client = new OkHttpClient.Builder()
                        .addInterceptor(provideOfflineCacheInterceptor(context))
                        .cache(provideCache(context))
                        .connectTimeout(10, TimeUnit.SECONDS)
                        .writeTimeout(10, TimeUnit.SECONDS)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .build();
            }*/
            Request request = new Request.Builder()
                    .url(jsonRequestData.getUrl())
                    .method("PUT", RequestBody.create(null, new byte[0]))
                    .addHeader("Authorization", jsonRequestData.getAuthToken())
                    .put(jsonRequestData.getRequestBody())
                    .build();
            Response response = client.newCall(request).execute();
            jsonResponse.setResponse(response.body().string());
            jsonResponse.setResponseCode(response.code());
            jsonResponse.setSuccessful(response.isSuccessful());

           /* if (!response.isSuccessful()) {
                DataResponse dataResponse = ResponseParser.parseCurrentServerResponse(value);
                if (!dataResponse.msg.isEmpty())
                    General.customToastLong(dataResponse.msg, context);
                else
                    General.customToastLong(context.getString(R.string.serverProblem), context);
                General.hideloading();
            }*/

        } catch (Exception e) {
            General.hideloading();
            General.customToastLong(context.getString(R.string.something_wrong), context);
            Log.e(TAG, "makeOkHttpGetCall: " + e.getLocalizedMessage());
            jsonResponse.setSuccessful(false);
            e.printStackTrace();
        }
        return jsonResponse;
    }


    private TicketJsonRequestData makeOkHttpDeleteCall(TicketJsonRequestData jsonRequestData) {
        TicketJsonRequestData jsonResponse = new TicketJsonRequestData();
        try {
            OkHttpClient client = null;

            client = UnsafeOkHttpClient.getUnsafeOkHttpClient()
                    .addInterceptor(provideCacheInterceptor())
                    .addInterceptor(provideOfflineCacheInterceptor(context))
                    .cache(provideCache(context))
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS).build();

           /* client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();*/

            Request request = new Request.Builder()
                    .url(jsonRequestData.getUrl())
                    .method("DELETE", RequestBody.create(null, new byte[0]))
                    .delete(jsonRequestData.getRequestBody())
                    .build();
            Response response = client.newCall(request).execute();
            jsonResponse.setResponse(response.body().string());
            jsonResponse.setResponseCode(response.code());
            jsonResponse.setSuccessful(response.isSuccessful());
        } catch (Exception e) {
            General.hideloading();
            General.customToastLong(context.getString(R.string.something_wrong), context);
            Log.e(TAG, "makeOkHttpGetCall: " + e.getLocalizedMessage());
            jsonResponse.setSuccessful(false);
            e.printStackTrace();
        }
        return jsonResponse;
    }

    private TicketJsonRequestData makeOkHttpDeleteDocumentCall(TicketJsonRequestData jsonRequestData) {
        TicketJsonRequestData jsonResponse = new TicketJsonRequestData();
        try {
            OkHttpClient client = null;

            client = UnsafeOkHttpClient.getUnsafeOkHttpClient()
                    .addInterceptor(provideCacheInterceptor())
                    .addInterceptor(provideOfflineCacheInterceptor(context))
                    .cache(provideCache(context))
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS).build();

           /* client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();*/

            Request request = new Request.Builder()
                    .url(jsonRequestData.getUrl())
                    .addHeader("Authorization", jsonRequestData.getAuthToken())
                    .method("DELETE", RequestBody.create(null, new byte[0]))
                    .delete(jsonRequestData.getRequestBody())
                    .build();
            Response response = client.newCall(request).execute();
            jsonResponse.setResponse(response.body().string());
            jsonResponse.setResponseCode(response.code());
            jsonResponse.setSuccessful(response.isSuccessful());

         /*   if (!response.isSuccessful()) {
                DataResponse dataResponse = ResponseParser.parseCurrentServerResponse(value);
                if (!dataResponse.msg.isEmpty())
                    General.customToastLong(dataResponse.msg, context);
                else
                    General.customToastLong(context.getString(R.string.serverProblem), context);
                General.hideloading();
            }*/

        } catch (Exception e) {
            General.hideloading();
            General.customToastLong(context.getString(R.string.something_wrong), context);
            Log.e(TAG, "makeOkHttpGetCall: " + e.getLocalizedMessage());
            jsonResponse.setSuccessful(false);
            e.printStackTrace();
        }
        return jsonResponse;
    }

    private TicketJsonRequestData makeOkHttpDeleteWithTokenCall(TicketJsonRequestData jsonRequestData) {
        TicketJsonRequestData jsonResponse = new TicketJsonRequestData();
        try {


            OkHttpClient client = null;

            client = UnsafeOkHttpClient.getUnsafeOkHttpClient()
                    .addInterceptor(provideCacheInterceptor())
                    .addInterceptor(provideOfflineCacheInterceptor(context))
                    .cache(provideCache(context))
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS).build();


           /* client = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build();*/

            Request request = new Request.Builder()
                    .url(jsonRequestData.getUrl())
                    .addHeader("Authorization", jsonRequestData.getAuthToken())
                    .method("DELETE", RequestBody.create(null, new byte[0]))
                    .delete(jsonRequestData.getRequestBody())
                    .build();
            Response response = client.newCall(request).execute();
            jsonResponse.setResponse(response.body().string());
            jsonResponse.setResponseCode(response.code());



         /*  if (!response.isSuccessful()) {
                DataResponse dataResponse = ResponseParser.parseCurrentServerResponse(value);
                if (!dataResponse.msg.isEmpty())
                    General.customToastLong(dataResponse.msg, context);
                else
                    General.customToastLong(context.getString(R.string.serverProblem), context);
                General.hideloading();
            }*/

        } catch (Exception e) {
            General.hideloading();
            General.customToastLong(context.getString(R.string.something_wrong), context);
            Log.e(TAG, "makeOkHttpGetCall: " + e.getLocalizedMessage());
            jsonResponse.setSuccessful(response.isSuccessful());
            e.printStackTrace();
        }
        return jsonResponse;
    }

    private void getProgressDialogflag() {


        switch (REQUEST_CODE) {
            case 1:
                progressDialogLoader = true;
                break;
            case 2:
                progressDialogLoader = false;
                break;
            case 3:
                progressDialogLoader = true;
                break;
            case 4:
                progressDialogLoader = true;
                break;
            case 5:
                progressDialogLoader = false;
                break;
            case 6:
                progressDialogLoader = false;
                break;
            case 7:
                progressDialogLoader = false;
                break;
            case 8:
                progressDialogLoader = true;
                break;
            case 9:
                progressDialogLoader = true;
                break;
            case 10:
                progressDialogLoader = true;
                break;
            case 11:
                progressDialogLoader = true;
                break;
            case 12:
                progressDialogLoader = true;
                break;
            case 13:
                progressDialogLoader = false;
                break;
            case 14:
                progressDialogLoader = false;
                break;
            case 15:
                progressDialogLoader = true;
                break;
            case 16:
                progressDialogLoader = true;
                break;
        }
    }

    private void OkHttpClientInterceptor() {
        OkHttpClient client = null;
        try {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .addInterceptor(provideCacheInterceptor())
                    .callTimeout(Duration.ofSeconds(3000))
                    .addInterceptor(provideOfflineCacheInterceptor(context))
                    .cache(provideCache(context))
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .build();
        } catch (Exception e) {
            General.hideloading();
            Log.e(TAG, "makeOkHttpGetCall: " + e.getLocalizedMessage());
        }
    }


    public static Cache provideCache(Context context) {
        Cache cache = null;
        try {
            cache = new Cache(new File(context.getCacheDir(), "http-cache"), 10 * 1024 * 1024);
        } catch (Exception e) {
            Log.e(TAG, "Could not create Cache!");
            e.printStackTrace();
        }
        return cache;
    }

    public static Interceptor provideCacheInterceptor() {
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {

                okhttp3.Response response = chain.proceed(chain.request());

                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(2, TimeUnit.MINUTES)
                        .build();

                return response.newBuilder()
                        .header("Cache-Control", cacheControl.toString())
                        .build();
            }
        };
        return interceptor;
    }

    public static Interceptor provideOfflineCacheInterceptor(final Context context) {
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                if (Connectivity.isConnected(context)) {
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale(7, TimeUnit.DAYS)
                            .build();
                    request = request.newBuilder()
                            .cacheControl(cacheControl)
                            .build();
                }
                return chain.proceed(request);
            }
        };
        return interceptor;
    }


}
