package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        String msg = null;
        Intent myIntent = getIntent();
        Bundle data = getIntent().getExtras();
        Movie movie = (Movie) data.getParcelable("movie");
        String title = movie.getTitle();
        String overview = movie.getOverview();
        String releaseDate = movie.getReleaseDate();
        Double voteAvg = movie.getVoteAverage();
        String vote = voteAvg.toString();
        String imageURL = movie.getImageUrl();
        TextView tv = (TextView)findViewById(R.id.titleTV);
        TextView overviewTV = (TextView)findViewById(R.id.synopsisTV);
        TextView releaseDateTV = (TextView)findViewById(R.id.releasedateTV);
        TextView voteTextView = (TextView)findViewById(R.id.voteTV);
        ImageView image = (ImageView)findViewById(R.id.imageView);

        tv.setText(title);
        overviewTV.setText(overview);
        releaseDateTV.setText(releaseDate);
        voteTextView.setText(vote);
        Picasso.with(this)
                .load(imageURL)
                .into(image);

    }
}
