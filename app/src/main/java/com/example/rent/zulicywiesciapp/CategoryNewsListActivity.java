package com.example.rent.zulicywiesciapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.rent.zulicywiesciapp.adapters.NewsAdapter;
import com.example.rent.zulicywiesciapp.adapters.NothingToLoadAdapter;
import com.example.rent.zulicywiesciapp.model.Categories;
import com.example.rent.zulicywiesciapp.model.Category;
import com.example.rent.zulicywiesciapp.model.NewsItem;
import com.example.rent.zulicywiesciapp.exceptions.ApiConnectException;
import com.example.rent.zulicywiesciapp.retrofit.ApiManager;
import com.example.rent.zulicywiesciapp.utils.CategoryUtil;
import com.example.rent.zulicywiesciapp.utils.NothingToDisplayMessage;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.rent.zulicywiesciapp.MainNewsListFragment.NEWS_ID;

public class CategoryNewsListActivity extends AppCompatActivity implements ApiManager.OnCategoryFetchedListener,NewsAdapter.OnNewsListItemClickListener
                                                                            ,NavigationView.OnNavigationItemSelectedListener{

    public static final String CATEGORY_TO_LIST = "categoryToList";

    @BindView(R.id.activity_category_news_list_recyclerView)
    RecyclerView recyclerView;

    private NewsAdapter adapter;

    @BindView(R.id.activity_category_news_list_drawer_layout)
    DrawerLayout drawerLayout;

    @BindView((R.id.toolbar))
    Toolbar toolbar;

    @BindView((R.id.app_bar_layout))
    AppBarLayout appBarLayout;

    @BindView((R.id.toolbar_title))
    AppCompatTextView toolbarTitle;


//
    private int categoryId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_news_list);

        categoryId = getIntent().getIntExtra(CATEGORY_TO_LIST,-1);
        setViews();
        
        if(categoryId !=-1){
            try {
                ApiManager.getCategory(categoryId,this);
            } catch (ApiConnectException e) {
                Toast.makeText(this,"Couldn't get news from this category.",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void setViews() {

        ButterKnife.bind(this);
        setRecyclerView();
        setToolbar();
        setTabsAndNavigationView();
    }

    private void setTabsAndNavigationView(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        setDrawerLayout(navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
    }

    private void setDrawerLayout(NavigationView nv){
        nv.inflateMenu(R.menu.activity_main_drawer);

    }

    private void setRecyclerView(){
        recyclerView.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new NewsAdapter(this,this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void setToolbar(){
        setSupportActionBar(toolbar);
        appBarLayout.setBackgroundColor(CategoryUtil.getIdOfColorFromCategoryId(categoryId,this));
        toolbarTitle.setText(CategoryUtil.getCategoryNameFromId(categoryId));
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_activity_toolbar_menu, menu);
//        return true;
//    }

    @Override
    public void onCategoryFetched(Category category) {
        if(category==null||category.getNews()==null || category.getNews().isEmpty()){
            recyclerView.setAdapter(new NothingToLoadAdapter(NothingToDisplayMessage.NO_NEWS,this));
        }
        else{
            adapter.setNewsList(category.getNews());

        }

    }

    @Override
    public void OnNewsListItemClicked(NewsItem newsItem) {

        Intent newsItemActivity = new Intent(this,NewsItemActivity.class);
        newsItemActivity.putExtra(NEWS_ID,newsItem.getId());
        newsItemActivity.putExtra(NewsItemActivity.SOURCE,NewsItemActivity.NEWS_FROM_FEED);
        startActivity(newsItemActivity);
    }

    @Override
    public void onNewsListItemLongClick(NewsItem newsItem) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemIdid = item.getItemId();
        drawerLayout.closeDrawer(GravityCompat.START);
        if(itemIdid==R.id.nav_home){
            startActivity(new Intent(this,MainActivity.class));
            return true;
        }
        else if(itemIdid==R.id.nav_capsule){
            startActivity(new Intent(this,CapsuleActivity.class));
            return true;
        }

        Intent categoryActivity = new Intent(this,CategoryNewsListActivity.class);
        int categoryId=-1;


        switch (itemIdid) {
            case R.id.nav_art:
                categoryId= Categories.ART.getId();
                break;
            case R.id.nav_economics:
                categoryId= Categories.ECONOMICS.getId();
                break;
            case R.id.nav_politics:
                categoryId= Categories.POLITICS.getId();
                break;
            case R.id.nav_technology:
                categoryId= Categories.TECHNOLOGY.getId();
                break;
            case R.id.nav_sport:
                categoryId= Categories.SPORT.getId();
        }

        categoryActivity.putExtra(CategoryNewsListActivity.CATEGORY_TO_LIST,categoryId);
        startActivity(categoryActivity);
        return true;
    }
}
