package com.chrisfeb.myquicktask;

import android.content.Intent;
import android.os.IBinder;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.util.Log;

public class CustomizeTile3 extends TileService {
    private static final String TAG = "CustomizeTile3";

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
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
        Log.d(TAG, "onTileRemoved");
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        Log.d(TAG, "onStartListening");

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
        Log.d(TAG, "onStopListening");
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
        Log.d(TAG, "onBind");
        return super.onBind(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

}
