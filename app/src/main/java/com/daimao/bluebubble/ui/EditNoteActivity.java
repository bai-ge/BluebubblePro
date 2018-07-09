package com.daimao.bluebubble.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.daimao.bluebubble.R;

import java.util.List;

import butterknife.BindView;
import cn.droidlover.xdroidmvp.mvp.XActivity;
import jp.wasabeef.richeditor.RichEditor;

import static com.daimao.bluebubble.AppConfigure.LOG_TAG;

public class EditNoteActivity extends XActivity {

    @BindView(R.id.btn_back)
    ImageButton mBtnBack;

    @BindView(R.id.txt_edit_time)
    TextView mTxtEditTime;

    @BindView(R.id.btn_finish)
    Button mBtnFinish;

    @BindView(R.id.editor)
    RichEditor mRichEditor;




    @Override
    public void initData(Bundle savedInstanceState) {
        mRichEditor.setOnTextChangeListener(new RichEditor.OnTextChangeListener() {
            @Override
            public void onTextChange(String text) {
                Log.d(LOG_TAG, "onChange() ="+text);
            }

        });
        mRichEditor.setOnDecorationChangeListener(new RichEditor.OnDecorationStateListener() {
            @Override
            public void onStateChangeListener(String text, List<RichEditor.Type> types) {
                Log.d(LOG_TAG, "OnDecoration() ="+text + ", Types = "+types);
            }
        });
//        mRichEditor.setOnInitialLoadListener(new RichEditor.AfterInitialLoadListener() {
//            @Override
//            public void onAfterInitialLoad(boolean isReady) {
//
//            }
//        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.act_editnote;
    }

    @Override
    public Object newP() {
        return null;
    }

    /*动作*/

    public void onBack(View view){
        onBackPressed();
    }

    public void onFinish(View view){
        Log.d(LOG_TAG, mRichEditor.getHtml());

    }
    public void undo(View view){
        mRichEditor.undo();
    }

    public void redo(View view){
        mRichEditor.redo();
    }

    public void bold(View view){
        mRichEditor.focusEditor();
        mRichEditor.setBold();
    }

    public void italic(View view){
        mRichEditor.focusEditor();
        mRichEditor.setItalic();
    }


    public void underline(View view) {
        mRichEditor.focusEditor();
        mRichEditor.setUnderline();

    }


    public void heading1(View view) {
        mRichEditor.setHeading(1);
    }

    public void heading2(View view) {
        mRichEditor.setHeading(2);
    }

    public void heading3(View view) {
        mRichEditor.setHeading(3);
    }

    public void heading4(View view) {
        mRichEditor.setHeading(4);
    }

    public void heading5(View view) {
        mRichEditor.setHeading(5);
    }

    public void heading6(View view) {
        mRichEditor.setHeading(6);
    }

    public void txtColor(View view) {
        new MaterialDialog.Builder(EditNoteActivity.this)
                .title("选择颜色")
                .items(R.array.colors)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        dialog.dismiss();
                        switch (which){
                            case 0://红
                                mRichEditor.setTextColor(Color.RED);
                                break;
                            case 1://黄
                                mRichEditor.setTextColor(Color.YELLOW);
                                break;
                            case 2://蓝
                                mRichEditor.setTextColor(Color.GREEN);
                                break;
                            case 3://绿
                                mRichEditor.setTextColor(Color.BLUE);
                                break;
                            case 4://黑
                                mRichEditor.setTextColor(Color.BLACK);
                                break;
                        }
                        return false;
                    }
                }).show();
    }

    public void bgColor(View view) {
    }

    /**
     * 无序排列
     */
    public void insertBullets(View view) {
        mRichEditor.setBullets();
    }

    /**
     * 有序排列
     */
    public void insertNumbers(View view) {
        mRichEditor.setNumbers();
    }

    public void insertImage(View view) {
        mRichEditor.focusEditor();
        //TODO
    }


    public void insertLink(View view) {
        //TODO

    }

    public void insertCheckbox(View view) {
        mRichEditor.focusEditor();
        mRichEditor.insertTodo();
    }
}
