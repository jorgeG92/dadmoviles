package eps.android4_jorgegomez.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageButton;

import java.util.ArrayList;

import eps.android4_jorgegomez.R;
import eps.android4_jorgegomez.model.TableroConecta4;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.JugadorAleatorio;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.PartidaListener;
import es.uam.eps.multij.Tablero;

/**
 * 9.intenciones
 */
public class RoundActivity extends AppCompatActivity implements PartidaListener {
    public static final String EXTRA_ROUND_ID = "eps.android4_jorge.gomez";

    private final int ids[][] = {
            {R.id.er1, R.id.er2, R.id.er3},
            {R.id.er4, R.id.er5, R.id.er6},
            {R.id.er7, R.id.er8, R.id.er9}};
    private int SIZE = 3;

    /*7. Actividades*/
    private Partida game;
    private TableroConecta4 board;

    /* 8.Ciclo de vida de actividad*/
    private int numero = 0;
    public static final String BOARDSTRING = "es.uam.eps.dadm.er8.grid";

    /*7. Actividades*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_round);
        startRound();

        //8
        if (savedInstanceState != null) {
            try {
                board.stringToTablero(savedInstanceState.getString(BOARDSTRING));
                updateUI();
            } catch (ExcepcionJuego excepcionJuego) {
                excepcionJuego.printStackTrace();
            }
        }
        log("onCreate()");
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

    /*8. Ciclo de vida*/
    private void log(String text) {
        Log.d("LifeCycleTest", Integer.toString(numero) + " : " + text);
        numero++;
    }

    @Override
    protected void onStart() {
        super.onStart();
        log("onStart()");
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putString(BOARDSTRING, board.tableroToString());
        super.onSaveInstanceState(outState);
        log("onSaveInstanceState()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        log("onResume()");
    }
    @Override
    protected void onPause() {
        super.onPause();
        log("onPause()");
    }
    @Override
    protected void onStop() {
        super.onStop();
        log("onStop()");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        log("onDestroy()");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        log("onRestart()");
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        try {
            board.stringToTablero(savedInstanceState.getString(BOARDSTRING));
            updateUI();
        } catch (ExcepcionJuego excepcionJuego) {
            excepcionJuego.printStackTrace();
        }
    }

    /* 9. Intenciones */
    public static Intent newIntent(Context packageContext, String roundId) {
        Intent intent = new Intent(packageContext, RoundActivity.class);
        intent.putExtra(EXTRA_ROUND_ID, roundId);
        return intent;
    }


}
