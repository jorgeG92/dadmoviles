package eps.android4_jorgegomez.firedatabase;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import eps.android4_jorgegomez.model.ConectBoard;
import eps.android4_jorgegomez.model.Round;
import eps.android4_jorgegomez.model.RoundRepository;
import es.uam.eps.multij.ExcepcionJuego;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FBDataBase implements RoundRepository {

    private DatabaseReference db;

    public FBDataBase(){
        this.db = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void open() throws Exception {

    }

    @Override
    public void close() {

    }

    @Override
    public void login(final String playername, String password, final LoginRegisterCallback callback) {
        final FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(playername, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                            callback.onLogin(task.getResult().getUser().getUid());
                    }
                });

    }

    @Override
    public void register(String playername, String password, final LoginRegisterCallback callback) {
        final  FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(playername, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    AuthResult result = task.getResult();
                    FirebaseUser user = result.getUser();
                    if (task.isSuccessful())
                        callback.onLogin(user.getUid());
                    else
                        callback.onError("No se ha podido registrar el usuario");

            }
        });

    }

    @Override
    public List<Round> getRounds(final String playeruuid, String orderByField, String group, final RoundsCallback callback) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        final List<Round> rounds = new ArrayList<>();

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            Round round;
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                List<String> roundsSting = (List<String> ) dataSnapshot.child("usuarios").child(playeruuid).child("lista_partidas").getValue();

                if (roundsSting!=null) {
                    for (String rs : roundsSting) {
                        round = readRoundFromFirebase(dataSnapshot, rs);
                        if (round != null)
                            rounds.add(round);
                    }
                }
                callback.onResponse(rounds);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("DEBUG", "FBDataBase.getRounds:onCancelled. No se pudieron obtener las partidas");
                callback.onError("FBDataBase.getRounds:onCancelled. No se pudieron obtener las partidas");
            }
        });

        return rounds;
    }

    private Round readRoundFromFirebase(DataSnapshot dataSnapshot, String roundString){
        long size_long = (Long) dataSnapshot.child("partidas").child(roundString).child("size").getValue();
        Round round = new Round((int) size_long);
        ConectBoard board = new ConectBoard((int) size_long);
        Map<String, Object> roundMap = (Map<String, Object>) dataSnapshot.child("partidas").child(roundString).getValue();

        for (String var: roundMap.keySet()){

            if (var.equals("date"))
                round.setDate((String) roundMap.get(var));
            else if (var.equals("id"))
                round.setId((String) roundMap.get(var));
            else if (var.equals("firstPlayerId"))
                round.setFirstPlayerUUID((String) roundMap.get(var));
            else if (var.equals("firstPlayerName"))
                round.setFirstPlayerName((String) roundMap.get(var));
            else if (var.equals("secondPlayerName"))
                round.setSecondPlayerName((String) roundMap.get(var));
            else if (var.equals("secondPlayerId"))
                round.setSecondPlayerUUID((String) roundMap.get(var));
            else if (var.equals("title"))
                round.setTitle((String) roundMap.get(var));
            else if (var.equals("board")) {
                try {
                    board.stringToTablero((String) roundMap.get(var));
                } catch (ExcepcionJuego excepcionJuego) {
                    excepcionJuego.printStackTrace();
                }
                round.setBoard(board);
            }

        }
        return round;

    }


    @Override
    public void addRound(final Round round, final BooleanCallback callback) {
        //Añadimos ronda a la BD
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("partidas").child(round.getId()).setValue(round);
        db.child("partidas").child(round.getId()).child("board").setValue(round.getBoard().tableroToString());

        //Añadimos la ronda a la lista de partidas del usuario
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                List<String> rondas_uno, rondas_dos;

                //Para el usuario 1
                rondas_uno = (List<String>) dataSnapshot.child("usuarios").child(round.getFirstPlayerUUID()).child("lista_partidas").getValue();
                if(rondas_uno==null)
                    rondas_uno = new ArrayList<>();
                rondas_uno.add(round.getId());
                db.child("usuarios").child(round.getFirstPlayerUUID()).child("lista_partidas").setValue(rondas_uno);

                //Para el usuario 2
                rondas_dos = (List<String>) dataSnapshot.child("usuarios").child(round.getFirstPlayerUUID()).child("lista_partidas").getValue();
                if(rondas_dos==null)
                    rondas_dos = new ArrayList<>();
                rondas_dos.add(round.getId());
                db.child("usuarios").child(round.getSecondPlayerUUID()).child("lista_partidas").setValue(rondas_dos);

                callback.onResponse(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("DEBUG", "Error al añadir una nueva ronda");
            }
        });
    }

    @Override
    public void updateRound(final Round round, BooleanCallback callback) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                db.child("partidas").child(round.getId()).child("board").setValue(round.getBoard().tableroToString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("DEBUG", "No se pudo actualizar la ronda");
            }
        });
    }
}
