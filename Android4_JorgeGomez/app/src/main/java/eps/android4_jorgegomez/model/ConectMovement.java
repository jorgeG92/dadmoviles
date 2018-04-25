package eps.android4_jorgegomez.model;

import es.uam.eps.multij.Movimiento;

/** Clase ConectMovement que extiende la clase Movimiento.
 * Orientada a ser utilizada unicamente para el juego Conecta 4
 *
 * @author Jorge Gomez Conde
 * @version 1.0 Febreo 18, 2018
 */
public class ConectMovement extends Movimiento {

    /** Columna elegida por el jugador*/
    private int columna;
    private int fila;

    /** Constructor de MoviemientoConecta4
     * @param columna elegida por el jugador
     */
    public ConectMovement(int columna) { this.columna = columna; fila =-1; }

    public ConectMovement(int fila, int columna) { this.columna = columna; this.fila=fila;}

    @Override
    public String toString() {
        String rtrn =  "Columna: "+columna;
        if (fila<0) return rtrn;
        else return rtrn + " Fila: "+fila;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ConectMovement))
            return false;
        else
            return this.toString().equals(o.toString());
    }

    /** Metodo get para la columna
     * @return columna correspondiente al movimieto
     */
    public int getColumna() {
        return columna;
    }

    /** Metodo get para la fila
     * @return fila correspondiente al movimieto
     */
    public int getFila() { return fila; }

    /** Metodo set para el parametro fila
     * @param fila
     */
    public void setFila(int fila) { this.fila = fila; }

}
