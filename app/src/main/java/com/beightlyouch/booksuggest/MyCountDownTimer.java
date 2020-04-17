package com.beightlyouch.booksuggest;

import android.os.CountDownTimer;
import android.widget.Button;

public class MyCountDownTimer extends CountDownTimer {


    //カウントダウンの開始値, 何秒ごとにカウントダウンを行うか
    public MyCountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }
    @Override
    public void onTick(long millisUntilFinished) {
        //インターバルごとに呼ばれる

    }

    @Override
    public void onFinish() {
        //カウントダウン完了後に呼ばれる

    }
}
