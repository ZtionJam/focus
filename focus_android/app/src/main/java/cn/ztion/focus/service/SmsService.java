package cn.ztion.focus.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.ztion.focus.MainActivity;

public class SmsService extends ContentObserver {
    private Context context;
    private Handler handler;

    public static String serverAddress = "";
    public static String token = "";

    public static ExecutorService executor = Executors.newFixedThreadPool(1);


    public SmsService(Context context, Handler handler) {
        super(handler);
        this.context = context;
        this.handler = handler;
    }

    @Override
    @SuppressLint("Range")
    public void onChange(boolean selfChange, @Nullable Uri uri) {
        super.onChange(selfChange, uri);
        if (uri == null || uri.toString().equals("content://sms/raw")) {
            return;
        }
        Uri inbox = Uri.parse("content://sms/inbox");
        Cursor cursor = context.getContentResolver().query(inbox, null, null, null, "date desc");
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String address = cursor.getString(cursor.getColumnIndex("address"));//发件人地址（手机号）
                String body = cursor.getString(cursor.getColumnIndex("body"));//信息内容
                Log.d("TAG", "发件人为：" + address + " " + "短信内容为：" + body);
                executor.execute(()->this.pushToSever(address, body));

            }
            cursor.close();
        }
    }

    public void pushToSever(String phone, String content) {
        JSONObject jo = new JSONObject()
                .set("time", DateUtil.now())
                .set("phone", phone)
                .set("content", content);
        try (HttpResponse response = HttpRequest.post("https://" + serverAddress)
                .header("token", token)
                .body(jo.toString()).execute()) {
            if (response.getStatus() == 200) {
               Log.d("成功","短信推送成功");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}













