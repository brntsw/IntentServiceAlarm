package br.com.bruno.intentservicealarm.sqlite;

import android.content.Context;

/**
 * Created by Bruno on 10/04/2016.
 */
public class Queries {

    private static Banco banco;

    public static Banco getBanco(Context context){
        if (banco == null){
            banco = new Banco(new BdPlace(context));
            banco.open();
        } else {
            if (!banco.get().isOpen()){
                banco.open();
            }
        }

        return banco;
    }

    public static Integer deleteTable(Context context, String nameTable, String whereClause, String[] whereArgs){
        long id = -1;

        getBanco(context).get().beginTransaction();
        try {
            getBanco(context).get().delete(nameTable, whereClause, whereArgs);
            getBanco(context).get().setTransactionSuccessful();
        } finally {
            getBanco(context).get().endTransaction();
        }

        return Integer.valueOf(String.valueOf(id));
    }

}
