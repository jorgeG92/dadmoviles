import es.uam.eps.multij.*;
import java.util.ArrayList;

public class Main {
	
	public static void main(String[] args) {
		
		/*Inicializacion de los jugadores*/
		JugadorAleatorio jA = new JugadorAleatorio("Maquina");
		JugadorHumano jH = new JugadorHumano("El Humano");
		ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
		jugadores.add(jA);
		jugadores.add(jH);
		
		/*Inicializacion del tablero y la partida*/
		Partida partida = new Partida(new TableroConecta4(2), jugadores);
		partida.addObservador(new JugadorObservador());		
		//TableroConecta4 tablero = (TableroConecta4) partida.getTablero();
		
		/*Comienza la partida*/
		partida.comenzar();		
	}

}
