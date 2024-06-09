package com.hynguyen.chitieucanhan.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.hynguyen.chitieucanhan.R;
import com.hynguyen.chitieucanhan.adapter.ChiTieuAdapter;
import com.hynguyen.chitieucanhan.database.AppViewModel;
import com.hynguyen.chitieucanhan.mdel.ChiTieu;
import com.hynguyen.chitieucanhan.mdel.DanhMuc;
import com.hynguyen.chitieucanhan.mdel.LoaiTienTe;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChiTieuFragment extends Fragment {
    private View view;
    private AppViewModel appViewModel;
    private TextView txtChuaCoGiaoDich;
    //Recycler View Thu Chi
    private RecyclerView rvChiTieu;
    private ChiTieuAdapter chiTieuAdapter;
    private LiveData<List<ChiTieu>> listChiTieu;
    private LiveData<List<ChiTieu>> listTatCaChiTieu;
    private List<ChiTieu> chiTieus;
    private LiveData<List<DanhMuc>> listDanhMuc;
    private List<DanhMuc> danhMucs;
    //BarChart
    private BarChart bcChiTieu;
    private BarData barDataChiTieu;
    private BarDataSet barDataSetChiTieu;
    private ArrayList<BarEntry> giaTriChiTieu;
    private ArrayList<String> nhanChiTieu;
    private LiveData<List<ChiTieu>> chiTieuLiveData;
    private LiveData<List<DanhMuc>> danhMucLiveData;
    private XAxis xAxis;
    private Date date;

    private BarChart bcThuNhap;
    private BarData barDataThuNhap;
    private BarDataSet barDataSetThuNhap;
    private ArrayList<BarEntry> giaTriThuNhap;
    private ArrayList<String> nhanThuNhap;
    //Thống kê
    private TextView txtTongThuTrongNgay;
    private TextView txtTongChiTrongNgay;
    private TextView txtHieuThuChi;
    private LiveData<LoaiTienTe> loaiTienTeLiveData;
    private String loaiTienTe;
    private long tongThu;
    private long tongChi;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chitieu, container, false);

        buttonScrollToTop();

        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);

        txtChuaCoGiaoDich = view.findViewById(R.id.txtChuaCoGiaoDich);
        //Recycler View Thu Chi
        rvChiTieu = view.findViewById(R.id.rvChiTieu);
        chiTieuAdapter = new ChiTieuAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvChiTieu.setLayoutManager(linearLayoutManager);
        rvChiTieu.setAdapter(chiTieuAdapter);

        //Bar Chart Chi Tiêu trong ngày
        chiTieus = appViewModel.xuatChiTieu();
        danhMucs = appViewModel.xuatDanhMuc();
        bcChiTieu = view.findViewById(R.id.bcChiTieu);
        bcThuNhap = view.findViewById(R.id.bcThuNhap);
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        date = new Date(year, month, day);
        barChartChiTieu();
        barChartThuNhap();
        //Thống kê
        txtTongThuTrongNgay = view.findViewById(R.id.txtTongThuTrongNgay);
        txtTongChiTrongNgay = view.findViewById(R.id.txtTongChiTrongNgay);
        txtHieuThuChi = view.findViewById(R.id.txtHieuThuChi);
        loaiTienTe = appViewModel.loaiTienTe().getName();
        xuLyThongKeSoLieuThuChi();

        //LiveData
        //Chi Tiêu Full
        chiTieuLiveData = appViewModel.tatCaChiTieu();
        chiTieuLiveData.observe(getViewLifecycleOwner(), new Observer<List<ChiTieu>>() {
            @Override
            public void onChanged(List<ChiTieu> chiTieuList) {
                List<ChiTieu> chiTieuHomNay = new ArrayList<>();
                for (int i = 0; i < chiTieuList.size(); i++) {
                    ChiTieu chiTieu = chiTieuList.get(i);
                    if (chiTieu.getDate().equals(date)){
                        chiTieuHomNay.add(chiTieu);
                    }
                }
                chiTieuAdapter.submitList(chiTieuHomNay);
                chiTieus = chiTieuList;
                duLieuChiTieu();
                duLieuThuNhap();
                xuLyThongKeSoLieuThuChi();
            }
        });
        //Danh Mục
        danhMucLiveData = appViewModel.tatCaDanhMuc();
        danhMucLiveData.observe(getViewLifecycleOwner(), new Observer<List<DanhMuc>>() {
            @Override
            public void onChanged(List<DanhMuc> danhMucList) {
                danhMucs = danhMucList;
                duLieuChiTieu();
                duLieuThuNhap();
            }
        });
        //Loại tiền tệ
        loaiTienTeLiveData = appViewModel.xuatLoaiTienTe();
        loaiTienTeLiveData.observe(getActivity(), new Observer<LoaiTienTe>() {
            @Override
            public void onChanged(LoaiTienTe tienTe) {
                loaiTienTe = tienTe.getName();
                xuLyThongKeSoLieuThuChi();
            }
        });

        return view;
    }

    private void xuLyThongKeSoLieuThuChi() {
        tongChi = 0;
        tongThu = 0;
        for (int i = 0; i < chiTieus.size(); i++) {
            ChiTieu chiTieu = chiTieus.get(i);
            if (chiTieu.getType() == 2 && chiTieu.getDate().equals(date)) {
                tongChi += Long.parseLong(chiTieu.getMoney());
            } else if (chiTieu.getType() == 1 && chiTieu.getDate().equals(date)) {
                tongThu += Long.parseLong(chiTieu.getMoney());
            }
        }
        txtTongChiTrongNgay.setText(numberFormat(String.valueOf(tongChi)));
        txtTongThuTrongNgay.setText(numberFormat(String.valueOf(tongThu)));
        txtHieuThuChi.setText(numberFormat(String.valueOf(tongThu - tongChi)) + " " + loaiTienTe);
    }

    //Tạo BarChart Dữ liệu chi trong ngày
    private void barChartThuNhap() {
        //Khởi tạo dữ liệu
        giaTriThuNhap = new ArrayList<>();
        nhanThuNhap = new ArrayList<>();

        duLieuThuNhap();
    }

    private void duLieuThuNhap() {
        giaTriThuNhap.clear();
        nhanThuNhap.clear();
        int k = 0;
        for (int i = 0; i < danhMucs.size(); i++) {
            DanhMuc danhMuc = danhMucs.get(i);
            long sum = 0;
            for (int j = 0; j < chiTieus.size(); j++) {
                ChiTieu chiTieu = chiTieus.get(j);
                if (chiTieu.getDate().equals(date)
                        && chiTieu.getType() == 1
                        && chiTieu.getCategory() == danhMuc.getId()) {
                    sum += Long.parseLong(chiTieu.getMoney());
                }
            }
            if (sum > 0) {
                giaTriThuNhap.add(new BarEntry(k, sum));
                nhanThuNhap.add(danhMuc.getTenDanhMuc());
                k++;
            }
        }
        barDataSetThuNhap = new BarDataSet(giaTriThuNhap, null);
        barDataSetThuNhap.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSetThuNhap.setValueTextColor(Color.BLACK);
        bcThuNhap.getDescription().setEnabled(false);
        bcThuNhap.getAxisRight().setEnabled(false);
        bcThuNhap.getAxisLeft().setEnabled(false);
        bcThuNhap.getAxisLeft().setAxisMinValue(0f);
        bcThuNhap.getLegend().setEnabled(false);
        bcThuNhap.getXAxis().setDrawGridLines(false);
        bcThuNhap.animateY(500);
        xAxis = bcThuNhap.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return nhanThuNhap.get((int) value);
            }
        };
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);
        bcThuNhap.setDrawGridBackground(false);
        bcThuNhap.setDrawBarShadow(false);
        barDataThuNhap = new BarData(barDataSetThuNhap);
        barDataThuNhap.setBarWidth(1f);
        bcThuNhap.setData(barDataThuNhap);
        bcThuNhap.invalidate();
    }
    //Tạo BarChart Dữ liệu chi trong ngày
    private void barChartChiTieu() {
        //Khởi tạo dữ liệu
        giaTriChiTieu = new ArrayList<>();
        nhanChiTieu = new ArrayList<>();

        duLieuChiTieu();
    }

    private void duLieuChiTieu() {
        giaTriChiTieu.clear();
        nhanChiTieu.clear();
        int k = 0;
        for (int i = 0; i < danhMucs.size(); i++) {
            DanhMuc danhMuc = danhMucs.get(i);
            long sum = 0;
            for (int j = 0; j < chiTieus.size(); j++) {
                ChiTieu chiTieu = chiTieus.get(j);
                if (chiTieu.getDate().equals(date)
                        && chiTieu.getType() == 2
                        && chiTieu.getCategory() == danhMuc.getId()) {
                    sum += Long.parseLong(chiTieu.getMoney());
                }
            }
            if (sum > 0) {
                giaTriChiTieu.add(new BarEntry(k, sum));
                nhanChiTieu.add(danhMuc.getTenDanhMuc());
                k++;
            }
        }
        if (giaTriChiTieu.size()>0 ){
            txtChuaCoGiaoDich.setVisibility(View.GONE);
        } else {
            txtChuaCoGiaoDich.setVisibility(View.VISIBLE);
        }
        barDataSetChiTieu = new BarDataSet(giaTriChiTieu, null);
        barDataSetChiTieu.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSetChiTieu.setValueTextColor(Color.BLACK);
        bcChiTieu.getDescription().setEnabled(false);
        bcChiTieu.getAxisRight().setEnabled(false);
        bcChiTieu.getAxisLeft().setEnabled(false);
        bcChiTieu.getAxisLeft().setAxisMinValue(0f);
        bcChiTieu.getLegend().setEnabled(false);
        bcChiTieu.getXAxis().setDrawGridLines(false);
        bcChiTieu.animateY(500);
        xAxis = bcChiTieu.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.BLACK);
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return nhanChiTieu.get((int) value);
            }
        };
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);
        bcChiTieu.setDrawGridBackground(false);
        bcChiTieu.setDrawBarShadow(false);
        barDataChiTieu = new BarData(barDataSetChiTieu);
        barDataChiTieu.setBarWidth(1f);
        bcChiTieu.setData(barDataChiTieu);
        bcChiTieu.invalidate();
    }
    //Định dạng số tiền
    public String numberFormat(String string) {
        Long number = Long.parseLong(string);
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        String formattedString = formatter.format(number);
        return formattedString;
    }

    //Nút cuộn lên đầu trang
    private void buttonScrollToTop() {
        ImageButton btnScrollToTopChiTieu = view.findViewById(R.id.btnScrollToTopChiTieu);
        ScrollView scrollView = view.findViewById(R.id.scrollView);
        btnScrollToTopChiTieu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.smoothScrollTo(0, 0);
                    }
                });
            }
        });
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrollView.getScrollY();
                if (scrollY > 500) {
                    btnScrollToTopChiTieu.setVisibility(View.VISIBLE);
                } else {
                    btnScrollToTopChiTieu.setVisibility(View.GONE);
                }
            }
        });
    }
}
