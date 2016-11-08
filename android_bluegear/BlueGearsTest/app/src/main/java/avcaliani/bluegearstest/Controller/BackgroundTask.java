package avcaliani.bluegearstest.Controller;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class BackgroundTask extends AsyncTask<Void, Void, String>{
    protected String jsonUrl;
    protected String JSON_STRING;

    private TaskCompleted mCallback;
    private Context mContext;

    public BackgroundTask (Context context, String url){
        mContext = context;
        mCallback = (TaskCompleted) context;
        jsonUrl = url;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    } // End-Function

    @Override
    protected String doInBackground(Void... voids) {
        try{
            URL url = new URL(jsonUrl);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            InputStream inputStream = http.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();

            while ((JSON_STRING = bufferedReader.readLine()) != null){
                stringBuilder.append(JSON_STRING);
            }

            bufferedReader.close();
            inputStream.close();
            http.disconnect();

            return stringBuilder.toString().trim();
        } catch (MalformedURLException urlError){
            urlError.printStackTrace();
        } catch (IOException ioError){
            ioError.printStackTrace();
        } catch (Exception error){
            error.printStackTrace();
        }

        return null;
    } // End-Function

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    } // End-Function

    @Override
    protected void onPostExecute(String result) {
        mCallback.onTaskComplete(result);
    } // End-Function
} // End-SubClass