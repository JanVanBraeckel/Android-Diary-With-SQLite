package com.example.jan.sqlitediary;


import android.content.ClipData;
import android.content.ContentValues;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.jan.sqlitediary.database.Constants;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DiaryAddFragment extends Fragment {

    @Bind(R.id.diaryAddContent)
    EditText diaryAddContent;

    @Bind(R.id.diaryAddTitle)
    EditText diaryAddTitle;

    private ItemSubmittedListener mListener;

    public void setItemSubmittedListener(ItemSubmittedListener listener){
        mListener = listener;
    }

    public interface ItemSubmittedListener{
        void onSubmit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_diary_add, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @OnClick(R.id.diaryAddSubmit)
    public void saveToDB(View v){
        String diaryTitle = diaryAddTitle.getText().toString();
        String diaryContent= diaryAddContent.getText().toString();

        if(TextUtils.isEmpty(diaryTitle)){
            diaryAddTitle.setError(getString(R.string.empty));
        }
        if(TextUtils.isEmpty(diaryContent)){
            diaryAddContent.setError(getString(R.string.empty));
        }

        if(!TextUtils.isEmpty(diaryTitle) && !TextUtils.isEmpty(diaryContent)){
            ContentValues values = new ContentValues();
            values.put(Constants.DiaryTable.COLUMN_NAME_DIARY_TITLE, diaryTitle);
            values.put(Constants.DiaryTable.COLUMN_NAME_DIARY_CONTENT, diaryContent);
            getActivity().getContentResolver().insert(Constants.DiaryTable.CONTENT_URI, values);

            diaryAddContent.setText("");
            diaryAddTitle.setText("");
            mListener.onSubmit();
        }
    }
}
