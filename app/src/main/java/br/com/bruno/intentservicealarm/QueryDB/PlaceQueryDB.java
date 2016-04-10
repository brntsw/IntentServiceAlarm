package br.com.bruno.intentservicealarm.QueryDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import br.com.bruno.intentservicealarm.model.Category;
import br.com.bruno.intentservicealarm.model.Place;
import br.com.bruno.intentservicealarm.sqlite.Queries;

/**
 * Created by Bruno on 10/04/2016.
 */
public class PlaceQueryDB extends Queries {

    public static void insertPlaces(Context context, List<Place> places){
        for(Place place : places){
            ContentValues insertValues = new ContentValues();
            insertValues.put("place_id", place.getPlace_id());
            insertValues.put("title", place.getTitle());
            insertValues.put("address", place.getAddress());
            insertValues.put("city", place.getCity());
            insertValues.put("state", place.getState());
            insertValues.put("phone", place.getPhone());
            insertValues.put("latitude", place.getLatitude());
            insertValues.put("longitude", place.getLongitude());
            insertValues.put("avarageRating", place.getAvarageRating());
            insertValues.put("lastReviewIntro", place.getLastReviewIntro());
            insertValues.put("businessUrl", place.getBusinessUrl());

            String where = "place_id = ?";
            String[] whereArgs = new String[]{
                    String.valueOf(place.getPlace_id())
            };

            if(getBanco(context).get().update("PLACE", insertValues, where, whereArgs) == 0){
                getBanco(context).get().insert("PLACE", null, insertValues);
            }
        }
    }

    public static List<Place> getPlaces(Context context){
        List<Place> places = new ArrayList<>();

        final Cursor cursor = getBanco(context).get().rawQuery("SELECT * FROM PLACE", null);
        while(cursor.moveToNext()){
            Place place = new Place();
            place.setId(cursor.getInt(cursor.getColumnIndex("id")));
            place.setPlace_id(cursor.getInt(cursor.getColumnIndex("place_id")));
            place.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            place.setCity(cursor.getString(cursor.getColumnIndex("city")));
            place.setState(cursor.getString(cursor.getColumnIndex("state")));
            place.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            place.setLatitude(cursor.getString(cursor.getColumnIndex("latitude")));
            place.setLongitude(cursor.getString(cursor.getColumnIndex("longitude")));
            place.setAvarageRating(cursor.getString(cursor.getColumnIndex("avarageRating")));
            place.setLastReviewIntro(cursor.getString(cursor.getColumnIndex("lastReviewIntro")));
            place.setBusinessUrl(cursor.getString(cursor.getColumnIndex("businessUrl")));

            places.add(place);
        }

        cursor.close();

        return places;
    }

    public static long insertCategory(Context context, Category category){
        ContentValues insertValues = new ContentValues();
        insertValues.put("place_id", category.getPlace_id());
        insertValues.put("content", category.getContent());

        String where = "place_id = ?";
        String[] whereArgs = new String[]{
                String.valueOf(category.getPlace_id())
        };

        if(getBanco(context).get().update("CATEGORY", insertValues, where, whereArgs) == 0){
            return getBanco(context).get().insert("CATEGORY", null, insertValues);
        }

        return 0;
    }

    public static Category getCategory(Context context, Place place){
        final Cursor cursor = getBanco(context).get().rawQuery("SELECT * FROM CATEGORY WHERE place_id = " + place.getId(), null);

        Category category = null;

        if(cursor.getCount() > 0){
            cursor.moveToNext();

            category = new Category();
            category.setPlace_id(cursor.getInt(cursor.getColumnIndex("place_id")));
            category.setContent(cursor.getString(cursor.getColumnIndex("content")));
        }

        cursor.close();

        return category;
    }

}
