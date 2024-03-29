package com.source.ui.materialDesign;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.source.ui.R;
import com.source.ui.materialDesign.adapter.EndlessRecyclerOnScrollListener;
import com.source.ui.materialDesign.adapter.LoadMoreAdapter;
import com.source.ui.materialDesign.bean.Movie;
import com.source.ui.materialDesign.net.ApiService;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

import java.util.ArrayList;

/**
 * 带有ToolbarActivity的使用
 */
public class ToolbarActivity extends AppCompatActivity {

    private DrawerLayout dlAtDrawLayout;
    private ArrayList<String> mData = new ArrayList<>();
    private SwipeRefreshLayout srlRefresh;
    private RecyclerView mRvList;
    private Toolbar tbAtToolbar;
    private LoadMoreAdapter loadMoreAdapter;
    private Intent intent;

    private int start = 0;
    private int end = 20;

    private ArrayList<Movie.SubjectsBean> mMovieList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toolbar);

        //如果不设置，则不会出现标题
        Toolbar tbAtToolbar = findViewById(R.id.tb_at_toolbar);
        //不设置会显示label的属性,也可以在清单文件中进行配置
//      tbAtToolbar.setTitle(" I am toolbar ");
        setSupportActionBar(tbAtToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //设置Toolbar home键可点击
            actionBar.setDisplayHomeAsUpEnabled(true);
            //设置Toolbar home键图标
            actionBar.setHomeAsUpIndicator(R.drawable.ic_drawer_am);
        }

        //Toolbar关联侧滑菜单
        dlAtDrawLayout = findViewById(R.id.dl_at_draw_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, dlAtDrawLayout, tbAtToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dlAtDrawLayout.addDrawerListener(toggle);
        toggle.syncState();

        //浮动按钮
        FloatingActionButton fabButton = findViewById(R.id.fab_at_action);
        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出提示
                Snackbar.make(v, "snack action ", 1000)
                        //Snackbar点击响应
                        .setAction("Toast", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(ToolbarActivity.this, " to do ", Toast.LENGTH_SHORT).show();
                            }
                        }).show();
            }
        });

        mRvList = findViewById(R.id.rv_at_list);
        srlRefresh = findViewById(R.id.srl_refresh);
        srlRefresh.setRefreshing(true);
        mRvList.setLayoutManager(new LinearLayoutManager(this));

        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mMovieList.clear();
                end = 20;
                getNetData(start, end, true);
            }
        });

        getNetData(start, end, false);

        mRvList.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMoreData() {
                loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mMovieList.size() < 250) {
                            getNetData(end, 20, false);
                            end += 20;
                        } else {
                            loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_END);
                        }
                    }
                }, 2000);
            }
        });
    }

    private void getNetData(int start, int end, final boolean isRefresh) {
//        mMovieList.add(new Movie.SubjectsBean());
//        mMovieList.add(new Movie.SubjectsBean());
//        mMovieList.add(new Movie.SubjectsBean());
//        mMovieList.add(new Movie.SubjectsBean());
//        mMovieList.add(new Movie.SubjectsBean());
//        mMovieList.add(new Movie.SubjectsBean());
//        mMovieList.add(new Movie.SubjectsBean());
//        mMovieList.add(new Movie.SubjectsBean());
//        mMovieList.add(new Movie.SubjectsBean());
//        mMovieList.add(new Movie.SubjectsBean());

//        if (loadMoreAdapter == null) {
//            loadMoreAdapter = new LoadMoreAdapter(mMovieList, ToolbarActivity.this);
//            mRvList.setAdapter(loadMoreAdapter);
//        } else {
//            loadMoreAdapter.notifyDataSetChanged();
//        }

        srlRefresh.setRefreshing(false);
//        initItemListener(mMovieList);

//        loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_COMPLETE);

        new ApiService().getTopMovie(new Observer<Movie>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(Movie movie) {
                if (loadMoreAdapter == null) {
                    mMovieList.addAll(movie.getSubjects());
                    loadMoreAdapter = new LoadMoreAdapter((ArrayList<Movie.SubjectsBean>) movie.getSubjects(), ToolbarActivity.this);
                    mRvList.setAdapter(loadMoreAdapter);
                } else {
                    loadMoreAdapter.notifyDataSetChanged();
                }

                initItemListener((ArrayList<Movie.SubjectsBean>) movie.getSubjects());
            }

            @Override
            public void onError(Throwable e) {
                srlRefresh.setRefreshing(false);
            }

            @Override
            public void onComplete() {
                srlRefresh.setRefreshing(false);
                loadMoreAdapter.setLoadState(loadMoreAdapter.LOADING_COMPLETE);
            }
        }, start, end);

    }

    private void initItemListener(final ArrayList<Movie.SubjectsBean> movie) {
        loadMoreAdapter.setOnItemClickListener(new LoadMoreAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Activity共享元素转场动画
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        ToolbarActivity.this,
                        view.findViewById(R.id.iv_icon),
                        "basic"
                );

                intent = new Intent(ToolbarActivity.this, MovieDetailActivity.class);
                intent.putExtra("URL", movie.get(position).getImages().getMedium());
                intent.putExtra("NAME", movie.get(position).getTitle());
                intent.putExtra("ID", movie.get(position).getId());
                startActivity(intent, optionsCompat.toBundle());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toobalr, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            dlAtDrawLayout.openDrawer(Gravity.START);
        } else if (item.getItemId() == R.id.add) {
            Toast.makeText(this, "add", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.delete) {
            Toast.makeText(this, "delete", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.tb_setting) {
            Toast.makeText(this, "setting", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

}
