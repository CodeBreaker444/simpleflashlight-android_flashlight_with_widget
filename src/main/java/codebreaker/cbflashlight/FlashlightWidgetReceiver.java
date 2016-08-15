package codebreaker.cbflashlight;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Created by Code Breaker on 11-06-2016.
 */
public class FlashlightWidgetReceiver extends BroadcastReceiver {
    private static boolean isLightOn = false;
    private static Camera camera;

    @Override
    public void onReceive(Context context, Intent intent) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        if(isLightOn) {

            views.setImageViewResource(R.id.offbuttonw, R.drawable.offw);
        } else {
            views.setImageViewResource(R.id.offbuttonw, R.drawable.onw);
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(new ComponentName(context,     FlashlightWidgetProvider.class),
                views);

        if (isLightOn) {
            if (camera != null) {
                camera.stopPreview();
                camera.release();
                camera = null;


                isLightOn = false;
            }

        } else {
            // Open the default i.e. the first rear facing camera.
            camera = Camera.open();

            if(camera == null) {
                Toast.makeText(context, R.string.no_flash, Toast.LENGTH_SHORT).show();
            } else {
                // Set the torch flash mode
                Camera.Parameters param = camera.getParameters();
                param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                try {
                    camera.setParameters(param);
                    camera.startPreview();
                    isLightOn = true;
                } catch (Exception e) {
                    Toast.makeText(context, R.string.no_flash, Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

}