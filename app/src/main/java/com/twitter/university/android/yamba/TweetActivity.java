package com.twitter.university.android.yamba;

import android.os.Bundle;
import android.widget.Toast;

import com.twitter.university.android.yamba.svc.YambaService;


public class TweetActivity extends YambaActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
    }

    public void postTweet(String tweet) {
        YambaService svc = getService();
        if (null == svc) { return; }
         svc.post(
              tweet,
              new YambaService.TweetCompleteListener() {
                  @Override
                  public void onTweetComplete(int msg) {
                      Toast.makeText(TweetActivity.this, msg, Toast.LENGTH_LONG).show();
                  }
              } );
    }
}
