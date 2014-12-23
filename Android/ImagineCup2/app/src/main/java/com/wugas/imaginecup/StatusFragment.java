package com.wugas.imaginecup;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by suhon_000 on 12/16/2014.
 */
public class StatusFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.status_layout, container, false);
        ListView listView  = (ListView) rootView.findViewById(android.R.id.list);
        Resources res = getResources();
        String values[] = res.getStringArray(R.array.disease_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, values);
        listView.setAdapter(adapter);
        return rootView;
    }
}
