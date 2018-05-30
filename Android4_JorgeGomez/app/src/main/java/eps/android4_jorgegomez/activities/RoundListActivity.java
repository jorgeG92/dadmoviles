package eps.android4_jorgegomez.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;

import eps.android4_jorgegomez.R;
import eps.android4_jorgegomez.model.Round;
import eps.android4_jorgegomez.model.RoundRepository;
import eps.android4_jorgegomez.model.RoundRepositoryFactory;


public class RoundListActivity extends AppCompatActivity implements RoundListFragment.Callbacks, RoundFragment.Callbacks{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_detail);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new RoundListFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        PreferenceManager.setDefaultValues(this, R.xml.settings, false);
    }

    @Override
    public void onRoundSelected(Round round){
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = RoundActivity.newIntent(this, round.getId(), round.getFirstPlayerName(),
                    round.getSecondPlayerName(), round.getFirstPlayerUUID(), round.getSecondPlayerUUID(),
                    round.getTitle(), round.getSize(), round.getDate(), round.getBoard().tableroToString());

            startActivity(intent);
        } else {
            RoundFragment roundFragment = RoundFragment.newInstance(round.getId(), round.getFirstPlayerName(),
                    round.getSecondPlayerName(), round.getFirstPlayerUUID(), round.getSecondPlayerUUID(),
                    round.getTitle(), round.getSize(), round.getDate(), round.getBoard().tableroToString(),
                    ConectPreferenceActivity.getGameMode(this).equals(ConectPreferenceActivity.GAMEMODE_DEFAULT));

            //Revisar new instance
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, roundFragment)
                    .commit();
        }
    }

    @Override
    public void onPreferencesSelected() {
        startActivity(new Intent(this, ConectPreferenceActivity.class));
    }

    @Override
    public void onNewRoundAdded(Round round) { }

    @Override
    public void onNewRoundAdded(Round round, String userId, String userName) {
        RoundRepository repository = RoundRepositoryFactory.createRepository(this,ConectPreferenceActivity.getGameMode(this).equals(ConectPreferenceActivity.GAMEMODE_DEFAULT));
        round.setSecondPlayerUUID(userId);
        round.setSecondPlayerName(userName);
        RoundRepository.BooleanCallback booleanCallback = new RoundRepository.BooleanCallback() {
            @Override
            public void onResponse(boolean ok) {
                onRoundUpdated();

            }
        };
        repository.addRound(round, booleanCallback, false);
    }

    @Override
    public void onRoundUpdated() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        RoundListFragment roundListFragment = (RoundListFragment)
                fragmentManager.findFragmentById(R.id.fragment_container);
        roundListFragment.updateUI();
    }

    @Override
    public void onCloseSession(){
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void onRoundDeleted(){
        onRoundUpdated();
    }

    @Override
    public void onRestart(){
        super.onRestart();
        onRoundUpdated();

    }
}