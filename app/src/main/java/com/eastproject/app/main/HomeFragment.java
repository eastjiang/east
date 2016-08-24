package com.eastproject.app.main;

import android.app.Fragment;
import android.app.NativeActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.eastproject.app.R;
import com.eastproject.app.sort.SortSearchActivity;
import com.eastproject.app.views.NavigationActivity;

/**
 * Created by TMD on 2016/8/16.
 */
public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        NavigationActivity activity = (NavigationActivity) getActivity();
        activity.setRightBtn();
        activity.getRightImgBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SortSearchActivity.class);
                startActivity(intent);
            }
        });
    }
}
