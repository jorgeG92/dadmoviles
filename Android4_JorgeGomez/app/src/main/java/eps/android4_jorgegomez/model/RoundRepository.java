package eps.android4_jorgegomez.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class RoundRepository {

    public static final int SIZE = 4;
    private static RoundRepository repository;
    private List<Round> rounds;

    public static RoundRepository get(Context context) {
        if (repository == null) {
            repository = new RoundRepository(context);
        }
        return repository;
    }

    private RoundRepository(Context context) {
        rounds = new ArrayList<Round>();
    }

    public List<Round> getRounds() {
        return rounds;
    }

    public Round getRound(String id) {
        for (Round round : rounds) {
            if (round.getId().equals(id))
                return round;
        }
        return null;
    }

    public void addRound(Round round) { rounds.add(round); }
}