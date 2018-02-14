import es.uam.eps.multij.Movimiento;

/**
 * 
 * @author jorge
 *
 */
public class MovimientoConecta4 extends Movimiento {
	
	/** Columna elegida por el jugador*/
	private int columna;
	
	/**
	 * Constructor de MoviemientoConecta4
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
	
	/**
	 * @return columna correspondiente al movimieto
	 */
	public int getColumna() {
		return columna;
	}

}
