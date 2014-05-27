package com.twitter.university.android.yamba;

import android.app.Fragment;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.marakana.android.yamba.clientlib.YambaClientException;


public class TweetFragment extends Fragment {
    private static final String TAG = "TWEET";

    static final class Poster extends AsyncTask<String, Void, Integer> {
        private final YambaApplication app;

        public Poster(YambaApplication app) { this.app = app; }

        @Override
        protected Integer doInBackground(String... tweet) {
            int msg = R.string.tweet_succeeded;
            try { app.getClient().postStatus(tweet[0]); }
            catch (YambaClientException e) {
                Log.e(TAG, "post failed", e);
                msg = R.string.tweet_failed;
            }
            return Integer.valueOf(msg);
        }

        @Override
        protected void onPostExecute(Integer msg) {
            cleanup(msg.intValue());
        }

        @Override
        protected void onCancelled() {
            cleanup(R.string.tweet_failed);
        }

        private void cleanup(int msg) {
            Toast.makeText(app, msg, Toast.LENGTH_LONG).show();
        }
    }


    private int okColor;
    private int warnColor;
    private int errColor;

    private int tweetMax;
    private int warnMax;
    private int errMax;

    private EditText tweetView;
    private TextView countView;
    private View submitButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Resources rez = getResources();

        okColor = rez.getColor(R.color.tweet_len_ok);
        warnColor = rez.getColor(R.color.tweet_len_warn);
        errColor = rez.getColor(R.color.tweet_len_err);

        tweetMax = rez.getInteger(R.integer.tweet_max);
        warnMax = rez.getInteger(R.integer.warn_max);
        errMax = rez.getInteger(R.integer.err_max);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle state) {
       View v = inflater.inflate(R.layout.fragment_tweet, root, false);

        submitButton = v.findViewById(R.id.tweet_submit);
        submitButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) { post(); }
            }
        );

        countView = (TextView) v.findViewById(R.id.tweet_count);
        tweetView = (EditText) v.findViewById(R.id.tweet_tweet);
        tweetView.addTextChangedListener(
            new TextWatcher() {

                @Override
                public void afterTextChanged(Editable str) { updateCount(); }

                @Override
                public void beforeTextChanged(CharSequence str, int s, int c, int a) { }

                @Override
                public void onTextChanged(CharSequence str, int s, int c, int a) { }
            }
        );

        return v;
    }

    void updateCount() {
        int n = tweetView.getText().length();

        submitButton.setEnabled(checkTweetLen(n));

        n = tweetMax - n;

        int color;
        if (n > warnMax) { color = okColor; }
        else if (n > errMax) { color = warnColor; }
        else  { color = errColor; }

        countView.setText(String.valueOf(n));
        countView.setTextColor(color);
    }

    void post() {
        String tweet = tweetView.getText().toString();
        if (!checkTweetLen(tweet.length())) { return; }

        tweetView.setText("");

        new Poster((YambaApplication) getActivity().getApplication()).execute(tweet);
    }

    private boolean checkTweetLen(int n) {
        return (errMax < n) && (tweetMax > n);
    }
}
