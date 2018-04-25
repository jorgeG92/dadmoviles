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
import android.widget.TextView;

import eps.android4_jorgegomez.R;
import eps.android4_jorgegomez.model.Round;
import eps.android4_jorgegomez.model.RoundRepository;

import eps.android4_jorgegomez.model.RoundRepositoryFactory;
import eps.android4_jorgegomez.model.ConectBoard;
import eps.android4_jorgegomez.view.ConectView;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.Tablero;
import es.uam.eps.multij.PartidaListener;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.JugadorAleatorio;



public class RoundFragment extends Fragment implements PartidaListener{

    public static final String DEBUG = "DEBUG";
    public static final String ARG_ROUND_ID = "es.uam.eps.dadm.er19.round_id";
    public static final String ARG_FIRST_PLAYER_NAME = "es.uam.eps.dadm.er19.first_player_name";
    public static final String ARG_ROUND_TITLE = "es.uam.eps.dadm.er19.round_title";
    public static final String ARG_ROUND_SIZE = "es.uam.eps.dadm.er19.round_size";
    public static final String ARG_ROUND_DATE = "es.uam.eps.dadm.er19.round_date";
    public static final String ARG_ROUND_BOARD = "es.uam.eps.dadm.er19.round_board";

    private String BOARDSTRING;

    private int roundSize;
    private ConectBoard board;
    private Partida game;
    private ConectView boardView;
    private String roundId, firstPlayerName, roundTitle, roundDate, boardString;
    private Callbacks callbacks;

    public RoundFragment() { }

    public interface Callbacks {
        void onRoundUpdated();
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

    public static RoundFragment newInstance(String roundId, String firstPlayerName,
                            String roundTitle, int roundSize, String roundDate, String roundBoard) {
        Bundle args = new Bundle();
        args.putString(ARG_ROUND_ID, roundId);
        args.putString(ARG_FIRST_PLAYER_NAME, firstPlayerName);
        args.putString(ARG_ROUND_TITLE, roundTitle);
        args.putInt(ARG_ROUND_SIZE, roundSize);
        args.putString(ARG_ROUND_DATE, roundDate);
        args.putString(ARG_ROUND_BOARD, roundBoard);
        RoundFragment roundFragment = new RoundFragment();
        roundFragment.setArguments(args);
        return roundFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ROUND_ID)) {
            roundId = getArguments().getString(ARG_ROUND_ID);
        }
        if (getArguments().containsKey(ARG_FIRST_PLAYER_NAME)) {
            firstPlayerName = getArguments().getString(ARG_FIRST_PLAYER_NAME);
        }
        if (getArguments().containsKey(ARG_ROUND_TITLE)) {
            roundTitle = getArguments().getString(ARG_ROUND_TITLE);
        }
        if (getArguments().containsKey(ARG_ROUND_SIZE)) {
            roundSize = getArguments().getInt(ARG_ROUND_SIZE);
        }
        if (getArguments().containsKey(ARG_ROUND_DATE)) {
            roundDate = getArguments().getString(ARG_ROUND_DATE);
        }
        if (getArguments().containsKey(ARG_ROUND_BOARD)) {
            boardString = getArguments().getString(ARG_ROUND_BOARD);
        }
        if (savedInstanceState != null)
            boardString = savedInstanceState.getString(ARG_ROUND_BOARD);
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_round, container, false);
        TextView roundTitleTextView = (TextView) rootView.findViewById(R.id.round_title);
        roundTitleTextView.setText(round.getTitle());
        return rootView;
    }*/

    @Override
    public void onStart(){
        super.onStart();
        updateRound();
        //startRound();
    }

    /*void startRound() {
        ArrayList<Jugador> players = new ArrayList<Jugador>();
        JugadorAleatorio randomPlayer = new JugadorAleatorio("Random player");
        JugadorLocal localPlayer = new JugadorLocal();
        players.add(randomPlayer);
        players.add(localPlayer);

        game = new Partida(round.getBoard(), players);
        game.addObservador(this);

        localPlayer.setPartida(game);
        boardView = (ConectView) getView().findViewById(R.id.board_erview);
        boardView.setBoard(roundSize, round.getBoard());
        boardView.setOnPlayListener(localPlayer);
        registerListeners(localPlayer);

        if (game.getTablero().getEstado() == Tablero.EN_CURSO)
            game.comenzar();
    }*/


    /*private void registerListeners(JugadorLocal local) {
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
                        callbacks.onRoundUpdated();
                        boardView.invalidate();
                        Snackbar.make(getView(), R.string.round_restarted, Snackbar.LENGTH_SHORT).show();
                    }
                });
    }*/

    @Override
    public void onCambioEnPartida(Evento evento) {
        switch (evento.getTipo()) {
            case Evento.EVENTO_CAMBIO:
                boardView.invalidate();
                callbacks.onRoundUpdated();
                break;
            case Evento.EVENTO_FIN:
                boardView.invalidate();
                callbacks.onRoundUpdated();
                new AlertDialogFragment().show(getActivity().getSupportFragmentManager(),"ALERT DIALOG");
                break;
        }
    }

    private Round createRound() {
        Round round = new Round(roundSize);
        round.setPlayerUUID(ConectPreferenceActivity.getPlayerUUID(getActivity()));
        round.setId(roundId);
        round.setFirstPlayerName("random");
        round.setSecondPlayerName(firstPlayerName);
        round.setDate(roundDate);
        round.setTitle(roundTitle);
        round.setBoard(board);
        return round;
    }

    private void updateRound() {
        Round round = createRound();
        RoundRepository repository = RoundRepositoryFactory.createRepository(getActivity());
        RoundRepository.BooleanCallback callback = new RoundRepository.BooleanCallback() {
            @Override
            public void onResponse(boolean response) {
                if (response == false)
                    Snackbar.make(getView(), R.string.error_updating_round,
                            Snackbar.LENGTH_LONG).show();
            }
        };
        repository.updateRound(round, callback);
    }

}
