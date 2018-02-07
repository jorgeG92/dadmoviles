import java.util.ArrayList;

import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Movimiento;
import es.uam.eps.multij.Tablero;

public class TableroConecta4 extends Tablero {
	
	/** Array que contiene el tablero*/
	private int[][] tablero;
	/** Array que contiene los movimientos validos*/
	//private ArrayList<Movimiento> movimientosValidos;
	/** Entero que contiene el numero de columanas y de filas del juego*/
	private int tamanioFilas;
	/** Entero que contiene el numero de columanas y de filas del juego*/
	private int tamanioColumnas;
	
	/**
	 * 
	 */
	public TableroConecta4() {
		
		// Inicializamos la tabla de movimientos validos
		tamanioFilas=6;
		tamanioColumnas=7;
		tablero = new int[tamanioFilas][tamanioColumnas];
		estado = Tablero.EN_CURSO;
		
		/** Inicializamos el tablero con todas las posiciones a 1*/
		for (int fila=0; fila<tamanioFilas; fila++) {
			for(int col=0; col<tamanioColumnas ;col++) {
				tablero[fila][col]=-1;
			}
		}
	}
	
	/**
	 * 
	 * @param tam_tablero
	 */
	public TableroConecta4(int tam_tablero) {
		
		/* Inicializamos la tabla de movimientos validos en funcion del tamaño del tablero */ 
		/* introducido por parametro */
		tamanioFilas=tam_tablero;
		tamanioColumnas=tam_tablero;
		tablero = new int[tamanioFilas][tamanioColumnas];
		estado = Tablero.EN_CURSO;
		
		/** Inicializamos el tablero con todas las posiciones a 1*/
		for (int fila=0; fila<tamanioFilas; fila++) {
			for(int col=0; col<tamanioColumnas ;col++) {
				tablero[fila][col]=-1;
			}
		}
	}

	@Override
	protected void mueve(Movimiento m) throws ExcepcionJuego {
		
		// TODO Auto-generated method stub
		/* No se comprueba si es valido, se da por echo que lo es*/
		MovimientoConecta4 m4 = (MovimientoConecta4) m;
		int filaM = m4.getFila();
		int colM =  m4.getColumna();
		
		/** Actualizamos el tablero */
		tablero[filaM][colM] = turno;		
		
		/**Actualizamos el utlimo movimiento*/
		ultimoMovimiento = m;
		
		 /** Cambiamos el turno */
		cambiaTurno();
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
		for(int col=0; col<tamanioColumnas; col++) {
			for(int fila=0; fila<tamanioFilas; fila++) {
				
				/** Si encontramos una ficha en una columna, 		*
				 *  devolvemos el movimiento de una fila mas arriba */
				if (tablero[fila][col] !=-1 && fila>0) {					
					mV.add(new MovimientoConecta4(fila+1, col));
					break;
				}
				
				/** Si llegamos al ultima fila de la columna */
				if (fila==tamanioFilas-1)
					mV.add(new MovimientoConecta4(fila, col));
			}
		}
		return mV;
	}

	@Override
	public String tableroToString() {
		// TODO Auto-generated method stub
		return "Cadena que muestra el tablero con toda su informacion";
	}

	@Override
	public void stringToTablero(String cadena) throws ExcepcionJuego {
		// TODO Auto-generated method stub
		return;

	}

	@Override
	public String toString() {
		
		// TODO Auto-generated method stub
		String mesa = "\n****Tablero Conecta 4****\n";
		
		for(int fila=0; fila<tamanioFilas; fila++) {
			for(int col=0; col<tamanioColumnas; col++) {
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
	 * @return Devuelve el numero de filas del tablero
	 */
	public int getTamanioFilas() {
		return tamanioFilas;
	}
	
	/**
	 * 
	 * @return Devuelve el numero de columnas del tablero
	 */
	public int getTamanioColumnas() {
		return tamanioColumnas;
	}
	

}
