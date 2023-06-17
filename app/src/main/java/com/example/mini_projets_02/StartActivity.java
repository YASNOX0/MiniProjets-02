package com.example.mini_projets_02;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mini_projets_02.models.Quote;

import org.json.JSONException;
import org.json.JSONObject;

public class StartActivity extends AppCompatActivity {

    TextView tv_startActQuote, tv_startActAuthor;
    Button btn_startActPass;
    ToggleButton tb_startActPinUnpin;
    SharedPreferences sharedPreferences;
    ImageView iv_startActIsFavorite;
    boolean isFavorite = false;
    FavoriteQuotesDbOpenHelper db;
    TextView tv_startActId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        tv_startActQuote = findViewById(R.id.tv_startActQuote);
        tv_startActAuthor = findViewById(R.id.tv_startActAuthor);
        btn_startActPass = findViewById(R.id.btn_startActPass);
        tb_startActPinUnpin = findViewById(R.id.tb_startActPinUnpin);
        iv_startActIsFavorite = findViewById(R.id.iv_startActIsFavorite);
        tv_startActId = findViewById(R.id.tv_startActId);

        //region Pin | Unpin Quote
        sharedPreferences = getSharedPreferences("pinned-quote", MODE_PRIVATE);
        String pinnedQuote = sharedPreferences.getString("quote", null);

        if (pinnedQuote == null) {
            getRandomQuote();
        } else {
            String author = sharedPreferences.getString("author", null);
            tv_startActQuote.setText(pinnedQuote);
            tv_startActAuthor.setText(author);
            tb_startActPinUnpin.setChecked(true);
        }

        tb_startActPinUnpin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
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
        //endregion

        db = new FavoriteQuotesDbOpenHelper(this);
        iv_startActIsFavorite.setOnClickListener(v -> {
            int id = Integer.parseInt(tv_startActId.getText().toString().substring(1));
            if (isFavorite) {
                iv_startActIsFavorite.setImageResource(R.drawable.dislike);
                db.deleteQuote(id);
            } else {
                iv_startActIsFavorite.setImageResource(R.drawable.like);
                String quote = tv_startActQuote.getText().toString();
                String author = tv_startActAuthor.getText().toString();
                db.saveQuote(new Quote(id, quote, author));
            }
            isFavorite = !isFavorite;

            for (Quote quote : db.getAll()) {
                Log.e("Sqlite", quote.toString());
            }

        });

        btn_startActPass.setOnClickListener(v -> {
            finish();
        });
    }

    private void getRandomQuote() {
        RequestQueue queue = Volley.newRequestQueue(StartActivity.this);
        String url = "https://dummyjson.com/quotes/random";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int id = response.getInt("id");
                    String quote = response.getString("quote");
                    String author = response.getString("author");

                    if (db.isFavorite(id)) {
                        iv_startActIsFavorite.setImageResource(R.drawable.like);
                    } else {
                        iv_startActIsFavorite.setImageResource(R.drawable.dislike);
                    }
                    tv_startActId.setText(String.format("#%d", id));
                    tv_startActQuote.setText(quote);
                    tv_startActAuthor.setText(author);
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