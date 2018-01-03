package net.mosur.tomasz.pam11;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v4.app.INotificationSideChannel;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static net.mosur.tomasz.pam11.R.id.imageView;

public class view_image extends AppCompatActivity{

    int SWIPE_MIN_DISTANCE = 0;
    int SWIPE_THRESHOLD_VELOCITY = 0;
    ImageView iv;
    List<Integer> idList = new ArrayList<>();
    int position;
    GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
    final long animationDuration = 150;
    final long swipeDuration = 150;
    float swipeDistance = 1000f;
    final static int LEFT = 1;
    final static int RIGHT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        Intent i = getIntent();
        idList = i.getIntegerArrayListExtra("idList");
        position = i.getIntExtra("position", 0);
        iv = (ImageView) findViewById(R.id.imageView2);
        iv.setImageResource(idList.get(position%3));



        gestureDetector = new GestureDetector(this, new MyGestureDetector());
        gestureListener = new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        };

       // iv.setOnClickListener(SelectFilterActivity.this);
        iv.setOnTouchListener(gestureListener);
    }

    private void positionUp()
    {
        if(position>1) position = 0;
        else { position++;}
    }

    private void positionDown()
    {
        if(position==0) position = 2;
        else { position--;}
    }

    private void animate(int direction)
    {
        if(direction == LEFT && swipeDistance > 0 ) swipeDistance=-swipeDistance;
        if(direction == RIGHT && swipeDistance < 0 ) swipeDistance=-swipeDistance;
        final int dir= direction;

        ObjectAnimator fade = ObjectAnimator.ofFloat(iv,View.ALPHA, 1.0f, 0.0f);
        ObjectAnimator swipe = ObjectAnimator.ofFloat(iv, "x", swipeDistance);
        final ObjectAnimator swipe2 = ObjectAnimator.ofFloat(iv, "x", 0);
        final ObjectAnimator resetDist = ObjectAnimator.ofFloat(iv, "x", -swipeDistance);
        final ObjectAnimator resetFade = ObjectAnimator.ofFloat(iv, View.ALPHA, 0.0f, 1.0f);


        fade.setDuration(animationDuration);
        resetFade.setDuration(animationDuration);
        resetDist.setDuration(1L);
        swipe.setDuration(swipeDuration);
        swipe2.setDuration(swipeDuration);

        resetDist.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                AnimatorSet animset2 = new AnimatorSet();
                animset2.playTogether(resetFade, swipe2);
                animset2.start();
            }
        });
        swipe.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                AnimatorSet animset2 = new AnimatorSet();
                animset2.play(resetDist);
                animset2.start();

                if(dir == LEFT) positionDown();
                else { positionUp();}
                iv.setImageResource(idList.get(position%3));
            }
        });
        AnimatorSet animset = new AnimatorSet();
        animset.play(fade);
        animset.start();
        animset.play(swipe);
        animset.start();
    }

    class MyGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {

                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    animate(LEFT);

                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    animate(RIGHT);
                   // positionUp();
                    //iv.setImageResource(idList.get(position%3));

                }

            } catch (Exception e) {
                // nothing
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    }
}
