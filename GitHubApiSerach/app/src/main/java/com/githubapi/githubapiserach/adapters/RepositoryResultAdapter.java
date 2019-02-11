package com.githubapi.githubapiserach.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.githubapi.githubapiserach.R;
import com.githubapi.githubapiserach.model.RepositoryItem;

import java.util.List;

public class RepositoryResultAdapter extends RecyclerView.Adapter<RepositoryResultAdapter.MyViewHolder> {

    private List<RepositoryItem> gitHubRepositoryLists;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, description, stars;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.textView_name);
            description = view.findViewById(R.id.textView_desc);
            stars = view.findViewById(R.id.textView_stars);
            image = view.findViewById(R.id.imageView_avatar);
        }
    }


    public RepositoryResultAdapter(List<RepositoryItem> moviesList, Context context) {
        this.gitHubRepositoryLists = moviesList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        RepositoryItem repositoryItem = gitHubRepositoryLists.get(position);
        holder.name.setText(repositoryItem.getName());
        holder.description.setText(repositoryItem.getDescription());
        holder.stars.setText(repositoryItem.getStargazers_count() + " ");

        if (repositoryItem.getOwner().getAvatar_url() != null) {
            Glide.with(context.getApplicationContext())
                    .load(repositoryItem.getOwner().getAvatar_url())
                    .apply(new RequestOptions().placeholder(R.mipmap.ic_launcher)
                            .centerCrop()
                            .dontAnimate()
                            .dontTransform())
                    .into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        return gitHubRepositoryLists.size();
    }
}