package com.example.user.retrofit.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.user.retrofit.ApiClient;
import com.example.user.retrofit.ApiInterface;
import com.example.user.retrofit.R;
import com.example.user.retrofit.RecyclerItemClickListener;
import com.example.user.retrofit.data.Movie;
import com.example.user.retrofit.data.MoviesResponse;
import com.example.user.retrofit.detail.DetailActivity;

import java.util.List;

import info.hoang8f.android.segmented.SegmentedGroup;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public final static String API_KEY = "4b43a899845cd81dae2a06c36f8f9b9b";
    private RecyclerView recyclerView = null;
    private SegmentedGroup mSegment;
    private RadioButton mRadioPopular, mRadioTopRated;

    //"request_token":"fe9f32824c6a481be7ad6729f0db560e060e0ea4"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY from themoviedb.org first!", Toast.LENGTH_LONG).show();
            return;
        }

        recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mSegment = (SegmentedGroup) findViewById(R.id.segmentedChoose);
        mSegment.setTintColor(Color.BLACK);
        mSegment.setOnCheckedChangeListener(this);

        mRadioPopular = (RadioButton) findViewById(R.id.buttonPopular);
        mRadioTopRated = (RadioButton) findViewById(R.id.buttonTopRated);

        mRadioPopular.setChecked(true);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MoviesResponse> call;

        switch (checkedId) {
            case R.id.buttonPopular:
                mRadioPopular.setChecked(true);
                call = apiService.getPopularMovies(API_KEY);
                connectAndGetApiData(call);
                break;
            case R.id.buttonTopRated:
                mRadioTopRated.setChecked(true);
                call = apiService.getTopRatedMovies(API_KEY);
                connectAndGetApiData(call);
                break;
            default:
                break;
        }
    }

    private void connectAndGetApiData(Call<MoviesResponse> call) {

        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                int statusCode = response.code();

                final List<Movie> movies = response.body().getResults();

                recyclerView.setAdapter(new MoviesAdapterV2(movies, R.layout.list_item_movie_v2, getApplicationContext()));
                recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                        intent.putExtra(DetailActivity.EXTRA_PARAM, movies.get(position).getId());
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                    }
                }));
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }
}
