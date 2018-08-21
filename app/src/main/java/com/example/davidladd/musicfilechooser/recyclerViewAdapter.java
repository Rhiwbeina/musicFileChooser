package com.example.davidladd.musicfilechooser;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class recyclerViewAdapter extends RecyclerView.Adapter<recyclerViewAdapter.ViewHolder> {
    private static final String TAG = "recyclerViewAdapter";

    private ArrayList<String> mitem_titles = new ArrayList<>();
    private ArrayList<Integer> mitem_icons = new ArrayList<>();
    private Context mContext;

    public recyclerViewAdapter(ArrayList<String> mitem_titles, ArrayList<Integer> mitem_icons, Context mContext) {
        this.mitem_titles = mitem_titles;
        this.mitem_icons = mitem_icons;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called");
        holder.item_title.setText(mitem_titles.get(position));

        holder.item_icon.setImageResource(mitem_icons.get(position));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + mitem_titles.get(position));
                //Toast.makeText(mContext, mitem_titles.get(position), Toast.LENGTH_SHORT).show();
                dirMTbrowse.getInstance().tryAddToPath(mitem_titles.get(position));
               //progressBarLoading.setVisibility(View.VISIBLE);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mitem_titles.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView item_title;
        ImageView item_icon;
        RelativeLayout parentLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_title = itemView.findViewById(R.id.item_title);
            item_icon = itemView.findViewById(R.id.item_icon);
            parentLayout = itemView.findViewById(R.id.copy_of_parent_layout);

        }
    }


}

