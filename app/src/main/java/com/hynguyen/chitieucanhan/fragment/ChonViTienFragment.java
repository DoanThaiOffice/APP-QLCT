package com.hynguyen.chitieucanhan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.hynguyen.chitieucanhan.R;
import com.hynguyen.chitieucanhan.adapter.DanhMucAdapter;
import com.hynguyen.chitieucanhan.adapter.ViTienAdapter;
import com.hynguyen.chitieucanhan.database.AppViewModel;
import com.hynguyen.chitieucanhan.mdel.DanhMuc;
import com.hynguyen.chitieucanhan.mdel.ViTien;

import java.util.List;

public class ChonViTienFragment  extends BottomSheetDialogFragment {
    private View view;
    public static final String TAG = "ChonViTien";
    private AppViewModel appViewModel;
    private ViTienAdapter viTienAdapter;
    private RecyclerView rvChonViTien;
    private LiveData<List<ViTien>> listViTien;
    private List<ViTien> viTiens;
    private ChonViTienFragment.ChonViTienFragmentListener listener;

    public interface ChonViTienFragmentListener {
        public void onClickListener(ViTien viTien);
    }

    public void setDialogFragmentListener(ChonViTienFragment.ChonViTienFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static ChonViTienFragment newInstance() {
        return new ChonViTienFragment();
    }

    //Lấy thông tin danh mục cần cập nhật
    public static ChonViTienFragment newInstance(ViTien viTien) {
        ChonViTienFragment dialog = new ChonViTienFragment();
        Bundle args = new Bundle();
        args.putInt("id", viTien.getId());
        args.putString("name", viTien.getName());
        args.putString("money", viTien.getMoney());
        args.putString("img", viTien.getImg());
        dialog.setArguments(args);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chonvitien, container, false);

        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        //Đổ dữ liệu vào Recycler View
        rvChonViTien = view.findViewById(R.id.rvChonViTien);
        rvChonViTien.setLayoutManager(new LinearLayoutManager(getContext()));
        viTienAdapter = new ViTienAdapter(getContext());
        rvChonViTien.setAdapter(viTienAdapter);
        viTienAdapter.setOnItemClickListener(new ViTienAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(ViTien viTien) {
                listener.onClickListener(viTien);
                dismiss();
            }
        });
        listViTien = appViewModel.tatCaViTien();
        listViTien.observe(getActivity(), new Observer<List<ViTien>>() {
            @Override
            public void onChanged(List<ViTien> listViTien) {
                viTienAdapter.submitList(listViTien);
                viTiens = listViTien;
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
