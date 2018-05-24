package eps.android4_jorgegomez.firedatabase;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import eps.android4_jorgegomez.activities.ConectPreferenceActivity;
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
    
    private static final String ROUNDVAR_PLAYER1_ID = "firstPlayerUUID";
    private static final String ROUNDVAR_PLAYER1_NAME = "firstPlayerName";
    private static final String ROUNDVAR_PLAYER2_ID = "secondPlayerUUID";
    private static final String ROUNDVAR_PLAYER2_NAME = "secondPlayerName";
    private static final String ROUNDVAR_ID = "id";
    private static final String ROUNDVAR_DATE = "date";
    private static final String ROUNDVAR_TITLE = "title";
    private static final String ROUNDVAR_BOARD = "board";

    private static final String FBD_ROUNDS = "partidas";
    private static final String FBD_USERS = "usuarios";
    private static final String FBD_USERS_ROUNDS = "lista_partidas";
    private static final String FBD_USERS_NAME = "nombre";
                
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
                        else
                            callback.onError("No se ha podido loggear el usuario");
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
                    if (task.isSuccessful())
                        callback.onLogin(task.getResult().getUser().getUid());
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
                List<String> roundsSting = (List<String> ) dataSnapshot.child(FBD_USERS).child(playeruuid).child(FBD_USERS_ROUNDS).getValue();
                List<Round> rondas_aux = new ArrayList();
                if (roundsSting!=null) {
                    for (String rs : roundsSting) {
                        round = readRoundFromFirebase(dataSnapshot, rs);
                        if (round != null) {
                            rounds.add(round);
                            rondas_aux.add(round);
                        }
                    }
                }
                callback.onResponse(rondas_aux);
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

            if (var.equals(ROUNDVAR_DATE))
                round.setDate((String) roundMap.get(var));
            else if (var.equals(ROUNDVAR_ID))
                round.setId((String) roundMap.get(var));
            else if (var.equals(ROUNDVAR_PLAYER1_ID))
                round.setFirstPlayerUUID((String) roundMap.get(var));
            else if (var.equals(ROUNDVAR_PLAYER1_NAME))
                round.setFirstPlayerName((String) roundMap.get(var));
            else if (var.equals(ROUNDVAR_PLAYER2_NAME))
                round.setSecondPlayerName((String) roundMap.get(var));
            else if (var.equals(ROUNDVAR_PLAYER2_ID))
                round.setSecondPlayerUUID((String) roundMap.get(var));
            else if (var.equals(ROUNDVAR_TITLE))
                round.setTitle((String) roundMap.get(var));
            else if (var.equals(ROUNDVAR_BOARD)) {
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
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child(FBD_ROUNDS).child(round.getId()).setValue(round);
        db.child(FBD_ROUNDS).child(round.getId()).child(ROUNDVAR_BOARD).setValue(round.getBoard().tableroToString());

        //Añadimos la ronda a la lista de partidas del usuario
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                List<String> rondas_uno, rondas_dos;
                Map<String, String> playersInfo = playersInfo(dataSnapshot, round);
                
                db.child(FBD_ROUNDS).child(round.getId()).child(ROUNDVAR_PLAYER1_NAME).setValue(playersInfo.get(ROUNDVAR_PLAYER1_NAME));
                db.child(FBD_ROUNDS).child(round.getId()).child(ROUNDVAR_PLAYER1_ID).setValue(playersInfo.get(ROUNDVAR_PLAYER1_ID));
                db.child(FBD_ROUNDS).child(round.getId()).child(ROUNDVAR_PLAYER2_NAME).setValue(playersInfo.get(ROUNDVAR_PLAYER2_NAME));
                db.child(FBD_ROUNDS).child(round.getId()).child(ROUNDVAR_PLAYER2_ID).setValue(playersInfo.get(ROUNDVAR_PLAYER2_ID));

                //Para el usuario 1
                rondas_uno = (List<String>) dataSnapshot.child(FBD_USERS).child(playersInfo.get(ROUNDVAR_PLAYER1_ID)).child(FBD_USERS_ROUNDS).getValue();
                if(rondas_uno==null)
                    rondas_uno = new ArrayList<>();
                rondas_uno.add(round.getId());
                db.child(FBD_USERS).child(playersInfo.get(ROUNDVAR_PLAYER1_ID)).child(FBD_USERS_ROUNDS).setValue(rondas_uno);

                //Para el usuario 2
                rondas_dos = (List<String>) dataSnapshot.child(FBD_USERS).child(playersInfo.get(ROUNDVAR_PLAYER2_ID)).child(FBD_USERS_ROUNDS).getValue();
                if(rondas_dos==null)
                    rondas_dos = new ArrayList<>();
                rondas_dos.add(round.getId());
                db.child(FBD_USERS).child(playersInfo.get(ROUNDVAR_PLAYER2_ID)).child(FBD_USERS_ROUNDS).setValue(rondas_dos);

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

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                db.child(FBD_ROUNDS).child(round.getId()).child(ROUNDVAR_BOARD).setValue(round.getBoard().tableroToString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("DEBUG", "No se pudo actualizar la ronda");
            }
        });
    }

    /** Establece el nombre de un jugador en la base de datos
     * @param playername Nombre nuevo para el jugador
     */
    public void setPlayerNameSettings(final String playername, final String playerid){
        if (!playername.equals(ConectPreferenceActivity.PLAYERNAME_DEFAULT))
            db.child(FBD_USERS).child(playerid).child(FBD_USERS_NAME).setValue(playername);

    }

    /** Obtiene el usuario que ha creado la ronda y asigna un jugador aleatoramente
     *  a la partida
     *
     * @param dataSnapshot
     * @param round Ronda de a obtener usuarios
     * @return Map con el ID y nombre de los usuarios
     */
    private Map<String, String> playersInfo(DataSnapshot dataSnapshot, Round round) {
        Map<String, Object> userIds;
        Map<String, String> map = new HashMap<>();
        Random r = new Random();
        String firstPlayerName=null, firstPlayerUUID=null, secondPlayerName=null, secondPlayerUUID=null;
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        List<String> users = new ArrayList<>();

        userIds = (Map<String, Object>) dataSnapshot.child(FBD_USERS).getValue();
        users.addAll(userIds.keySet());

        if (users.contains("0000"))
            users.remove("0000");

        if (round.getFirstPlayerUUID().equals("0000-0000-0000")){
            secondPlayerName = round.getSecondPlayerName();
            secondPlayerUUID = round.getSecondPlayerUUID();
            while (firstPlayerUUID == null || secondPlayerUUID.equals(firstPlayerUUID))
                firstPlayerUUID = users.get(r.nextInt(userIds.size()-1));
            firstPlayerName = (String) dataSnapshot.child(FBD_USERS).child(firstPlayerUUID).child(FBD_USERS_NAME).getValue();
        }else{
            firstPlayerName = round.getFirstPlayerName();
            firstPlayerUUID = round.getFirstPlayerUUID();
            while (secondPlayerUUID == null || firstPlayerUUID.equals(secondPlayerUUID))
                secondPlayerUUID = users.get(r.nextInt(userIds.size()-1));
            secondPlayerName = (String) dataSnapshot.child(FBD_USERS).child(secondPlayerUUID).child(FBD_USERS_NAME).getValue();
        }

        map.put(ROUNDVAR_PLAYER1_NAME, firstPlayerName);
        map.put(ROUNDVAR_PLAYER1_ID, firstPlayerUUID);
        map.put(ROUNDVAR_PLAYER2_NAME, secondPlayerName);
        map.put(ROUNDVAR_PLAYER2_ID, secondPlayerUUID);
        return map;
    }

}


