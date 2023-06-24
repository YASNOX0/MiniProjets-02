package com.example.mini_projets_02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mini_projets_02.Adapters.FavoriteQuotesAdapter;

public class AllFavoriteQuotesActivity extends AppCompatActivity {
    RecyclerView rv_allFavQuoteActList;
    FavoriteQuotesDbOpenHelper db;
    TextView tv_allFavQuoteActChooseLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_favorite_quotes);

        rv_allFavQuoteActList = findViewById(R.id.rv_allFavQuoteActList);
        tv_allFavQuoteActChooseLayout = findViewById(R.id.tv_allFavQuoteActChooseLayout);

        //region Persistence Object

        db = new FavoriteQuotesDbOpenHelper(this);

        //endregion

        FavoriteQuotesAdapter adapter = new FavoriteQuotesAdapter(db.getAll());
        rv_allFavQuoteActList.setLayoutManager(new GridLayoutManager(this, 2));
        registerForContextMenu(tv_allFavQuoteActChooseLayout);
        rv_allFavQuoteActList.setAdapter(adapter);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Layout type");
        menu.add(0, v.getId(), 0, "List");
        menu.add(0, v.getId(), 0, "Grid");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle() == "List") {
            rv_allFavQuoteActList.setLayoutManager(new LinearLayoutManager(this));
        } else {
            rv_allFavQuoteActList.setLayoutManager(new GridLayoutManager(this, 2));
        }
        return true;
    }
}