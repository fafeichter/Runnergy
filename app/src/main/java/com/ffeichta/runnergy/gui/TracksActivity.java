package com.ffeichta.runnergy.gui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ffeichta.runnergy.R;
import com.ffeichta.runnergy.model.DBAccessHelper;
import com.ffeichta.runnergy.model.Track;

import java.util.ArrayList;

/**
 * Created by Fabian on 28.12.2015.
 */
public class TracksActivity extends android.support.v4.app.Fragment {
    private static final String TAG = TracksActivity.class.getSimpleName();
    private ArrayList<Track> tracks = null;
    private ListView listView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_tracks, container, false);
        listView = (ListView) v.findViewById(R.id.listView);
        tracks = DBAccessHelper.getInstance(getContext()).getTracks();
        Log.d(TAG, "####" + tracks.size());
        for (Track t : tracks) {
            Log.d(TAG, "###" + t.getName());
        }
        if (tracks == null) {
            tracks = new ArrayList<>();
        }

        TrackAdapter trackAdapter = new TrackAdapter(this.getActivity(), tracks);
        listView.setAdapter(trackAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(),
                        ActivitiesActivity.class);
                i.putExtra("track", tracks.get(position).getActivities());
                startActivity(i);
            }
        });
        return v;
    }

    /**
     * Called when an activity started by this Fragment is finished
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        tracks = DBAccessHelper.getInstance(getContext()).getTracks();
        if (tracks == null) {
            tracks = new ArrayList<>();
        }

        TrackAdapter trackAdapter = new TrackAdapter(this.getActivity(), tracks);
        listView.setAdapter(trackAdapter);
    }
}
