package com.eastproject.app.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;
import com.eastproject.app.R;
import com.eastproject.app.models.SortModel;

import java.util.List;

/**
 * Created by jiang.dong on 2016/8/19.
 */
public class SortAdapter extends BaseAdapter implements SectionIndexer {
    private List<SortModel> list = null;
    private Context mContext;

    public SortAdapter(Context context, List<SortModel> list) {
        this.list = list;
        mContext = context;
    }

    public void updateListView(List<SortModel> list){
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sort, parent, false);
        }

        TextView mTitle = (TextView) convertView.findViewById(R.id.title);
        TextView mName = (TextView) convertView.findViewById(R.id.car_brand);

        //根据position获取分类的首字母的char ascii值
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的char ascii值 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            mTitle.setVisibility(View.VISIBLE);
            mTitle.setText(list.get(position).getSortLetters());
        } else {
            mTitle.setVisibility(View.GONE);
        }

        mName.setText(list.get(position).getName());
        return convertView;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     * @param section 首字母的Char ascii值
     * @return 第一次出现该首字母的位置
     */
    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            char firstChar = list.get(i).getSortLetters().toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的char ascii值
     * @param position 当前位置
     * @return 首字母的char ascii值
     */
    @Override
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().toUpperCase().charAt(0);
    }
}
