package eps.android4_jorgegomez.activities;

import android.support.design.widget.Snackbar;
import android.view.View;

import eps.android4_jorgegomez.R;
import eps.android4_jorgegomez.model.ConectMovement;
import eps.android4_jorgegomez.view.ConectView;
import es.uam.eps.multij.AccionMover;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.Tablero;

public class JugadorLocal implements ConectView.OnPlayListener, Jugador {
    private String nombre;
    private int turno;
    private Partida game;

    public JugadorLocal(String nombre) {this.nombre = nombre;}

    public void setPartida(Partida game) {
        this.game = game;
        if (game.getJugador(0).getNombre().equals(nombre))
            turno = 0;
        else
            turno = 1;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean puedeJugar(Tablero tablero) {
        return true;
    }

    @Override
    public void onCambioEnPartida(Evento evento) {
    }

    @Override
    public void onPlay(int row, int column, View view) {
        try {
            if (game.getTablero().getEstado() != Tablero.EN_CURSO) {
                return;
            }

            if (game.getTablero().getTurno() == turno) {
                ConectMovement mov = new ConectMovement(row, column);
                if (game.getTablero().esValido(mov))
                    game.realizaAccion(new AccionMover(this, mov));

            }else{
                Snackbar.make(view, "Este turno no te corresponde", Snackbar.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
        }
    }
}
