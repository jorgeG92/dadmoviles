import java.util.ArrayList;

import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Movimiento;
import es.uam.eps.multij.Tablero;

public class TableroConecta4 extends Tablero {
	
	/** Array que contiene los movimientos validos*/
	private ArrayList<Movimiento> movimientosValidos;
	/** Entero que contiene el numero de columanas y de filas del juego*/
	private int tamanio;
	
	
	public TableroConecta4() {
		
		// Inicializamos la tabla de movimientos validos
		tamanio=4;
		movimientosValidos = new ArrayList<Movimiento>();		
		for (int i = 1; i<=tamanio; i++) {
			movimientosValidos.add(new MovimientoConecta4(1,i));
		}
	}
	
	public TableroConecta4(int tam_tablero) {
		
		/* Inicializamos la tabla de movimientos validos en funcion del tamaño del tablero */ 
		/* introducido por parametro */
		tamanio = tam_tablero;
		movimientosValidos = new ArrayList<Movimiento>();		
		for (int i = 0; i<tamanio; i++) {
			movimientosValidos.add(new MovimientoConecta4(1,i+1));
		}
	}

	@Override
	protected void mueve(Movimiento m) throws ExcepcionJuego {
		// TODO Auto-generated method stub
		/* Comprobar el estado de la partida */
		/* Comprobar que el movimiento es valido*/
		/* Si no es valido anunciarlo*/
		/* Si es valido, ejecutarlo*/

	}

	@Override
	public boolean esValido(Movimiento m) {
		// TODO Auto-generated method stub
		return movimientosValidos.contains(m);
	}

	@Override
	public ArrayList<Movimiento> movimientosValidos() {
		// TODO Auto-generated method stub
		return movimientosValidos;
	}

	@Override
	public String tableroToString() {
		// TODO Auto-generated method stub
		return "Cadena que muestra el tablero";
	}

	@Override
	public void stringToTablero(String cadena) throws ExcepcionJuego {
		// TODO Auto-generated method stub
		return;

	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Cadena que muestra el tablero con toda la informacion";
	}
	
	/**
	 * 
	 * @return Devuelve el tamaño del tablero
	 */
	public int getTamanio() {
		return tamanio;
	}

}
