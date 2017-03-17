package com.shivagoud.cardzoom;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<ResolveInfo> appsList;
    RecyclerView cardsView;
    float mScale = 1f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        appsList = getPackageManager().queryIntentActivities( mainIntent, 0);

        cardsView = (RecyclerView)findViewById(R.id.cardsView);
        cardsView.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));

        cardsView.setAdapter(new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.zoomable_card, parent, false);
                view.setScaleX(mScale);
                view.setScaleY(mScale);
                view.invalidate();
                return new ZoomableCardViewHolder(view);
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                ZoomableCardViewHolder h = (ZoomableCardViewHolder) holder;
                Drawable icon =appsList.get(position).loadIcon(getPackageManager());
                CharSequence name =appsList.get(position).loadLabel(getPackageManager());
                h.icon.setImageDrawable(icon);
                h.title.setText(name);
            }

            @Override
            public int getItemCount() {
                return appsList.size();
            }
        });

        final ScaleGestureDetector mScaleDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
            }
            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                Log.d("Scale_On_Pinch", "zoom ongoing, scale: " + detector.getScaleFactor());

                mScale *= detector.getScaleFactor();

                // Don't let the object get too small or too large.
                mScale = Math.max(0.2f, Math.min(mScale, 1.2f));

                zoomTiles(mScale);
                return true;


            }
        });

        cardsView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScaleDetector.onTouchEvent(event);
                return false;
            }
        });
    }

    void zoomTiles(float scale){
        int n = cardsView.getLayoutManager().getChildCount();
        for(int i=0;i<n;i++){
            View v = cardsView.getLayoutManager().getChildAt(i);
            v.setScaleX(scale);
            v.setScaleY(scale);
            v.invalidate();
        }
    }

    private class ZoomableCardViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;
        public ZoomableCardViewHolder(View view) {
            super(view);
            icon = (ImageView)view.findViewById(R.id.iconView);
            title = (TextView)view.findViewById(R.id.titleView);
        }
    }
}
