package br.com.bruno.intentservicealarm.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import br.com.bruno.intentservicealarm.R;

/**
 * Created by Bruno on 10/04/2016.
 */
public class BdPlace extends BdManager {
    public static final String NAME = "LOCALDB";
    public static final String TAG_LOG = "LOCALDB";
    public static final int VERSAO = 1;

    public BdPlace(Context context){
        //defino pelo contrutor do BdManager a versão e o nome do banco
        super(context, NAME, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        criaTabelas(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void criaTabelas(SQLiteDatabase bd) {
        try {
            byFile(R.raw.script, bd);
        } catch (Exception e) {
            Log.e(TAG_LOG, "criaTabelas", e);
        }
    }
}
