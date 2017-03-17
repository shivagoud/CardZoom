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
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

        cardsView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mScale = mScale==1?1.2f:1;
                zoomTiles(mScale);
                return false;
            }
        });
    }

    void zoomTiles(float scale){
        int n = cardsView.getLayoutManager().getChildCount();
        for(int i=0;i<n;i++){
            View v = cardsView.getLayoutManager().getChildAt(i);
            v.setScaleX(scale);
            v.setScaleX(scale);
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
