package com.example.mini_projets_02;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
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

    private final static int INVALID_ID = -1;
    TextView tv_startActQuote, tv_startActAuthor;
    Button btn_startActShowAllFavQuotes;
    ToggleButton tb_startActPinUnpin;
    SharedPreferences sharedPreferences;
    ImageView iv_startActIsFavorite;
    FavoriteQuotesDbOpenHelper db;
    TextView tv_startActId;
    ImageView iv_startActBgColors;
    private String[] bgColorsNames;
    private String[] bgColorsCodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        tv_startActQuote = findViewById(R.id.tv_startActQuote);
        tv_startActAuthor = findViewById(R.id.tv_startActAuthor);
        btn_startActShowAllFavQuotes = findViewById(R.id.btn_startActShowAllFavQuotes);
        tb_startActPinUnpin = findViewById(R.id.tb_startActPinUnpin);
        iv_startActIsFavorite = findViewById(R.id.iv_startActIsFavorite);
        tv_startActId = findViewById(R.id.tv_startActId);
        iv_startActBgColors = findViewById(R.id.iv_startActBgColors);

        //region Pin | Unpin Quote
        sharedPreferences = getSharedPreferences("pinned-quote", MODE_PRIVATE);
        String pinnedQuote = sharedPreferences.getString("quote", null);
        db = new FavoriteQuotesDbOpenHelper(this);
        int id = sharedPreferences.getInt("id", INVALID_ID);

        if (id == INVALID_ID) {
            getRandomQuote();
        } else {
            String author = sharedPreferences.getString("author", null);
            tv_startActQuote.setText(pinnedQuote);
            tv_startActAuthor.setText(author);
            tv_startActId.setText(String.format("#%d", id));
            iv_startActIsFavorite.setImageResource(db.isFavorite(id) ? R.drawable.like : R.drawable.dislike);
            tb_startActPinUnpin.setChecked(true);
        }

        tb_startActPinUnpin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                int pinnedQuoteId = INVALID_ID;
                String quote = null;
                String author = null;

                if (isChecked) {
                    quote = tv_startActQuote.getText().toString();
                    author = tv_startActAuthor.getText().toString();
                    pinnedQuoteId = Integer.parseInt(tv_startActId.getText().toString().substring(1));
                    if (!db.isFavorite(pinnedQuoteId)) {
                        iv_startActIsFavorite.setImageResource(R.drawable.like);
                        db.saveQuote(new Quote(pinnedQuoteId, quote, author));
                    }
                } else {
                    getRandomQuote();
                }
                editor.putString("quote", quote);
                editor.putString("author", author);
                editor.putInt("id", pinnedQuoteId);

                editor.commit();
            }
        });
        //endregion

        //region Like | Dislike Quote
        iv_startActIsFavorite.setOnClickListener(v -> {
            int i = Integer.parseInt(tv_startActId.getText().toString().substring(1));
            if (db.isFavorite(i)) {
                tb_startActPinUnpin.setChecked(false);
                iv_startActIsFavorite.setImageResource(R.drawable.dislike);
                db.deleteQuote(i);
            } else {
                iv_startActIsFavorite.setImageResource(R.drawable.like);
                String quote = tv_startActQuote.getText().toString();
                String author = tv_startActAuthor.getText().toString();
                db.saveQuote(new Quote(i, quote, author));
            }

        });
        //endregion

        //region Handle btn_startActShowAllFavQuotes
        btn_startActShowAllFavQuotes.setOnClickListener(v -> {
            Intent intent = new Intent(this, AllFavoriteQuotesActivity.class);
            startActivity(intent);
        });
        //endregion

        bgColorsNames = getResources().getStringArray(R.array.bgColorsNames);
        bgColorsCodes = getResources().getStringArray(R.array.bgColorsCodes);

        registerForContextMenu(iv_startActBgColors);

        iv_startActBgColors.setOnClickListener(view -> {
            openContextMenu(view);
        });

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        for (String bgColor : bgColorsNames) {
            menu.add(0, v.getId(), 0, bgColor);
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        for (int i = 0; i < bgColorsNames.length; i++) {
            if (item.getTitle().equals(bgColorsNames[i])) {
                getWindow().getDecorView().setBackgroundColor(Color.parseColor(bgColorsCodes[i]));
            }
        }

        return true;
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

                    iv_startActIsFavorite.setImageResource(db.isFavorite(id) ? R.drawable.like : R.drawable.dislike);

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