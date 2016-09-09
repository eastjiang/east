package com.eastproject.app.search;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.eastproject.app.R;
import com.eastproject.app.views.NavigationActivity;

/**
 * Created by TMD on 2016/8/24.
 */
public class SortSearchActivity extends NavigationActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sortsearch_activity);

        setLeftBtn();
        setSearchEdit();
        getLeftImgBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SortSearchActivity.this.onBackPressed();
            }
        });

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.search_view_content, new CarBrandSearchFragment());
        ft.commit();
    }
}
