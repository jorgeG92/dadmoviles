package eps.android4_jorgegomez.activities;

import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;

import eps.android4_jorgegomez.R;

public class ERPreferenceActivity extends AppCompatActivity {

    public final static String BOARDSIZE_KEY = "boardsize";
    public final static String BOARDSIZE_DEFAULT = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        FragmentManager fm = getSupportFragmentManager();
        ERPreferenceFragment fragment = new ERPreferenceFragment();
        fm.beginTransaction().replace(android.R.id.content, fragment).commit();
    }

    public static String getBoardSize(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(BOARDSIZE_KEY, BOARDSIZE_DEFAULT);
    }

    public static void setBoardSize(Context context, int size){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(ERPreferenceActivity.BOARDSIZE_KEY, size);
        editor.commit();
    }

}
