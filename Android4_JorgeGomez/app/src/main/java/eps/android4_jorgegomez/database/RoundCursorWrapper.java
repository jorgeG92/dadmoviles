package eps.android4_jorgegomez.database;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Log;

import eps.android4_jorgegomez.model.Round;
import es.uam.eps.multij.ExcepcionJuego;
import eps.android4_jorgegomez.database.RoundDataBaseSchema.UserTable;
import eps.android4_jorgegomez.database.RoundDataBaseSchema.RoundTable;

public class RoundCursorWrapper extends CursorWrapper{

    public RoundCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Round getRound() {
        String playername = getString(getColumnIndex(UserTable.Cols.PLAYERNAME));
        String playeruuid = getString(getColumnIndex(UserTable.Cols.PLAYERUUID));
        String rounduuid = getString(getColumnIndex(RoundTable.Cols.ROUNDUUID));
        String date = getString(getColumnIndex(RoundTable.Cols.DATE));
        String title = getString(getColumnIndex(RoundTable.Cols.TITLE));
        String size = getString(getColumnIndex(RoundTable.Cols.SIZE));
        String board = getString(getColumnIndex(RoundTable.Cols.BOARD));

        Round round = new Round(Integer.parseInt(size));
        round.setFirstPlayerName("random");
        round.setSecondPlayerName(playername);
        round.setFirstPlayerUUID(playeruuid);
        round.setId(rounduuid);
        round.setDate(date);
        round.setTitle(title);

        try {
            round.getBoard().stringToTablero(board);
        } catch (ExcepcionJuego e) {
            Log.d("DEBUG", "Error turning string into tablero");
        } catch (NullPointerException n){
            Log.d("DEBUG", "No hay tablero.");
        }

        return round;
    }
}
