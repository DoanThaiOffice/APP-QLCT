package com.hynguyen.chitieucanhan.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.util.StringUtil;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.hynguyen.chitieucanhan.R;
import com.hynguyen.chitieucanhan.adapter.HinhDanhMucAdapter;
import com.hynguyen.chitieucanhan.database.AppViewModel;
import com.hynguyen.chitieucanhan.mdel.ViTien;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class ThemViTienFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    public static final String TAG = "ViTien";
    private AppViewModel appViewModel;
    private ImageView imgChonHinh;
    private ExpandableLayout expanHinhViTien;
    private RecyclerView rvHinhViTien;
    private List<String> listHinh;
    private HinhDanhMucAdapter hinhDanhMucAdapter;
    private MaterialButton btnHuyBo, btnHoanThanh;
    private TextView txtThemViTien;
    private TextInputEditText txtTenViTien;
    private TextInputEditText txtSoTien;
    private TextView txtLoaiTienTe;
    private String hinhViTien;
    private String tenViTien;
    private String soTien;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_themvitien, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        txtThemViTien = view.findViewById(R.id.txtThemViTien);
        //Chọn hình ví tiền
        imgChonHinh = view.findViewById(R.id.imgChonHinh);
        imgChonHinh.setOnClickListener(this);
        expanHinhViTien = view.findViewById(R.id.expanHinhViTien);
        rvHinhViTien = view.findViewById(R.id.rvHinhViTien);
        rvHinhViTien.setLayoutManager(new GridLayoutManager(getContext(), 5));
        listHinh = Arrays.asList(getResources().getStringArray(R.array.img_wallet));
        hinhDanhMucAdapter = new HinhDanhMucAdapter(getContext(), listHinh);
        rvHinhViTien.setAdapter(hinhDanhMucAdapter);
        hinhDanhMucAdapter.setOnItemClickListener(new HinhDanhMucAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String name) {
                imgChonHinh.setImageResource(getIdHinh(name));
                hinhViTien = name;
            }
        });
        txtTenViTien = view.findViewById(R.id.txtTenViTien);
        txtSoTien = view.findViewById(R.id.txtSoTien);
        txtSoTien.addTextChangedListener(onTextChangedListener());
        //Hiển thị loại tiền tệ
        txtLoaiTienTe = view.findViewById(R.id.txtLoaiTienTe);
        txtLoaiTienTe.setText(appViewModel.loaiTienTe().getName());

        btnHuyBo = view.findViewById(R.id.btnHuyBo);
        btnHuyBo.setOnClickListener(this);
        btnHoanThanh = view.findViewById(R.id.btnHoanThanh);
        btnHoanThanh.setOnClickListener(this);

        //Hiển thị cập nhật ví tiền
        if (getArguments() != null) {
            hinhViTien = getArguments().getString("img");
            imgChonHinh.setImageResource(getIdHinh(getArguments().getString("img")));
            tenViTien = getArguments().getString("name");
            txtTenViTien.setText(tenViTien);
            soTien = getArguments().getString("money");
            txtSoTien.setText(soTien);
            txtThemViTien.setText("Cập nhật ví tiền");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgChonHinh:
                expanHinhViTien.toggle();
                break;
            case R.id.btnHuyBo:
                dismiss();
                break;
            case R.id.btnHoanThanh:
                xuLyThemViTien();
                break;
        }
    }

    private void xuLyThemViTien() {
        tenViTien = txtTenViTien.getText().toString().trim();
        soTien = txtSoTien.getText().toString().replace(",","");
        if (TextUtils.isEmpty(tenViTien)) {
            Toasty.error(getContext(), "Tên ví tiền không được để trống!", Toasty.LENGTH_SHORT, true).show();
            return;
        }
        if (TextUtils.isEmpty(soTien)) {
            Toasty.error(getContext(), "Bạn chưa nhập số tiền ban đầu!", Toasty.LENGTH_SHORT, true).show();
            return;
        }
        if (TextUtils.isEmpty(hinhViTien)) {
            hinhViTien = "wallet_cash";
        }
        if (!isNumeric(soTien)){
            Toasty.error(getContext(),"Số tiền không được chứa ký tự đặc biệt!",Toasty.LENGTH_SHORT,true).show();
            return;
        }
        if (getArguments() == null) {
            ViTien viTien = new ViTien(hinhViTien, tenViTien, soTien);
            appViewModel.themViTien(viTien);
            Toasty.success(getContext(), "Thêm ví tiền thành công", Toasty.LENGTH_SHORT, true).show();
        } else {
            ViTien viTien = new ViTien(getArguments().getInt("id"), hinhViTien, tenViTien, soTien);
            appViewModel.capNhatViTien(viTien);
            Toasty.success(getContext(), "Cập nhật ví tiền thành công", Toasty.LENGTH_SHORT, true).show();
        }
        dismiss();
    }

    //Lấy thông tin danh mục cần cập nhật
    public static ThemViTienFragment newInstance(ViTien viTien) {
        ThemViTienFragment dialog = new ThemViTienFragment();
        Bundle args = new Bundle();
        args.putInt("id", viTien.getId());
        args.putString("name", viTien.getName());
        args.putString("img", viTien.getImg());
        args.putString("money", viTien.getMoney());
        dialog.setArguments(args);
        return dialog;
    }

    public static ThemViTienFragment newInstance() {
        return new ThemViTienFragment();
    }


    private int getIdHinh(String name) {
        int drawableResourceId = getContext().getResources().getIdentifier(name, "drawable", getContext().getPackageName());
        return drawableResourceId;
    }

    public static boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    private TextWatcher onTextChangedListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txtSoTien.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    txtSoTien.setText(formattedString);
                    txtSoTien.setSelection(txtSoTien.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }

                txtSoTien.addTextChangedListener(this);
            }
        };
    }
}
