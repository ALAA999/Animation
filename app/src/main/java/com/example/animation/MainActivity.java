package com.example.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Adapter.ListItemClickListener {

    // If you want to play the animation immediately, without requiring interaction (ClickListener), then you might want to call it from the onStart() method in your Activity, which will get called when Android makes the view visible on screen.
    AnimationDrawable animationDrawable;
    ArrayList<String> strings = new ArrayList<>();
    RecyclerView recyclerView;
    Adapter adapter;
    LinearLayout my_layout;
    private View contentView;
    private View loadingView;
    private int shortAnimationDuration = 500;
    boolean isContentShown = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView image = (ImageView) findViewById(R.id.my_image);
        image.setBackgroundResource(R.drawable.animation);
        animationDrawable = (AnimationDrawable) image.getBackground();
        my_layout = findViewById(R.id.my_layout);

        findViewById(R.id.card_flip_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CardFlipActivity.class));
            }
        });

        findViewById(R.id.circle_reveal_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CircularRevealActivity.class));
            }
        });

        findViewById(R.id.zoom_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ZoomActivity.class));
            }
        });

        findViewById(R.id.fling_animation_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, FlingAnimationActivity.class));
            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationDrawable.start();
                RadioButton radioButton = new RadioButton(MainActivity.this);
                radioButton.setText("Hello");
                my_layout.addView(radioButton);
            }
        });

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

        createRc();

// >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        contentView = findViewById(R.id.content);
        loadingView = findViewById(R.id.loading_spinner);

        // Initially hide the content view.
        contentView.setVisibility(View.GONE);

        // Retrieve and cache the system's default "short" animation time.
       // shortAnimationDuration = getResources().getInteger(
         //       android.R.integer.config_shortAnimTime);

        // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
    }

    private void createRc() {
        recyclerView = findViewById(R.id.rv);
        strings.add("Hello World");
        strings.add("Hello World");
        strings.add("Hello World");

        adapter = new Adapter(this, strings, this);
        LinearLayoutManager LayoutManagaer
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(LayoutManagaer);
        recyclerView.setAdapter(adapter);
    }

    private void crossfadeProgress() {
        isContentShown = false;
        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        contentView.setAlpha(0f);
        contentView.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        contentView.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        loadingView.animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        loadingView.setVisibility(View.GONE);

//                        // Set the content view to 0% opacity but visible, so that it is visible
//                        // (but fully transparent) during the animation.
//                        contentView.setAlpha(0f);
//                        contentView.setVisibility(View.VISIBLE);
//
//                        // Animate the content view to 100% opacity, and clear any animation
//                        // listener set on the view.
//                        contentView.animate()
//                                .alpha(1f)
//                                .setDuration(shortAnimationDuration)
//                                .setListener(null);
                    }
                });
    }

    private void crossfadeContent() {
        isContentShown = true;
        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        loadingView.setAlpha(0f);
        loadingView.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        loadingView.animate()
                .alpha(1f)
                .setDuration(shortAnimationDuration)
                .setListener(null);

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        contentView.animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        contentView.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onListItemClicked(int position) {
        //strings.add("Hello World 2");
       // adapter.notifyDataSetChanged();
        if (isContentShown)
            crossfadeProgress();
        else
            crossfadeContent();
    }
}
