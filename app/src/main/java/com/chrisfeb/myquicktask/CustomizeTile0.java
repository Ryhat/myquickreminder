package com.chrisfeb.myquicktask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

import androidx.preference.PreferenceManager;

public class CustomizeTile0 extends TileService {
    private static final String TAG = "CustomizeTile0";
    public static String str;
    //SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        Tile tile = getQsTile();

        if (tile != null){
            tile.setState(Tile.STATE_INACTIVE);
            tile.updateTile();
        }
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();

        Tile tile = getQsTile();
        if (tile != null){
            if (tile.getState() == Tile.STATE_ACTIVE){
                tile.setLabel("Running");
            } else {
                tile.setLabel("QuickTask");
            }
            tile.updateTile();
        }
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
    }

    @Override
    public void onClick() {
        super.onClick();

        String label;
        if (str.equals("") || str.isEmpty()){
            label = "Unset";
        } else {
            label = str;
        }
        Tile tile = getQsTile();
        if (tile != null) {
            if (tile.getState() == Tile.STATE_ACTIVE) {
                tile.setLabel(label);
                tile.setState(Tile.STATE_INACTIVE);
            } else {
                tile.setLabel("已经添加" + label + "后的提醒");
                tile.setState(Tile.STATE_ACTIVE);
            }

            tile.updateTile();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

}