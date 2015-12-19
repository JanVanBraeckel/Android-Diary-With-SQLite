package com.example.jan.sqlitediary;


import android.database.ContentObserver;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jan.sqlitediary.database.Constants;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiaryListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter mAdapter;
    private OnItemClickedListener mListener;

    public interface OnItemClickedListener {
        void OnItemClicked(String... args);
    }

    public void setOnItemClickedListener(OnItemClickedListener listener) {
        mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Uri uri = Constants.DiaryTable.CONTENT_URI;
        Cursor c = getActivity().getContentResolver().query(uri, null, null, null, null);

        mAdapter = new SimpleCursorAdapter(getContext(),
                R.layout.diary_entry,
                c,
                new String[]{Constants.DiaryTable.COLUMN_NAME_DIARY_TITLE, Constants.DiaryTable.COLUMN_NAME_DIARY_DATE},
                new int[]{R.id.entryName, R.id.entryDate},
                0);
        mAdapter.setViewBinder((view, cursor, columnIndex) -> {
            if (columnIndex == 1) {
                long date = cursor.getLong(columnIndex);
                TextView textView = (TextView) view;
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
                StringBuilder dateBuilder = new StringBuilder();
                Date d = new Date(date);
                dateBuilder.append(getContext().getString(R.string.madeOn))
                        .append(dateFormat.format(d))
                        .append(getContext().getString(R.string.at))
                        .append(hourFormat.format(d));
                textView.setText(dateBuilder.toString());
                return true;
            }
            return false;


        });
        setListAdapter(mAdapter);

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        CursorWrapper wrapper = (CursorWrapper) l.getItemAtPosition(position);
        String title = wrapper.getString(wrapper.getColumnIndex(Constants.DiaryTable.COLUMN_NAME_DIARY_TITLE));
        String content = wrapper.getString(wrapper.getColumnIndex(Constants.DiaryTable.COLUMN_NAME_DIARY_CONTENT));
        String date = wrapper.getString(wrapper.getColumnIndex(Constants.DiaryTable.COLUMN_NAME_DIARY_DATE));

        if (title != null && content != null && date != null && mListener != null)
            mListener.OnItemClicked(title, content, date);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Constants.DiaryTable.CONTENT_URI;

        return new CursorLoader(getActivity(), baseUri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
