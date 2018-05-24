package eps.android4_jorgegomez.model;

import java.util.List;

public interface RoundRepository {

    void open() throws Exception;

    void close();

    interface LoginRegisterCallback{
        void onLogin(String playerUuid);
        void onError(String error);
    }

    void login(String playername, String password, LoginRegisterCallback callback);

    void register(String playername, String password, LoginRegisterCallback callback);

    interface BooleanCallback {
        void onResponse(boolean ok);
    }

    List<Round> getRounds(String playeruuid, String orderByField, String group,
                   RoundsCallback callback);

    void addRound(Round round, BooleanCallback callback);

    void updateRound(Round round, BooleanCallback callback);

    interface RoundsCallback {
        void onResponse(List<Round> rounds);
        void onError(String error);
    }

    public void setPlayerNameSettings(final String playername, final String playerid);
}