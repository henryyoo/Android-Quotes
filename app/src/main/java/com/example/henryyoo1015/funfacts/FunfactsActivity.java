package com.example.henryyoo1015.funfacts;

import android.app.Activity;
import android.app.AlertDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class FunfactsActivity extends Activity {

    private ColorBook mColorBook = new ColorBook();
    private JSONObject quotesData;

    private String quotes= "";
    public static final String TAG = FunfactsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_funfacts);


        //create View variables and assign them using IDs
        final RelativeLayout background = (RelativeLayout) findViewById(R.id.display);
        final TextView funFact = (TextView) findViewById(R.id.quoteView);
        final Button nexButton = (Button) findViewById(R.id.showNextQuote);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetWorkAvailable()) {

                    GetQuotesTask getQuotesTask = new GetQuotesTask();
                    getQuotesTask.execute();

                }else {

                }

                int color = mColorBook.getColor();
                if(handleBlogResponse().length()>300){
                    funFact.setTextSize(12);
                    funFact.setText(handleBlogResponse());
                }else {
                    funFact.setTextSize(20);
                    funFact.setText(handleBlogResponse());
                }
                background.setBackgroundColor(color);
                nexButton.setTextColor(color);

            }
        };
        nexButton.setOnClickListener(listener);

        Log.d(TAG, "I am logging on debugging");


    }

    private boolean isNetWorkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean networkConnected = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            networkConnected = true;
        }
        return networkConnected;
    }
    private String handleBlogResponse() {

        if (quotesData == null){
            updateDisplayForError();

        }else{
            try {
                quotes = quotesData.getString("quote");

            }catch(JSONException e){
                Log.e(TAG, "error", e);
            }

        }
        return quotes;
    }
    private void updateDisplayForError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.error_title));
        builder.setMessage(getString(R.string.error_message));
        builder.setPositiveButton(android.R.string.ok, null);
        AlertDialog alert = builder.create();
        alert.show();
    }



    private class GetQuotesTask extends AsyncTask<Object, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(Object... arg0) {

            JSONObject jsonObject = null;
            try {
                URL quotesData = new URL("http://www.iheartquotes.com/api/v1/random?format=json");
                HttpURLConnection connection = (HttpURLConnection) quotesData.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();
                StringBuffer buffer = new StringBuffer();

                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }
                quotes = buffer.toString();
                jsonObject = new JSONObject(quotes);


            } catch (MalformedURLException e) {
                errorCatch(e);

            } catch (IOException e) {
                errorCatch(e);
            } catch (Exception e) {
                errorCatch(e);
            }
            return jsonObject;

        }

        private void errorCatch(Exception e) {
            Log.e(TAG, "Exception caught: ", e);
        }
        @Override
        protected void onPostExecute(JSONObject result){
            quotesData = result;
            handleBlogResponse();

        }





    }
}