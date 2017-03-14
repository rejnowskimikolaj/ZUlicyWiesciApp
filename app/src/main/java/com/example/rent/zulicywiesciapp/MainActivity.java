package com.example.rent.zulicywiesciapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.rent.zulicywiesciapp.model.Categories;
import com.example.rent.zulicywiesciapp.model.NewsItemList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        ViewPager viewPager = (ViewPager) findViewById(R.id.activity_main_viewPager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.activity_main_tabLayout);
        tabLayout.setupWithViewPager(viewPager);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void setupViewPager(ViewPager viewPager) {

        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MainNewsListFragment(), "Online");
        adapter.addFragment(new MainNewsListFragment(), "Offline");
        adapter.addFragment(new MainNewsListFragment(), "Empty");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.test) {
            startActivity(new Intent(this, TestRetrofitActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemIdid = item.getItemId();
        drawerLayout.closeDrawer(GravityCompat.START);
        if(itemIdid==R.id.nav_home){
            startActivity(new Intent(this,MainActivity.class));
            return true;
        }

        Intent categoryActivity = new Intent(this,CategoryNewsListActivity.class);
        int categoryId=-1;


        switch (itemIdid) {
            case R.id.nav_art:
                categoryId= Categories.ART.getId();
                break;
            case R.id.nav_money:
                categoryId= Categories.MONEY.getId();
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

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}