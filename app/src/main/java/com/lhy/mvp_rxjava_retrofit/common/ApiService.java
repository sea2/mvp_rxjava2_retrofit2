package com.lhy.mvp_rxjava_retrofit.common;


import com.lhy.mvp_rxjava_retrofit.entity.BannerEntity;
import com.lhy.mvp_rxjava_retrofit.entity.DataTestInfo;
import com.lhy.mvp_rxjava_retrofit.http.entity.BaseResponse;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2018/3/23.
 */

public interface ApiService {

    String BASR_URL = "http://192.168.5.121:8081/";
    //缓存时间一个月
    long cache_time = 60 * 60 * 24 * 30;


    //缓存一个小时
    @Headers("Cache-Control: public, max-age=3600")
    @GET("appapi/index/banner/id/1?cmd=home_slider_top&limit=5")
    Observable<BaseResponse<List<BannerEntity>>> getBanner();

    @Headers("Cache-Control: public, max-age=3600")
    @GET("app/account/adddatatestget")
    Observable<BaseResponse<DataTestInfo>> getDataType(@Query("first_name") String first);

    int request_data_type_code = 1;

    @Headers({
            "Cache-Control: public, max-age=" + cache_time,
            "Content-Type: application/json;charset=UTF-8"
    })
    @POST("app/account/databytype")
    Observable<BaseResponse<DataTestInfo>> postDataType(@Body HashMap map);

    @Headers({
            "Cache-Control: public, max-age=" + cache_time,
            "Content-Type: application/json;charset=UTF-8"
    })
    @POST("app/account/databytype")
    Observable<String> postDataTypeString(@Body HashMap map);


    @Headers({
            "Cache-Control: public, max-age=" + cache_time,
            "Content-Type: application/json;charset=UTF-8"
    })
    @POST("app/account/databytype")
    Observable<BaseResponse<DataTestInfo>> postDataType1(@Body BannerEntity map);

    @POST("app/account/databytype")
    Observable<BaseResponse<DataTestInfo>> postDataType2(@FieldMap HashMap map);

    @Streaming
    @GET
    Observable<ResponseBody> downLoad(@Url String url);




   /* @Query、@QueryMap
    用于Http Get请求传递参数.如:*/

    @GET("group/users")
    Observable<DataTestInfo> groupList3(@Query("id") int groupId);

    /* 等同于:*/
    @GET("group/users?id=groupId")
    Observable<List<DataTestInfo>> groupList2(@Query("id") int groupId);
    /*   即将@Query的key-value添加到url后面组成get方式的参数,@QueryMap同理*/

   /* @Field
    用于Post方式传递参数,需要在请求接口方法上添加@FormUrlEncoded,即以表单的方式传递参数.示例:*/

    @FormUrlEncoded
    @POST("user/edit")
    Call<DataTestInfo> updateUser(@Field("first_name") String first, @Field("last_name") String last);
   /* @Body
    用于Post,根据转换方式将实例对象转化为对应字符串传递参数.比如Retrofit添加GsonConverterFactory则是将body转化为gson字符串进行传递.
            converter有如下:
    Gson: com.squareup.retrofit2:converter-gson
    Jackson: com.squareup.retrofit2:converter-jackson
    Moshi: com.squareup.retrofit2:converter-moshi
    Protobuf: com.squareup.retrofit2:converter-protobuf
    Wire: com.squareup.retrofit2:converter-wire
    Simple XML: com.squareup.retrofit2:converter-simplexml*/

    /*  @Path
      用于URL上占位符.如:*/
    @GET("group/{id}/users")
    Call<List<DataTestInfo>> groupList4(@Path("id") int groupId);
    /*将groupId变量的值替换到url上的id位置*/

   /* @Part
    配合@Multipart使用,一般用于文件上传,看官方文档示例:*/

    @Multipart
    @PUT("user/photo")
    Call<DataTestInfo> updateUser(@Part("photo") RequestBody photo, @Part("description") RequestBody description);

    /* @Header
     添加http header*/
    @GET("user")
    Call<DataTestInfo> getUser(@Header("Authorization") String authorization);

    /* 等同于:*/
    @Headers("Authorization: authorization")//这里authorization就是上面方法里传进来变量的值
    @GET("widget/list")
    Call<DataTestInfo> getUser();

    /* @Headers
     跟@Header作用一样,只是使用方式不一样,@Header是作为请求方法的参数传入,@Headers是以固定方式直接添加到请求方法上.示例:*/
    @Headers("Cache-Control: max-age=640000")
    @GET("widget/list")
    Call<List<DataTestInfo>> widgetList();

    /* 多个设置:*/
    @Headers({
            "Accept: application/vnd.github.v3.full+json",
            "User-Agent: Retrofit-Sample-App"
    })
    @GET("widget/list")
    Call<List<DataTestInfo>> widgetList2();
}
