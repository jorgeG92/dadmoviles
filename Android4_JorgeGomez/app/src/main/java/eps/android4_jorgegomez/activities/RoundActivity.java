package eps.android4_jorgegomez.activities;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

import java.util.ArrayList;

import eps.android4_jorgegomez.R;
import eps.android4_jorgegomez.model.TableroConecta4;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.JugadorAleatorio;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.PartidaListener;
import es.uam.eps.multij.Tablero;

public class RoundActivity extends AppCompatActivity implements PartidaListener {
    private final int ids[][] = {
            {R.id.er1, R.id.er2, R.id.er3},
            {R.id.er4, R.id.er5, R.id.er6},
            {R.id.er7, R.id.er8, R.id.er9}};
    private int SIZE = 3;
    private Partida game;
    private TableroConecta4 board;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);
        startRound();
    }
    private void registerListeners(JugadorLocal local) {
        ImageButton button;
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++) {
                button = (ImageButton) findViewById(ids[i][j]);
                button.setOnClickListener(local);
            }
    }
    void startRound() {
        ArrayList<Jugador> players = new ArrayList<Jugador>();
        JugadorAleatorio randomPlayer = new JugadorAleatorio("Random player");
        JugadorLocal localPlayer = new JugadorLocal();
        players.add(randomPlayer);
        players.add(localPlayer);
        board = new TableroConecta4(SIZE);
        game = new Partida(board, players);
        game.addObservador(this);
        localPlayer.setPartida(game);
        registerListeners(localPlayer);
        if (game.getTablero().getEstado() == Tablero.EN_CURSO)
            game.comenzar();
    }

    private void updateUI() {
        ImageButton button;
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++) {
                button = (ImageButton) findViewById(ids[i][j]);
                if (board.getTablero(i, j) == TableroConecta4.JUGADOR1)
                    button.setBackgroundResource(R.drawable.blue_button_48dp);
                else if (board.getTablero(i, j) == TableroConecta4.VACIO)
                    button.setBackgroundResource(R.drawable.void_button_48dp);
                else
                    button.setBackgroundResource(R.drawable.green_button_48dp);
            }
    }

    @Override
    public void onCambioEnPartida(Evento evento) {

        switch (evento.getTipo()) {
            case Evento.EVENTO_CAMBIO:
                android.util.Log.i("INF", board.toString());
                updateUI();
                break;
            case Evento.EVENTO_FIN:
                updateUI();
                Snackbar.make(findViewById(R.id.round_title), R.string.game_over,
                        Snackbar.LENGTH_SHORT).show();
                break;
        }
    }
}
