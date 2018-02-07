import es.uam.eps.multij.*;
import java.util.*;

public class JugadorHumano implements Jugador{
	
	/** Nombre del jugador	 */
	private String nombre;
		
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
    			System.out.print("Evento cambio\n");
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
    				//
    			}
    			break;
    			
    		case Evento.EVENTO_ERROR:
    			System.out.print("Evento error\n");
    			System.out.print("ERROR\n"+evento.getCausa().toString());
    			break;
    			
    		case Evento.EVENTO_FIN:
    			System.out.print("Final de la partida");
    			break;
    			
    		case Evento.EVENTO_TURNO:
    			/*Pintamos el tablero*/
    			System.out.println(evento.getPartida().getTablero().toString());
    			/*Pedimos la usuario que elija una columna*/
    			TableroConecta4 tablero = (TableroConecta4) evento.getPartida().getTablero();
    			MovimientoConecta4 mov = null;
    			int seleccion = -1;
    			
    			
    			Scanner boton = null;
    			/* Pedimos en bucle que se introduzca una columna valida */
    			while (seleccion<0 || mov==null || !tablero.esValido(mov)) {
	    			System.out.println("Elije una columna:\n");
	    			boton = new Scanner(System.in);
	    			int col = boton.nextInt();
	    			seleccion = generarMovimiento((TableroConecta4) evento.getPartida().getTablero(), col);
	    			if (seleccion <0)
	    				System.out.println("La columna no esta disponible\n");
	    			else
	    				mov = (MovimientoConecta4) tablero.movimientosValidos().get(seleccion);	    			
    			}
    			boton.close();
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
    	/*Mostrara la descripcion del evento*/
    	System.out.print("Descripcion: "+evento.getDescripcion()+"\n");
    	
    }
    
    /**
     * Comprueba que la columna elegida por el jugador es valida
     * 
     * @param tablero tablero de juego
     * @param col columna delegida por el jugador
     * @return si hay movimiento en la columna elegida -> movimiento, si no NULL
     */
    private int generarMovimiento(TableroConecta4 tablero, int col) {   	
    	
    	/* Recuperamos el moviemiento valido en la columna elegida */
    	for(int iCol=0; iCol < tablero.getTamanioColumnas(); iCol++) {
    		MovimientoConecta4 mov = (MovimientoConecta4) tablero.movimientosValidos().get(iCol);
    		if (mov.getColumna() == col)
    			return iCol;    			
    	}
    	return -1;
    }

}
