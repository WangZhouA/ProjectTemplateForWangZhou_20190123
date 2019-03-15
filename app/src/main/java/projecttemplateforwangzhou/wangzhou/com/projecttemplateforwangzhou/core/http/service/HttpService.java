package projecttemplateforwangzhou.wangzhou.com.projecttemplateforwangzhou.core.http.service;

import com.lib.fast.common.http.BaseResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * created by siwei on 2018/12/3
 */
public interface HttpService {

    //===================================用户模块接口================================

//    /**
//     * 登录
//     */
//    @POST("user/queryobj")
//    Observable<BaseResponse<User>> login(@Body RequestBody requestBody);
//
//    /**
//     * 注册
//     */
//    @POST("user/add")
//    Observable<BaseResponse<Void>> register(@Body RequestBody requestBody);

    /**
     * 发送验正码
     */
    @GET("authcode/send/{phone}")
    Observable<BaseResponse<Void>> sendCode(@Path("phone") String phone);

//
//    /**
//     * 查询个人信息
//     */
//    @POST("user/queryone")
//    Observable<BaseResponse<UserBean>> getUserInfo(@Body RequestBody requestBody);


    /**
     * 上传图片
     */
    @Multipart
    @POST("keycabinet/app/user/addHeadPicture")
    Observable<BaseResponse<String>> addHeadPicture(@Part List<MultipartBody.Part> parts);


}
