package orion.com.droidsample1;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName();
    private final String droidFragTag = "DroidFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager manager = getSupportFragmentManager();

        DroidFragment fragment = (DroidFragment) manager.findFragmentByTag(droidFragTag);
        if (fragment == null) {
            Log.d(LOG_TAG, "Fragment was null.. create new transaction");
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(new DroidFragment(), droidFragTag);
            transaction.commit();
        } else {
            //show downloaded result from asynctask
            Log.d(LOG_TAG, "Show saved result ");
            String result = getSharedPreferences(DroidFragment.PREF_NAME, MODE_PRIVATE).getString(DroidFragment.PREF_KEY, "");
            Toast.makeText(this, "Saved Result " + result, Toast.LENGTH_SHORT).show();
        }
    }
}
