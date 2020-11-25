package com.lanky.videobanner.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lanky.videobanner.GlobalInfo;

import static com.lanky.videobanner.GlobalInfo.Constants.TAG;

/**
 * LankyBin create on 2020/11/23
 */
public class USBBroadcastReceiver extends BroadcastReceiver {
    private Intent intent_broadcast = new Intent();
    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case Intent.ACTION_MEDIA_MOUNTED: {
                Log.d(TAG, "onReceive: ACTION_MEDIA_MOUNTED");
                sendUsbBroadcast(context,GlobalInfo.Constants.RECEIVER_VALUE_USB_MOUNTED);
                break;
            }
            case Intent.ACTION_MEDIA_UNMOUNTED: {
                Log.d(TAG, "onReceive: ACTION_MEDIA_UNMOUNTED");
                sendUsbBroadcast(context,GlobalInfo.Constants.RECEIVER_VALUE_USB_UNMOUNTED);
                break;
            }
            default:
                break;
        }
    }

    private void sendUsbBroadcast(Context context, int value){
        intent_broadcast.putExtra(GlobalInfo.Constants.RECEIVER_KEY_USB_MOUNT, value);
        intent_broadcast.setAction(GlobalInfo.Constants.ACTION_NAME);
        context.sendBroadcast(intent_broadcast);
    }
}
