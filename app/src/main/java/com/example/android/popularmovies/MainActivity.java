package com.example.android.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ArrayList moviesArray = new ArrayList();
    private List movieArrayForHighRated = new ArrayList();
    private final String POP_MOVIES_API_KEY = "029dd43d99fabe5bd5cdd5bc52356baf";
    private ImageAdapter mAdapter;
    private int most_popularChecked = 0;
    private int high_ratedChecked = 0;
    String moviesJsonStr;
    private String baseUrl = "http://api.themoviedb.org/3/movie/popular";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final GridView gridview = (GridView) findViewById(R.id.movies_gridview);
        mAdapter = new ImageAdapter(MainActivity.this, moviesArray);
        gridview.setAdapter(mAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override


            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                long itemId = mAdapter.getItemId(position);
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Connection connection = new Connection();
                try {
                    String[] movieTitle = connection.getMovieTitleFromJson(moviesJsonStr);
                    String[] movieOverview = connection.getplotsynopsisFromJson(moviesJsonStr);
                    String[] releaseDate = connection.getreleaseDateFromJson(moviesJsonStr);
                    String[] posterStrings = connection.getPosterDataFromJson(moviesJsonStr);
                    Double[] voteAvg = connection.getVoteAverageFromJson(moviesJsonStr);
                    int count = 0;
                    while (count != itemId) {
                        count++;
                    }
                    intent.putExtra("movie", new Movie(movieTitle[count], "http://image.tmdb.org/t/p/" + "w185/" + posterStrings[count], releaseDate[count], voteAvg[count], movieOverview[count]));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
            }
        });
        if (moviesArray.isEmpty() && high_ratedChecked == 0 && most_popularChecked == 0) {
            Connection connection = new Connection();
            baseUrl = "http://api.themoviedb.org/3/movie/popular";
            connection.execute();
            moviesArray.clear();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.most_popular:
                Connection popularmovies = new Connection();
                item.setChecked(true);
                baseUrl = "http://api.themoviedb.org/3/movie/popular";
                moviesArray.clear();
                most_popularChecked = 1;
                popularmovies.execute();
                return true;
            case R.id.high_rating:
                Connection highratedmovies = new Connection();
                item.setChecked(true);
                baseUrl = "http://api.themoviedb.org/3/movie/top_rated";
                moviesArray.clear();
                high_ratedChecked = 1;
                highratedmovies.execute();
                return true;
            default:
                baseUrl = "http://api.themoviedb.org/3/movie/most_popular";
                moviesArray.clear();
        }
        return super.onOptionsItemSelected(item);
    }


    public class Connection extends AsyncTask<Void, Void, String[]> {

        private final String LOG_TAG = Connection.class.getSimpleName();

        private String[] getPosterDataFromJson(String moviesJsonStr) throws JSONException {
            String[] posters = new String[moviesJsonStr.length()];
            final String RESULTS = "results";
            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArrayForJson = moviesJson.getJSONArray(RESULTS);

            for (int i = 0; i < moviesArrayForJson.length(); i++) {
                String image;
                JSONObject poster = moviesArrayForJson.getJSONObject(i);
                image = poster.getString("poster_path");
                posters[i] = image;
            }
            return posters;
        }

        private String[] getMovieTitleFromJson(String moviesJsonStr) throws JSONException {
            String[] titles = new String[moviesJsonStr.length()];
            final String RESULTS = "results";
            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArrayForJson = moviesJson.getJSONArray(RESULTS);

            for (int i = 0; i < moviesArrayForJson.length(); i++) {
                String movietitle;
                JSONObject title = moviesArrayForJson.getJSONObject(i);
                movietitle = title.getString("title");
                titles[i] = movietitle;
            }
            return titles;
        }

        private Double[] getVoteAverageFromJson(String moviesJsonStr) throws JSONException {

            Double[] votes = new Double[moviesJsonStr.length()];
            final String RESULTS = "results";
            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArrayForJson = moviesJson.getJSONArray(RESULTS);

            for (int i = 0; i < moviesArrayForJson.length(); i++) {
                Double voteAverages;
                JSONObject voteAvg = moviesArrayForJson.getJSONObject(i);
                voteAverages = voteAvg.getDouble("vote_average");
                votes[i] = voteAverages;
            }
            return votes;
        }

        private String[] getplotsynopsisFromJson(String moviesJsonStr) throws JSONException {

            String[] synopsis = new String[moviesJsonStr.length()];
            final String RESULTS = "results";
            Log.v("MainAcity", "getPosterDataFromJson" + moviesJsonStr);
            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArrayForJson = moviesJson.getJSONArray(RESULTS);

            for (int i = 0; i < moviesArrayForJson.length(); i++) {
                String movieoverview;
                JSONObject overview = moviesArrayForJson.getJSONObject(i);
                movieoverview = overview.getString("overview");
                synopsis[i] = movieoverview;
            }
            return synopsis;
        }

        private String[] getreleaseDateFromJson(String moviesJsonStr) throws JSONException {

            String[] dates = new String[moviesJsonStr.length()];
            final String RESULTS = "results";
            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArrayForJson = moviesJson.getJSONArray(RESULTS);

            for (int i = 0; i < moviesArrayForJson.length(); i++) {
                String moviedate;
                JSONObject releaseDate = moviesArrayForJson.getJSONObject(i);
                moviedate = releaseDate.getString("release_date");
                dates[i] = moviedate;
            }
            return dates;
        }

        @Override
        protected String[] doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {

                String apiKey = "?api_key=" + POP_MOVIES_API_KEY;
                if (most_popularChecked == 1) {
                    baseUrl = "http://api.themoviedb.org/3/movie/popular";
                    most_popularChecked = 0;
                } else if (high_ratedChecked == 1) {
                    baseUrl = "http://api.themoviedb.org/3/movie/top_rated";
                    high_ratedChecked = 0;
                }
                URL url = new URL(baseUrl.concat(apiKey));
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();

                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }
                moviesJsonStr = buffer.toString();
                String[] data = getPosterDataFromJson(moviesJsonStr);
                String[] title = getMovieTitleFromJson(moviesJsonStr);

                for (int i = 0; i < data.length; i++) {
                    while (data[i] != null) {
                        Log.v(LOG_TAG, data[i]);
                        break;
                    }
                }
                for (int i = 0; i < title.length; i++) {
                    while (title[i] != null) {
                        Log.v(LOG_TAG, title[i]);
                        break;
                    }
                }
                return data;
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                moviesArray.clear();
                for (String posterStrings : result) {
                    while (posterStrings != null) {
                        String url = "http://image.tmdb.org/t/p/" + "w185/" + posterStrings;
                        Log.v(LOG_TAG, url);
                        moviesArray.add(url);
                        break;
                    }
                }
                mAdapter.updateMovies(moviesArray);
            }
        }
    }
}
