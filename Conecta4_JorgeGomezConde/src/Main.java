import es.uam.eps.multij.*;
import java.util.ArrayList;

public class Main {
	
	public void main() {
		
		JugadorAleatorio jA = new JugadorAleatorio("Maquina");
		JugadorHumano jH = new JugadorHumano("El Cholo");
		ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
		jugadores.add(jA);
		jugadores.add(jH);
		
		Partida partida = new Partida(new TableroConecta4(), jugadores);
		partida.addObservador(new JugadorHumano());
		
		TableroConecta4 tablero = (TableroConecta4) partida.getTablero();
		partida.comenzar();		
	}

}
