package com.example.ncovi_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.ncovi_app.UI.Home.MapActivity;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "GeofenceBroadcastReceiv";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
//        Toast.makeText(context, "Geofence triggered...", Toast.LENGTH_SHORT).show();

        NotificationHelper notificationHelper = new NotificationHelper(context);

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()){
            Log.d(TAG, "onReceive: Error receiving geofence event...");
            return;
        }

        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();
        for (Geofence geofence : geofenceList){
            Log.d(TAG, "onReceive: " + geofence.getRequestId());
        }
//        Location location = geofencingEvent.getTriggeringLocation();
        int transitironType = geofencingEvent.getGeofenceTransition();

        switch (transitironType){
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                Toast.makeText(context, context.getResources().getString(R.string.enter_disease_area_text), Toast.LENGTH_SHORT).show();
                notificationHelper.sendHighPriorityNotification(context.getResources().getString(R.string.enter_disease_area_title), context.getResources().getString(R.string.enter_disease_area_body), MapActivity.class);
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                Toast.makeText(context, context.getResources().getString(R.string.dwell_disease_area_text), Toast.LENGTH_SHORT).show();
                notificationHelper.sendHighPriorityNotification(context.getResources().getString(R.string.dwell_disease_area_title), context.getResources().getString(R.string.dwell_disease_area_body), MapActivity.class);
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                Toast.makeText(context, context.getResources().getString(R.string.exit_disease_area_text), Toast.LENGTH_SHORT).show();
                notificationHelper.sendHighPriorityNotification(context.getResources().getString(R.string.exit_disease_area_title), context.getResources().getString(R.string.exit_disease_area_body), MapActivity.class);
                break;
        }
    }
}
