import es.uam.eps.multij.Evento;
import es.uam.eps.multij.Jugador;
import es.uam.eps.multij.Tablero;

/**
 * 
 * @author jorge
 *
 */
public class JugadorObservador implements Jugador{
	
	public JugadorObservador() {
		
	}
	
	@Override
	public String getNombre() {
		return "Jugador Observador";
		
	}
	    
	@Override
	public boolean puedeJugar(Tablero tablero) {	
		return false;		
	}
	
	@Override
	public void onCambioEnPartida(Evento evento) {
		switch(evento.getTipo()) {
		case Evento.EVENTO_CAMBIO:
			System.out.println("(Observador) Cambio: ");
			System.out.println(evento.getDescripcion()+"\n");
			System.out.println(evento.getPartida().getTablero().toString());
			break;
			
		case Evento.EVENTO_CONFIRMA:
			System.out.println("(Observador) Confirma: ");
			System.out.println(evento.getDescripcion()+"\n");
			break;
			
		case Evento.EVENTO_ERROR:
			System.out.println("(Observador) Error: ");
			System.out.println(evento.getDescripcion()+"\n");
			break;
		case Evento.EVENTO_FIN:
			System.out.println("(Observador) Fin: ");
			System.out.println(evento.getDescripcion()+"\n");
			break;
		case Evento.EVENTO_TURNO:
			System.out.println("(Observador) Turno: "+evento.getPartida().getTablero().getTurno());
			System.out.println(evento.getDescripcion()+"\n");
			break;
		}		
	}
	


}
