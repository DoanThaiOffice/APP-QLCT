package com.hynguyen.chitieucanhan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.hynguyen.chitieucanhan.R;
import com.hynguyen.chitieucanhan.database.AppViewModel;
import com.hynguyen.chitieucanhan.mdel.ViTien;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class ViTienAdapter extends ListAdapter<ViTien, ViTienAdapter.ViTienHolder> {
    private Context context;
    private OnItemClickListener listener;
    private AppViewModel appViewModel;

    public ViTienAdapter(Context context) {
        super(DIFF_CALLBACK);
        this.context = context;
    }

    private static final DiffUtil.ItemCallback<ViTien> DIFF_CALLBACK = new DiffUtil.ItemCallback<ViTien>() {
        @Override
        public boolean areItemsTheSame(ViTien oldItem, ViTien newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(ViTien oldItem, ViTien newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getImg().equals(newItem.getImg()) &&
                    oldItem.getMoney().equals(newItem.getMoney());
        }
    };

    @NonNull
    @NotNull
    @Override
    public ViTienHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vitien, parent, false);
        appViewModel = new ViewModelProvider((ViewModelStoreOwner) context).get(AppViewModel.class);
        return new ViTienHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViTienHolder holder, int position) {
        ViTien viTien = getItem(position);
        holder.txtTenViTien.setText(viTien.getName());
        holder.txtSoTien.setText("Số tiền: " + numberFormat(viTien.getMoney()) + " " + appViewModel.loaiTienTe().getName());
        holder.imgHinhViTien.setImageResource(getIdHinh(viTien.getImg()));
    }

    public class ViTienHolder extends RecyclerView.ViewHolder {
        private ImageView imgHinhViTien;
        private TextView txtTenViTien;
        private TextView txtSoTien;

        public ViTienHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            imgHinhViTien = itemView.findViewById(R.id.imgHinhViTien);
            txtTenViTien = itemView.findViewById(R.id.txtTenViTien);
            txtSoTien = itemView.findViewById(R.id.txtSoTien);
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
        void onItemClick(ViTien viTien);
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
