package com.hynguyen.chitieucanhan.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.hynguyen.chitieucanhan.R;
import com.hynguyen.chitieucanhan.database.AppViewModel;
import com.hynguyen.chitieucanhan.fragment.ChonDanhMucFragment;
import com.hynguyen.chitieucanhan.fragment.ChonViTienFragment;
import com.hynguyen.chitieucanhan.fragment.SuaLoaiTienTeFragment;
import com.hynguyen.chitieucanhan.mdel.ChiTieu;
import com.hynguyen.chitieucanhan.mdel.DanhMuc;
import com.hynguyen.chitieucanhan.mdel.ViTien;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class ThemChiTieuActivity extends AppCompatActivity implements ChonDanhMucFragment.ChonDanhMucFragmentListener, View.OnClickListener, ChonViTienFragment.ChonViTienFragmentListener {
    private AppViewModel appViewModel;

    private MaterialToolbar tbThemChiTieu;
    private TextView txtTBTitle;
    private ImageView imgHinhDanhMuc;
    private TextView txtTenDanhMuc;
    private TextView txtLoaiDanhMuc;
    private EditText txtSoTien;
    private TextView txtSoTienConLai;
    private RelativeLayout rlChonDanhMuc;
    private RelativeLayout rlChonViTien;
    private ImageView imgHinhViTien;
    private TextView txtTenViTien;
    private TextView txtSoTienVi;
    private MaterialButton btnHoanThanh;
    private TextView txtNgayChiTieu;
    private EditText txtGhiChu;

    //Thông tin Lưu CSDL
    private DanhMuc danhMuc;
    private ViTien viTien;
    private String note;
    private String money;
    private Date date;
    private int type;
    private int category;
    private int wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_chi_tieu);

        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
        //Toolbar
        tbThemChiTieu = findViewById(R.id.tbThemChiTieu);
        txtTBTitle = findViewById(R.id.txtTBTitle);
        tbThemChiTieu.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Chọn ví tiền
        rlChonViTien = findViewById(R.id.rlChonViTien);
        rlChonViTien.setOnClickListener(this);
        imgHinhViTien = findViewById(R.id.imgHinhViTien);
        txtTenViTien = findViewById(R.id.txtTenViTien);
        txtSoTienVi = findViewById(R.id.txtSoTienVi);

        //Chọn danh mục
        rlChonDanhMuc = findViewById(R.id.rlChonDanhMuc);
        rlChonDanhMuc.setOnClickListener(this);
        imgHinhDanhMuc = findViewById(R.id.imgHinhDanhMuc);
        txtTenDanhMuc = findViewById(R.id.txtTenDanhMuc);
        txtLoaiDanhMuc = findViewById(R.id.txtLoaiDanhMuc);

        //Số tiền
        txtSoTienConLai = findViewById(R.id.txtSoTienConLai);
        txtSoTien = findViewById(R.id.txtSoTien);
        txtSoTien.addTextChangedListener(onTextChangedListener());

        //Ghi chú
        txtGhiChu = findViewById(R.id.txtGhiChu);

        //Ngày chi tiêu
        txtNgayChiTieu = findViewById(R.id.txtNgayChiTieu);
        txtNgayChiTieu.setOnClickListener(this);

        //Hoàn thành
        btnHoanThanh = findViewById(R.id.btnHoanThanh);
        btnHoanThanh.setOnClickListener(this);

        //Hiển thị thông tin ban đầu
        danhMuc = appViewModel.xuatDanhMuc().get(0);
        hienThiThongTinDanhMuc(danhMuc);
        showChonDanhMuc(danhMuc);
        viTien = appViewModel.xuatViTien().get(0);
        hienThiThongTinViTien(viTien);
        Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        txtNgayChiTieu.setText(mDay + "/" + (mMonth + 1) + "/" + mYear);
        date = new Date(mYear, mMonth, mDay);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlChonDanhMuc:
                showChonDanhMuc(danhMuc);
                break;
            case R.id.rlChonViTien:
                ChonViTienFragment chonViTienFragment = ChonViTienFragment.newInstance();
                chonViTienFragment.setDialogFragmentListener(this);
                chonViTienFragment.show(getSupportFragmentManager(), ChonViTienFragment.TAG);
                break;
            case R.id.txtNgayChiTieu:
                buttonDatePicker();
                break;
            case R.id.btnHoanThanh:
                xuLyThemThuChi();
                break;
        }
    }

    //Xủ lý khi nhấn nút hoàn thành
    private void xuLyThemThuChi() {
        note = txtGhiChu.getText().toString();
        money = txtSoTien.getText().toString().replace(",", "");
        if (TextUtils.isEmpty(money)) {
            Toasty.error(this, "Số tiền không được để trống!", Toasty.LENGTH_LONG, true).show();
            return;
        }
        if (!isNumeric(money)) {
            Toasty.error(this, "Số tiền không được chứa ký tự đặc biệt!", Toasty.LENGTH_LONG, true).show();
            return;
        }
        ChiTieu chiTieu = new ChiTieu(note, money, date, type, category, wallet);
        appViewModel.themChiTieu(chiTieu);
        if (viTien != null) {
            if (type == 2) {
                viTien.setMoney(String.valueOf(Long.parseLong(viTien.getMoney()) - Long.parseLong(money)));
            } else {
                viTien.setMoney(String.valueOf(Long.parseLong(viTien.getMoney()) + Long.parseLong(money)));
            }
            appViewModel.capNhatViTien(viTien);

        }
        Toasty.success(this, "Thêm thành công", Toasty.LENGTH_LONG, true).show();
        finish();
    }

    //Lắng nghe chọn danh mục
    @Override
    public void onClickListener(DanhMuc danhMuc) {
        this.danhMuc = danhMuc;
        hienThiThongTinDanhMuc(danhMuc);
    }

    //Hiển thị thông tin danh mục lên View
    private void hienThiThongTinDanhMuc(DanhMuc danhMuc) {
        imgHinhDanhMuc.setImageResource(getIdHinh(danhMuc.getHinhAnh()));
        txtTenDanhMuc.setText(danhMuc.getTenDanhMuc());
        if (danhMuc.getLoaiDanhMuc() == 1) {
            txtLoaiDanhMuc.setText("Khoản thu");
            txtSoTien.setHintTextColor(getResources().getColor(R.color.button_success));
            txtSoTien.setTextColor(getResources().getColor(R.color.button_success));
            txtTBTitle.setText("Thêm thu nhập");
        } else {
            txtLoaiDanhMuc.setText("Khoản chi");
            txtTBTitle.setText("Thêm khoản chi");
            txtSoTien.setHintTextColor(getResources().getColor(R.color.button_cancel));
            txtSoTien.setTextColor(getResources().getColor(R.color.button_cancel));
        }
        category = danhMuc.getId();
        type = danhMuc.getLoaiDanhMuc();

    }

    //Hiển thị chọn danh mục
    private void showChonDanhMuc(DanhMuc danhMuc) {
        ChonDanhMucFragment chonDanhMucFragment;
        chonDanhMucFragment = ChonDanhMucFragment.newInstance(danhMuc);
        chonDanhMucFragment.setDialogFragmentListener(this);
        chonDanhMucFragment.show(this.getSupportFragmentManager(), ChonDanhMucFragment.TAG);
    }

    //Chọn ví tiền
    @Override
    public void onClickListener(ViTien viTien) {
        this.viTien = viTien;
        hienThiThongTinViTien(viTien);
    }

    //Hiển thị thông tin ví tiền lên View
    private void hienThiThongTinViTien(ViTien viTien) {
        imgHinhViTien.setImageResource(getIdHinh(viTien.getImg()));
        txtSoTienVi.setText("Số tiền: " + numberFormat(viTien.getMoney()));
        txtTenViTien.setText(viTien.getName());
        wallet = viTien.getId();
    }

    // Chọn ngày
    private void buttonDatePicker() {
        // Get Current Date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        txtNgayChiTieu.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        date = new Date(year, monthOfYear, dayOfMonth);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    //Lắng nghe chuyển đổi số dạng 0.000.000
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

                    //Cập nhật thông tin số tiền còn lại
                    String sotienconlai;
                    Character dauThuChi;
                    if (type == 2) {
                        sotienconlai = numberFormat(String.valueOf(Long.parseLong(viTien.getMoney()) - Long.parseLong(formattedString.replace(",", ""))));
                        dauThuChi = '-';
                    } else {
                        sotienconlai = numberFormat(String.valueOf(Long.parseLong(viTien.getMoney()) + Long.parseLong(formattedString.replace(",", ""))));
                        dauThuChi = '+';
                    }
                    txtSoTienConLai.setText(" " + dauThuChi + " " + formattedString + " = " + sotienconlai + " " + appViewModel.loaiTienTe().getName());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                txtSoTien.addTextChangedListener(this);
            }
        };
    }

    //Lấy id của hình theo tên
    private int getIdHinh(String name) {
        int drawableResourceId = getResources().getIdentifier(name, "drawable", getPackageName());
        return drawableResourceId;
    }

    //Kiểm tra số tiền
    public static boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
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