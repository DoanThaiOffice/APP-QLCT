package com.hynguyen.chitieucanhan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.hynguyen.chitieucanhan.R;
import com.hynguyen.chitieucanhan.mdel.DanhMuc;

import org.jetbrains.annotations.NotNull;

public class DanhMucAdapter extends ListAdapter<DanhMuc, DanhMucAdapter.DanhMucHolder> {
    private Context context;
    private OnItemClickListener listener;

    public DanhMucAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    private static final DiffUtil.ItemCallback<DanhMuc> DIFF_CALLBACK = new DiffUtil.ItemCallback<DanhMuc>() {
        @Override
        public boolean areItemsTheSame(DanhMuc oldItem, DanhMuc newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(DanhMuc oldItem, DanhMuc newItem) {
            return oldItem.getTenDanhMuc().equals(newItem.getTenDanhMuc()) &&
                    oldItem.getHinhAnh().equals(newItem.getHinhAnh());
        }
    };

    @NonNull
    @NotNull
    @Override
    public DanhMucHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_danh_muc, parent, false);
        return new DanhMucHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull DanhMucHolder holder, int position) {
        DanhMuc danhMuc = getItem(position);
        holder.txtTenDanhMuc.setText(danhMuc.getTenDanhMuc());
        holder.imgHinhDanhMuc.setImageResource(getIdHinh(danhMuc.getHinhAnh()));
    }

    public class DanhMucHolder extends RecyclerView.ViewHolder {
        private ImageView imgHinhDanhMuc;
        private TextView txtTenDanhMuc;

        public DanhMucHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgHinhDanhMuc = itemView.findViewById(R.id.imgHinhDanhMuc);
            txtTenDanhMuc = itemView.findViewById(R.id.txtTenViTien);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DanhMuc danhMuc);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private int getIdHinh(String name) {
        int drawableResourceId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        return drawableResourceId;
    }
}
