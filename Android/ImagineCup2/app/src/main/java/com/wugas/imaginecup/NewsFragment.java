package com.wugas.imaginecup;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by suhon_000 on 12/16/2014.
 */
public class NewsFragment extends ListFragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        final View listFragmentView = super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.news_layout, container, false);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        ListView listView = (ListView) rootView.findViewById(android.R.id.list);
        Resources res = getResources();
        String values[] = res.getStringArray(R.array.sample_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, values);
        listView.setAdapter(adapter);
        return rootView;
        // add list fragment content view to SwipeRefreshLayout
//        mSwipeRefreshLayout.addView(listFragmentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//
//        mSwipeRefreshLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        return mSwipeRefreshLayout;
    }

//    public boolean isRefreshing() {
//        return mSwipeRefreshLayout.isRefreshing();
//    }
//
//    public void setRefreshing(boolean refreshing) {
//        mSwipeRefreshLayout.setRefreshing(refreshing);
//    }
//
//    public void setColorScheme(int color1, int color2, int color3, int color4) {
//        mSwipeRefreshLayout.setColorSchemeColors(color1, color2, color3, color4);
//    }

    @Override
    public void onRefresh() {
        //TO DO
        // TIME ON REFRESH
        long refreshTime = 5000;
        Log.d(MainActivity.TAG, "refresh");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, refreshTime);
    }

//    private class ListFragmentSwipeRefreshLayout extends SwipeRefreshLayout {
//
//        public ListFragmentSwipeRefreshLayout(Context context) {
//            super(context);
//        }
//
//        @Override
//        public boolean canChildScrollUp() {
//            final ListView listView = getListView();
//            if (listView.getVisibility() == View.VISIBLE) {
//                return canListViewScrollUp(listView);
//            }
//            else {
//                return false;
//            }
//        }
//    }
//    private static boolean canListViewScrollUp(ListView listView) {
//        if (Build.VERSION.SDK_INT >= 14) {
//            return ViewCompat.canScrollVertically(listView, -1);
//        }
//        else {
//            return listView.getChildCount() > 0 &&
//                    (listView.getFirstVisiblePosition() > 0 || listView.getChildAt(0).getTop() < listView.getPaddingTop());
//        }
//    }
}
