package com.eastproject.app.search;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.alibaba.fastjson.JSONArray;
import com.eastproject.app.R;
import com.eastproject.app.client.EASTCallback;
import com.eastproject.app.client.EASTClient;
import com.eastproject.app.client.EASTException;
import com.eastproject.app.models.SortModel;
import com.eastproject.app.utils.CharacterParser;
import com.eastproject.app.utils.PinyinComparator;
import com.eastproject.app.utils.PopUtils;
import com.eastproject.app.views.NavigationActivity;
import com.eastproject.app.views.SideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by TMD on 2016/9/9.
 */
public class CarBrandSearchFragment extends Fragment {
    private TextView dialog;
    private ListView sortListView;
    private SideBar sideBar;
    private EditText mSearchEdit;
    /**
     * 显示字母的TextView
     */
    private SortAdapter adapter;

    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser characterParser;
    private List<SortModel> mSourceDateList;

    /**
     * 根据拼音来排列ListView里面的数据类
     */
    private PinyinComparator pinyinComparator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_carbrand_search, container, false);
        initViews(v);
        return v;
    }

    private void initViews(View view) {
        //实例化汉字转拼音类
        characterParser = CharacterParser.getInstance();

        pinyinComparator = new PinyinComparator();

        sideBar = (SideBar) view.findViewById(R.id.side_bar);
        dialog = (TextView) view.findViewById(R.id.letter);
        sideBar.setTextView(dialog);
        mSearchEdit = ((NavigationActivity) getActivity()).getSearchEdit();

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if(position != -1){
                    sortListView.setSelection(position);
                }

            }
        });

        sortListView = (ListView) view.findViewById(R.id.car_brand);
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //这里要利用adapter.getItem(position)来获取当前position所对应的对象
                Toast.makeText(getActivity(), ((SortModel)adapter.getItem(position)).getName(), Toast.LENGTH_SHORT).show();
            }
        });

//        SourceDateList = filledData(getResources().getStringArray(R.array.date));
        if (mSourceDateList == null) {
            loadData();
        }
    }

    private void loadData() {
        EASTClient.getInstance(getActivity()).queryUrl("/car_brand.php", new EASTCallback<JSONArray>() {
            @Override
            public void done(JSONArray array, EASTException e) {
                if (e != null) {
                    PopUtils.showToast(getActivity(), e.getMessage());
                    getActivity().onBackPressed();
                } else {
                    if (array != null) {
                        mSourceDateList = filledData(array);
                        // 根据a-z进行排序源数据
                        Collections.sort(mSourceDateList, pinyinComparator);
                        adapter = new SortAdapter(getActivity(), mSourceDateList);
                        sortListView.setAdapter(adapter);
                    }
                }
            }
        }, JSONArray.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSearchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterData(s.toString());
            }
        });
    }

    /**
     * 为ListView填充数据
     * @param date
     * @return
     */
    private List<SortModel> filledData(JSONArray date){
        List<SortModel> mSortList = new ArrayList<SortModel>();

        for(int i=0; i<date.size(); i++){
            SortModel sortModel = new SortModel();
            sortModel.setName(date.getJSONObject(i).get("brand_name").toString());
            //汉字转换成拼音
            String pinyin = characterParser.getSelling(date.getJSONObject(i).get("brand_name").toString());
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if(sortString.matches("[A-Z]")){
                sortModel.setSortLetters(sortString.toUpperCase());
            }else{
                sortModel.setSortLetters("#");
            }

            mSortList.add(sortModel);
        }
        return mSortList;

    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<SortModel> filterDateList = new ArrayList<SortModel>();

        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = mSourceDateList;
        } else {
            filterDateList.clear();
            for (SortModel sortModel : mSourceDateList) {
                String name = sortModel.getName();
                if (name.toUpperCase().indexOf(
                        filterStr.toString().toUpperCase()) != -1
                        || characterParser.getSelling(name).toUpperCase()
                        .startsWith(filterStr.toString().toUpperCase())) {
                    filterDateList.add(sortModel);
                }
            }
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        adapter.updateListView(filterDateList);
    }
}
