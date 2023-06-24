package com.example.mini_projets_02.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mini_projets_02.R;
import com.example.mini_projets_02.models.Quote;

import java.util.ArrayList;

public class FavoriteQuotesAdapter extends RecyclerView.Adapter<FavoriteQuotesAdapter.ViewHolder> {

    private ArrayList<Quote> quotes;

    public FavoriteQuotesAdapter(ArrayList<Quote> quotes) {
        this.quotes = quotes;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_FavQuoteItemQuote;
        TextView tv_FavQuoteItemAuthor;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_FavQuoteItemQuote = itemView.findViewById(R.id.tv_FavQuoteItemQuote);
            tv_FavQuoteItemAuthor = itemView.findViewById(R.id.tv_tv_FavQuoteItemAuthor);
        }
    }

    @NonNull
    @Override
    public FavoriteQuotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorite_quote, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteQuotesAdapter.ViewHolder holder, int position) {
        Quote quote = quotes.get(position);
        holder.tv_FavQuoteItemQuote.setText(quote.spannableQuote());
        holder.tv_FavQuoteItemAuthor.setText(quote.spannableAuthor());
    }

    @Override
    public int getItemCount() {
        return quotes.size();
    }

}
