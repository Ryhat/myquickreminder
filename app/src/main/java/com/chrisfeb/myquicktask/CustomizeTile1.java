package com.chrisfeb.myquicktask;

import android.content.Intent;
import android.os.IBinder;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

public class CustomizeTile1 extends TileService {
    private static final String TAG = "CustomizeTile1";

    public static String label;
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
        if (tile != null && !label.equals("") && !label.isEmpty()){
            if (tile.getState() == Tile.STATE_ACTIVE){
                tile.setLabel(label);
            } else {
                tile.setLabel("已设" + label + "后的提醒");
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
        Log.d(TAG, "onClick");

        Tile tile = getQsTile();
        if (tile != null) {
            if (tile.getState() == Tile.STATE_ACTIVE) {
                tile.setLabel("QuickStart");
                tile.setState(Tile.STATE_INACTIVE);
            } else {
                tile.setLabel("Running...");
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
