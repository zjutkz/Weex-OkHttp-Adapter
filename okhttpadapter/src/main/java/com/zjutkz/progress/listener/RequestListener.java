package com.zjutkz.progress.listener;

/**
 * Created by kangzhe on 16/9/22.
 */
public interface RequestListener {

    void onRequest(long consumed,long total,boolean done);
}
