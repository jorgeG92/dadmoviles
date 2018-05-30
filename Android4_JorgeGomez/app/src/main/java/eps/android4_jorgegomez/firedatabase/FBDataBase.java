package eps.android4_jorgegomez.firedatabase;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import eps.android4_jorgegomez.activities.AlertDialogSelectPlayerFragment;
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

        db.addValueEventListener(new ValueEventListener() {
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
        try {
            long size_long = (Long) dataSnapshot.child("partidas").child(roundString).child("size").getValue();
            Round round = new Round((int) size_long);
            ConectBoard board = new ConectBoard((int) size_long);
            Map<String, Object> roundMap = (Map<String, Object>) dataSnapshot.child("partidas").child(roundString).getValue();

            for (String var : roundMap.keySet()) {

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

        }catch (NullPointerException n){
            return null;
        }
    }

    /**
     *
     * @return
     */
    public Map<String, String> getUsers(final UsersCallback callback){
        final Map<String, String> usersIdName_aux = new HashMap<>();
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> users = ((Map<String, Object>) dataSnapshot.child(FBD_USERS).getValue());
                Map<String, String> usersIdName = new HashMap<>();
                List<String> usersId = new ArrayList<>();

                usersId.addAll(users.keySet());
                if (usersId.contains("0000"))
                    usersId.remove("0000");

                for (String id : usersId)
                    usersIdName.put(id, (String) dataSnapshot.child(FBD_USERS).child(id).child(FBD_USERS_NAME).getValue());

                callback.onResponse(usersIdName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("DEBUG", "FBDataBase.getUsers:onCancelled. No se pudieron obtener las partidas");
                callback.onError("FBDataBase.getRounds:onCancelled. No se pudieron obtener las partidas");
            }
        });

        return usersIdName_aux;
    }


    @Override
    public void addRound(final Round round, final BooleanCallback callback) {}

    @Override
    public void addRound(final Round round, final BooleanCallback callback, final boolean randomFlag) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child(FBD_ROUNDS).child(round.getId()).setValue(round);
        db.child(FBD_ROUNDS).child(round.getId()).child(ROUNDVAR_BOARD).setValue(round.getBoard().tableroToString());

        //Añadimos la ronda a la lista de partidas del usuario
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                List<String> listUserRounds;
                Map<String, String> playersInfo = playersInfo(dataSnapshot, round, randomFlag);
                
                db.child(FBD_ROUNDS).child(round.getId()).child(ROUNDVAR_PLAYER1_NAME).setValue(playersInfo.get(ROUNDVAR_PLAYER1_NAME));
                db.child(FBD_ROUNDS).child(round.getId()).child(ROUNDVAR_PLAYER1_ID).setValue(playersInfo.get(ROUNDVAR_PLAYER1_ID));
                db.child(FBD_ROUNDS).child(round.getId()).child(ROUNDVAR_PLAYER2_NAME).setValue(playersInfo.get(ROUNDVAR_PLAYER2_NAME));
                db.child(FBD_ROUNDS).child(round.getId()).child(ROUNDVAR_PLAYER2_ID).setValue(playersInfo.get(ROUNDVAR_PLAYER2_ID));

                //Para el usuario 1
                listUserRounds = lista_rondas(dataSnapshot,round,playersInfo.get(ROUNDVAR_PLAYER1_ID));
                db.child(FBD_USERS).child(playersInfo.get(ROUNDVAR_PLAYER1_ID)).child(FBD_USERS_ROUNDS).setValue(listUserRounds);

                //Para el usuario 2
                listUserRounds = lista_rondas(dataSnapshot,round,playersInfo.get(ROUNDVAR_PLAYER2_ID));
                db.child(FBD_USERS).child(playersInfo.get(ROUNDVAR_PLAYER2_ID)).child(FBD_USERS_ROUNDS).setValue(listUserRounds);

                callback.onResponse(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("DEBUG", "Error al añadir una nueva ronda");
            }
        });
    }

    @Override
    public void deleteRound(final Round round, BooleanCallback callback) {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        /* Tabla partidas */
        db.child(FBD_ROUNDS).child(round.getId()).removeValue();

        /* Tabla usuarios */
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                List<String> lr =lista_rondas(dataSnapshot,round, round.getFirstPlayerUUID());
                List<String> lr_aux = new ArrayList<>();
                //Borrando enlace con jugador 1
                for(String roundId: lr)
                    if (!roundId.equals(round.getId()))
                        lr_aux.add(roundId);
                db.child(FBD_USERS).child(round.getFirstPlayerUUID()).child(FBD_USERS_ROUNDS).setValue(lr_aux);
                //Borrando enlace con jugador 2
                lr_aux = new ArrayList<>();
                lr = lista_rondas(dataSnapshot,round, round.getSecondPlayerUUID());
                for(String roundId: lr)
                    if (!roundId.equals(round.getId()))
                        lr_aux.add(roundId);
                db.child(FBD_USERS).child(round.getSecondPlayerUUID()).child(FBD_USERS_ROUNDS).setValue(lr_aux);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("DEBUG", "Error al borrar una ronda");
            }
        });

        callback.onResponse(true);
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

    /** Obtiene el usuario que ha creado la ronda y asigna un jugardor de Firebase aleatoramente
     *  o previemente definido a la partida.
     *
     * @param dataSnapshot
     * @param round Ronda de a obtener usuarios
     * @param randomFlag establece si la asignacion es aleatoria o definida
     * @return Map con el ID y nombre de los usuarios
     */
    private Map<String, String> playersInfo(DataSnapshot dataSnapshot, Round round, boolean randomFlag) {
        Map<String, Object> userIds;
        Map<String, String> map = new HashMap<>();
        Random r = new Random();
        String firstPlayerName=null, firstPlayerUUID=null, secondPlayerName=null, secondPlayerUUID=null;
        List<String> users = new ArrayList<>();

        userIds = ((Map<String, Object>) dataSnapshot.child(FBD_USERS).getValue());
        users.addAll(userIds.keySet());

        //Eliminamos el usuario residual del FB
        if (users.contains("0000"))
            users.remove("0000");

        if (!randomFlag) {
            firstPlayerName = round.getFirstPlayerName();
            firstPlayerUUID = round.getFirstPlayerUUID();
            secondPlayerName = round.getSecondPlayerName();
            secondPlayerUUID = round.getSecondPlayerUUID();
        }
        else if (round.getFirstPlayerUUID().equals("0000-0000-0000")){
            secondPlayerName = round.getSecondPlayerName();
            secondPlayerUUID = round.getSecondPlayerUUID();
            while (firstPlayerUUID == null || secondPlayerUUID.equals(firstPlayerUUID))
                firstPlayerUUID = users.get(r.nextInt(userIds.size()-1));
            firstPlayerName = (String) dataSnapshot.child(FBD_USERS).child(firstPlayerUUID).child(FBD_USERS_NAME).getValue();
        }
        else{
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

    /** Recupera la lista de rondas de un un usuario
     * @param dataSnapshot
     * @param round ronda que se añadira
     * @param userId ide del usuario correspondiente
     * @return
     */
    private List<String> lista_rondas(DataSnapshot dataSnapshot, Round round, String userId){
        List<String> lista = (List<String>) dataSnapshot.child(FBD_USERS).child(userId).child(FBD_USERS_ROUNDS).getValue();
        if(lista==null)
            lista = new ArrayList<>();
        lista.add(round.getId());
        return lista;
    }


}


