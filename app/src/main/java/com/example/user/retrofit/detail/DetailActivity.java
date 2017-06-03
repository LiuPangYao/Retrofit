package com.example.user.retrofit.detail;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.user.retrofit.ApiClient;
import com.example.user.retrofit.ApiInterface;
import com.example.user.retrofit.R;
import com.example.user.retrofit.data.Movie;
import com.example.user.retrofit.main.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 細節
 */
public class DetailActivity extends AppCompatActivity {

    public final static String EXTRA_PARAM = "電影ID";
    private static final String TAG = DetailActivity.class.getSimpleName();
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = this.getIntent();
        int param = intent.getIntExtra(EXTRA_PARAM, 0);
        Log.d(TAG, "onCreate: " + param);

        text = (TextView) findViewById(R.id.textView);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<Movie> call = apiService.getMovieDetails(param, MainActivity.API_KEY);

        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                int statusCode = response.code();

                final Movie movies = response.body();
                //text.setText(movies.toString());
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }
}
