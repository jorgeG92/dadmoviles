package eps.android4_jorgegomez.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import eps.android4_jorgegomez.R;
import eps.android4_jorgegomez.model.Round;
import eps.android4_jorgegomez.model.RoundRepository;
import eps.android4_jorgegomez.model.TableroConecta4;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.JugadorAleatorio;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.PartidaListener;
import es.uam.eps.multij.Tablero;

public class RoundFragment extends Fragment implements PartidaListener{

    public static final String ARG_ROUND_ID = "es.uam.eps.dadm.er10.round_id";

    private final int ids[][] = {
            {R.id.er1, R.id.er2, R.id.er3},
            {R.id.er4, R.id.er5, R.id.er6},
            {R.id.er7, R.id.er8, R.id.er9}};

    private int size;
    private Round round;
    private Partida game;
    private Callbacks callbacks;

    public RoundFragment() { }

    public interface Callbacks {
        void onRoundUpdated(Round round);
    }

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
        setHasOptionsMenu(true);
        if (getArguments().containsKey(ARG_ROUND_ID)){
            String roundId = getArguments().getString(ARG_ROUND_ID);
            round = RoundRepository.get(getActivity()).getRound(roundId);
            size = round.getSize();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_round, container,
                false);
        TextView roundTitleTextView = (TextView) rootView.findViewById(R.id.round_title);
        roundTitleTextView.setText(round.getTitle());
        return rootView;
    }

    public void updateUI(){

        //Aqui como actualizamos los botones???
    }

    @Override
    public void onCambioEnPartida(Evento evento) {
        switch (evento.getTipo()) {
            case Evento.EVENTO_CAMBIO:
                updateUI();
                callbacks.onRoundUpdated(round);
                break;
            case Evento.EVENTO_FIN:
                updateUI();
                callbacks.onRoundUpdated(round);
                new AlertDialogFragment().show(getActivity().getSupportFragmentManager(),"ALERT DIALOG");
                break;
        }
    }

}
