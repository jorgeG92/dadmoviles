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
    public static final String EXTRA_ROUND_FIRSTPLAYERNAME = "eps.android4_jorgegomez.first_player_name";
    public static final String EXTRA_ROUND_SECONDPLAYERNAME = "eps.android4_jorgegomez.second_player_name";
    public static final String EXTRA_ROUND_FIRSTPLAYERID = "eps.android4_jorgegomez.first_player_id";
    public static final String EXTRA_ROUND_SECONDPLAYERID = "eps.android4_jorgegomez.second_player_id";
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
            String secondPlayerName = getIntent().getStringExtra(EXTRA_ROUND_SECONDPLAYERNAME);
            String firstPlayerId = getIntent().getStringExtra(EXTRA_ROUND_FIRSTPLAYERID);
            String secondPlayerId = getIntent().getStringExtra(EXTRA_ROUND_SECONDPLAYERID);
            String roundTitle = getIntent().getStringExtra(EXTRA_ROUND_TITLE);
            int roundSize = getIntent().getIntExtra(EXTRA_ROUND_SIZE, 4);
            String roundDate = getIntent().getStringExtra(EXTRA_ROUND_DATE);
            String roundBoard = getIntent().getStringExtra(EXTRA_ROUND_BOARD);
            String roundId = getIntent().getStringExtra(EXTRA_ROUND_ID);

            RoundFragment roundFragment = RoundFragment.newInstance(roundId, firstPlayerName, secondPlayerName,
                    firstPlayerId, secondPlayerId, roundTitle, roundSize, roundDate, roundBoard,
                    ConectPreferenceActivity.getGameMode(this).equals(ConectPreferenceActivity.GAMEMODE_DEFAULT)); //Faltan el resto de argumentos
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, roundFragment).commit();
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
    }

    public static Intent newIntent(Context packageContext, String roundId, String firstPlayerName,
                                   String secondPlayerName, String firstPlayerId, String secondPlayerId,
                                   String title, int size, String date, String board){
        Intent intent = new Intent(packageContext, RoundActivity.class);

        intent.putExtra(EXTRA_ROUND_ID, roundId);
        intent.putExtra(EXTRA_ROUND_FIRSTPLAYERNAME, firstPlayerName);
        intent.putExtra(EXTRA_ROUND_SECONDPLAYERNAME, secondPlayerName);
        intent.putExtra(EXTRA_ROUND_FIRSTPLAYERID, firstPlayerId);
        intent.putExtra(EXTRA_ROUND_SECONDPLAYERID, secondPlayerId);
        intent.putExtra(EXTRA_ROUND_TITLE,title);
        intent.putExtra(EXTRA_ROUND_SIZE, size);
        intent.putExtra(EXTRA_ROUND_DATE, date);
        intent.putExtra(EXTRA_ROUND_BOARD, board);

        return intent;
    }

    @Override
    public void onRoundUpdated() { }

    @Override
    public void onRoundDeleted(){
        startActivity(new Intent( RoundActivity.this, RoundListActivity.class));
    }
}