package eps.android4_jorgegomez.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import eps.android4_jorgegomez.R;
import eps.android4_jorgegomez.activities.RoundAdapter;
import eps.android4_jorgegomez.model.Round;
import eps.android4_jorgegomez.model.RoundRepository;

/**
 * 9.intenciones
 */
public class RoundListActivity extends AppCompatActivity {

    private static final int SIZE = 3;
    private RecyclerView roundRecyclerView;
    private RoundAdapter roundAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round_list);
        roundRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager linearLayoutManager = new
                LinearLayoutManager(getApplicationContext());
        roundRecyclerView.setLayoutManager(linearLayoutManager);
        roundRecyclerView.setItemAnimator(new DefaultItemAnimator());
    }
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
    private void updateUI() {
        RoundRepository repository = RoundRepository.get(this);
        List<Round> rounds = repository.getRounds();
        if (roundAdapter == null) {
            roundAdapter = new RoundAdapter(rounds);
            roundRecyclerView.setAdapter(roundAdapter);
        } else {
            roundAdapter.notifyDataSetChanged();
        }
    }
}