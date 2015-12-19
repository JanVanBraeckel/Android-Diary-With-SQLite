package com.example.jan.sqlitediary;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements DiaryAddFragment.ItemSubmittedListener, DiaryListFragment.OnItemClickedListener {

    private FragmentManager mFragmentManager;
    private DiaryAddFragment mDiaryAddFragment;
    private DiaryListFragment mDiaryListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(mFragmentManager == null)
            mFragmentManager = getSupportFragmentManager();

        mDiaryAddFragment = new DiaryAddFragment();
        mDiaryListFragment = new DiaryListFragment();

        mDiaryListFragment.setOnItemClickedListener(this);
        mDiaryAddFragment.setItemSubmittedListener(this);

        mFragmentManager.beginTransaction().add(R.id.rootView, mDiaryListFragment, "list").commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(mFragmentManager == null)
            mFragmentManager = getSupportFragmentManager();
        if(id == R.id.action_addItem){
            if(mFragmentManager.findFragmentByTag("add") == null){
                if(mFragmentManager.findFragmentByTag("detail") != null){
                    mFragmentManager.popBackStack();
                }
                mFragmentManager
                        .beginTransaction()
                        .replace(R.id.rootView, mDiaryAddFragment, "add")
                        .addToBackStack(null)
                        .commit();

                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
        else if(id==android.R.id.home){
            mFragmentManager.popBackStack();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        super.onBackPressed();
    }

    @Override
    public void onSubmit() {
        if(mFragmentManager == null)
            mFragmentManager = getSupportFragmentManager();

        mFragmentManager.popBackStack();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void OnItemClicked(String... args) {
        if(mFragmentManager == null){
            mFragmentManager = getSupportFragmentManager();
        }

        Bundle bundle = new Bundle();
        bundle.putString("title", args[0]);
        bundle.putString("content", args[1]);
        bundle.putString("date", args[2]);

        DiaryDetailFragment detailFragment = new DiaryDetailFragment();
        detailFragment.setArguments(bundle);

        mFragmentManager
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.rootView, detailFragment, "detail")
                .commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
