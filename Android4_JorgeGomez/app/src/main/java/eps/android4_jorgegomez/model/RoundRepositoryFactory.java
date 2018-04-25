package eps.android4_jorgegomez.model;

import android.content.Context;

import eps.android4_jorgegomez.database.ConectDataBase;

public class RoundRepositoryFactory {

    private static final boolean LOCAL = true;

    public static RoundRepository createRepository(Context context){
        RoundRepository repository;

        repository = LOCAL ? new ConectDataBase(context) : new ConectDataBase(context);

        try{
            repository.open();
        }
        catch (Exception e){
            return null;
        }

        return repository;
    }
}
