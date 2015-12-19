package com.example.jan.sqlitediary;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.sql.Date;
import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiaryDetailFragment extends Fragment {

    @Bind(R.id.diaryDetailTitle)
    TextView diaryDetailTitle;

    @Bind(R.id.diaryDetailDate)
    TextView diaryDetailDate;

    @Bind(R.id.diaryDetailContent)
    TextView diaryDetailContent;

    private String mTitle,  mContent;
    private long mDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_diary_detail, container, false);

        ButterKnife.bind(this, v);

        Bundle args = getArguments();
        if(args != null && args.containsKey("title") && args.containsKey("content") && args.containsKey("date")){
            mTitle = args.getString("title");
            mContent = args.getString("content");
            mDate = Long.valueOf(args.getString("date"));
        }

        diaryDetailTitle.setText(mTitle);
        diaryDetailContent.setText(mContent);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
        StringBuilder dateBuilder = new StringBuilder();
        Date d = new Date(mDate);
        dateBuilder.append(getContext().getString(R.string.madeOn))
                .append(dateFormat.format(d))
                .append(getContext().getString(R.string.at))
                .append(hourFormat.format(d));

        diaryDetailDate.setText(dateBuilder.toString());

        return v;
    }
}
