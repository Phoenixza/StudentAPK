package com.asa.welser.student;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends Activity implements
        AnimationListener {

  ImageView img1;

  // Animation
  Animation animFadein;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_start);

    new Timer().schedule(new TimerTask(){
      public void run() {
        StartActivity.this.runOnUiThread(new Runnable() {
          public void run() {
            startActivity(new Intent(StartActivity.this, MainActivity.class));
            finish();
          }
        });
      }
    }, 5000);


    img1 = (ImageView) findViewById(R.id.imgMOS);

    // load the animation
    animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
            R.anim.fade_in);

    // set animation listener
    animFadein.setAnimationListener(this);


    img1.setVisibility(View.VISIBLE);


    // start the animation
    img1.startAnimation(animFadein);

  }



  @Override
  public void onAnimationEnd(Animation animation) {
    // Take any action after completing the animation

  }

  @Override
  public void onAnimationRepeat(Animation animation) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onAnimationStart(Animation animation) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}