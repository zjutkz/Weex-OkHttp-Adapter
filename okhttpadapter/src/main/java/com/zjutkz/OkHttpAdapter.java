package com.zjutkz;

import com.taobao.weex.adapter.IWXHttpAdapter;
import com.taobao.weex.common.WXRequest;
import com.taobao.weex.common.WXResponse;
import com.zjutkz.progress.IncrementalRequestBody;
import com.zjutkz.progress.IncrementalResponseBody;
import com.zjutkz.progress.listener.RequestListener;
import com.zjutkz.progress.listener.ResponseListener;
import com.zjutkz.utils.Assert;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by kangzhe on 16/9/22.
 */
public class OkHttpAdapter implements IWXHttpAdapter{

    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    public void sendRequest(WXRequest request, final OnHttpListener listener) {
        if (listener != null) {
            listener.onHttpStart();
        }

        RequestListener requestListener = new RequestListener() {
            @Override
            public void onRequest(long consumed, long total, boolean done) {
                if(Assert.checkNull(listener)){
                    listener.onHttpUploadProgress((int) (consumed / total));
                }
            }
        };

        final ResponseListener responseListener = new ResponseListener() {
            @Override
            public void onResponse(long consumed, long total, boolean done) {
                if(Assert.checkNull(listener)){
                    listener.onHttpResponseProgress((int) (consumed / total));
                }
            }
        };

        OkHttpClient client = new OkHttpClient();
        if(METHOD_GET.equalsIgnoreCase(request.method)){
            Request okHttpRequest = new Request.Builder().url(request.url).build();
            client.newCall(okHttpRequest).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(Assert.checkNull(listener)){

                        WXResponse wxResponse = new WXResponse();
                        wxResponse.statusCode = String.valueOf(response.code());
                        wxResponse.originalData = response.body().bytes();

                        listener.onHttpFinish(wxResponse);
                    }
                }
            });
        }else if(METHOD_POST.equalsIgnoreCase(request.method)){
            client.networkInterceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    //拦截
                    Response originalResponse = chain.proceed(chain.request());
                    //包装响应体并返回
                    return originalResponse.newBuilder()
                            .body(new IncrementalResponseBody(originalResponse.body(), responseListener))
                            .build();
                }
            });

            Request okHttpRequest = new Request.Builder()
                    .url(request.url)
                    .post(new IncrementalRequestBody(RequestBody.create(JSON,request.body),requestListener))
                    .build();

            client.newCall(okHttpRequest).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if(Assert.checkNull(listener)){

                        WXResponse wxResponse = new WXResponse();
                        wxResponse.statusCode = String.valueOf(response.code());
                        wxResponse.originalData = response.body().bytes();

                        listener.onHttpFinish(wxResponse);
                    }
                }
            });
        }
    }
}
