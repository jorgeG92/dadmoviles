import es.uam.eps.multij.Evento;
import es.uam.eps.multij.PartidaListener;

/** Clase JugadorObservador que implementa la interfaz Partida Listener
 * 
 * @author Jorge Gomez Conde
 * @version 1.0 Febreo 18, 2018
 */
public class JugadorObservador implements PartidaListener{
	
	public JugadorObservador() {
		
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
			System.out.println("El utlimo movimiento fue: "+evento.getPartida().getTablero().getUltimoMovimiento().toString());
			System.out.println("El utlimo movimiento fue: "+evento.getPartida().getTablero().getTurno());
			break;
		case Evento.EVENTO_TURNO:
			System.out.println("(Observador) Turno: "+evento.getPartida().getTablero().getTurno());
			System.out.println(evento.getDescripcion()+"\n");
			break;
		}		
	}
	


}
