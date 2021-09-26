package com.turtillion.estoredelivery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.turtillion.estoredelivery.R;
// import com.turtillion.estoredelivery.NotificationAdapter;
// import com.turtillion.estoredelivery.adapter.OrdersAdapter;
import com.turtillion.estoredelivery.base.BaseActivity;
import com.turtillion.estoredelivery.databinding.ActivityNotificationBinding;
// import com.turtillion.estoredelivery.models.NotificationDomain;
// import com.turtillion.estoredelivery.OrderDomain;
import com.turtillion.estoredelivery.utils.CommonUtils;
import com.turtillion.estoredelivery.utils.Constants;
import com.turtillion.estoredelivery.utils.UrlConstants;
import com.turtillion.estoredelivery.volleyservice.VolleyInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.turtillion.estoredelivery.utils.UrlConstants.REQ_addNewSubCategory;
// import com.turtillion.estoredelivery.utils.UrlConstants.REQ_shopOrderNotify;

public class NotificationActivity extends BaseActivity implements VolleyInterface {

    ActivityNotificationBinding binding;
    //NotificationAdapter adapter;
   // ArrayList<OrderDomain> notificationList = new ArrayList<>();

    String shopId;
    String shopName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification);



      //  setupRecyclerView();

    }

    @Override
    public void SuccessResponse(JSONObject response, int requestcode) {

    }

    @Override
    public void ErrorResponse(String msg, int requestcode) {

    }
}