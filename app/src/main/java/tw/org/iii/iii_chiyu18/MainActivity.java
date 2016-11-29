package tw.org.iii.iii_chiyu18;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.textView);
        Permission();
    }


    private TelephonyManager tmgr;
    void init(){
        Log.v("chiyu","init()");
        // 取得電話況狀元件
        tmgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
        // 掛上監聽
        tmgr.listen(new MyPhoneStateListener(), PhoneStateListener.LISTEN_CALL_STATE);
        // 取得帳號資料元件
        amrg = (AccountManager)getSystemService(ACCOUNT_SERVICE);
    }
    // 監聽程式
    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state){
                case TelephonyManager.CALL_STATE_RINGING:
                    // 電話 響了 記錄來電電話 時間
                    Log.v("chiyu", incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    Log.v("chiyu", "offhook");
                    // 接起來 開始錄音
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    Log.v("chiyu", "handout");
                    // 掛掉 結束錄音
                    break;
            }
        }
    }


    public void b1(View v){
        Log.v("chiyu","機碼: " + tmgr.getDeviceId());
        Log.v("chiyu","SIM: " + tmgr.getSubscriberId());
        tv.setText("機碼 : " + tmgr.getDeviceId() + "\nSIM : " + tmgr.getSubscriberId());
    }

    private AccountManager amrg;
    public void b2(View v){
        tv.setText("");
        Account[] AccList = amrg.getAccounts();
        for (Account Acc : AccList){
            Log.v("chiyu",Acc.name + " : " + Acc.type);
            tv.append(Acc.name + " : " + Acc.type + "\n");
        }
    }


    void Permission() {
        if (ContextCompat.checkSelfPermission(this,
                // 開發的時候 一個個加上去
                //Manifest.permission.READ_PHONE_STATE)
                Manifest.permission.GET_ACCOUNTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.GET_ACCOUNTS},
                    234);
        } else {
            init();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        init();
    }
}

