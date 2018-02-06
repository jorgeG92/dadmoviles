import java.util.ArrayList;

import es.uam.eps.multij.ExcepcionJuego;
import es.uam.eps.multij.Movimiento;
import es.uam.eps.multij.Tablero;

public class TableroConecta4 extends Tablero {
	
	private ArrayList<MovimientoConecta4> movimientosValidos;
	private int numColumnas;
	
	
	public TableroConecta4() {		
		movimientosValidos = new ArrayList<MovimientoConecta4>();
		
		for (int i = 0; i<numColumnas; i++) {
			movimientosValidos.add(new MovimientoConecta4(1,i+1));
		}
	}

	@Override
	protected void mueve(Movimiento m) throws ExcepcionJuego {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean esValido(Movimiento m) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<Movimiento> movimientosValidos() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String tableroToString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void stringToTablero(String cadena) throws ExcepcionJuego {
		// TODO Auto-generated method stub

	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

}
