import es.uam.eps.multij.Movimiento;

public class MovimientoConecta4 extends Movimiento {
	
	private int fila;
	private int columna;
	
	public MovimientoConecta4(int fila, int columna) {
		this.fila = fila;
		this.columna = columna;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return fila+":"+columna;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		MovimientoConecta4 m2 = (MovimientoConecta4) o;
		return (m2.fila==fila)&(m2.columna==columna);
	}

}
