package eps.android4_jorgegomez.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import eps.android4_jorgegomez.R;
import eps.android4_jorgegomez.model.Round;
import eps.android4_jorgegomez.model.RoundRepository;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.PartidaListener;

public class RoundFragment extends Fragment implements PartidaListener{

    public static final String ARG_ROUND_ID = "es.uam.eps.dadm.er10.round_id";

    private final int ids[][] = {
            {R.id.er1, R.id.er2, R.id.er3},
            {R.id.er4, R.id.er5, R.id.er6},
            {R.id.er7, R.id.er8, R.id.er9}};

    private int size;
    private Round round;
    private Partida game;

    public RoundFragment() { }

    public static RoundFragment newInstance(String roundId) {
        Bundle args = new Bundle();
        args.putString(ARG_ROUND_ID, roundId);
        RoundFragment roundFragment = new RoundFragment();
        roundFragment.setArguments(args);
        return roundFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RoundRepository repository;
        String roundId = getActivity().getIntent().getStringExtra(RoundActivity.EXTRA_ROUND_ID);
        repository = RoundRepository.get(getActivity());
        round = repository.getRound(roundId);
        size = round.getSize();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_round, container,
                false);
        TextView roundTitleTextView = (TextView)
                rootView.findViewById(R.id.round_title);
        roundTitleTextView.setText(round.getTitle());
        return rootView;
    }

    @Override
    public void onCambioEnPartida(Evento evento) {

    }


}
