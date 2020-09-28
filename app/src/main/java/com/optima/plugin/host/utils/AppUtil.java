package com.optima.plugin.host.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.KeyguardManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PowerManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AppUtil {

	//判断当前应用是否是debug状态
	public static boolean isApkInDebug(Context context) {
		try {
			ApplicationInfo info = context.getApplicationInfo();
			return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean checkIntent(Context context, Intent intent) {
		PackageManager manager = context.getPackageManager();
		List<ResolveInfo> resolveInfos = manager.queryIntentActivities(intent, PackageManager.MATCH_ALL);
		System.out.println(resolveInfos);
		return !resolveInfos.isEmpty();
	}

	/**
	 * 获取手机型号
	 * @return 手机型号
	 */
	public static String getSystemModel() {
		return Build.MODEL;
	}

	/**
	 * 获取当前版本标示号
	 * @param mContext
	 * @return
	 */
	public static int getCurrentVersionCode(Context mContext) {
		try {
			return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}

	/**
	 * 获取当前版本号
	 * @param mContext
	 * @return
	 */
	public static String getCurrentVersionName(Context mContext) {
		try {
			return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * 获取当前系统版本号
	 * @param mContext
	 * @return
	 */
	public static int getSDKVersionNumber(Context mContext) {
		try {
			return Integer.valueOf(Build.VERSION.SDK_INT);
		} catch (Exception e) {
			return Build.VERSION.SDK_INT;
		}
	}

	/**
	 * 程序是否在前台运行
	 * @return
	 */
	public static boolean isAppOnForeground(Context mContext) {

		ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		String packageName = mContext.getPackageName();

		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		if (appProcesses == null) {
			return false;
		}
		for (RunningAppProcessInfo appProcess : appProcesses) {
			if (appProcess.processName.equals(packageName) && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 判断是否亮屏
	 * @param context 上下文
	 * @return true 已经亮屏，false没有亮屏，获取失败时返回false
	 */
	public static boolean isScreenOn(Context context) {
		if (context != null) {
			PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
//            return powerManager.isScreenOn();
			return powerManager.isInteractive();
		}
		return false;
	}

	/**
	 * 是否锁屏
	 * @param context 上下文
	 * @return true已经锁屏，false没有锁屏，没有设置锁或者获取失败时返回false
	 */
	public static boolean isLockScreen(Context context) {
		KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
//        return keyguardManager.inKeyguardRestrictedInputMode();
		return keyguardManager.isKeyguardLocked();
	}

	/**
	 * 唤醒屏幕
	 */
	public static void wakeupScreen(Context context) {
		try {
			// 唤醒屏幕或者锁屏显示
			PowerManager systemService = (PowerManager) context.getSystemService(Context.POWER_SERVICE);

			// 点亮亮屏
			PowerManager.WakeLock wakeLock = systemService.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "myapp" +
					":mywakelocktag");
			wakeLock.acquire();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断service是否正在运行
	 * @param context
	 * @param serviceName
	 * @return
	 */
	public static boolean isServiceRunningByServiceName(Context context, String serviceName) {
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) activityManager.getRunningServices(50);
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service.getClassName().toString().equals(serviceName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 隐藏输入面板
	 * @param activity
	 * @return true 成功隐藏面板，false 没有隐藏面板或者没有面板可以隐藏
	 * 		<p>
	 * 		不过这里似乎衡返回false
	 */
	public static boolean hideSoftInputFromWindow(Activity activity) {
		if (activity != null) {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm != null) {
				return imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
			}
		}
		return false;
	}

	/**
	 * 返回当前是否正在打电话
	 * @param context
	 * @return {true : 正在打电话}
	 */
	public static boolean phoneIsInUse(Context context) {
		TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
		return TelephonyManager.CALL_STATE_IDLE != mTelephonyManager.getCallState();
	}

	/**
	 * 判断某个apk是否安装
	 * @param context
	 * @param pageName
	 * @return
	 */
	public static boolean appIsInstalled(Context context, String pageName) {
		try {
			context.getPackageManager().getPackageInfo(pageName, 0);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	/**
	 * 获取IMEI号
	 * @param context
	 * @return
	 */
	@SuppressLint("MissingPermission")
	public static String getIMEI(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = "";
		try {
			imei = telephonyManager.getDeviceId();//todo:200519 getDeviceId()安卓10不可用
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			imei = getUUID();
		}
		return imei;
	}

	private static String getMIMEType(File file) {
		String type = "*/*";
		if (null == file) {
			return "";
		}
		String fName = file.getName();
		int fNameLength = fName.length();
		// 获取后缀名前的分隔符"."在fName中的位置。
		int dotIndex = fName.lastIndexOf(".");
		if (dotIndex < 0 || fNameLength <= 1) {
			return type;
		}

		/* 获取文件的后缀名 */
		String end = fName.substring(dotIndex + 1, fName.length()).toLowerCase();
		if (end == "") {
			return type;
		}
		MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
		return mimeTypeMap.getMimeTypeFromExtension(end);
	}

	/**
	 * 打开文件
	 */
	public static void openFile(Context context, File file) {
		// 获取文件file的MIME类型
		String type = getMIMEType(file);
		if (TextUtils.isEmpty(type) || "*/*".equals(type)) {
			Toast.makeText(context, "未发现可打开该文件的应用", Toast.LENGTH_SHORT).show();
			return;
		}

		try {

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addCategory(Intent.CATEGORY_DEFAULT);

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
				//添加这一句表示对目标应用临时授权该Uri所代表的文件
				intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			}

			intent.setDataAndType(getOpenFileUri(context, file), type);// 设置intent的data和Type属性。

			context.startActivity(intent);// 跳转
		} catch (Exception e) { //当系统没有携带文件打开软件，提示
			Toast.makeText(context, "无法打开该格式文件!", Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}

	}

	/**
	 * 获取用于打开文件的uri，会自定适配7.0
	 * @param context
	 * @param file
	 * @return
	 */
	public static Uri getOpenFileUri(Context context, File file) {
		Uri uri = null;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);

//            //添加这一句表示对目标应用临时授权该Uri所代表的文件
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		} else {
			uri = Uri.fromFile(file);
		}
		return uri;
	}

	// android获取一个用于打开HTML文件的intent
	public static Intent getHtmlFileIntent(Context context, File file) {
		Uri uri =
				Uri.parse(file.toString()).buildUpon().encodedAuthority(context.getPackageName() + ".fileprovider").scheme("content").encodedPath(file.toString()).build();
		Intent intent = new Intent(Intent.ACTION_VIEW);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 添加这一句表示对目标应用临时授权该Uri所代表的文件
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		}
		intent.setDataAndType(getOpenFileUri(context, file), "text/html");// 设置intent的data和Type属性。

		return intent;
	}

	// android获取一个用于打开图片文件的intent
	public static Intent getImageFileIntent(Context context, File file) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			//添加这一句表示对目标应用临时授权该Uri所代表的文件
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		}
		intent.setDataAndType(getOpenFileUri(context, file), "image/*");// 设置intent的data和Type属性。
		return intent;
	}

	// android获取一个用于打开PDF文件的intent
	public static Intent getPdfFileIntent(Context context, File file) {
		if (file == null) {
			return null;
		}

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			//添加这一句表示对目标应用临时授权该Uri所代表的文件
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		}
		intent.setDataAndType(getOpenFileUri(context, file), "application/pdf");// 设置intent的data和Type属性。
		return intent;
	}

	// android获取一个用于打开文本文件的intent
	public static Intent getTextFileIntent(Context context, File file) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			//添加这一句表示对目标应用临时授权该Uri所代表的文件
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		}
		intent.setDataAndType(getOpenFileUri(context, file), "text/plain");// 设置intent的data和Type属性。
		return intent;
	}

	// android获取一个用于打开音频文件的intent
	public static Intent getAudioFileIntent(Context context, File file) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			//添加这一句表示对目标应用临时授权该Uri所代表的文件
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		}
		intent.setDataAndType(getOpenFileUri(context, file), "audio/*");// 设置intent的data和Type属性。
		return intent;
	}

	// android获取一个用于打开视频文件的intent
	public static Intent getVideoFileIntent(Context context, File file) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("oneshot", 0);
		intent.putExtra("configchange", 0);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			//添加这一句表示对目标应用临时授权该Uri所代表的文件
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		}
		intent.setDataAndType(getOpenFileUri(context, file), "video/*");// 设置intent的data和Type属性。
		return intent;
	}

	// android获取一个用于打开CHM文件的intent
	public static Intent getChmFileIntent(Context context, File file) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			//添加这一句表示对目标应用临时授权该Uri所代表的文件
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		}
		intent.setDataAndType(getOpenFileUri(context, file), "application/x-chm");// 设置intent的data和Type属性。
		return intent;
	}

	// android获取一个用于打开Word文件的intent
	public static Intent getWordFileIntent(Context context, File file) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			//添加这一句表示对目标应用临时授权该Uri所代表的文件
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		}
		intent.setDataAndType(getOpenFileUri(context, file), "application/msword");// 设置intent的data和Type属性。
		return intent;
	}

	// android获取一个用于打开Excel文件的intent
	public static Intent getExcelFileIntent(Context context, File file) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			//添加这一句表示对目标应用临时授权该Uri所代表的文件
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		}
		intent.setDataAndType(getOpenFileUri(context, file), "application/vnd.ms-excel");
		return intent;
	}

	// android获取一个用于打开PPT文件的intent
	public static Intent getPPTFileIntent(Context context, File file) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			//添加这一句表示对目标应用临时授权该Uri所代表的文件
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		}
		intent.setDataAndType(getOpenFileUri(context, file), "application/vnd.ms-powerpoint");
		return intent;
	}

	// android获取一个用于打开apk文件的intent
	public static Intent getApkFileIntent(Context context, File file) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			//添加这一句表示对目标应用临时授权该Uri所代表的文件
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		}
		intent.setDataAndType(getOpenFileUri(context, file), "application/vnd.android.package-archive");
		return intent;
	}

	/**
	 * 打开指定类型的默认应用是否存在
	 * @param context
	 * @param type
	 * @return
	 */
	public static List<ResolveInfo> getOpenApplicationByType(Context context, String type) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setType(type);
		List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
//        List<ResolveInfo> resolveInfos = context.getPackageManager().queryIntentActivities(intent, 0);
		return resolveInfos;
	}

	/**
	 * 返回是否可以打开这个文件
	 * @param context
	 * @param file
	 * @return
	 */
	public static boolean canOpenFile(Context context, File file) {
		if (file == null || file.exists() == false || file.isDirectory()) {
			return false;
		}
		String type = getMIMEType(file);
		List<ResolveInfo> resolveInfos = getOpenApplicationByType(context, type);
		return resolveInfos.size() > 0;
	}

	/**
	 * 安装app
	 * @param context 上下文
	 * @param apkPath apk路径
	 * @return
	 */
	public static boolean installApp(Context context, String apkPath) {
		if (context == null) {
			return false;
		}

		File file = (new File(apkPath));
		if (file.exists() == false) {
			return false;
		}

		Intent intent = new Intent(Intent.ACTION_VIEW);
		// 由于没有在Activity环境下启动Activity,设置下面的标签
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		//版本在7.0以上是不能直接通过uri访问的
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

			//参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
			Uri apkUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", file);

			//添加这一句表示对目标应用临时授权该Uri所代表的文件
			intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

			intent.setDataAndType(apkUri, "application/vnd.android.package-archive");

		} else {
			intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
		}
		try {
			context.startActivity(intent);
		} catch (Exception e) { //当系统没有携带文件打开软件，提示
			e.printStackTrace();
			return false;
		}

		return true;
	}

	private static Toast mToast;

	/**
	 * 显示toast
	 * @param applicationContext
	 * @param reason
	 */
	@SuppressLint("ShowToast")
	public static void showToast(Context applicationContext, String reason) {
		if (mToast == null) {
			mToast = Toast.makeText(applicationContext, reason, Toast.LENGTH_SHORT);
		}
		mToast.setText(reason);
		mToast.show();
	}

	/**
	 * 显示输入面板
	 * @param activity
	 * @return true 成功隐藏面板，false 没有隐藏面板或者没有面板可以隐藏
	 * 		<p>
	 * 		不过这里似乎衡返回false
	 */
	public static boolean showSoftInput(Activity activity, View view) {
		if (activity != null) {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm != null) {
				return imm.showSoftInput(view, 0);
			}
		}
		return false;
	}

	/**
	 * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
	 * @param context
	 * @return 平板返回 True，手机返回 False
	 */
	public static boolean isPad(Context context) {
		return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static boolean existSDCard() {
		boolean flag = false;
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			flag = true;
		}
		return flag;
	}

	//是否有sd卡
	public static boolean hasSdcard() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	@SuppressLint("MissingPermission")
	public static String getUUID() {

		String serial = null;

		String m_szDevIDShort = "35" + Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

				Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +

				Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +

				Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +

				Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +

				Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +

				Build.USER.length() % 10; //13 位

		try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
				serial = Build.getSerial();
			} else {
				serial = Build.SERIAL;
			}
			//API>=9 使用serial号
			return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
		} catch (Exception exception) {
			//serial需要一个初始化
			serial = "serial"; // 随便一个初始化
		}
		//使用硬件信息拼凑出来的15位号码
		return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
	}
}
