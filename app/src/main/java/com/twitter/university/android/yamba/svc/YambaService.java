package com.twitter.university.android.yamba.svc;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.twitter.university.android.yamba.R;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.SECONDS;


/**
 * Created by bmeike on 5/16/14.
 */
public class YambaService extends Service {
    private static final String TAG = "SVC";

    private static final ScheduledExecutorService EXECUTOR = Executors.newScheduledThreadPool(3);
    private static final AtomicInteger IN_QUEUE = new AtomicInteger();


    public interface TweetCompleteListener { void onTweetComplete(int msg); }

    public class SvcBinder extends Binder {
        public YambaService getService() { return YambaService.this; }
    }

    public abstract class Task<CALLBACK, RESPONSE> implements Runnable {
        private final WeakReference<CALLBACK> callback;
        private volatile boolean reqComplete;
        private RESPONSE response;

        protected abstract RESPONSE doRequest();
        protected abstract void doResponse(CALLBACK callback, RESPONSE response);

        public Task(CALLBACK callback) {
            this.callback = new WeakReference<CALLBACK>(callback);
        }

        @Override
        public void run() {
            if (reqComplete) {
                CALLBACK callback = this.callback.get();
                if (null != callback) { doResponse(callback, response); }
                return;
            }

            int n;
            try {
                n = IN_QUEUE.getAndIncrement();
                if (0 == n) {
                    YambaService.this.startService(new Intent(YambaService.this, YambaService.class));
                }

                try { response = doRequest(); }
                finally { reqComplete = true; }

                hdlr.post(this);
            }
            finally {
                n = IN_QUEUE.decrementAndGet();
                if (0 >= n) { YambaService.this.stopSelf(); }
            }
        }
    }

    private final Handler hdlr = new Handler();
    private final IBinder binder = new SvcBinder();

    private volatile int pollInterval;
    private volatile YambaLogic helper;

    private ScheduledFuture<?> poller;

    @Override
    public void onCreate() {
        super.onCreate();
        pollInterval = getResources().getInteger(R.integer.poll_interval);
        helper = new YambaLogic(this);
    }

    @Override
    public IBinder onBind(Intent intent) { return binder; }

    public void post(final String tweet, TweetCompleteListener listener) {
        EXECUTOR.execute(
            new Task<TweetCompleteListener, Integer>(listener) {
                @Override
                protected Integer doRequest() { return helper.doPost(tweet); }

                @Override
                protected void doResponse(TweetCompleteListener cb, Integer resp) {
                    cb.onTweetComplete(resp.intValue());
                }
            }
        );
    }

    public synchronized void startPolling() {
        if (null != poller) { return; }

        poller = EXECUTOR.scheduleAtFixedRate(
            new Runnable() {
                @Override public void run() { helper.doPoll(); }
            },
            10,
            pollInterval,
            SECONDS);
        Log.d(TAG, "Polling started");
    }

    public synchronized void stopPolling() {
        if (null == poller) { return; }

        poller.cancel(true);
        Log.d(TAG, "Polling stopped");
    }
}
