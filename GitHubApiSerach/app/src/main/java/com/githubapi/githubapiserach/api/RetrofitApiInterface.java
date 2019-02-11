package com.githubapi.githubapiserach.api;

import com.githubapi.githubapiserach.model.GitHubRepositoryList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitApiInterface {

    @GET("repositories?")
    Call<GitHubRepositoryList> getRepositoryList(@Query("q") String queryText, @Query("sort") String sortBy, @Query("order") String order);
}
