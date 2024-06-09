package com.hynguyen.chitieucanhan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hynguyen.chitieucanhan.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class HinhDanhMucAdapter extends RecyclerView.Adapter<HinhDanhMucAdapter.HinhDanhMucHolder> {
    private List<String> listHinh;
    private Context context;
    private OnItemClickListener listener;

    @NonNull
    @NotNull
    @Override
    public HinhDanhMucHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hinhdanhmuc, parent, false);
        return new HinhDanhMucHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull HinhDanhMucHolder holder, int position) {
        holder.imgHinhDanhMuc.setImageResource(getIdHinh(listHinh.get(position)));
    }

    @Override
    public int getItemCount() {
        return listHinh.size();
    }

    public HinhDanhMucAdapter(Context context, List<String> listHinh) {
        this.context = context;
        this.listHinh = listHinh;
    }

    class HinhDanhMucHolder extends RecyclerView.ViewHolder {
        public ImageView imgHinhDanhMuc;

        public HinhDanhMucHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgHinhDanhMuc = itemView.findViewById(R.id.imgHinhDanhMuc);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(listHinh.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String name);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private int getIdHinh(String name) {
        int drawableResourceId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        return drawableResourceId;
    }
}
