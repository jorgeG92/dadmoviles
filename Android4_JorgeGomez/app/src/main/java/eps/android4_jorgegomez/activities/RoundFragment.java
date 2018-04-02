package eps.android4_jorgegomez.activities;

import java.util.ArrayList;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import eps.android4_jorgegomez.R;
import eps.android4_jorgegomez.model.Round;
import eps.android4_jorgegomez.model.RoundRepository;

import eps.android4_jorgegomez.view.ERView;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.Tablero;
import es.uam.eps.multij.PartidaListener;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.JugadorAleatorio;



public class RoundFragment extends Fragment implements PartidaListener{

    public static final String ARG_ROUND_ID = "es.uam.eps.dadm.er15.round_id";

    private int size;
    private Round round;
    private Partida game;
    private ERView boardView;
    private Callbacks callbacks;

    public RoundFragment() { }

    public interface Callbacks {
        void onRoundUpdated(Round round);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
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
        final View rootView = inflater.inflate(R.layout.fragment_round, container, false);
        TextView roundTitleTextView = (TextView) rootView.findViewById(R.id.round_title);
        roundTitleTextView.setText(round.getTitle());
        return rootView;
    }

    @Override
    public void onStart(){
        super.onStart();
        startRound();
    }

    void startRound() {
        ArrayList<Jugador> players = new ArrayList<Jugador>();
        JugadorAleatorio randomPlayer = new JugadorAleatorio("Random player");
        JugadorLocal localPlayer = new JugadorLocal();
        players.add(randomPlayer);
        players.add(localPlayer);

        game = new Partida(round.getBoard(), players);
        game.addObservador(this);

        localPlayer.setPartida(game);
        boardView = (ERView) getView().findViewById(R.id.board_erview);
        boardView.setBoard(size, round.getBoard());
        boardView.setOnPlayListener(localPlayer);
        registerListeners(localPlayer);

        if (game.getTablero().getEstado() == Tablero.EN_CURSO)
            game.comenzar();
    }


    private void registerListeners(JugadorLocal local) {
        FloatingActionButton resetButton = getView().findViewById(R.id.reset_found_fab);
        resetButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        if (round.getBoard().getEstado() != Tablero.EN_CURSO){
                            Snackbar.make(getView(), R.string.round_already_finished, Snackbar.LENGTH_SHORT).show();
                        }
                        round.getBoard().reset();
                        startRound();
                        callbacks.onRoundUpdated(round);
                        boardView.invalidate();
                        Snackbar.make(getView(), R.string.round_restarted, Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onCambioEnPartida(Evento evento) {
        switch (evento.getTipo()) {
            case Evento.EVENTO_CAMBIO:
                boardView.invalidate();
                callbacks.onRoundUpdated(round);
                break;
            case Evento.EVENTO_FIN:
                boardView.invalidate();
                callbacks.onRoundUpdated(round);
                new AlertDialogFragment().show(getActivity().getSupportFragmentManager(),"ALERT DIALOG");
                break;
        }
    }

}
