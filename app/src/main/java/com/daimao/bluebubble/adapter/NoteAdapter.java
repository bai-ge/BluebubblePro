package com.daimao.bluebubble.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.daimao.bluebubble.R;
import com.daimao.bluebubble.data.model.NoteEntity;
import com.daimao.bluebubble.util.ImageLoader;
import com.daimao.bluebubble.util.Tools;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.etsy.android.grid.util.DynamicHeightTextView;

import java.util.Random;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.kit.KnifeKit;

import static com.daimao.bluebubble.AppConfigure.LOG_TAG;


/**
 * Created by baige on 2018/6/19.
 */

public class NoteAdapter extends ArrayAdapter<NoteEntity> {

    private int resourceId;

    private final Random mRandom;

    private static final SparseArray<Double> sPositionHeightRatios = new SparseArray<Double>();

    public NoteAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        resourceId = resource;
        mRandom = new Random();
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Nullable
    @Override
    public NoteEntity getItem(int position) {
        return super.getItem(position);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(resourceId, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        setHolder(holder, position);
        return convertView;
    }

    private void setHolder(final ViewHolder holder, int position) {
        final NoteEntity item = getItem(position);

        double positionHeight = getPositionRatio(position);
        if(item != null){
            holder.imgView.setHeightRatio(positionHeight);
            if(Tools.isEmpty(item.getImgPath())){
//                holder.imgView.setVisibility(View.GONE);
            }else{
                //TODO 加载图片
                Bitmap bitmap = ImageLoader.getInstance().getBitmapFromMemoryCache(item.getImgPath());
                if(bitmap == null){
                    bitmap = ImageLoader.decodeSampledBitmapFromResource(item.getImgPath(), 120);
                }
                if(bitmap != null){
                    holder.imgView.setVisibility(View.VISIBLE);
                    ImageLoader.getInstance().addBitmapToMemoryCache(item.getImgPath(), bitmap);
                    holder.imgView.setImageBitmap(bitmap);
                }

            }
//            Log.d(LOG_TAG, "count "+getCount());
//            Log.d(LOG_TAG, "holder "+holder);
//            Log.d(LOG_TAG, "titleView "+ holder.titleView);
            holder.titleView.setText(item.getNoteTitle());
            holder.contentView.setText(item.getNoteContent());
            holder.timeView.setText(Tools.getSuitableTimeFormat(item.getTime()));
            holder.checkBox.setChecked(item.isCheck());
            if(item.isCheck()){
                holder.checkboxGroup.setVisibility(View.VISIBLE);
            }
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    item.setCheck(!item.isCheck());
                }
            });

        }
    }

    private double getPositionRatio(final int position) {
        double ratio = sPositionHeightRatios.get(position, 0.0);
        // if not yet done generate and stash the columns height
        // in our real world scenario this will be determined by
        // some match based on the known height and width of the image
        // and maybe a helpful way to get the column height!
        if (ratio == 0) {
            ratio = getRandomHeightRatio();
            sPositionHeightRatios.append(position, ratio);
            Log.d(LOG_TAG, "getPositionRatio:" + position + " ratio:" + ratio);
        }
        return ratio;
    }

    private double getRandomHeightRatio() {
        return (mRandom.nextDouble() / 2.0) + 1.0; // height will be 1.0 - 1.5 the width
    }

    public static class ViewHolder {
        @BindView(R.id.panel_content)
        ViewGroup viewGroup;

        @BindView(R.id.img_note)
        DynamicHeightImageView imgView;

        @BindView(R.id.txt_note_title)
        DynamicHeightTextView titleView;

        @BindView(R.id.txt_note_content)
        DynamicHeightTextView contentView;

        @BindView(R.id.txt_note_time)
        TextView timeView;

        @BindView(R.id.linear_checkbox)
        ViewGroup checkboxGroup;

        @BindView(R.id.checkbox)
        CheckBox checkBox;

        public ViewHolder(View itemView) {
            KnifeKit.bind(this, itemView);
        }
    }
}
