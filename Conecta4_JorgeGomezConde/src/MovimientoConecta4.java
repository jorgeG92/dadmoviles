import es.uam.eps.multij.Movimiento;

/** Clase MovimientoConecta4 que extiende la clase Movimiento.
 * Orientada a ser utilizada unicamente para el juego Conecta 4
 * 
 * @author Jorge Gomez Conde
 * @version 1.0 Febreo 18, 2018
 */
public class MovimientoConecta4 extends Movimiento {
	
	/** Columna elegida por el jugador*/
	private int columna;
	
	/** Constructor de MoviemientoConecta4
	 * @param columna elegida por el jugador
	 */
	public MovimientoConecta4(int columna) { this.columna = columna; }

	@Override
	public String toString() { return "Columna: "+columna;	}

	@Override
	public boolean equals(Object o) {
		MovimientoConecta4 m2 = (MovimientoConecta4) o;
		return m2.columna==columna;
	}
	
	/** Metodo get para la columna
	 * @return columna correspondiente al movimieto
	 */
	public int getColumna() {
		return columna;
	}

}
