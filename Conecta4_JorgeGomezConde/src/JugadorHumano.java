import es.uam.eps.multij.*;
import java.util.*;

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
    	/*Mostrara la descripcion del evento*/
    	System.out.print(evento.getDescripcion()+"\n");
    	 
    	switch(evento.getTipo()) {
    	
    		case Evento.EVENTO_CAMBIO:
    			System.out.print("Evento cambio");
    			break;
    			
    		case Evento.EVENTO_CONFIRMA:    			
    			try {
        			/*Pedimos al usuario que confirme pulsando un boton*/
        			Scanner boton = new Scanner(System.in);
        			System.out.println("Pulsa cualquier boton para confirmar. \nPulsa 'N' si no estas de acuerdo.\n");
        			
        			/* Devolvemos la decision tomada en la partida */
        			if (boton.toString().compareTo("N")==0 || boton.toString().compareTo("n")==0) {
        				evento.getPartida().confirmaAccion(this, evento.getCausa(), false); 
        			}else {
        				evento.getPartida().confirmaAccion(this, evento.getCausa(), true);
        			}
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
    			/*Pedimos la usuario que elija una columna*/
    			TableroConecta4 tablero = (TableroConecta4) evento.getPartida().getTablero();
    			int seleccion = -1;
    			
    			/* Pedimos en bucle que se introduzca una columna valida */
    			while (seleccion<0) {
	    			Scanner boton = new Scanner(System.in);
	    			System.out.println("Elije una columna:\n");    			
	    			int col = Integer.parseInt(boton.toString());
	    			seleccion = generarMovimiento((TableroConecta4) evento.getPartida().getTablero(), col);
	    			if (seleccion <0)
	    				System.out.println("La columna no esta disponible\n");
    			}
    			/* Realizamos accion */
    			evento.getPartida().realizaAccion(new AccionMover(
            			this, t.movimientosValidos().get(seleccion)));    			
    			break;
    	}  	
    	
    }
    
    /**
     * Devuelve el movimiento valido elegida una columna
     * 
     * @param tablero tablero de juego
     * @param col columna delegida por el jugador
     * @return si hay movimiento en la columna elegida -> movimiento, si no NULL
     */
    private int generarMovimiento(TableroConecta4 tablero, int col) {   	
    	
    	/* Recuperamos el moviemiento valido en la columna elegida */
    	for(int i=0; i < tablero.getTamanio(); i++) {
    		MovimientoConecta4 mov = (MovimientoConecta4) tablero.movimientosValidos().get(i);
    		if (mov.getColumna() == col)
    			return i;    			
    	}
    	return -1;
    }

}
