package br.com.bruno.intentservicealarm.sqlite;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Bruno on 10/04/2016.
 */
public class Banco {

    private BdManager bancoManager;
    private SQLiteDatabase sqld;

    public Banco(BdManager bancoManager){
        this.bancoManager = bancoManager;
    }

    public void open(){
        sqld = bancoManager.getWritableDatabase();
    }

    public SQLiteDatabase get() {

        if (sqld != null && sqld.isOpen()) {
            return sqld;
        }
        return null;
    }

    public void close() {
        bancoManager.close();
    }

}
