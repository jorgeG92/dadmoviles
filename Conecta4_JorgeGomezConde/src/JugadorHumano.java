import es.uam.eps.multij.*;

public class JugadorHumano implements Jugador{
	
	private String nombre;
	private int turno;
	
	public JugadorHumano() {}
	
	public JugadorHumano(String nombre) {
		this.nombre = nombre;
	}
	
    @Override
    public String getNombre() {
    	return nombre;
    }
    
    @Override
    public boolean puedeJugar(Tablero tablero) {
    	//return tablero.getTurno()==turno;
    	return true;
    }
    
    @Override
    public void onCambioEnPartida(Evento evento) {
    	switch(evento.getTipo()) {
    		case Evento.EVENTO_CAMBIO:
    			System.out.print("Evento cambio");
    			break;
    		case Evento.EVENTO_CONFIRMA:
    			System.out.print("Evento confirma");
    			break;
    		case Evento.EVENTO_ERROR:
    			System.out.print("Evento error");
    			break;
    		case Evento.EVENTO_FIN:
    			System.out.print("Evento fin");
    			break;
    		case Evento.EVENTO_TURNO:
    			System.out.print("Evento turno");
    			break;
    	}
    	
    }    

}
