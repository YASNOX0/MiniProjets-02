package com.example.mini_projets_02;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.mini_projets_02.Adapters.FavoriteQuotesAdapter;

public class AllFavoriteQuotesActivity extends AppCompatActivity {
    RecyclerView rv_allFavQuoteActList;
    FavoriteQuotesDbOpenHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_favorite_quotes);

        rv_allFavQuoteActList = findViewById(R.id.rv_allFavQuoteActList);

        //region Persistence Object

        db = new FavoriteQuotesDbOpenHelper(this);

        //endregion

        FavoriteQuotesAdapter adapter = new FavoriteQuotesAdapter(db.getAll());

        rv_allFavQuoteActList.setAdapter(adapter);
        rv_allFavQuoteActList.setLayoutManager(new GridLayoutManager(this , 2));
    }
}