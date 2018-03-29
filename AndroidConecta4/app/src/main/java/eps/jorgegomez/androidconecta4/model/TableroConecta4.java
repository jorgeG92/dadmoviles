package eps.jorgegomez.androidconecta4.model;

import java.util.ArrayList;
import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Movimiento;
import es.uam.eps.multij.Tablero;

/** Clase TableroConecta4 que extiende la clase Tablero.
 * Orientada a ser utilizada unicamente para el juego Conecta 4.
 * Admite distintas dimesiones de tablero, siendo siempre la columna 
 * una unidad mayor que las filas.
 * 
 * Describipcion del formato en el que se guardan y cargan las partidas 
 * para los metodos stringToTablero() y tableroToString():
 * 
 * Separador: '%'
 * 	-Primera parte: datos de la partida
 *  -Segunda parte: disposicion del tablero
 * 
 * Separador: ':'
 * 	Corresponde a la parte de datos, define de izquierda a derecha:
 * 		-Numero de filas
 * 		-Numero de columnas
 * 		-Numero de jugadores
 * 		-Turno
 * 		-Ultimo movimiento
 * 
 * Separador: '!'
 * 	Corresponde a la parte de defincion del tablero.
 * 	Leyendo de izquierda a derecha, la primera corresponde con
 *  la fila que se encuntra mas arriba del tablero
 *
 * Separador: '='
 * 	Corresponde a la parte de defincion del tablero.
 *  Dentro de cada fila, el orden indica la columna de mas a 
 *  la izquierda a mas a la derecha. 
 * 
 * @author Jorge Gomez Conde
 * @version 1.0 Febrero 18, 2018
 */
public class TableroConecta4 extends Tablero {
	
	/** Array que contiene el tablero*/
	private int[][] tablero;
	/** Entero que contiene el numero de columanas y de filas del juego*/
	private int tamanioFilas;
	/** Entero que contiene el numero de columanas y de filas del juego*/
	private int tamanioColumnas;
	public static final int JUGADOR1=0;
	public static final int JUGADOR2=1;
    public static final int VACIO=-1;
	
	/** Construtor de la clase por defecto
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
	
	/**Constructor que permite definir la dimension 
	 * del tablero
	 * NOTA: las columnas son 1 unidad mas por defecto.
	 * 
	 * @param tam_tablero tamanio de las filas
	 */
	public TableroConecta4(int tam_tablero) {
		
		/* Inicializamos la tabla de movimientos validos en funcion del tamaño del tablero */ 
		/* introducido por parametro */
		tamanioFilas=tam_tablero;
		tamanioColumnas=tam_tablero;
		tablero = new int[tamanioFilas][tamanioColumnas];
		estado = Tablero.EN_CURSO;
		iniTablero();
	}

	@Override
	protected void mueve(Movimiento m) throws ExcepcionJuego {
		/* No se comprueba si es valido, se da por echo que lo es*/
		MovimientoConecta4 m4 = (MovimientoConecta4) m;
		int col =  m4.getColumna();
		/*Buscamos la fila que corresponde*/
		int fila = buscarFila(col);
		/* Actualizamos el tablero */
		tablero[fila][col] = (int)turno;
		/*Actualizamos el utlimo movimiento*/
		ultimoMovimiento = m4;
		/*Comprobamos el estado de la partida*/		
		estado = checkEstadoTablero();
		if (estado != EN_CURSO)
			System.out.println(this.toString());
		else
			cambiaTurno();
	}

	@Override
	public boolean esValido(Movimiento m) {
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
		String tableroString = new String();
		MovimientoConecta4 m4 = (MovimientoConecta4) this.ultimoMovimiento;
		/* Añadimos los datos del estado de la partida*/
		tableroString += this.tamanioFilas+":";
		tableroString += this.tamanioColumnas+":";
		tableroString += this.numJugadores+":";
		tableroString += this.turno+":";
		tableroString += this.numJugadas+":";
		if (m4==null)
			tableroString += "-1%";
		else
			tableroString += m4.getColumna()+"%";
		
		/* Añadimos la disposicion del tablero */		
		for(int fila=0; fila<tamanioFilas; fila++) {			
			for(int columna=0; columna<tamanioColumnas; columna++) {				
				if(columna!=tamanioColumnas-1) tableroString +=tablero[fila][columna]+"=";
				else tableroString += tablero[fila][columna];
			}			
			if(fila!=tamanioFilas-1) tableroString +="!";
		}			
		return tableroString;
	}

	@Override
	public void stringToTablero(String cadena) throws ExcepcionJuego {
		String datos = cadena.split("%")[0];
		String tablero = cadena.split("%")[1];	
		
		//Extraer los datos de la partida
		this.tamanioFilas = Integer.parseInt( datos.split(":")[0]);
		this.tamanioColumnas = Integer.parseInt( datos.split(":")[1]);
		this.numJugadores = Integer.parseInt( datos.split(":")[2]);
		this.turno = Integer.parseInt( datos.split(":")[3]);
		this.numJugadas = Integer.parseInt( datos.split(":")[4]);		
		if (Integer.parseInt( datos.split(":")[5])<0)
			ultimoMovimiento = null;
		else
			ultimoMovimiento = new MovimientoConecta4(Integer.parseInt( datos.split(":")[5]));		
		
		//Extraer el tamaño de la fila y de la columna
		for(int fila=0; fila<tamanioFilas; fila++) {
			String filaString = tablero.split("!")[fila];
			for(int columna=0; columna<tamanioColumnas; columna++ ) 
				this.tablero[fila][columna] = Integer.parseInt( filaString.split("=")[columna] );
		}
		estado = this.checkEstadoTablero();
		return;
	}
	
	/** Imprime el tablero de tal forma que sea legible 
	 * para un humano. */
	@Override
	public String toString() {
		String mesa = "\n****Tablero Conecta 4****\n";		
		/** Pintamos el tablero */		
		for(int fila=0; fila<tamanioFilas; fila++) {
			mesa+="{"+fila+"}";
			for(int col=0; col<tamanioColumnas; col++) {
				if (tablero[fila][col]!=-1)
					mesa += "["+tablero[fila][col]+"]";
				else
					mesa += "[ ]";
			}
			mesa += "\n";
		}
		/** Pintamos el numero de columnas */		
		mesa += "---------------------\n   "; 
		for(int i = 0;  i<tamanioColumnas; i++) {
			mesa += "{"+i+"}"; 			
		}
		return mesa;
	}
	
	/** Metodo get para el tamanio de las filas
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

	/** Devuelve el contenido en una posicion del tablero
	 * @param fila de la posicion
	 * @param columna de la posicion
	 * @return contenido del tablero
	 */
	public int getTablero(int fila, int columna){
	    if (tablero[fila][columna]==1 ||  tablero[fila][columna]==0)
	        return tablero[fila][columna];
	    else
	        return TableroConecta4.VACIO;
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
		if(fila == -1) fila +=1;

		if (contadorColumna(columna, fila)>=4) return Tablero.FINALIZADA; //Condicion de victoria

		if (contadorFila(columna, fila)>=4) return Tablero.FINALIZADA; //Condicion de victoria
		
		if (contadorDiagonalAscendente(columna, fila)>=4) return Tablero.FINALIZADA; //Condicion de victoria

		//Evaluacion final de la partida
		if (contadorDiagonalDescendente(columna, fila)>=4) return Tablero.FINALIZADA;
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

	/** Cuenta las fichas seguidas en columna de un jugador
	 * @param columna columna del movimiento
	 * @param fila fila del movimiento
	 * @return numero de fichas contiguas
	 */
	private int contadorColumna(int columna, int fila) {
		int contadorFichas = 0;
		for (int f = fila; f < fila + 4; f++) {
			if (f > tamanioFilas - 1 || (tablero[f][columna] != turno)) break;
			else contadorFichas += 1;
		}
		return contadorFichas;
	}

	/** Cuenta las fichas seguidas en fila
	 * @param columna columna del movimiento
	 * @param fila fila del movimiento
	 * @return numero de fichas contiguas
	 */
	private int contadorFila(int columna, int fila){
		int contadorFichas = 0;
		for(int c = columna; c > columna-4; c--) { 	//Lado izquierdo
			if(c<0 || (tablero[fila][c]!=turno)) break;
			else contadorFichas += 1;
		}

		for(int c = columna+1; c < columna+4; c++) { 	//Lado derecho
			if(c>tamanioColumnas-1 || (tablero[fila][c]!=turno)) break;
			else contadorFichas += 1;
		}
		return  contadorFichas;
	}

	/** Cuenta las fichas seguidas en una diagonal ascendente
	 * @param columna columna del movimiento
	 * @param fila fila del movimiento
	 * @return numero de fichas contiguas
	 */
	private int contadorDiagonalAscendente(int columna, int fila){
	    /*Comprobamos diagonales de la ficha*/
        //Diagonal abajo-derecha a arriba-izquierda [/]
        int diagonal_f = 0;
        int diagonal_c = 0;
        int contadorFichas=0;
        //Hacia arriba
        while(true) {
            if((fila+diagonal_f<0) || (columna+diagonal_c > tamanioColumnas-1) || tablero[fila+diagonal_f][columna+diagonal_c]!=turno)
                break;
            else{
                contadorFichas += 1;
                diagonal_f -=1;
                diagonal_c +=1;
            }
        }
        //Hacia abajo
        diagonal_f = +1;
        diagonal_c = -1;
        while(true) {
            if((fila+diagonal_f>tamanioFilas-1) || (columna+diagonal_c<0) || (tablero[fila+diagonal_f][columna+diagonal_c]!=turno))
                break;
            else{
                contadorFichas += 1;
                diagonal_f +=1;
                diagonal_c -=1;
            }
        }
        return contadorFichas;
    }

	/** Cuenta las fichas seguidas en una diagonal descendente
	 * @param columna columna del movimiento
	 * @param fila fila del movimiento
	 * @return numero de fichas contiguas
	 */
    private int contadorDiagonalDescendente(int columna, int fila){
        //Diagonal arriba-derecha a abajo-izquierda [\]
        int diagonal_f = 0;
        int diagonal_c = 0;
        int contadorFichas = 0;
        //Hacia arriba
        while(true) {
            if((fila+diagonal_f<0) || (columna+diagonal_c <0) || (tablero[fila+diagonal_f][columna+diagonal_c]!=turno))
                break;
            else {
                contadorFichas += 1;
                diagonal_f -=1;
                diagonal_c -=1;
            }
        }

        diagonal_f = +1;
        diagonal_c = +1;
        while(true) {
            if((fila+diagonal_f>tamanioFilas-1) || (columna+diagonal_c>tamanioColumnas-1) || (tablero[fila+diagonal_f][columna+diagonal_c]!=turno))
                break;
            else {
                contadorFichas += 1;
                diagonal_f +=1;
                diagonal_c +=1;
            }
        }
        return contadorFichas;
    }

}
