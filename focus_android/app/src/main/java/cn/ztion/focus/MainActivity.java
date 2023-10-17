package cn.ztion.focus;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.ztion.focus.service.BackendService;
import cn.ztion.focus.service.SmsService;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        requestPermissions(new String[]{"android.permission.READ_SMS", "android.permission.RECEIVE_SMS"}, 1);

        // 启动MyService
        Intent intent = new Intent(this, BackendService.class);
        startService(intent);
        //回显数据
        SharedPreferences shared = getSharedPreferences("focus", Context.MODE_PRIVATE);
        EditText serverInput = findViewById(R.id.serverInput);
        EditText serverPass = findViewById(R.id.serverPass);
        serverInput.setText(shared.getString("serverAddress", ""));
        serverPass.setText(shared.getString("serverPass", ""));
        //消息推送数据
        SmsService.token = shared.getString("serverPass", "");
        SmsService.serverAddress = shared.getString("serverAddress", "");
    }


    public void start(View view) {
        //关闭输入法
        this.closeInput(null);
        //校验输入
        EditText serverInput = findViewById(R.id.serverInput);
        EditText serverPass = findViewById(R.id.serverPass);
        String url = serverInput.getText().toString();
//        if (!isValidURL(url)) {
//            Toast.makeText(MainActivity.this, "服务器地址校验错误!", Toast.LENGTH_SHORT).show();
//            return;
//        }
        //保存
        SharedPreferences shared = getSharedPreferences("focus", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = shared.edit();
        edit.putString("serverAddress", url);
        edit.putString("serverPass", serverPass.getText().toString());
        edit.apply();
        Toast.makeText(MainActivity.this, "已保存", Toast.LENGTH_SHORT).show();
        //消息推送数据
        SmsService.token = serverPass.getText().toString();
        SmsService.serverAddress =url;
    }

    public void closeInput(View view) {
        EditText editText = findViewById(R.id.serverInput);
        EditText serverPass = findViewById(R.id.serverPass);
        serverPass.clearFocus();
        editText.clearFocus();
        InputMethodManager imm = (InputMethodManager)
                MainActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(R.id.box).getWindowToken(), 0);
    }

    public static boolean isValidURL(String url) {
        String regex = "^(https?|ftp|file)://.*$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                String code = (String) msg.obj;
                Log.d("TAG", code);
            }

        }
    };
}