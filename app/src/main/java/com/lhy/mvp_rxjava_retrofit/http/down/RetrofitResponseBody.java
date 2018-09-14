package com.lhy.mvp_rxjava_retrofit.http.down;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class RetrofitResponseBody extends ResponseBody {

    private ResponseBody responseBody;

    private RetrofitDownloadListener downloadListener;

    // BufferedSource 是okio库中的输入流，这里就当作inputStream来使用。
    private BufferedSource bufferedSource;

    public RetrofitResponseBody(ResponseBody responseBody, RetrofitDownloadListener downloadListener) {
        this.responseBody = responseBody;
        this.downloadListener = downloadListener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(@NonNull Buffer sink, long byteCount) throws IOException {
                final long bytesRead = super.read(sink, byteCount);

                // read() 返回读取的字节数，如果这个源耗尽，则返回-1。
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                if (null != downloadListener) {
                    if (bytesRead != -1 && contentLength() != -1) {
                        Log.e("cylog", "下载进度：" + (100 * totalBytesRead) / contentLength() + "%");
                        downloadListener.onProgress((int) (totalBytesRead * 100 / contentLength()));
                    }
                }
                return bytesRead;
            }
        };

    }
}
