package com.lib.fast.common.http.params;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * created by siwei on 2018/5/23
 * okhttp3 json request body 构建
 * Gson TypeAdapter用法:https://blog.csdn.net/piglite/article/details/72910598
 */
public class JsonRequestBodyBuilder {

    private static final String MEDIA_TYPE = "Content-Type, application/json; charset=UTF-8";

    Map<String, Object> jsonMap = new HashMap<>();
    private static Gson sDefaultGson = new Gson();
    private Gson mGson;

    public static JsonRequestBodyBuilder create(Gson gson) {
        return new JsonRequestBodyBuilder(gson);
    }

    public static JsonRequestBodyBuilder create() {
        return create(sDefaultGson);
    }

    private JsonRequestBodyBuilder(Gson gson) {
        mGson = gson;
    }

    /**
     * 添加参数
     *
     * @param key     参数key
     * @param value   参数
     * @param convert 参数转换器
     */
    public JsonRequestBodyBuilder addParam(String key, Object value, ValueConvert convert) {
        if (convert != null && value != null) {
            addParam(key, convert.convert(value));
        }
        return this;
    }

    /**
     * @param key      参数key
     * @param calendar 日期日历
     * @param format   格式转换器
     */
    public JsonRequestBodyBuilder addParam(String key, Calendar calendar, DateFormat format) {
        if (calendar != null) {
            addParam(key, calendar.getTime(), format);
        }
        return this;
    }

    /**
     * 添加参数
     *
     * @param key    参数key
     * @param date   时间戳
     * @param format 格式转换器
     */
    public JsonRequestBodyBuilder addParam(String key, long date, DateFormat format) {
        return addParam(key, new Date(date), format);
    }

    /**
     * 添加参数
     *
     * @param key    参数key
     * @param date   时间
     * @param format 格式转换器
     */
    public JsonRequestBodyBuilder addParam(String key, Date date, DateFormat format) {
        if (date != null && format != null) {
            addParam(key, format.format(date));
        }
        return this;
    }

    public JsonRequestBodyBuilder addParam(String key, Object value) {
        jsonMap.put(key, value == null ? "" : value);
        return this;
    }

    public RequestBody build() {
        return buildData(jsonMap, mGson);
    }

    public static RequestBody buildData(Object object) {
        return RequestBody.create(MediaType.parse(MEDIA_TYPE), sDefaultGson.toJson(object));
    }

    public static RequestBody buildData(Object object, Gson gson) {
        return RequestBody.create(MediaType.parse(MEDIA_TYPE), gson.toJson(object));
    }

    public static RequestBody buildJson(String json) {
        return RequestBody.create(MediaType.parse(MEDIA_TYPE), json);
    }

    public interface ValueConvert {

        String convert(Object value);
    }

}
