package com.mobilophilia.mydairy.timer;

import android.content.Context;

import com.mobilophilia.mydairy.common.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class Timeout {

  private static final String COOKIE_ID = "$_timeoutCookie";

  private static final ScheduledExecutorService worker =
      Executors.newSingleThreadScheduledExecutor();
  private static final Runnable task = new Runnable() {
    @Override public void run() {
      EventBus.getDefault().post(new OnTimeoutEvent());
    }
  };

  private static Timeout singleton;
  public long timeoutSeconds;
  private Context context;
  private ScheduledFuture<?> scheduledTask;

  private Timeout(Context context, long timeoutSeconds) {
    this.context = context;
    this.timeoutSeconds = timeoutSeconds;
  }

  public static Timeout getInstance(Context context, long timeoutSeconds) {
    if (singleton == null) {
      Log.d("Creating new timeout instance with Context=" ,""+context.getApplicationContext().toString() + ", timeoutSeconds=" + timeoutSeconds);
      singleton = new Timeout(context.getApplicationContext(), timeoutSeconds);
    }
    return singleton;
  }

  public static void resetCookie(Context context) {
    Log.d("Resetting timeout cookie","resetCookie");
    new Cookie(context, COOKIE_ID).write(Cookie.INVALID_TIMESTAMP);
  }

  /**
   * @return true - has timed out, false - otherwise
   */
  public boolean interact() {
    boolean result = false;
    synchronized (this) {
      Cookie cookie = new Cookie(context, COOKIE_ID);
      long prevTimestamp = cookie.read();
      long now = System.currentTimeMillis();
      if (prevTimestamp != Cookie.INVALID_TIMESTAMP) {
        if (now - prevTimestamp > timeoutSeconds * 1000) {
          Log.d("now - prevTimestamp / 1000 = " ,""+ ((now - prevTimestamp) / 1000));
          result = true;
        }
      }
      cookie.write(now);
    }
    if (scheduledTask != null) {
      scheduledTask.cancel(false);
    }
    scheduledTask = worker.schedule(task, timeoutSeconds, TimeUnit.SECONDS);
    return result;
  }

  public void resetTimeout() {
    Log.d("Resetting timeout","resetTimeout");
    synchronized (this) {
      Cookie cookie = new Cookie(context, COOKIE_ID);
      cookie.write(Cookie.INVALID_TIMESTAMP);
    }
    if (scheduledTask != null) {
      scheduledTask.cancel(false);
    }
  }
}
