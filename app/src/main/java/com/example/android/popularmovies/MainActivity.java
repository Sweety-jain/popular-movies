package com.example.android.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
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
    private List moviesArray= new ArrayList();
   private List movieArrayForHighRated = new ArrayList();
    private final String POP_MOVIES_API_KEY = "029dd43d99fabe5bd5cdd5bc52356baf";
    private ImageAdapter mAdapter;
    private int most_popularChecked=0;
    private int high_ratedChecked=0;
    String moviesJsonStr;
    private String baseUrl = "http://api.themoviedb.org/3/movie/popular";
 //   private String baseUrlForHighRated = "http://api.themoviedb.org/3/movie/top_rated";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final GridView gridview = (GridView) findViewById(R.id.movies_gridview);
//String[] movieTitle;

        // gridview.setAdapter(new ImageAdapter(this));
       // gridview.setAdapter(new ImageAdapter(MainActivity.this, moviesArray));
        mAdapter = new  ImageAdapter(MainActivity.this, moviesArray);
        gridview.setAdapter(mAdapter);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override


            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
              // ImageView imageView= (ImageView) mAdapter.getItem(position);
long itemId=mAdapter.getItemId(position);

Log.v("MainActivity","itemid:"+itemId);

                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                Connection connection = new Connection();
                try {
                    String[] movieTitle= connection.getMovieTitleFromJson(moviesJsonStr);
                    String[] movieOverview = connection.getplotsynopsisFromJson(moviesJsonStr);
                    String [] releaseDate = connection.getreleaseDateFromJson(moviesJsonStr);
                    String[] posterStrings = connection.getPosterDataFromJson(moviesJsonStr);
                    Double[] voteAvg = connection.getVoteAverageFromJson(moviesJsonStr) ;
                   // String[] mainUrl; = ;
                    String date = null;
                  //  while (i != itemId)
                    int count = 0;
                    while(count != itemId)
                    {
                        count++;
                    }
                    intent.putExtra("movie", new Movie(movieTitle[count],"http://image.tmdb.org/t/p/" + "w185/" + posterStrings[count],releaseDate[count],voteAvg[count],movieOverview[count]));
                  //  intent.putExtra(Intent.EXTRA_TITLE,movieTitle[count]);
                    //intent.putExtra(Intent.EXTRA_TEXT,movieOverview[count]);
                    //intent.putExtra(date,releaseDate[count]);

                  //  Log.v("MainA","movieTitlesingridview" + )
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(intent);
             //   Toast.makeText(MainActivity.this, "KJKJK" + position,
               //         Toast.LENGTH_SHORT).show();
            }
        });
        if(moviesArray.isEmpty() && high_ratedChecked == 0 && most_popularChecked == 0) {
            Connection connection = new Connection();
             baseUrl = "http://api.themoviedb.org/3/movie/popular";
            Log.v("MainActivity.this", "before connection.execute");
            connection.execute();
            moviesArray.clear();


        }
    //    else
      //      mAdapter.updateMovies(moviesArray);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu,menu);
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


                Log.v("MainActivity.this","switch case most_popular"+most_popularChecked);
                Toast.makeText(this, "You have selected most popular Menu", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.high_rating:
                Connection highratedmovies = new Connection();
                item.setChecked(true);
                baseUrl = "http://api.themoviedb.org/3/movie/top_rated";
                moviesArray.clear();
                               high_ratedChecked = 1;

                highratedmovies.execute();

                Log.v("MainActivity.this","switch case high rated"+high_ratedChecked);

                Toast.makeText(this, "You have selected high rating Menu", Toast.LENGTH_SHORT).show();
                return true;
            default:
                baseUrl= "http://api.themoviedb.org/3/movie/most_popular";
                moviesArray.clear();

        }
        return super.onOptionsItemSelected(item);
    }



    public class Connection extends AsyncTask<Void  , Void, String[]> {

        private final String LOG_TAG = Connection.class.getSimpleName();
      //  private ImageAdapter mAdapter;

        private String[] getPosterDataFromJson(String moviesJsonStr) throws JSONException {

                String[] posters = new String[moviesJsonStr.length()];
              //  String[] urls = new String[moviesJsonStr.length()];
                // These are the names of the JSON objects that need to be extracted
                final String RESULTS = "results";
                Log.v("MainAcity","getPosterDataFromJson"+ moviesJsonStr);
//
                // convertimos el JSON en un array
                JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArrayForJson = moviesJson.getJSONArray(RESULTS);

            for(int i = 0; i < moviesArrayForJson.length(); i++) {

                String image;

                JSONObject poster = moviesArrayForJson.getJSONObject(i);
                image = poster.getString("poster_path");
                posters[i] = image;

            }
            Log.v("MainAcity","posters"+ posters);
            return posters;
        }
        private String[] getMovieTitleFromJson(String moviesJsonStr) throws JSONException {

            String[] titles = new String[moviesJsonStr.length()];
            //  String[] urls = new String[moviesJsonStr.length()];
            // These are the names of the JSON objects that need to be extracted
            final String RESULTS = "results";
            Log.v("MainAcity","getPosterDataFromJson"+ moviesJsonStr);
//
            // convertimos el JSON en un array
            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArrayForJson = moviesJson.getJSONArray(RESULTS);

            for(int i = 0; i < moviesArrayForJson.length(); i++) {

                String movietitle;

                JSONObject title = moviesArrayForJson.getJSONObject(i);
                movietitle = title.getString("title");
                titles[i] = movietitle;

            }
            Log.v("MainAcity","titles"+ titles);
            return titles;
        }
        private Double[] getVoteAverageFromJson(String moviesJsonStr) throws JSONException {

            Double[] votes = new Double[moviesJsonStr.length()];
            //  String[] urls = new String[moviesJsonStr.length()];
            // These are the names of the JSON objects that need to be extracted
            final String RESULTS = "results";
            Log.v("MainAcity","getPosterDataFromJson"+ moviesJsonStr);
//
            // convertimos el JSON en un array
            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArrayForJson = moviesJson.getJSONArray(RESULTS);

            for(int i = 0; i < moviesArrayForJson.length(); i++) {

                Double voteAverages;

                JSONObject voteAvg = moviesArrayForJson.getJSONObject(i);
                voteAverages = voteAvg.getDouble("vote_average");
                votes[i] = voteAverages;

            }
            Log.v("MainAcity","votes"+ votes);
            return votes;
        }
        private String[] getplotsynopsisFromJson(String moviesJsonStr) throws JSONException {

            String[] synopsis = new String[moviesJsonStr.length()];
            //  String[] urls = new String[moviesJsonStr.length()];
            // These are the names of the JSON objects that need to be extracted
            final String RESULTS = "results";
            Log.v("MainAcity","getPosterDataFromJson"+ moviesJsonStr);
//
            // convertimos el JSON en un array
            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArrayForJson = moviesJson.getJSONArray(RESULTS);

            for(int i = 0; i < moviesArrayForJson.length(); i++) {

                String movieoverview;

                JSONObject overview = moviesArrayForJson.getJSONObject(i);
                movieoverview = overview.getString("overview");
                synopsis[i] = movieoverview;

            }
            Log.v("MainAcity","synopsis"+ synopsis);
            return synopsis;
        }

        private String[] getreleaseDateFromJson(String moviesJsonStr) throws JSONException {

            String[] dates = new String[moviesJsonStr.length()];
            //  String[] urls = new String[moviesJsonStr.length()];
            // These are the names of the JSON objects that need to be extracted
            final String RESULTS = "results";
            Log.v("MainAcity","getPosterDataFromJson"+ moviesJsonStr);
//
            // convertimos el JSON en un array
            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArrayForJson = moviesJson.getJSONArray(RESULTS);

            for(int i = 0; i < moviesArrayForJson.length(); i++) {

                String moviedate;

                JSONObject releaseDate = moviesArrayForJson.getJSONObject(i);
                moviedate = releaseDate.getString("release_date");
                dates[i] = moviedate;

            }
            Log.v("MainAcity","synopsis"+ dates);
            return dates;
        }
      /*  private String[] movieDetails(String moviesJsonStr) throws JSONException{
            final String RESULTS = "results";
            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArrayForJson = moviesJson.getJSONArray(RESULTS);
            for(int i = 0; i < moviesArrayForJson.length(); i++) {
                String title;
                JSONObject titleJson = moviesArrayForJson.getJSONObject(i);
                title = titleJson.getString("title");
                Log.v("MainActivity.this","title:"+title);
                return title;
            }
return null;
        }*/


        @Override
        protected String[] doInBackground(Void... params) {
            Log.e("MainActivity.this","doInBackground");
            HttpURLConnection urlConnection = null;
            //HttpURLConnection urlConnection2 = null;
            BufferedReader reader = null;
            //BufferedReader reader2 = null;

            // Will contain the raw JSON response as a string.

            //String movieJsonStr2;
          //  String baseUrl;
            //Menu settings = (Menu)findViewById(R.id.settingsMenuId);
          //  if()
            try {

                String apiKey = "?api_key=" + POP_MOVIES_API_KEY;

               // baseUrl = "http://api.themoviedb.org/3/movie/popular";
                if(most_popularChecked == 1) {
                    Log.v("MainActivity ", "doinbackground most_popularChecked"+baseUrl);
                    baseUrl = "http://api.themoviedb.org/3/movie/popular";
                    most_popularChecked = 0;
                }
                else if(high_ratedChecked == 1) {
                    Log.v("MainActivity ", "doinbackground high_ratedChecked"+baseUrl);
                    baseUrl = "http://api.themoviedb.org/3/movie/top_rated";
                    high_ratedChecked = 0;
                }
                URL url = new URL(baseUrl.concat(apiKey));
              //  URL url2 = new URL(baseUrlForHighRated.concat(apiKey));

                // Create the request and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                //urlConnection2 = (HttpURLConnection) url2.openConnection();
                urlConnection.setRequestMethod("GET");
                //urlConnection2.setRequestMethod("GET");
                urlConnection.connect();
                //urlConnection2.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                //InputStream inputStream2 = urlConnection2.getInputStream();
                StringBuffer buffer = new StringBuffer();
                //StringBuffer buffer2 = new StringBuffer();

                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                //if(inputStream2 == null){
                  //  return null;
                //}
                reader = new BufferedReader(new InputStreamReader(inputStream));
                //reader2 = new BufferedReader(new InputStreamReader(inputStream2));

                String line;

                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }
                //while ((line2 = reader2.readLine() ) != null){
                  //  buffer2.append(line2 + "\n");
                //}

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                //if (buffer2.length() == 0) {
                    // Stream was empty.  No point in parsing.
                  //  return null;
                //}


                moviesJsonStr = buffer.toString();
                //movieJsonStr2 = buffer2.toString();


                String[] data = getPosterDataFromJson(moviesJsonStr);
               String[] title = getMovieTitleFromJson(moviesJsonStr);

                // Verificamos que haya algo en el vector

                //Log.v(LOG_TAG, "Estoy en CONNECTION y el tamaño de DATOS es: " + Integer.toString(datos.length));
                for (int i=0; i<data.length; i++){
                    while(data[i] != null){
                        Log.v(LOG_TAG, data[i]);
                     //   Log.v("MainActivity.this","data"+data[i]);
                        break;
                    }

                }
                for (int i=0; i<title.length; i++){
                    while(title[i] != null){
                        Log.v(LOG_TAG, title[i]);
                        //   Log.v("MainActivity.this","data"+data[i]);
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
        protected void onPostExecute(String[] result ) {
            if (result != null) {
                moviesArray.clear();
                for(String posterStrings : result) {
                    while(posterStrings != null){
                        String direccion = "http://image.tmdb.org/t/p/" + "w185/" + posterStrings;
                        Log.v(LOG_TAG, direccion);
                        Log.v("MainActivity","onPostExeceute");
                        moviesArray.add(direccion);
                        break;
                    }
                }
                mAdapter.updateMovies(moviesArray);
              //  moviesArray.clear();
            }
        }
    }
}
/*
public class MainActivity extends AppCompatActivity {

    // the array to populate with data from the API
    private List moviesArray= new ArrayList();
    private ImageAdapter mAdapter;
    private final String POP_MOVIES_API_KEY = "029dd43d99fabe5bd5cdd5bc52356baf";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gridView = (GridView) findViewById(R.id.movies_gridview);
     //   mAdapter = new CustomAdapter(this.getContext(), moviesArray);
       // gridView.setAdapter(mAdapter);
       // gridView.setAdapter(new ImageAdapter(this.getContext(), moviesArray));
        mAdapter = new  ImageAdapter(MainActivity.this, moviesArray);
        gridView.setAdapter(mAdapter);
      //  gridView.setAdapter(new ImageAdapter(MainActivity.this, moviesArray));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override


            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(MainActivity.this, "KJKJK" + position,
                        Toast.LENGTH_SHORT).show();
            }
        });

        Connection connection = new Connection();
        connection.execute();
    }



    public class Connection extends AsyncTask<Void, Void, String[]> {

        private final String LOG_TAG = Connection.class.getSimpleName();

        private String[] getPosterDataFromJson(String moviesJsonStr) throws JSONException {

            String[] posters = new String[moviesJsonStr.length()];
            // These are the names of the JSON objects that need to be extracted
            final String RESULTS = "results";
//
            // convertimos el JSON en un array
            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray moviesArray = moviesJson.getJSONArray(RESULTS);

            for(int i = 0; i < moviesArray.length(); i++) {

                String image;

                JSONObject poster = moviesArray.getJSONObject(i);
                image = poster.getString("poster_path");
                posters[i] = image;

            }
            return posters;
        }

        @Override
        protected String[] doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr;

            try {
                // Construct the URL
                String baseUrl = "http://api.themoviedb.org/3/movie/popular";
                String apiKey = "?api_key=" + POP_MOVIES_API_KEY;
                URL url = new URL(baseUrl.concat(apiKey));

                // Create the request and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                // Paso todo el JSON a una variable
                moviesJsonStr = buffer.toString();

                // Extraigo del JSON lo que me interesa y lo guardo en un vector
                String[] datos = getPosterDataFromJson(moviesJsonStr);

                // Verificamos que haya algo en el vector

                //Log.v(LOG_TAG, "Estoy en CONNECTION y el tamaño de DATOS es: " + Integer.toString(datos.length));
                for (int i=0; i<datos.length; i++){
                    while(datos[i] != null){
                        Log.v(LOG_TAG, datos[i]);
                        break;
                    }

                }

                return datos;
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
                for(String posterStrings : result) {
                    while(posterStrings != null){
                        String direccion = "http://image.tmdb.org/t/p/" + "w185/" + posterStrings;
                        Log.v(LOG_TAG, direccion);
                        moviesArray.add(direccion);
                        break;
                    }
                }
                mAdapter.updateMovies(moviesArray);
            }
        }
    }
}
*/