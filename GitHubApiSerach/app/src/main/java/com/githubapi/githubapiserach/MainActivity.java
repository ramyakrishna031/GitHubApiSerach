package com.githubapi.githubapiserach;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.githubapi.githubapiserach.adapters.RepositoryResultAdapter;
import com.githubapi.githubapiserach.api.RetrofitApiClient;
import com.githubapi.githubapiserach.api.RetrofitApiInterface;
import com.githubapi.githubapiserach.model.GitHubRepositoryList;
import com.githubapi.githubapiserach.model.RepositoryItem;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String SORT_BY = "starts";
    private static final String ORDER = "desc";
    private RecyclerView recyclerView;
    private RepositoryResultAdapter repositoryResultAdapter;
    private List<RepositoryItem> repositoryItemList = new ArrayList<>();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final EditText edtSearch = findViewById(R.id.editText_search);
        Button btnSearch = findViewById(R.id.button_search);
        recyclerView = findViewById(R.id.searchResultList);

        repositoryResultAdapter = new RepositoryResultAdapter(repositoryItemList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(repositoryResultAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String query = edtSearch.getText().toString().trim();
                if (!TextUtils.isEmpty(query)) {
                    progressDialog.show();
                    callGetRepositoryListApi(query);
                } else {
                    Toast.makeText(MainActivity.this, "Search text can't be empty.", Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    private void callGetRepositoryListApi(String query) {

        RetrofitApiInterface retrofitApiInterface = RetrofitApiClient.getRetrofitClient().create(RetrofitApiInterface.class);
        repositoryItemList.clear();
        Call<GitHubRepositoryList> repositoryListCall = retrofitApiInterface.getRepositoryList(query, SORT_BY, ORDER);
        repositoryListCall.enqueue(new Callback<GitHubRepositoryList>() {
            @Override
            public void onResponse(Call<GitHubRepositoryList> call, Response<GitHubRepositoryList> response) {

                if (response.code() == 200) {
                    if (response.body().getRepositoryItemList() != null && response.body().getRepositoryItemList().size() > 0) {
                        repositoryItemList.addAll(response.body().getRepositoryItemList());
                        repositoryResultAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MainActivity.this, "No result found.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "No result found.", Toast.LENGTH_LONG).show();
                }

                progressDialog.dismiss();

            }

            @Override
            public void onFailure(Call<GitHubRepositoryList> call, Throwable t) {
                Log.d("MainActivity ", "onFailure");
                Toast.makeText(MainActivity.this, "Something went wrong.", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });


    }
}
