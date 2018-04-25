package eps.android4_jorgegomez.model;

import java.util.Date;
import java.util.UUID;

public class Round {

    private int size;
    private String id;
    private String firstPlayerId;
    private String firstPlayerName;
    private String secondPlayerName;
    private String secondPlayerId;
    private String title;
    private String date;
    private ConectBoard board;

    public Round(int size) {
        this.size = size;
        id = UUID.randomUUID().toString();
        title = "ROUND " + id.toString().substring(19, 23).toUpperCase();
        date = new Date().toString();
        board = new ConectBoard(size);
    }

    public int getSize() { return size;}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ConectBoard getBoard() {
        return board;
    }

    public void setBoard(ConectBoard board) {
        this.board = board;
    }

    public void setPlayerUUID(String uuid){ firstPlayerId = uuid; }

    public String getPlayerUUID(){ return firstPlayerId; }

    public void setFirstPlayerName(String name){ firstPlayerName = name; }

    public String getFirstPlayerName() { return firstPlayerName; }

    public void setSecondPlayerName(String name){ secondPlayerName = name; }
}