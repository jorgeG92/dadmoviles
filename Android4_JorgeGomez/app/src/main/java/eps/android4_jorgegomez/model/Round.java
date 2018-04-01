package eps.android4_jorgegomez.model;

import java.util.Date;
import java.util.UUID;

import es.uam.eps.multij.Tablero;

public class Round {
    private int size;
    private String id;
    private String title;
    private String date;
    private TableroConecta4 board;

    public Round(int size) {
        this.size = size;
        id = UUID.randomUUID().toString();
        title = "ROUND " + id.toString().substring(19, 23).toUpperCase();
        date = new Date().toString();
        board = new TableroConecta4(size);
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

    public TableroConecta4 getBoard() {
        return board;
    }

    public void setBoard(TableroConecta4 board) {
        this.board = board;
    }
}