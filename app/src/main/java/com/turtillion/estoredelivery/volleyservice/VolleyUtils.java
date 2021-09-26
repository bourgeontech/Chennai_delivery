package com.turtillion.estoredelivery.volleyservice;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.turtillion.estoredelivery.utils.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolleyUtils {
    //    private int[] myIntArray = {};
//    private int[] cancelquearray = {};
    private VolleyInterface volleyInterface;
    /**
     * senttoken = 1 sent
     * 2 notsent
     * <p>
     * get = 2
     * post = 1
     **/
    private Handler handler = new Handler(Looper.getMainLooper());

    public VolleyUtils() {
    }

    public static void volleyerror(Context context, VolleyError error, VolleyInterface listener, int requestcode) {


        String Error = null;
        NetworkResponse networkResponse = error.networkResponse;


        if (networkResponse != null && networkResponse.statusCode == 400) {
            String jsonError = new String(networkResponse.data);
            Log.d("jsonerror", jsonError);

            try {

                JSONObject obj = new JSONObject(jsonError);

                Error = obj.getString("result");

            } catch (Throwable t) {
                Log.e("My App", "Could not parse malformed JSON: \"" + jsonError + "\"");
            }


        } else if (networkResponse != null && networkResponse.statusCode == 463) {
            Error = "Invalid Token (Token Expired)";

        } else if (networkResponse != null && networkResponse.statusCode == 404) {
            Error = "Error code 404";
        } else if (networkResponse != null && networkResponse.statusCode == 461) {
            Error = "Parameter missing";
        } else if (networkResponse != null && networkResponse.statusCode == 462) {
            Error = "Time Out";
        } else if (networkResponse != null && networkResponse.statusCode == 465) {
            Error = "Method not supported";
        } else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Error = "Request time out. Please check you network connection and try again";
        } else if (error instanceof AuthFailureError) {
            Error = "Authentication Faliure";


        } else if (error instanceof ServerError) {
            Error = "Error contacting server";
        } else if (error instanceof NetworkError) {
            Error = "Network Error. Please check you network connection and try again";
        }
//        else if (error instanceof ParseError) {
//            Error = "There is a Problem Parsing the Package";
//            Log.d("error", "error" + error);
//        }
        else {

            Error = "Something went wrong! Please try again later";
        }


        if (Error != null) {
//            listener.ErrorResponse(Error, requestcode);

//            Common.showError(context, Error);
            listener.ErrorResponse(Error, requestcode);
        }
//        showError(context, Error);
    }

    private void progresscancelfunt(final int requestcode) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //hide Progressbar here or close your activity.

                CommonUtils.cancelProgressBar();
                volleyInterface.ErrorResponse("Something went wrong", requestcode);


            }
        }, 20 * 1000);

    }


    public void callApi(final Context context, final VolleyInterface volleyInterface, String url, final int reqCode,
                        final HashMap<String, Object> params, int method, boolean showProgress) {


        if (CommonUtils.checkConnectivity(context)) {

            if (showProgress)
                CommonUtils.setProgressBar(context);

            final JSONObject parameters = new JSONObject(params);
            Log.d("request-->", parameters.toString());

            StringRequest stringRequest = new StringRequest(method, url,
                    response -> {
                        CommonUtils.cancelProgressBar();
                        try {
                            JSONObject obj = new JSONObject(response);
                            Log.d("response-->", obj.toString());
                            volleyInterface.SuccessResponse(obj, reqCode);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }, error -> {
                        CommonUtils.cancelProgressBar();
    //                    Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return super.getHeaders();
                }

                @Override
                public byte[] getBody() {
                    return parameters.toString().getBytes();
                }
            };


            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(stringRequest);

        } else {
            CommonUtils.cancelProgressBar();
            Toast.makeText(context, "Check your internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void callnetworkrequest(final Context context, final VolleyInterface listener, int getorpost,
                                   String url, HashMap<String, Object> params, final String TAG, final int requestcode, final int senttoken, final boolean progress) {


        int request;
        SharedPreferences shared = context.getApplicationContext().getSharedPreferences("my_pref", Context.MODE_PRIVATE);
        final String token = shared.getString("token", "");

        try {

            this.volleyInterface = listener;


            if (progress) {

                CommonUtils.setProgressBar(context);
//                progresscancelfunt(requestcode);
            }


            if (CommonUtils.checkConnectivity(context)) {

                JSONObject jsonObject;
                if (params == null) {
                    jsonObject = null;
                } else {
                    jsonObject = new JSONObject(params);
                }

                if (jsonObject != null) {
                    Log.d(TAG, "params-->" + jsonObject.toString());
                }

                if (getorpost == 1) {
                    request = Request.Method.POST;
                } else if (getorpost == 3) {
                    request = Request.Method.PUT;

                } else {
                    request = Request.Method.GET;

                }
                final JsonObjectRequest jsonObjReq = new JsonObjectRequest(request,
                        url, jsonObject, response -> {

                            CommonUtils.cancelProgressBar();
                            handler.removeCallbacksAndMessages(null);

                            if (response != null) {
                                try {
                                    Log.d("Success", response.toString());
                                    if (response.has("Success")) {
                                        String success = response.getString("Success");
                                        Log.d("Success", success);
                                        if (success.equals("1")) {

                                            volleyInterface.SuccessResponse(response, requestcode);

                                        }
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }


                            } else {
                                volleyInterface.ErrorResponse("Something went wrong", requestcode);
    //                            Common.showError(context, "Something went wrong");
                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CommonUtils.cancelProgressBar();
                        Log.d("error-->", error.getMessage() + "");
                        handler.removeCallbacksAndMessages(null);
                        boolean contain = false;
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<>();
                        if (senttoken == 1) {
                            params.put("Authorization", "Bearer " + token);
                        }
                        Log.d("token", token);
                        return params;
                    }
                };

                RequestQueue requestQueue = MySingleTon.getInstance(context).getRequestQueue();
                boolean contain = false;
                requestQueue.cancelAll(context);
                jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                        20000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(jsonObjReq);

            } else {

                CommonUtils.cancelProgressBar();
                handler.removeCallbacksAndMessages(null);
                volleyInterface.ErrorResponse("Network Error. Please check your network connection and try again", requestcode);
                boolean contain = false;
            }

        } catch (Exception e) {
            Log.d("error-->", e.getMessage() + "");
            handler.removeCallbacksAndMessages(null);
            volleyInterface.ErrorResponse("Something went wrong", requestcode);
//            CommonUtils.showError(context, "Something went wrong");
            CommonUtils.cancelProgressBar();
            e.printStackTrace();
        }

    }


    public void sendNotification(Context context, String type, HashMap<String, Object> params, final NotiInterface notiInterface, final int i) {

        try {

            String url = "https://fcm.googleapis.com/fcm/send";
            if (CommonUtils.checkConnectivity(context)) {

                JSONObject jsonObject = new JSONObject(params);
                Log.d("notification-->", jsonObject.toString());
                final JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                        url, jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        Log.d("notiresponse-->", "" + response.toString());

                        handler.removeCallbacksAndMessages(null);

//                        if (response.has("Success")) {
                        notiInterface.NotificationSuccess(response, i);
//                        }
                    }
                }, error -> {
                    Log.d("response2", "" + error.toString());
                    handler.removeCallbacksAndMessages(null);
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<>();
                        params.put("Content-Type", "application/json");
                        params.put("Authorization", "key=" + "AAAABYUIzkQ:APA91bG0ThQJy8Vihyby-D9YLKIxl509_cY36NuZQPOYUn-C7X1S49KXMf1dT0shONrRYK214OMZgeitslaeV5qc8qdOavgSLNO5N3JEgBLfVpym4AecU8Kevk1zSm6t_VE7_Vi49Vqc");
                        return params;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                requestQueue.add(jsonObjReq);


            } else {
                handler.removeCallbacksAndMessages(null);
                Log.d("response2", "no internet");
            }

        } catch (Exception e) {
            handler.removeCallbacksAndMessages(null);
            Log.d("response2", "" + e.toString());
            e.printStackTrace();
        }

    }

}

