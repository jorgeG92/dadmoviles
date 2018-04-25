package eps.android4_jorgegomez.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import eps.android4_jorgegomez.R;

/**
 * 9.intenciones
 */
public class RoundActivity extends AppCompatActivity implements RoundFragment.Callbacks{

    public static final String EXTRA_ROUND_ID = "eps.android4_jorgegomez.round_id";
    public static final String EXTRA_ROUND_FIRSTPLAYERNAME = "eps.android4_jorgegomez.round_firstplayer";
    public static final String EXTRA_ROUND_TITLE = "eps.android4_jorgegomez.round_title";
    public static final String EXTRA_ROUND_SIZE = "eps.android4_jorgegomez.round_size";
    public static final String EXTRA_ROUND_DATE = "eps.android4_jorgegomez.round_date";
    public static final String EXTRA_ROUND_BOARD = "eps.android4_jorgegomez.round_board";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            String firstPlayerName = getIntent().getStringExtra(EXTRA_ROUND_FIRSTPLAYERNAME);
            String roundTitle = getIntent().getStringExtra(EXTRA_ROUND_TITLE);
            int roundSize = getIntent().getIntExtra(EXTRA_ROUND_SIZE, 0);
            String roundDate = getIntent().getStringExtra(EXTRA_ROUND_DATE);
            String roundBoard = getIntent().getStringExtra(EXTRA_ROUND_BOARD);
            String roundId = getIntent().getStringExtra(EXTRA_ROUND_ID);
            RoundFragment roundFragment = RoundFragment.newInstance(roundId, firstPlayerName, roundTitle,
                    roundSize, roundDate, roundBoard); //Faltan el resto de argumentos
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, roundFragment).commit();
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    public static Intent newIntent(Context packageContext, String roundId){
        Intent intent = new Intent(packageContext, RoundActivity.class);
        intent.putExtra(EXTRA_ROUND_ID, roundId);
        return intent;
    }

    @Override
    public void onRoundUpdated() {

    }
}