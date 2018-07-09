package com.daimao.bluebubble.fragment;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.daimao.bluebubble.BaseApplication;
import com.daimao.bluebubble.R;
import com.daimao.bluebubble.adapter.NoteAdapter;
import com.daimao.bluebubble.data.model.NoteEntity;
import com.daimao.bluebubble.ui.EditNoteActivity;
import com.etsy.android.grid.StaggeredGridView;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import java.util.ArrayList;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XFragment;
import cn.droidlover.xdroidmvp.router.Router;

/**
 * Created by baige on 2018/6/18.
 */

public class NotebookFragment extends XFragment {

    @BindView(R.id.search_box)
    SearchBox mSearchBox;

    @BindView(R.id.grid_view)
    StaggeredGridView mGridView;

    @BindView(R.id.fab_add_note)
    FloatingActionButton fabAddNote;

    @BindView(R.id.layout_null)
    ViewGroup mNothingView;

    private NoteAdapter mAdapter;

    private ArrayList<NoteEntity> mNoteList = new ArrayList<>();

    @Override
    public void initData(Bundle savedInstanceState) {
        initSearchBox();
        initNotebook();
        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Router.newIntent(getActivity()).to(EditNoteActivity.class).launch();
//                Intent intent = new Intent(getContext(), EditNoteActivity.class);
//                startActivity(intent);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_notebook;
    }

    @Override
    public Object newP() {
        return null;
    }

    public static NotebookFragment newInstance() {
        return new NotebookFragment();
    }

    private void initSearchBox(){
        mSearchBox.enableVoiceRecognition(this);
        for(int x = 0; x < 10; x++){
            SearchResult option = new SearchResult("Result " + Integer.toString(x), getResources().getDrawable(R.drawable.ic_history));
            mSearchBox.addSearchable(option);
        }
        mSearchBox.setMenuListener(new SearchBox.MenuListener() {
            @Override
            public void onMenuClick() {
                BaseApplication.getInstance().showTip("菜单");
            }
        });
        mSearchBox.setSearchListener(new SearchBox.SearchListener(){

            @Override
            public void onSearchOpened() {

            }

            @Override
            public void onSearchCleared() {

            }

            @Override
            public void onSearchClosed() {

            }

            @Override
            public void onSearchTermChanged(String s) {

            }

            @Override
            public void onSearch(String s) {

            }

            @Override
            public void onResultClick(SearchResult searchResult) {

            }
        });

        mSearchBox.setOverflowMenu(R.menu.overflow_menu);
        mSearchBox.setOverflowMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.test_menu_item:
                        BaseApplication.getInstance().showTip("onClick");
                        return true;
                }
                return false;
            }
        });
    }

    private void initNotebook(){
        mAdapter = new NoteAdapter(getContext(), R.layout.item_note);
        mAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                BaseApplication.getInstance().showTip("change" + mAdapter.getCount());
                if(mAdapter.isEmpty()){
                    mNothingView.setVisibility(View.VISIBLE);
                }else{
                    mNothingView.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onInvalidated() {
                super.onInvalidated();
            }
        });

        for (int i = 0; i < 10; i++) {
            NoteEntity note = new NoteEntity();
            note.setNoteTitle("标题"+i);
            note.setNoteContent("内容"+i);
            mNoteList.add(note);
        }
        mAdapter.addAll(mNoteList);
        mGridView.setAdapter(mAdapter);

    }
}
