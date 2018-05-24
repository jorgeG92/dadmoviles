package eps.android4_jorgegomez.activities;

import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.Tablero;

public class JugadorRemoto implements Jugador {

    private String nombre;

    public JugadorRemoto(String nombre){
        this.nombre = nombre;
    }

    @Override
    public String getNombre() {
        return nombre;
    }

    @Override
    public boolean puedeJugar(Tablero tablero) {
        return false;
    }

    @Override
    public void onCambioEnPartida(Evento evento) { }
}
