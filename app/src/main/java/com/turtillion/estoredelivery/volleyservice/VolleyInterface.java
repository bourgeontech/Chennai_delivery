package com.turtillion.estoredelivery.volleyservice;

import org.json.JSONObject;

public interface VolleyInterface {
    void SuccessResponse(JSONObject response, int requestcode);

    void ErrorResponse(String msg, int requestcode);

}
