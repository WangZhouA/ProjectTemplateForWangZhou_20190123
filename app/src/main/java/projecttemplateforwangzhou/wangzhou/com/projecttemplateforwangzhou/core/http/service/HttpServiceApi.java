package projecttemplateforwangzhou.wangzhou.com.projecttemplateforwangzhou.core.http.service;


import com.lib.fast.common.http.BaseResponse;
import com.lib.fast.common.http.RetorfitServices;

import io.reactivex.Observable;


/**
 * created by siwei on 2018/12/3
 */
public class HttpServiceApi {

    private static HttpServiceApi sHttpServiceApi;
    private HttpService mHttpService;

    private HttpServiceApi() {
        mHttpService = RetorfitServices.getService(HttpService.class);
    }

    public static HttpServiceApi instance() {
        if (sHttpServiceApi == null) {
            synchronized (HttpServiceApi.class) {
                sHttpServiceApi = new HttpServiceApi();
            }
        }
        return sHttpServiceApi;
    }

    //===================================用户模块接口================================

//    /**
//     * 登录
//     *  @param phone 密码
//     * @param pwd   手机号
//     */
//    public Observable<BaseResponse<User>> login(String phone, String pwd) {
//        RequestBody requestBody = JsonRequestBodyBuilder.create()
//                .addParam("userPhone", phone)
//                .addParam("userPwd", pwd)
//                .build();
//        return mHttpService.login(requestBody);
//    }
//
//    /**
//     * 注册
//     *
//     * @param phone      手机号
//     * @param password   密码
//     * @param name      昵称
//     * @param code       验证码
//     */
//    public Observable<BaseResponse<Void>> register(String phone, String password, String name, String code) {
//        RequestBody requestBody = JsonRequestBodyBuilder.create()
//                .addParam("userPhone", phone)
//                .addParam("userPwd", password)
//                .addParam("userName", name)
//                .addParam("authCode", code).build();
//        return mHttpService.register(requestBody);
//    }


    /**
     * @param  phone 电话号码
     * */

    public Observable<BaseResponse<Void>> sendCode(String phone) {
        return mHttpService.sendCode(phone);
    }

//
//    /**
//     * @param  phone 查询个人信息
//     * */
//
//    public Observable<BaseResponse<UserBean>> getUserInfo(String phone) {
//        RequestBody requestBody = JsonRequestBodyBuilder.create()
//                .addParam("userPhone", phone)
//                .build();
//        return mHttpService.getUserInfo(requestBody);
//    }


//    /**
//     * 上传图片
//     *
//     * @param uid     用户id
//     * @param imgFile 图片文件
//     */
//    public Observable<BaseResponse<String>> addHeadPicture(long uid, File imgFile) {
//        MultipartBody.Builder builder = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("userId", String.valueOf(uid));
//        RequestBody imgRequestBody = RequestBody.create(MultipartBody.FORM, imgFile);
//        XLog.d("fileName:%s", String.format("avater_%s_%s", String.valueOf(uid), imgFile.getName()));
//        builder.addFormDataPart("file", String.format("avater_%s_%s", String.valueOf(uid), imgFile.getName()), imgRequestBody);
//        return mHttpService.addHeadPicture(builder.build().parts());
//    }
//
}
