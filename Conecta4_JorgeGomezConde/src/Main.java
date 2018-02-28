import es.uam.eps.multij.*;
import java.util.ArrayList;

/**
 * @author Jorge Gomez Conde
 * @version 1.0 Febreo 18, 2018
 */ 
public class Main {
	
	public static void main(String[] args) {
		
		/*Inicializacion de los jugadores*/
		JugadorAleatorio jA = new JugadorAleatorio("Maquina 1");
		JugadorHumanoConecta4 jH = new JugadorHumanoConecta4("El Humano");
		//JugadorAleatorio jH = new JugadorAleatorio("Maquina 2");
		ArrayList<Jugador> jugadores = new ArrayList<Jugador>();
		jugadores.add(jA);
		jugadores.add(jH);
		
		/*Inicializacion del tablero y la partida*/
		TableroConecta4 tablero = new TableroConecta4();
		/** Pruebas para cargar partida
		try {
			//tablero.stringToTablero("6:7:2:0:24:1%-1=0=-1=-1=-1=-1=-1!-1=0=-1=-1=0=1=-1!-1=0=-1=-1=1=1=-1!1=0=-1=-1=1=1=0!0=1=-1=1=0=0=0!1=1=0=0=1=1=0"); //Partida acabada por le jugador con 0
			tablero.stringToTablero("6:7:2:1:13:1%-1=-1=-1=-1=-1=-1=-1!-1=-1=-1=-1=-1=-1=-1!-1=1=-1=-1=-1=-1=-1!-1=1=1=-1=-1=-1=-1!0=1=0=1=-1=-1=-1!0=1=0=0=1=0=0"); //Partida acabada por le jugador con 1
		} catch (ExcepcionJuego e) {
			e.printStackTrace();
		}	
		*/	

		Partida partida = new Partida(tablero, jugadores);
		partida.addObservador(new JugadorObservadorConecta4());		
		//TableroConecta4 tablero = (TableroConecta4) partida.getTablero();
		
		/*Comienza la partida*/
		partida.comenzar();		
	}

}
