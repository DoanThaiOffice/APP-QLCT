package com.hynguyen.chitieucanhan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.hynguyen.chitieucanhan.R;
import com.hynguyen.chitieucanhan.database.AppViewModel;
import com.hynguyen.chitieucanhan.mdel.ChiTieu;
import com.hynguyen.chitieucanhan.mdel.DanhMuc;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

public class ChiTieuAdapter extends ListAdapter<ChiTieu, ChiTieuAdapter.ChiTieuHolder> {
    private Context context;
    private OnItemClickListener listener;
    private AppViewModel appViewModel;

    public ChiTieuAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    private static final DiffUtil.ItemCallback<ChiTieu> DIFF_CALLBACK = new DiffUtil.ItemCallback<ChiTieu>() {
        @Override
        public boolean areItemsTheSame(ChiTieu oldItem, ChiTieu newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(ChiTieu oldItem, ChiTieu newItem) {
            return oldItem.getMoney().equals(newItem.getMoney()) &&
                    oldItem.getCategory() == newItem.getCategory() &&
                    oldItem.getType() == newItem.getType() &&
                    oldItem.getWallet() == newItem.getWallet() &&
                    oldItem.getNote().equals(newItem.getNote());
        }
    };

    @NonNull
    @NotNull
    @Override
    public ChiTieuHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chitieu, parent, false);
        return new ChiTieuHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ChiTieuHolder holder, int position) {
        ChiTieu chiTieu = getItem(position);
        appViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(AppViewModel.class);
        DanhMuc danhMuc = appViewModel.timDanhMucTheoId(chiTieu.getCategory());
        holder.imgHinhDanhMuc.setImageResource(getIdHinh(danhMuc.getHinhAnh()));
        holder.txtTenDanhMuc.setText(danhMuc.getTenDanhMuc());
        if (!chiTieu.getNote().isEmpty()) {
            holder.txtGhiChu.setVisibility(View.VISIBLE);
            holder.txtGhiChu.setText(chiTieu.getNote());
        } else {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) holder.txtTenDanhMuc.getLayoutParams();
            holder.txtGhiChu.setVisibility(View.GONE);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, 1);
            holder.txtTenDanhMuc.setLayoutParams(layoutParams);
        }

        holder.txtSoTien.setText(numberFormat(chiTieu.getMoney()));
        if (chiTieu.getType() == 1) {
            holder.txtSoTien.setTextColor(context.getResources().getColor(R.color.button_success));
            holder.imgUpDown.setImageResource(R.drawable.ic_up);
            holder.imgUpDown.setColorFilter(ContextCompat.getColor(context, R.color.button_success));
        } else {
            holder.txtSoTien.setTextColor(context.getResources().getColor(R.color.button_cancel));
            holder.imgUpDown.setImageResource(R.drawable.ic_down);
            holder.imgUpDown.setColorFilter(ContextCompat.getColor(context, R.color.button_cancel));
        }
        Date date = chiTieu.getDate();
        holder.txtNgayChiTieu.setText(date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getYear());
    }

    public class ChiTieuHolder extends RecyclerView.ViewHolder {
        private ImageView imgHinhDanhMuc;
        private TextView txtTenDanhMuc;
        private TextView txtGhiChu;
        private TextView txtSoTien;
        private ImageView imgUpDown;
        private TextView txtNgayChiTieu;

        public ChiTieuHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgHinhDanhMuc = itemView.findViewById(R.id.imgHinhDanhMuc);
            txtTenDanhMuc = itemView.findViewById(R.id.txtTenDanhMuc);
            txtGhiChu = itemView.findViewById(R.id.txtGhiChu);
            txtSoTien = itemView.findViewById(R.id.txtSoTien);
            imgUpDown = itemView.findViewById(R.id.imgUpDown);
            txtNgayChiTieu = itemView.findViewById(R.id.txtNgayChiTieu);
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
        void onItemClick(ChiTieu chiTieu);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    private int getIdHinh(String name) {
        int drawableResourceId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
        return drawableResourceId;
    }

    //Định dạng số tiền
    public String numberFormat(String string) {
        Long number = Long.parseLong(string);
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        String formattedString = formatter.format(number);
        return formattedString;
    }
}
