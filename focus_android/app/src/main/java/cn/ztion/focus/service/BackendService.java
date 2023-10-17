package cn.ztion.focus.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import cn.ztion.focus.R;

public class BackendService extends Service {

    // 定义通知渠道 ID
    private static final String CHANNEL_ID = "channel_id";

    // 定义通知ID
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        //启动短信监听服务
        SharedPreferences shared = getSharedPreferences("focus", Context.MODE_PRIVATE);
        //消息推送数据
        SmsService.token = shared.getString("serverPass", "");
        SmsService.serverAddress = shared.getString("serverAddress", "");
        SmsService smsService = new SmsService(BackendService.this, new Handler());
        Uri uri = Uri.parse("content://sms");
        getContentResolver().registerContentObserver(uri, true, smsService);
        Toast.makeText(BackendService.this, "焦距App运行中", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 创建通知
        Notification notification = createNotification();

        // 设置前台服务
        startForeground(NOTIFICATION_ID, notification);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // 创建通知
    private Notification createNotification() {
        // 创建通知渠道（Android 8.0及以上需要）
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Focus Channel",
                NotificationManager.IMPORTANCE_DEFAULT);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        // 创建通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.text_service_running))
                .setOngoing(true);
        return builder.build();
    }

}