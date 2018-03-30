package eps.android4_jorgegomez.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import eps.android4_jorgegomez.R;

/**
 * 9.intenciones
 */
public class RoundActivity extends AppCompatActivity{

    public static final String EXTRA_ROUND_ID = "es.uam.eps.dadm.er9.round_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            String roundId = getIntent().getStringExtra(EXTRA_ROUND_ID);
            RoundFragment roundFragment = RoundFragment.newInstance(roundId);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, roundFragment).commit();

        }
    }

    public static Intent newIntent(Context packageContext, String roundId){
        Intent intent = new Intent(packageContext, RoundActivity.class);
        intent.putExtra(EXTRA_ROUND_ID, roundId);
        return intent;
    }


}