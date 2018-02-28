import es.uam.eps.multij.*;
import java.util.*;

/** Clase JugadorHumano que implementa la interfaz Jugador
 * 
 * @author Jorge Gomez Conde
 * @version 1.0 Febreo 18, 2018
 */
public class JugadorHumanoConecta4 implements Jugador{
	
	/** Nombre del jugador	*/
	private String nombre;
	
	/**
	 * Constructor de JugadorHumano por defecto
	 */	
	public JugadorHumanoConecta4() { nombre = "default"; }
	
	/**
	 * Constructor de JugadorHumano que asigna el nombre elegido por
	 * parametro
	 * @param nombre
	 */
	public JugadorHumanoConecta4(String nombre) {	this.nombre = nombre;	}
	
	/** Metodo get para nombre	 */
    @Override
    public String getNombre() { return nombre; }
    
    /** Metodo que define si el jugador puede jugar, por defecto siempre puede */
    @Override
    public boolean puedeJugar(Tablero tablero) { return true; }
    
    /** 
     * 
     * @param evento describe lo sucdido en la partida
     */
    @Override
    public void onCambioEnPartida(Evento evento) { 	
    	 
    	switch(evento.getTipo()) {
    	
    		case Evento.EVENTO_CAMBIO:
    			break;
    			
    		case Evento.EVENTO_CONFIRMA:    			
    			try {

        			Scanner boton = new Scanner(System.in); //Pedimos al usuario que confirme pulsando un boton
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
    			System.out.println(evento.getDescripcion());
    			break;
    			
    		case Evento.EVENTO_FIN:
    			break;
    			
    		case Evento.EVENTO_TURNO:
    			// Pedimos la usuario que elija una columna
    			TableroConecta4 tablero = (TableroConecta4) evento.getPartida().getTablero();
    			MovimientoConecta4 mov = null;
    			int seleccion = -1;
    			Scanner boton = null;
    			// Pedimos en bucle que se introduzca una columna valida
    			while (seleccion<0 || mov==null || !tablero.esValido(mov)) {
	    			System.out.println("Jugador "+nombre+", elije una columna:\n");
	    			String col = new String();
		    		try {
		    			boton = new Scanner(System.in);
		    			col = boton.next().trim();
		    			seleccion = generarMovimiento((TableroConecta4) evento.getPartida().getTablero(), Integer.parseInt(col));
		    			if (seleccion <0)
		    				System.out.println("La columna no esta disponible "+nombre+"\n");
		    			else
		    				mov = (MovimientoConecta4) tablero.movimientosValidos().get(seleccion);
	    			}catch(NumberFormatException nfe) {
	    				if (col.length()>1)
	    					this.onCambioEnPartida(new Evento(Evento.EVENTO_TURNO, "No se permiten cadenas tan largas",null,null));
	    				else
	    					this.onCambioEnPartida(new Evento(Evento.EVENTO_TURNO, "Formato no permitido",null,null));
	    			}
	    			catch(InputMismatchException ime) {
	    				this.onCambioEnPartida(new Evento(Evento.EVENTO_TURNO, "Formato no permitido",null,null));
	    			}
	    		
	    			
    			}
    			
    			try {
    				evento.getPartida().realizaAccion(new AccionMover( // Realizamos accion
            			this, tablero.movimientosValidos().get(seleccion)));
    			}
    			catch(Exception e) {
    				this.onCambioEnPartida(new Evento(Evento.EVENTO_ERROR, "No se pudo realizar el movimiento", null, null));
    			}
    			break;
    	}    	
    }
    
    /** Comprueba que la columna elegida por el jugador es valida
     * 
     * @param tablero tablero de juego
     * @param col columna delegida por el jugador
     * @return si hay movimiento en la columna elegida -> movimiento, si no NULL
     */
    private int generarMovimiento(TableroConecta4 tablero, int col) {
    	MovimientoConecta4 movimientoJugador = new MovimientoConecta4(col);    	// Recuperamos el moviemiento valido en la columna elegida
    	for(int i = 0; i<tablero.movimientosValidos().size(); i++) {
    		if (movimientoJugador.equals((MovimientoConecta4) tablero.movimientosValidos().get(i)))
    			return col;    			
    	}
    	return -1;
    }

}
