package com.twitter.university.android.yamba;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.twitter.university.android.yamba.svc.YambaService;


public abstract class YambaActivity extends Activity implements ServiceConnection {
    private YambaService service;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.yamba, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_tweet:
                nextPage(TweetActivity.class);
                break;

            case R.id.menu_timeline:
                nextPage(TimelineActivity.class);
                break;

            case R.id.menu_prefs:
                startActivity(new Intent(this, PrefsActivity.class));
                break;

            case R.id.menu_about:
                about();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onServiceConnected(ComponentName comp, IBinder binder) {
        service = ((YambaService.SvcBinder) binder).getService();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        service = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        unbindService(this);
        service = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        bindService(
            new Intent(this, YambaService.class),
            this,
            BIND_AUTO_CREATE);
    }

    public YambaService getService() { return service; }

    private void nextPage(Class<?> klass) {
        Intent i = new Intent(this, klass);
        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(i);
    }

    private void about() {
        Toast.makeText(this, R.string.about, Toast.LENGTH_LONG).show();
    }
}
