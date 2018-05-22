package eps.android4_jorgegomez.model;

import android.content.Context;

import eps.android4_jorgegomez.database.ConectDataBase;
import eps.android4_jorgegomez.firedatabase.FBDataBase;

public class RoundRepositoryFactory {

    public static RoundRepository createRepository(Context context, boolean offlineFlag){
        RoundRepository repository;

        //Como gestionar para saber si estamos el LOCAL o ONLINE
        repository = offlineFlag ? new ConectDataBase(context) : new FBDataBase();

        try{
            repository.open();
        }
        catch (Exception e){
            return null;
        }

        return repository;
    }
}
