package eps.android4_jorgegomez.activities;

import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;

import eps.android4_jorgegomez.R;

public class ConectPreferenceActivity extends AppCompatActivity {

    public final static String BOARDSIZE_KEY = "boardsize";
    public final static String BOARDSIZE_DEFAULT = "0";
    public final static String PLAYERNAME_KEY = "playername";
    public final static String PLAYER_PASS = "password";
    public final static String PLAYERNAME_DEFAULT = "default";
    public final static String PLAYERID_KEY = "player_id";
    public final static String PLAYERID_DEFAULT = "0000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        ConectPreferenceFragment fragment = new ConectPreferenceFragment();
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
        fm.beginTransaction().replace(R.id.fragment_container, fragment).commit();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

    }

    public static String getBoardSize(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(BOARDSIZE_KEY, BOARDSIZE_DEFAULT);
    }

    public static void setBoardSize(Context context, int size){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(ConectPreferenceActivity.BOARDSIZE_KEY, size);
        editor.commit();
    }

    public static String getPlayerName(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PLAYERNAME_KEY, PLAYERNAME_DEFAULT);
    }

    public static void setPlayerUUID(Context context, String id){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(ConectPreferenceActivity.PLAYERID_KEY, id);
        editor.commit();
    }

    public static String getPlayerUUID(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PLAYERID_KEY, PLAYERID_DEFAULT);
    }

    public static void setPlayerName(Context context, String pass){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(ConectPreferenceActivity.PLAYER_PASS, pass);
        editor.commit();
    }

    public static void setPlayerPassword(Context context, String pass){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(ConectPreferenceActivity.PLAYER_PASS, pass);
        editor.commit();
    }

}
