package ehi1vsc.saxion.twitterapp.WebAsynctasks;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Base64;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import ehi1vsc.saxion.twitterapp.Model.Ref;

/**
 * Created by Gijs on 31-5-2016.
 */
public class BearerToken extends AsyncTask<Context, Void, Void>{

    @Override
    protected Void doInBackground(Context ... params) {
        try {

            // Prepare request
            URL url = new URL("https://api.twitter.com/oauth2/token");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            // Encode API key and secret
            String authString = URLEncoder.encode(Ref.API_KEY, Ref.CHARSET_UTF_8) + ":" + URLEncoder.encode(Ref.API_SECRET, Ref.CHARSET_UTF_8);

            // Apply Base64 encoding on the encoded string
            String authStringBase64 = Base64.encodeToString(authString.getBytes(Ref.CHARSET_UTF_8), Base64.NO_WRAP);

            // Set headers
            conn.setRequestProperty("Authorization", "Basic " + authStringBase64);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            // Set body
            conn.setDoOutput(true);
            byte[] body = "grant_type=client_credentials".getBytes("UTF-8");
            conn.setFixedLengthStreamingMode(body.length);
            BufferedOutputStream os = new BufferedOutputStream(conn.getOutputStream());
            os.write(body);
            os.close();

            // get bearertoken
            InputStream inputStream = conn.getInputStream();
            String response = IOUtils.toString(inputStream, "UTF-8");
            Ref.bearertoken = (String) new JSONObject(response).get("access_token");
            IOUtils.closeQuietly(inputStream);

            // save bearertoken for later
            SharedPreferences.Editor editor = params[0].getSharedPreferences("login", Context.MODE_PRIVATE).edit();
            editor.putString("bearer", Ref.bearertoken);
            editor.apply();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException pe) {
            pe.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            return null;
        }
    }

}
