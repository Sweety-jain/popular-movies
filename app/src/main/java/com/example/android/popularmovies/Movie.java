package com.example.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sweety on 1/22/2018.
 */

public class Movie implements Parcelable {

    private String movieTitle;
    private String imageUrl;
    private String releaseDate;
    private Double voteAverage;
    private String plotSynopsis;

    public Movie(String mTitle, String iUrl, String rDate, Double vAverage, String pSynopsis) {
        this.movieTitle = mTitle;
        this.imageUrl = iUrl;
        this.releaseDate = rDate;
        this.voteAverage = vAverage;
        this.plotSynopsis = pSynopsis;
    }

    protected Movie(Parcel in) {
        this.movieTitle = in.readString();
        this.imageUrl = in.readString();
        this.releaseDate = in.readString();
        this.voteAverage = in.readDouble();
        this.plotSynopsis = in.readString();
    }

    public String getTitle() {
        return movieTitle;
    }

    public String getOverview() {
        return plotSynopsis;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(movieTitle);
        parcel.writeString(imageUrl);
        parcel.writeString(releaseDate);
        parcel.writeDouble(voteAverage);
        parcel.writeString(plotSynopsis);
    }
}
