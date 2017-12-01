package com.mobilophilia.mydairy.timer;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public abstract class TimeoutActivity extends AppCompatActivity {

  protected abstract void onTimeout();

  protected abstract long getTimeoutInSeconds();

  @Override protected void onResume() {
    super.onResume();
    EventBus.getDefault().register(this);
    Timeout timeout = Timeout.getInstance(this, getTimeoutInSeconds());
    boolean isTimeout = timeout.interact();
    if (isTimeout) {
      timeout.resetTimeout();
      onTimeout();
    }
  }
  @Subscribe(threadMode = ThreadMode.MAIN)
  public void onEventMainThread(OnTimeoutEvent event) {
    Timeout.getInstance(this, getTimeoutInSeconds()).resetTimeout();
    onTimeout();
  }

  @Override protected void onPause() {
    super.onPause();
    EventBus.getDefault().unregister(this);
  }

  @Override public void onUserInteraction() {
    super.onUserInteraction();
    Timeout.getInstance(this, getTimeoutInSeconds()).interact();
  }
}
