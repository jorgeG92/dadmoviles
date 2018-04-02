package eps.android4_jorgegomez.activities;

import android.support.design.widget.Snackbar;
import android.view.View;

import eps.android4_jorgegomez.R;
import eps.android4_jorgegomez.model.MovimientoConecta4;
import eps.android4_jorgegomez.view.ERView;
import es.uam.eps.multij.AccionMover;
import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.Partida;
import es.uam.eps.multij.Tablero;

public class JugadorLocal implements ERView.OnPlayListener, Jugador {
    private String nombre;
    Partida game;

    public JugadorLocal() { nombre = "Default"; }

    public JugadorLocal(String nombre) {this.nombre = nombre;}

    public void setPartida(Partida game) {
        this.game = game;
    }
    @Override
    public String getNombre() {
        return "Local player";
    }
    @Override
    public boolean puedeJugar(Tablero tablero) {
        return true;
    }
    @Override
    public void onCambioEnPartida(Evento evento) {
    }

    @Override
    public void onPlay(int row, int column) {
        try {
            if (game.getTablero().getEstado() != Tablero.EN_CURSO) {
                return;
            }
            MovimientoConecta4 m;
            m = new MovimientoConecta4(row, column);
            if (game.getTablero().esValido(m))
                game.realizaAccion(new AccionMover(this, m));
        } catch (Exception e) {
        }
    }
}
