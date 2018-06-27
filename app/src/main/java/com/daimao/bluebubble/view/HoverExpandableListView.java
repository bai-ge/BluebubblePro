package com.daimao.bluebubble.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

import com.daimao.bluebubble.adapter.AbstractHoverExpandableListAdapter;

/**
 * Created by baige on 2018/6/23.
 */

public class HoverExpandableListView extends ExpandableListView implements AbsListView.OnScrollListener,ExpandableListView.OnGroupClickListener {


    public static final int PINNED_HEADER_GONE = 0;
    public static final int PINNED_HEADER_VISIBLE = 1;
    public static final int PINNED_HEADER_PUSHED_UP = 2;

    private AbstractHoverExpandableListAdapter mAdapter;

    // header
    private SparseIntArray groupStatusMap = new SparseIntArray();
    private View mHeaderView;
    private boolean mHeaderViewVisible;
    private int mHeaderViewWidth;
    private int mHeaderViewHeight;

    private float mDownX;
    private float mDownY;

    public HoverExpandableListView(Context context) {
        super(context);
        init();
    }

    public HoverExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HoverExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setOnScrollListener(this);
        setOnGroupClickListener(this);
    }

//    @Override
//    public void setAdapter(ExpandableListAdapter adapter) {
//        super.setAdapter(adapter);
//        mAdapter = (HoverExpandaleListAdapter) adapter;
//    }

    public void setAdapter(AbstractHoverExpandableListAdapter adapter){
        super.setAdapter(adapter);
        this.mAdapter = adapter;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        //headerview的点击事件
        if (mHeaderViewVisible) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDownX = ev.getX();
                    mDownY = ev.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    float x = ev.getX();
                    float y = ev.getY();
                    float offsetX = Math.abs(x - mDownX);
                    float offsetY = Math.abs(y - mDownY);
                    // 如果 HeaderView 是可见的 , 点击在 HeaderView 内 , 那么触发 headerClick()
                    if (x <= mHeaderViewWidth && y <= mHeaderViewHeight
                            && offsetX <= mHeaderViewWidth && offsetY <= mHeaderViewHeight) {
                        if (mHeaderView != null) {
                            headerViewClick();
                        }
                        return true;
                    }
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }

    /**
     * 点击 HeaderView 触发的事件
     */
    private void headerViewClick() {
        long packedPosition = getExpandableListPosition(this.getFirstVisiblePosition());

        int groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);

        if (getGroupClickStatus(groupPosition) == 1) {
            this.collapseGroup(groupPosition);
            setGroupClickStatus(groupPosition, 0);
        }
        else{
            this.expandGroup(groupPosition);
            setGroupClickStatus(groupPosition, 1);
        }

        this.setSelectedGroup(groupPosition);
    }

    /**
     *
     * 点击了 Group 触发的事件 , 要根据根据当前点击 Group 的状态来
     */
    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//		if(groupPosition==0)
//			return true;
        if (getGroupClickStatus(groupPosition) == 0) {
            setGroupClickStatus(groupPosition, 1);
            parent.expandGroup(groupPosition);
            //Header自动置顶
            //parent.setSelectedGroup(groupPosition);

        } else if (getGroupClickStatus(groupPosition) == 1) {
            setGroupClickStatus(groupPosition, 0);
            parent.collapseGroup(groupPosition);
        }

        // 返回 true 才可以弹回第一行 , 不知道为什么
        return true;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView != null) {
            Log.v("my", "1width:"+mHeaderViewWidth);
            measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
            mHeaderViewWidth = mHeaderView.getMeasuredWidth();
            mHeaderViewHeight = mHeaderView.getMeasuredHeight();
            Log.v("my", "width:"+mHeaderViewWidth);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        final long flatPostion = getExpandableListPosition(getFirstVisiblePosition());
        Log.v("my", "first:"+flatPostion);
        final int groupPos = ExpandableListView.getPackedPositionGroup(flatPostion);
        final int childPos = ExpandableListView.getPackedPositionChild(flatPostion);
        Log.v("my", "position:"+groupPos+","+childPos);
        if(mAdapter != null){
            mHeaderView = mAdapter.getHeaderView(groupPos, getGroupClickStatus(groupPos) == 1, mHeaderView);
            if(mHeaderView != null){
                mHeaderView.setLayoutParams(new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }
        if (mHeaderView != null && mAdapter != null ) {
            mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
        }
        configureHeaderView(groupPos, childPos);
    }

    public void configureHeaderView(int groupPosition, int childPosition) {
        if (mHeaderView == null || mAdapter == null
                ||  mAdapter.getGroupCount() == 0) {
            return;
        }

        int state = getHeaderState(groupPosition, childPosition);
//        setHeaderData(groupPosition);
        if(mAdapter != null){
            mHeaderView = mAdapter.getHeaderView(groupPosition, getGroupClickStatus(groupPosition) == 1, mHeaderView);
            if(mHeaderView != null){
                mHeaderView.setLayoutParams(new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }
        switch (state) {
            case PINNED_HEADER_GONE: {
                mHeaderViewVisible = false;
                break;
            }

            case PINNED_HEADER_VISIBLE: {

                if (mHeaderView.getTop() != 0){
                    mHeaderView.layout(0, 0, mHeaderViewWidth, mHeaderViewHeight);
                }
                mHeaderViewVisible = true;
                break;
            }

            case PINNED_HEADER_PUSHED_UP: {
                View firstView = getChildAt(0);
                int bottom = firstView.getBottom();
                int headerHeight = mHeaderView.getHeight();

                int y;

                if (bottom < headerHeight) {
                    y = (bottom - headerHeight);

                } else {
                    y = 0;
                }
                if (mHeaderView.getTop() != y) {
                    mHeaderView.layout(0, y, mHeaderViewWidth, mHeaderViewHeight + y);
                }
                mHeaderViewVisible = true;
                break;
            }
        }
    }


    /**
     * 列表界面更新时调用该方法(如滚动时)
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (mHeaderViewVisible) {
            //分组栏是直接绘制到界面中，而不是加入到ViewGroup中
            drawChild(canvas, mHeaderView, getDrawingTime());
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        final long flatPos = getExpandableListPosition(firstVisibleItem);
        int groupPosition = ExpandableListView.getPackedPositionGroup(flatPos);
        int childPosition = ExpandableListView.getPackedPositionChild(flatPos);

        configureHeaderView(groupPosition, childPosition);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    public int getHeaderState(int groupPosition, int childPosition) {
        final int childCount = mAdapter.getChildrenCount(groupPosition);

        if (childPosition == childCount - 1) {
            Log.v("my", "up");
            return PINNED_HEADER_PUSHED_UP;
        } else if (childPosition == -1
                && !isGroupExpanded(groupPosition)) {
            Log.v("my", "gone");
            return PINNED_HEADER_GONE;
        } else {
            Log.v("my", "visible");
            return PINNED_HEADER_VISIBLE;
        }
    }

    public void setGroupClickStatus(int groupPosition, int status) {
        groupStatusMap.put(groupPosition, status);
    }

    /**
     * @param groupPosition
     * @return 0 表示收起来，1表示展开
     */
    public int getGroupClickStatus(int groupPosition) {

        if (groupPosition >=0 && groupStatusMap.keyAt(groupPosition) >= 0) {
            return groupStatusMap.get(groupPosition);
        } else {
            return 0;
        }
    }

}
