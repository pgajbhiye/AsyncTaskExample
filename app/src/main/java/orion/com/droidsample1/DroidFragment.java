package orion.com.droidsample1;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class DroidFragment extends Fragment {

    private static final String LOG_TAG = DroidFragment.class.getName();
    private final String url = "http://ip.jsontest.com/?callback=showMyIP";
    public static String PREF_KEY = "IP";
    public static final String PREF_NAME = "Droid";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        Log.d(LOG_TAG, "Init async task here");

        DroidAsyncTask asyncTask = new DroidAsyncTask((MainActivity) getActivity());
        asyncTask.execute(url);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    //Using non-static inner classes for long running operations is always a bad practice
    private static class DroidAsyncTask extends AsyncTask<String, String, String> {

        private WeakReference<MainActivity> activity;

        private DroidAsyncTask(MainActivity mainActivity) {
            activity = new WeakReference<>(mainActivity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String[] params) {
            String url = params[0];
            StringBuilder stringBuilder = new StringBuilder();
            try {
                String line;
                URL link = new URL(url);
                HttpURLConnection urlConnection = (HttpURLConnection) link.openConnection();
                urlConnection.setRequestMethod("GET");
                BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                while ((line = reader.readLine()) != null) {
                    //avoid using readline for large strings
                    //might cause OutOfMemoryError
                    stringBuilder.append(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

            return stringBuilder.toString();
        }

        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);
            //If activity reference is not garbage collected, do some operation
            if (activity.get() != null) {
                //do some operation
                SharedPreferences pref = activity.get().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                pref.edit().putString(PREF_KEY, o).apply();

                Toast.makeText(activity.get(), "got result " + o, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
