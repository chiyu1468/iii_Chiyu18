package tw.org.iii.iii_chiyu18;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = (TextView) findViewById(R.id.textView);
        iv = (ImageView) findViewById(R.id.imageView);
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

    // 抓機碼
    public void b1(View v){
        Log.v("chiyu","機碼: " + tmgr.getDeviceId());
        Log.v("chiyu","SIM: " + tmgr.getSubscriberId());
        tv.setText("機碼 : " + tmgr.getDeviceId() + "\nSIM : " + tmgr.getSubscriberId());
    }
    // 抓帳號
    private AccountManager amrg;
    public void b2(View v){
        tv.setText("");
        Account[] AccList = amrg.getAccounts();
        for (Account Acc : AccList){
            Log.v("chiyu",Acc.name + " : " + Acc.type);
            tv.append(Acc.name + " : " + Acc.type + "\n");
        }
    }
    // 抓聯絡人
    public void b3(View v){
        ContentResolver ctrv = getContentResolver();
        String[] project = new String[]{
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER};

        Cursor cursor = ctrv.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                project, null, null,null);

        Log.v("chiyu","Count : " + cursor.getCount());
        tv.setText("");
        while (cursor.moveToNext()){
            String name = cursor.getString(0);
            String tel = cursor.getString(1);
            tv.append(name + " : " + tel + "\n");
            //Log.v("chiyu",name + " : " + tel);
        }
    }
    // 抓照片
    public void b4(View v){
        ContentResolver ctrv = getContentResolver();
        Cursor c = ctrv.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);

        Log.v("chiyu","Photo count : " + c.getCount());

        c.moveToLast();
        String data = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
        Log.v("chiyu","Photo Path : " + data);

        Bitmap bm = BitmapFactory.decodeFile(data);
        iv.setImageBitmap(bm);

    }



    void Permission() {
        if (ContextCompat.checkSelfPermission(this,
                // 開發的時候 一個個加上去
                //Manifest.permission.READ_PHONE_STATE)
                //Manifest.permission.GET_ACCOUNTS)
                //Manifest.permission.READ_CONTACTS)
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                 Manifest.permission.GET_ACCOUNTS,
                                 Manifest.permission.READ_CONTACTS,
                                 Manifest.permission.READ_PHONE_STATE},
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

