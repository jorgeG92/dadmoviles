
import java.util.ArrayList;

import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Movimiento;
import es.uam.eps.multij.Tablero;

/**
 * 
 * @author jorge
 *
 */
public class TableroConecta4 extends Tablero {
	
	/** Array que contiene el tablero*/
	private int[][] tablero;
	/** Entero que contiene el numero de columanas y de filas del juego*/
	private int tamanioFilas;
	/** Entero que contiene el numero de columanas y de filas del juego*/
	private int tamanioColumnas;
	
	/**
	 * 
	 */
	public TableroConecta4() {
		
		/** Inicializamos la tabla de movimientos validos */
		tamanioFilas=6;
		tamanioColumnas=7;
		tablero = new int[tamanioFilas][tamanioColumnas];
		estado = Tablero.EN_CURSO;		
		iniTablero();
	}
	
	/**
	 * 
	 * @param tam_tablero
	 */
	public TableroConecta4(int tam_tablero) {
		
		/* Inicializamos la tabla de movimientos validos en funcion del tamaño del tablero */ 
		/* introducido por parametro */
		tamanioFilas=tam_tablero;
		tamanioColumnas=tam_tablero+1;
		tablero = new int[tamanioFilas][tamanioColumnas];
		estado = Tablero.EN_CURSO;
		iniTablero();
	}

	@Override
	protected void mueve(Movimiento m) throws ExcepcionJuego {
		
		// TODO Auto-generated method stub
		/* No se comprueba si es valido, se da por echo que lo es*/
		MovimientoConecta4 m4 = (MovimientoConecta4) m;
		int col =  m4.getColumna();
		
		/** Buscamos la fila que corresponde*/
		int fila = buscarFila(col);		
		
		/** Actualizamos el tablero */
		tablero[fila][col] = (int)turno;		
		
		/**Actualizamos el utlimo movimiento*/
		ultimoMovimiento = m;
		
		/*Comprobamos el estado de la partida*/		
		estado = checkEstadoTablero();
		
		switch(estado) {
			case Tablero.FINALIZADA:			
				System.out.println(toString());
			break;
			case Tablero.TABLAS:
				System.out.println(toString());
			break;			
			default:
			/** Cambiamos el turno */
			cambiaTurno();
			break;		
		}
		
		
		 
	}

	@Override
	public boolean esValido(Movimiento m) {
		// TODO Auto-generated method stub
		/** Si no hay moviemiento es falso*/
		if (m==null) 
			return false;
		ArrayList<Movimiento> mV = movimientosValidos();
		/** Comprobamos si existe el movimiento dentro de movimientos validos*/
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
			if (tablero[0][col] ==-1) {
				mV.add(new MovimientoConecta4(col));
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
		
		/** Pintamos el tablero */
		for(int fila=0; fila<tamanioFilas; fila++) {
			for(int col=0; col<tamanioColumnas; col++) {
				if (tablero[fila][col]!=-1)
					mesa += "["+tablero[fila][col]+"]";
				else
					mesa += "[ ]";
			}
			mesa += "\n";
		}
		/** Pintamos el numero de columnas */
		mesa += "---------------------\n"; 
		for(int i = 0;  i<tamanioColumnas; i++) {
			mesa += "["+i+"]"; 			
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
	
	/**
	 * Busca la ultima fila vacia de la columna
	 * @param col
	 * @return Fila de la columna con el primer hueco, si no
	 * 			existe el hueco sera -1
	 */
	private int buscarFila(int col) {
		for (int i = 0; i < tamanioFilas; i++) 
			if (tablero[i][col] > -1) 
				return i-1;			
		return tamanioFilas-1;
	}
	
	/**
	 * Comprueba en el tablero si el jugador al que le corresponde el 
	 * turno ha ganado, ha empatado o sigue en curso la partida
	 * 
	 * @return estado del tablero (EN_CURSO, TABLAS, FINALIZADA)
	 */
	private int checkEstadoTablero() {		
		MovimientoConecta4 ultimoMovimiento = (MovimientoConecta4) this.ultimoMovimiento;
		int contadorFichas = 0;		
		int fila = buscarFila(ultimoMovimiento.getColumna())+1;
		int columna = ultimoMovimiento.getColumna();
		
		/* Compromabamos que si es la ultima ficha de la columna
		 * no utilicemos un indice negativo*/
		if(fila == -1)
			fila +=1;
		
		/* Comprobamos debajo de la ficha */	
		for (int f=fila; f < fila+4; f++) {			
			if(f>tamanioFilas-1) break;			
			if(tablero[f][columna]==turno)	contadorFichas +=1;			
		}
		
		if (contadorFichas>=4) return Tablero.FINALIZADA; //Condicion de victoria
		contadorFichas=0;		
		
		/* Comprobamos laterales de la ficha */
		for(int c = columna; c > columna-4; c--) { 	//Lado izquierdo
			if(c<0)	
				break;
			else if (tablero[fila][c]!=turno)
				break;
			else
				contadorFichas += 1;
		}		

		for(int c = columna+1; c < columna+4; c++) { 	//Lado derecho
			if(c>tamanioColumnas-1 )	
				break; 
			else if (tablero[fila][c]!=turno)
				break;
			else
				contadorFichas += 1;
		}
		
		if (contadorFichas>=4) return Tablero.FINALIZADA; //Condicion de victoria
		contadorFichas=0;		
		
		/*Comprobamos diagonales de la ficha*/
		//Diagonal abajo-derecha a arriba-izquierda [/]
		int diagonal_f = 0;
		int diagonal_c = 0;
		//Hacia arriba
		while(true) {			
			if((fila+diagonal_f<0) || (columna+diagonal_c > tamanioColumnas-1))
				break;			
			if (tablero[fila+diagonal_f][columna+diagonal_c]==turno) {
				contadorFichas += 1;
				diagonal_f -=1;
				diagonal_c +=1;
			}else
				break;			
		}
		//Hacia abajo
		diagonal_f = +1;
		diagonal_c = -1;
		while(true) {	
			if((fila+diagonal_f>tamanioFilas-1) || (columna+diagonal_c<0))
				break;			
			if (tablero[fila+diagonal_f][columna+diagonal_c]==turno) {
				contadorFichas += 1;
				diagonal_f +=1;
				diagonal_c -=1;
			}else
				break;
		}
		
		if (contadorFichas>=4) return Tablero.FINALIZADA; //Condicion de victoria
		contadorFichas=0;
		
		//Diagonal arriba-derecha a abajo-izquierda [\]
		diagonal_f = 0;
		diagonal_c = 0;
		//Hacia arriba
		while(true) {			
			if((fila+diagonal_f<0) || (columna+diagonal_c <0))
				break;			
			if (tablero[fila+diagonal_f][columna+diagonal_c]==turno) {
				contadorFichas += 1;
				diagonal_f -=1;
				diagonal_c -=1;
			}else
				break;			
		}
		//Hacia abajo
		diagonal_f = +1;
		diagonal_c = +1;
		while(true) {			
			if((fila+diagonal_f>tamanioFilas-1) || (columna+diagonal_c>tamanioColumnas-1))
				break;			
			if (tablero[fila+diagonal_f][columna+diagonal_c]==turno) {
				contadorFichas += 1;
				diagonal_f +=1;
				diagonal_c +=1;
			}else
				break;
		}
		
		//Evaluacion final de la partida
		if (contadorFichas>=4) return Tablero.FINALIZADA;		
		if(movimientosValidos().isEmpty()) return Tablero.TABLAS;		
		return Tablero.EN_CURSO;
		
	}
	
	/**
	 * Inicializa el tablero con todas sus casillas con un -1
	 */
	private void iniTablero() {
		for (int fila=0; fila<tamanioFilas; fila++) {
			for(int col=0; col<tamanioColumnas ;col++) {
				tablero[fila][col]=-1;
			}
		}
		
	}

}
