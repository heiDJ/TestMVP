package com.dsc.testmvp.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by staff on 2016/9/12.
 * 定位服务工具类
 *
 */
public class LocationUtil {
    private static final String TAG = "LocationUtil";
    private static final String SEND_URL = "http://10.0.6.75:8085/api/stock/saveLongitudeAndLatitude";
    private static AMapLocationClient locationClient = null;
    /**
     * 每次定位间隔时间
     */
    private static final long INTERVAL = 1000*10;
    /**
     * 定位超时时间
     */
    private static final long TIME_OUT = 1000*30;

    private LocationUtil(){} //禁止实例化

    /**
     * 开启定位
     */
    public static void startLocation(Context context){
        initLocation(context);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 停止定位
     */
    public static void stopLocation(){
        if (null != locationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
        }
    }

    /**
     * 定位服务初始化
     */
    private static void initLocation(Context context){
        //初始化client
        locationClient = new AMapLocationClient(context);
        //设置定位参数
        locationClient.setLocationOption(getDefaultOption());
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
    }

    private static AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
//        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);//在这种模式下，将只使用高德网络定位
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//在这种定位模式下，将同时使用高德网络定位和GPS定位,优先返回精度高的定位
//        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Device_Sensors);//在这种模式下，将只使用GPS定位。
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(TIME_OUT);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(INTERVAL);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是ture
        mOption.setLocationCacheEnable(true);// 设置是否开启缓存
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        return mOption;
    }
    /**
     * 定位监听
     */
    private static final AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation loc) {
            if (null != loc) {
                //解析定位结果
                String result = getLocationStr(loc);
                Log.e(TAG, "onLocationChanged: \n"+result);
                sendLocation(1L,loc.getLongitude(),loc.getLatitude());
                //TODO
            } else {
                Log.e(TAG, "定位失败!");
                //TODO
            }
        }
    };


    /**
     *  发送经纬度到后台
     * @param idMachine 机器id
     * @param longitude 经度
     * @param latitude 维度
     */
    private static void sendLocation(long idMachine,double longitude,double latitude){
        try {
            URL url = new URL(SEND_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置参数
            conn.setDoOutput(true);   //需要输出
            conn.setDoInput(true);   //需要输入
            conn.setUseCaches(false);  //不允许缓存
            conn.setRequestMethod("POST");   //设置POST方式连接
            //设置请求属性
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
            conn.setRequestProperty("Charset", "UTF-8");

            StringBuffer params = new StringBuffer();
            params.append("idMachine").append("=").append(idMachine).append("&")
                    .append("longitude").append("=").append(longitude).append("&")
                    .append("latitude").append("=").append(latitude);

            conn.connect();
            //建立输入流，向指向的URL传入参数
            DataOutputStream dos=new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(params.toString());
            dos.flush();
            dos.close();

            //获得响应状态
            int resultCode=conn.getResponseCode();
            Log.e(TAG,resultCode+"");
            if(HttpURLConnection.HTTP_OK==resultCode){
                StringBuffer sb=new StringBuffer();
                String readLine=new String();
                BufferedReader responseReader=new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                while((readLine=responseReader.readLine())!=null){
                    sb.append(readLine).append("\n");
                }
                responseReader.close();
                Log.e(TAG, "上传定位成功!");
                Log.e(TAG,sb.toString());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

        public static byte[] readInputStream(InputStream inStream) throws Exception{
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while( (len = inStream.read(buffer)) !=-1 ){
                outStream.write(buffer, 0, len);
            }
            byte[] data = outStream.toByteArray();//网页的二进制数据
            outStream.close();
            inStream.close();
            return data;
        }


    /**
     * 根据定位结果返回定位信息的字符串
     * @param location
     * @return
     */
    private synchronized static String getLocationStr(AMapLocation location){
        if(null == location){
            return null;
        }
        StringBuffer sb = new StringBuffer();
        //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
        if(location.getErrorCode() == 0){
            sb.append("定位成功" + "\n");
            sb.append("定位类型: " + location.getLocationType() + "\n");
            sb.append("经    度    : " + location.getLongitude() + "\n");
            sb.append("纬    度    : " + location.getLatitude() + "\n");
            sb.append("精    度    : " + location.getAccuracy() + "米" + "\n");
            sb.append("提供者    : " + location.getProvider() + "\n");

            if (location.getProvider().equalsIgnoreCase(
                    android.location.LocationManager.GPS_PROVIDER)) {
                // 以下信息只有提供者是GPS时才会有
                sb.append("速    度    : " + location.getSpeed() + "米/秒" + "\n");
                sb.append("角    度    : " + location.getBearing() + "\n");
                // 获取当前提供定位服务的卫星个数
                sb.append("星    数    : "
                        + location.getSatellites() + "\n");
            } else {
                // 提供者是GPS时是没有以下信息的
                sb.append("国    家    : " + location.getCountry() + "\n");
                sb.append("省            : " + location.getProvince() + "\n");
                sb.append("市            : " + location.getCity() + "\n");
                sb.append("城市编码 : " + location.getCityCode() + "\n");
                sb.append("区            : " + location.getDistrict() + "\n");
                sb.append("区域 码   : " + location.getAdCode() + "\n");
                sb.append("地    址    : " + location.getAddress() + "\n");
                sb.append("兴趣点    : " + location.getPoiName() + "\n");
                //定位完成的时间
                sb.append("定位时间: " + formatUTC(location.getTime(), "yyyy-MM-dd HH:mm:ss:sss") + "\n");
            }
        } else {
            //定位失败
            sb.append("定位失败" + "\n");
            sb.append("错误码:" + location.getErrorCode() + "\n");
            sb.append("错误信息:" + location.getErrorInfo() + "\n");
            sb.append("错误描述:" + location.getLocationDetail() + "\n");
        }
        //定位之后的回调时间
        sb.append("回调时间: " + formatUTC(System.currentTimeMillis(), "yyyy-MM-dd HH:mm:ss:sss") + "\n");
        return sb.toString();
    }
    private static SimpleDateFormat sdf = null;
    private synchronized static String formatUTC(long l, String strPattern) {
        if (TextUtils.isEmpty(strPattern)) {
            strPattern = "yyyy-MM-dd HH:mm:ss";
        }
        if (sdf == null) {
            try {
                sdf = new SimpleDateFormat(strPattern, Locale.CHINA);
            } catch (Throwable e) {
            }
        } else {
            sdf.applyPattern(strPattern);
        }
        if (l <= 0l) {
            l = System.currentTimeMillis();
        }
        return sdf == null ? "NULL" : sdf.format(l);
    }
}
