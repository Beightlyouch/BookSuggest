package com.beightlyouch.booksuggest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReciever extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent)
    {
        //処理の内容
        Toast.makeText(context, "2秒後です", Toast.LENGTH_SHORT).show();

    }
}
