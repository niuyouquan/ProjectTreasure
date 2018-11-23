package com.nyq.projecttreasure.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.widget.TextView;
import android.widget.Toast;

import com.nyq.projecttreasure.dialog.ApkUpdateDialog;
import com.nyq.projecttreasure.dialog.ConfirmDialog;
import com.nyq.projecttreasure.interfaces.Callback;
import com.nyq.projecttreasure.interfaces.OnDownAPKListenter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * 类名： ApkUpdateManager
 * 时间：2017/11/20 14:57
 * 描述： 更新帮助类
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @author niuyq
 */
public class ApkUpdateManager {

    private static final String TAG = "ApkUpdateManager";

    private Context mContext;

    /**
     * 返回的安装包url
     */
    private String apkDownUrl;
    /**
     * 下载包安装路径
     */
    private static final String savePath = "/storage/emulated/0/agricultural_update_apk/";

    private static final String saveFileName = savePath + "UpdateRelease.apk";

    private static final int DOWN_UPDATE1 = 0;

    private static final int DOWN_UPDATE = 1;

    private static final int DOWN_OVER = 2;

    private static final int DOWN_NONTIFICATION = 3;

    private int progress;

    private Thread downLoadThread;

    private OnDownAPKListenter onDownAPKListenter;

    private boolean interceptFlag = false;

    public ApkUpdateManager(Context context) {
        this.mContext = context;

    }

    private TextView tv_sure;

    private ApkUpdateDialog apkUpdateDialog;

    private String content = "";


    /**
     * 更新
     */
    public void updateShowDialog(final String DownUrl, int fileSize,
                                 String version, String content) {
        apkDownUrl = DownUrl;
        double size = (fileSize / 1024) / 1024;
        apkUpdateDialog = new ApkUpdateDialog(mContext, new Callback() {
            @Override
            public void callback(int position) {
                if (position == 1) {
                    apkUpdateDialog.isEnabled();
                    if (isWifiConnected(mContext)) {
//                    //wifi状态
                        downloadApk(apkDownUrl);
                    } else {
                        new ConfirmDialog(mContext, new Callback() {
                            @Override
                            public void callback(int position) {
                                if (position == 1) {
                                    downloadApk(apkDownUrl);
                                } else {
                                    apkUpdateDialog.dimiss();
                                    interceptFlag = true;
                                    if (getOnDownAPKListenter() != null) {
                                        getOnDownAPKListenter().onCancel();
                                    }
                                }
                            }
                        }).setContent("目前手机不是WiFi状态\n确认是否继续下载更新？").show();
                    }
                } else {
                    apkUpdateDialog.dimiss();
                    interceptFlag = true;
                    if (getOnDownAPKListenter() != null) {
                        getOnDownAPKListenter().onCancel();
                    }
                }
            }
        });
        apkUpdateDialog.setContent("项目宝v" + version + "新特性：",
                content,
                "安装包大小：" + String.format("%.2f", size) + "MB");
        apkUpdateDialog.show();


    }

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                URL url = new URL(apkDownUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();
                File file = new File(savePath);
                //先删除之前的异常信息
                if (file.exists()) {
                    DeleteFile(file);
                }
                if (!file.exists()) {
                    file.mkdir();
                }
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte[] buf = new byte[1024];
                while (!interceptFlag) {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    //更新进度
                    if (progress >= 96) {
                        mHandler.sendEmptyMessage(DOWN_UPDATE1);
                    } else {
                        mHandler.sendEmptyMessage(DOWN_UPDATE);
                    }
                    if (numread <= 0) {
                        //下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                    //点击取消就停止下载.
                }

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    /**
     * 下载apk
     */

    private void downloadApk(String apkDownUrl) {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    /**
     * 安装apk
     */
    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            Toast.makeText(mContext, "下载的安装包不存在", Toast.LENGTH_SHORT).show();
            return;
        }
        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //在AndroidManifest中的android:authorities值
            Uri apkUri = FileProvider.getUriForFile(mContext, "com.nyq.projecttreasure.provider", apkfile);
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
            mContext.startActivity(install);
        } else {
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(install);
        }
    }


    public OnDownAPKListenter getOnDownAPKListenter() {
        return onDownAPKListenter;
    }

    public void setOnDownAPKListenter(OnDownAPKListenter onDownAPKListenter) {
        this.onDownAPKListenter = onDownAPKListenter;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    apkUpdateDialog.setSureText("正在下载..." + progress + "%");
                    break;
                case DOWN_OVER:
                    Toast.makeText(mContext, "下载成功", Toast.LENGTH_SHORT).show();
                    apkUpdateDialog.dimiss();
                    //TODO: 把应用关闭
                    installApk();
                    break;
                case DOWN_NONTIFICATION:
//                    notificationHelper.setmCurrent(progress);
                    break;
                case DOWN_UPDATE1:
                    apkUpdateDialog.setSureText("正在校验...");
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 检测wifi是否连接
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            }
        }
        return false;
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public void DeleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                DeleteFile(f);
            }
            file.delete();
        }
    }
}
