import java.util.ArrayList;

import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Movimiento;
import es.uam.eps.multij.Tablero;

public class TableroConecta4 extends Tablero {
	
	/** Array que contiene el tablero*/
	private int[][] tablero;
	/** Array que contiene los movimientos validos*/
	private ArrayList<Movimiento> movimientosValidos;
	/** Entero que contiene el numero de columanas y de filas del juego*/
	private int tamanio;
	
	
	public TableroConecta4() {
		
		// Inicializamos la tabla de movimientos validos
		tamanio=4;
		/*movimientosValidos = new ArrayList<Movimiento>();		
		for (int i = 1; i<=tamanio; i++) {
			movimientosValidos.add(new MovimientoConecta4(1,i));
		}*/
		
		/** Inicializamos el tablero con todas las posiciones a 1*/
		for (int fila=0; fila<tamanio; fila++) {
			for(int col=0; col<tamanio ;col++) {
				tablero[fila][col]=-1;
			}
		}
	}
	
	public TableroConecta4(int tam_tablero) {
		
		/* Inicializamos la tabla de movimientos validos en funcion del tamaño del tablero */ 
		/* introducido por parametro */
		tamanio = tam_tablero;
		/** Inicializamos el tablero con todas las posiciones a 1*/
		for (int fila=0; fila<tamanio; fila++) {
			for(int col=0; col<tamanio ;col++) {
				tablero[fila][col]=-1;
			}
		}
	}

	@Override
	protected void mueve(Movimiento m) throws ExcepcionJuego {
		// TODO Auto-generated method stub
		/* No se comprueba si es valido, se da por echo que lo es*/
		

	}

	@Override
	public boolean esValido(Movimiento m) {
		// TODO Auto-generated method stub
		ArrayList<Movimiento> mV = movimientosValidos();		
		for(int i=0; i<mV.size();i++) 
			if (m.equals(mV.get(i))) 
				return true;		
		return false;
	}

	@Override
	public ArrayList<Movimiento> movimientosValidos() {
		// TODO Auto-generated method stub
		ArrayList<Movimiento> mV = new ArrayList<Movimiento>();
		/** Recorremos el tablero para encontrar los movimientos validos */
		for(int col=0; col<tamanio; col++) {
			for(int fila=0; fila<tamanio; fila++) {
				/** Si encontramos una ficha en una columna, 		*
				 *  devolvemos el movimiento de una fila mas arriba */
				if (tablero[col][fila] !=-1) {
					mV.add(new MovimientoConecta4(fila+1, col));
					break;
				}
				/** Si llegamos al ultimo de la fila */
				if (fila==tamanio-1)
					mV.add(new MovimientoConecta4(fila, col));
			}
		}
		return mV;
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
		String mesa = "****Tablero Conecta 4****\n\n";
		
		for(int fila=0; fila<tamanio; fila++) {
			for(int col=0; col<tamanio; col++) {
				if (tablero[fila][col]!=-1)
					mesa += "["+tablero[fila][col]+"]";
				else
					mesa += "[ ]";
			}
			mesa += "\n";
		}
		return mesa;
	}
	
	/**
	 * 
	 * @return Devuelve el tamaño del tablero
	 */
	public int getTamanio() {
		return tamanio;
	}

}
