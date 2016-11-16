package com.endless.coin;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView tv;
    Button btn;
    TextView result;
    CoinThread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView)findViewById(R.id.text);
        result = (TextView)findViewById(R.id.result);
        btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(this);
        thread = new CoinThread();
    }

    private int getCoinSide(){
        return getRandomNumber(System.currentTimeMillis())%2;
    }
    private int getRandomNumber(long seed){
        Random r  = new Random(seed);
        return r.nextInt();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn:
                thread.resetDelta();
                thread.start();
                break;
        }
    }



    class CoinThread extends Thread{

        private boolean yesOrNo;

        private int original = -150;
        private int delta = original;
        private void resetDelta(){
            delta = original;
        }

        @Override
        public void run() {
            occursThis();
        }

        private void occursThis(){
            original+=5;
            if(Math.abs(original)!=150){
                try {
                    Thread.sleep(Math.abs(original));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message = new Message();
                message.what= 0;
                yesOrNo = !yesOrNo;
                message.obj = yesOrNo;
                mHandler.sendMessage(message);
                occursThis();
            }else{
                Message message = new Message();
                message.what = 0;
                boolean coin = getCoinSide()==0;
                message.obj = coin;
                mHandler.sendMessage(message);
            }
        }
    }



    private String yes = getString(R.string.yes);
    private String no = getString(R.string.no);

    Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    boolean yesOrNo = (boolean) msg.obj;
                    result.setText(yesOrNo?yes:no);
                    break;
            }
        }
    };
}
