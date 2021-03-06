package ehi1vsc.saxion.twitterapp.WebAsynctasks;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ehi1vsc.saxion.twitterapp.Model.Model;
import ehi1vsc.saxion.twitterapp.Model.Adapters.TweetAdapter;
import ehi1vsc.saxion.twitterapp.Model.Ref;
import ehi1vsc.saxion.twitterapp.Model.Tweet;

/**
 * Created by Gijs on 6-6-2016.
 */
public class SearchTweets extends AsyncTask<Object, Double, Object> {

    String response;

    @Override
    protected Object doInBackground(Object... params) {
        try {

            // prepare request
            URL url = new URL("https://api.twitter.com/1.1/search/tweets.json?q=" + params[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // set header
            conn.addRequestProperty("Authorization", "Bearer " + Ref.bearertoken);
            String test = Ref.bearertoken;
            Log.d("responseCode: ", conn.getResponseCode() + "");

            // Set body
//            conn.setDoOutput(true);
//            byte[] body = "grant_type=client_credentials".getBytes("UTF-8");
//            conn.setFixedLengthStreamingMode(body.length);
//            BufferedOutputStream os = new BufferedOutputStream(conn.getOutputStream());
//            os.write(body);
//            os.close();

            // get tweets
            InputStream inputStream = conn.getInputStream();
            response = IOUtils.toString(inputStream, "UTF-8");
            IOUtils.closeQuietly(inputStream);

            return params[1];

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(Object o) {
        try {

            JSONObject jsonObject = new JSONObject(response);
            Model.getInstance().getTweets().clear();
            for (int x = 0; jsonObject.getJSONArray("statuses").length() > x; x++) {
                Model.getInstance().getTweets().add(new Tweet(jsonObject.getJSONArray("statuses").getJSONObject(x)));
            }

            ((TweetAdapter)o).notifyDataSetChanged();


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
