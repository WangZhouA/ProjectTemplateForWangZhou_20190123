package projecttemplateforwangzhou.wangzhou.com.projecttemplateforwangzhou.ble;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleWriteResponse;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import java.util.Map;
import java.util.UUID;

import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DISCONNECTED;

/**
 * Created by tt on 2017/8/15.
 * 单例模式处理蓝牙
 * 感觉还行，
 * https://github.com/dingjikerbo/BluetoothKit/tree/master
 */

public class BleManger {
    BluetoothClient mClient;


    private Context context;



    private static class SingletonHolder {
        private static final BleManger INSTANCE = new BleManger();
    }

    private BleManger() {
    }

    public static final BleManger getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * 初始化，在application里面
     */
    public void intiBle(Context context) {
        this.context=context;
        mClient = new BluetoothClient(context);
    }

    /**
     *  获取蓝牙的连接状态  0是未连接  2是连接
     * */

    public   int  getBleState(  String MAC  ){

        int status = mClient.getConnectStatus(MAC);

        return  status;
    }

    /**
     * 是否支持蓝牙
     */
    public boolean isBleSupported() {
        return mClient.isBleSupported();
    }


    /**
     * 打开蓝牙
     */
    public void openBle(Context context) {
        mClient.openBluetooth();
    }

    /**
     * 搜索蓝牙
     *
     * @param response
     */
    public void startSearch(SearchResponse response) {
        //扫描ble10秒.2次
        SearchRequest request = new SearchRequest.Builder()
                .searchBluetoothLeDevice(10000, 2)
                .build();
        mClient.search(request, response);
    }

    /**
     * 停止搜索蓝牙
     */
    public void stopSearch() {
        mClient.stopSearch();
    }

    /**
     * 连接蓝牙
     */
    public void connectBle(final String MAC, BleConnectResponse bleConnectResponse) {
        BleConnectOptions options = new BleConnectOptions.Builder()
                .setConnectRetry(2)   // 连接如果失败重试3次
                .setConnectTimeout(20000)   // 连接超时30s
                .setServiceDiscoverRetry(3)  // 发现服务如果失败重试3次
                .setServiceDiscoverTimeout(10000)  // 发现服务超时20s
                .build();

        mClient.connect(MAC, options, bleConnectResponse);

        mClient.registerConnectStatusListener(MAC, mBleConnectStatusListener);


    }
    /**
     *  查看连接状态
     * */
    private final BleConnectStatusListener mBleConnectStatusListener = new BleConnectStatusListener() {

        @Override
        public void onConnectStatusChanged(String mac, int status) {
            if (status == STATUS_CONNECTED) {

                Log.e("====连接状态" , "连接");
            } else if (status == STATUS_DISCONNECTED) {
                Log.e("====连接状态" ,"断开");

            }
        }
    };


    /**
     * 断开蓝牙
     */
    public void disConnectBle(String mac) {
        mClient.disconnect(mac);
        mClient.unregisterConnectStatusListener(mac, mBleConnectStatusListener);

    }

    /**
     * 发送消息
     */
    public void sendCmd(String mac, String serviceUUIDstr, String characterUUIDstr, byte[] bytes) {
        UUID serviceUUID = UUID.fromString(serviceUUIDstr);
        UUID characterUUID = UUID.fromString(characterUUIDstr);

        mClient.write(mac, serviceUUID, characterUUID, bytes, new BleWriteResponse() {
            @Override
            public void onResponse(int code) {

                Log.e("code===========" , code+"");
                if (code == Constants.REQUEST_SUCCESS) {
                    Log.e("发送成功===========","");
                }
            }
        });
    }


    public void openNotify(String mac, String serviceUUIDstr, String characterUUIDstr) {

        UUID serviceUUID = UUID.fromString(serviceUUIDstr);
        UUID characterUUID = UUID.fromString(characterUUIDstr);

        mClient.notify(mac, serviceUUID, characterUUID, new BleNotifyResponse() {
            @Override
            public void onNotify(UUID service, UUID character, byte[] value) {
                Log.e("收到byte（ZZ）=======" ,HexDump.BytetohexString( value));

            }

            @Override
            public void onResponse(int code) {
                if (code == Constants.REQUEST_SUCCESS) {
                    Log.e("通知开启成功===========","");
                }
            }
        });
    }


    /**
     *   发送广播
     * */

    // 发送广播消息
    private void broadcastUpdate(final String action ) {
        final Intent intent = new Intent(action);
        System.out.println("发现广播了===========");
        context.sendBroadcast(intent);
    }


    private void broadcastUpdate(final String action, Map<String,String>maps) {
        final Intent intent = new Intent(action);
        for (Map.Entry<String, String> entry : maps.entrySet()) {
           intent.putExtra(entry.getKey(),entry.getValue());
        }
        context.sendBroadcast(intent);
    }



}
