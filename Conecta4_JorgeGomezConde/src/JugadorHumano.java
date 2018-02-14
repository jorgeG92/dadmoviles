import es.uam.eps.multij.*;
import java.util.*;

/**
 * 
 * @author jorge
 *
 */
public class JugadorHumano implements Jugador{
	
	/** Nombre del jugador	*/
	private String nombre;
	
	/**
	 * Constructor de JugadorHumano por defecto
	 */	
	public JugadorHumano() { nombre = "default"; }
	
	/**
	 * Constructor de JugadorHumano que asigna el nombre elegido por
	 * parametro
	 * @param nombre
	 */
	public JugadorHumano(String nombre) {	this.nombre = nombre;	}
	
    @Override
    public String getNombre() { return nombre; }
    
    @Override
    public boolean puedeJugar(Tablero tablero) { return true; }
    
    @Override
    public void onCambioEnPartida(Evento evento) { 	
    	 
    	switch(evento.getTipo()) {
    	
    		case Evento.EVENTO_CAMBIO:
    			break;
    			
    		case Evento.EVENTO_CONFIRMA:    			
    			try {
        			/*Pedimos al usuario que confirme pulsando un boton*/
        			Scanner boton = new Scanner(System.in);
        			System.out.println("Pulsa cualquier boton para confirmar. \nPulsa 'N' si no estas de acuerdo.\n");
        			
        			/* Devolvemos la decision tomada en la partida */
        			if (boton.toString().compareTo("N")==0 || boton.toString().compareTo("n")==0) 
        				evento.getPartida().confirmaAccion(this, evento.getCausa(), false); 
        			else 
        				evento.getPartida().confirmaAccion(this, evento.getCausa(), true);
        			boton.close();
        			
    			}catch(Exception e) {
    				e.printStackTrace();
    			}
    			break;
    			
    		case Evento.EVENTO_ERROR:
    			break;
    			
    		case Evento.EVENTO_FIN:
    			break;
    			
    		case Evento.EVENTO_TURNO:
    			/*Pedimos la usuario que elija una columna*/
    			TableroConecta4 tablero = (TableroConecta4) evento.getPartida().getTablero();
    			MovimientoConecta4 mov = null;
    			int seleccion = -1;
    			Scanner boton = null;
    			/* Pedimos en bucle que se introduzca una columna valida */
    			while (seleccion<0 || mov==null || !tablero.esValido(mov)) {
	    			System.out.println("Jugador "+nombre+", elije una columna:\n");
	    			boton = new Scanner(System.in);
	    			int col = boton.nextInt();
	    			seleccion = generarMovimiento((TableroConecta4) evento.getPartida().getTablero(), col);
	    			if (seleccion <0)
	    				System.out.println("La columna no esta disponible "+nombre+"\n");
	    			else
	    				mov = (MovimientoConecta4) tablero.movimientosValidos().get(seleccion);	    			
    			}
    			/**boton.close();
    			boton.reset();**/
    			/* Realizamos accion */
    			try {
    				evento.getPartida().realizaAccion(new AccionMover(
            			this, tablero.movimientosValidos().get(seleccion)));
    			}
    			catch(Exception e) {
    				//Movimiento no valido???
    			}
    			break;
    	}    	
    }
    
    /**
     * Comprueba que la columna elegida por el jugador es valida
     * 
     * @param tablero tablero de juego
     * @param col columna delegida por el jugador
     * @return si hay movimiento en la columna elegida -> movimiento, si no NULL
     */
    private int generarMovimiento(TableroConecta4 tablero, int col) {   	
    	MovimientoConecta4 movimientoJugador = new MovimientoConecta4(col);
    	/* Recuperamos el moviemiento valido en la columna elegida */
    	for(int i = 0; i<tablero.movimientosValidos().size(); i++) {
    		if (movimientoJugador.equals((MovimientoConecta4) tablero.movimientosValidos().get(i)))
    			return col;    			
    	}
    	return -1;
    }

}
