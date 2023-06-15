package com.example.mini_projets_02;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.TreeMap;

public class StartActivity extends AppCompatActivity {

    TextView tv_startActQuote, tv_startActAuthor;
    Button btn_startActPass;
    ToggleButton tb_startActPinUnpin;
    SharedPreferences sharedPreferences;
    Spinner spinner_startActBgColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //region Initializing attributes
        tv_startActQuote = findViewById(R.id.tv_startActQuote);
        tv_startActAuthor = findViewById(R.id.tv_startActAuthor);
        btn_startActPass = findViewById(R.id.btn_startActPass);
        tb_startActPinUnpin = findViewById(R.id.tb_startActPinUnpin);
        spinner_startActBgColors = findViewById(R.id.spinner_startActBgColors);
        //endregion

        //region Spinner code
        //region Filling the spinner
        String[] bgColors = {"Default", "LightSalmon", "Plum", "PaleGreen", "CornflowerBlue"};
        TreeMap<String, String> bgColorsValues = new TreeMap<>();
        bgColorsValues.put(bgColors[0], String.valueOf(((ColorDrawable) getWindow().getDecorView().getBackground()).getColor()));
        bgColorsValues.put(bgColors[1], "#DDA0DD");
        bgColorsValues.put(bgColors[2], "#FFA07A");
        bgColorsValues.put(bgColors[3], "#98FB98");
        bgColorsValues.put(bgColors[4], "#6495ED");
        //endregion
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, bgColors);
        spinner_startActBgColors.setAdapter(adapter);
        //endregion

        sharedPreferences = getSharedPreferences("pinned-quote", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int bgColor = sharedPreferences.getInt("bgColor", 0);
        spinner_startActBgColors.setSelection(bgColor);

        String quote = sharedPreferences.getString("quote", null);

        if (quote == null) {
            getRandomQuote();
        } else {
            String author = sharedPreferences.getString("author", null);
            tv_startActQuote.setText(quote);
            tv_startActAuthor.setText(author);
            tb_startActPinUnpin.setChecked(true);
        }

        tb_startActPinUnpin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                String quote = null;
                String author = null;

                if (isChecked) {
                    quote = tv_startActQuote.getText().toString();
                    author = tv_startActAuthor.getText().toString();
                } else {
                    getRandomQuote();
                }
                editor.putString("quote", quote);
                editor.putString("author", author);

                editor.commit();
            }
        });
        btn_startActPass.setOnClickListener(v -> {
            finish();
        });


        spinner_startActBgColors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (spinner_startActBgColors.getItemAtPosition(i).equals(bgColors[i])) {
                    int color = i == 0 ? Integer.parseInt((Objects.requireNonNull(bgColorsValues.get(bgColors[i])))) : Color.parseColor(bgColorsValues.get(bgColors[i]));
                    getWindow().getDecorView().setBackgroundColor(color);
                    editor.putInt("bgColor", i);
                    editor.commit();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void getRandomQuote() {
        RequestQueue queue = Volley.newRequestQueue(StartActivity.this);
        String url = "https://dummyjson.com/quotes/random";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    tv_startActQuote.setText(response.getString("quote"));
                    tv_startActAuthor.setText(response.getString("author"));
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjectRequest);
    }
}