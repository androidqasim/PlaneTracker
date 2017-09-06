package ve.com.abicelis.planetracker.ui.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import ve.com.abicelis.planetracker.R;
import ve.com.abicelis.planetracker.application.Message;
import ve.com.abicelis.planetracker.application.PlaneTrackerApplication;
import ve.com.abicelis.planetracker.data.model.TripViewModel;
import ve.com.abicelis.planetracker.ui.base.BaseActivity;
import ve.com.abicelis.planetracker.util.SnackbarUtil;

/**
 * Created by abicelis on 4/9/2017.
 */

public class HomeActivity extends BaseActivity implements HomeMvpView {

    @Inject
    HomePresenter mHomePresenter;

    @BindView(R.id.activity_home_container)
    CoordinatorLayout mContainer;

    @BindView(R.id.activity_home_toolbar)
    Toolbar mToolbar;

    @BindView(R.id.activity_home_search_view)
    MaterialSearchView mSearchView;

    @BindView(R.id.activity_home_recyclerview)
    RecyclerView mRecycler;
    private LinearLayoutManager mLayoutManager;
    private TripAdapter mTripAdapter;

    @BindView(R.id.activity_home_fab_add_trip)
    FloatingActionButton mAddTrip;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        getPresenterComponent().inject(this);
        mHomePresenter.attachView(this);

        setUpToolbar();
        setUpRecyclerView();
        mHomePresenter.refreshTripList();

        mAddTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "Inserting fake trip", Toast.LENGTH_SHORT).show();
                mHomePresenter.insertFakeTrip();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        mHomePresenter.refreshTripList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_home_search:
                mHomePresenter.insertFakeTrip();
                Toast.makeText(this, "Inserting fake trips", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_home_settings:
                mHomePresenter.refreshTripList();
                Toast.makeText(this, "Refreshing", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_home_about:
                Toast.makeText(this, "TODO: About", Toast.LENGTH_SHORT).show();

                break;
        }


        return super.onOptionsItemSelected(item);
    }

    private void setUpToolbar() {
        //Setup toolbar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setLogo(R.drawable.ic_plane);
    }

    private void setUpRecyclerView() {

        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mTripAdapter = new TripAdapter(this);

        mRecycler.setLayoutManager(mLayoutManager);
        mRecycler.setAdapter(mTripAdapter);
    }


    /* HomeMvpView implementation */

    @Override
    public void showErrorMessage(Message message) {
        SnackbarUtil.showSnackbar(mContainer, SnackbarUtil.SnackbarType.ERROR, message.getFriendlyNameRes(), SnackbarUtil.SnackbarDuration.SHORT, null);
    }

    @Override
    public void showTrips(List<TripViewModel> trips) {
        mTripAdapter.getItems().clear();
        mTripAdapter.getItems().addAll(trips);
        mTripAdapter.notifyDataSetChanged();
    }
}