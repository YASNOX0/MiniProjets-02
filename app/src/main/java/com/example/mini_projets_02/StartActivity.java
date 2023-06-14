package com.example.mini_projets_02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class StartActivity extends AppCompatActivity {

    TextView tv_startActQuote, tv_startActAuthor;
    Button btn_startActPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        tv_startActQuote = findViewById(R.id.tv_startActQuote);
        tv_startActAuthor = findViewById(R.id.tv_startActAuthor);
        btn_startActPass = findViewById(R.id.btn_startActPass);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://dummyjson.com/quotes/random";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            tv_startActQuote.setText(jsonObject.getString("quote"));
                            tv_startActAuthor.setText(jsonObject.getString("author"));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tv_startActQuote.setText("That didn't work!");
            }
        });

        // Add the request to the RequestQueue.
        stringRequest.setTag("TAG");
        queue.add(stringRequest);

        btn_startActPass.setOnClickListener(v -> {
            finish();
        });
    }
}