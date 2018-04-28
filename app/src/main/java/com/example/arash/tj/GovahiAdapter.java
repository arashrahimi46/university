package com.example.arash.tj;

import android.content.Context;
import android.graphics.Movie;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ARASH on 2/21/2018.
 */

public class GovahiAdapter  extends RecyclerView.Adapter<GovahiAdapter.MyViewHolder>{
    private List<Govahi> moviesList;


    private Context _context;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, year, genre , status;

        Typeface custom_font = Typeface.createFromAsset(itemView.getContext().getAssets(), "fonts/IRANSansMobile.ttf");



        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.textView4);
            status =(TextView)view.findViewById(R.id.textView);

            title.setTypeface(custom_font);
            status.setTypeface(custom_font);
        }
    }


    public GovahiAdapter(List<Govahi> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyle_govahi_row, parent, false);

        return new MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Govahi movie = moviesList.get(position);

        holder.title.setText(movie.getTitle());
        holder.status.setText(movie.getStatus());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }
}
