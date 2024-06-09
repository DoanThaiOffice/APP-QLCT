package com.hynguyen.chitieucanhan.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.hynguyen.chitieucanhan.R;
import com.hynguyen.chitieucanhan.activity.ViTienActivity;
import com.hynguyen.chitieucanhan.adapter.ChiTieuAdapter;
import com.hynguyen.chitieucanhan.adapter.ChonThangAdapter;
import com.hynguyen.chitieucanhan.adapter.ViTienAdapter;
import com.hynguyen.chitieucanhan.database.AppViewModel;
import com.hynguyen.chitieucanhan.mdel.ChiTieu;
import com.hynguyen.chitieucanhan.mdel.ViTien;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private View view;

    //Database
    private AppViewModel appViewModel;
    //Chọn ngày
    private ExpandableLayout expMonth;
    private GridView gvPickMonth;
    private TextView txtYear;
    private ChonThangAdapter chonThangAdapter;
    private ImageButton btnLastYear, btnNextYear;
    //Chart
    private LineChart lcThongKeThuChi;
    //Dữ liệu
    private LiveData<List<ChiTieu>> chiTieuLiveData;
    private List<ChiTieu> listChiTieu;
    private long day;
    private long month;
    private long year;

    //Ví tiền
    private RecyclerView rvViTien;
    private ViTienAdapter viTienAdapter;
    private LiveData<List<ViTien>> viTienLiveData;

    //Thu Chi
    private RecyclerView rvCacChiTieu;
    private ChiTieuAdapter chiTieuAdapter;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        buttonScrollToTop();
        addDatabase();
        chonThangNam();
        addView();
        lineChart();
        return view;
    }

    private void addDatabase() {
        appViewModel = new ViewModelProvider(this).get(AppViewModel.class);
    }

    private void showThongTinBieuDo(Entry entry) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Date date = new Date((long) entry.getX());
        builder.setMessage("Ngày: " + date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getYear()
                + "\nSố tiền: " + numberFormat(String.valueOf((int)entry.getY())));
        builder.setCancelable(true);
        // Create AlertDialog:
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void addView() {
        lcThongKeThuChi = view.findViewById(R.id.lcThongKeThuChi);
        //Line chart
        chiTieuLiveData = appViewModel.tatCaChiTieu();
        listChiTieu = appViewModel.xuatChiTieu();
        chiTieuLiveData.observe(getViewLifecycleOwner(), new Observer<List<ChiTieu>>() {
            @Override
            public void onChanged(List<ChiTieu> chiTieuList) {
                chiTieuAdapter.submitList(chiTieuList);
                listChiTieu = chiTieuList;
                lineChart();
            }
        });
        lcThongKeThuChi.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                showThongTinBieuDo(e);
            }

            @Override
            public void onNothingSelected() {

            }
        });
        //Ví Tiền
        rvViTien = view.findViewById(R.id.rvViTien);
        rvViTien.setLayoutManager(new LinearLayoutManager(getContext()));
        viTienAdapter = new ViTienAdapter(getContext());
        rvViTien.setAdapter(viTienAdapter);
        viTienLiveData = appViewModel.tatCaViTien();
        viTienLiveData.observe(getViewLifecycleOwner(), new Observer<List<ViTien>>() {
            @Override
            public void onChanged(List<ViTien> viTienList) {
                viTienAdapter.submitList(viTienList);
            }
        });
        viTienAdapter.setOnItemClickListener(new ViTienAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ViTien viTien) {
                startActivity(new Intent(getActivity(), ViTienActivity.class));
            }
        });

        //Chi Tiêu
        rvCacChiTieu = view.findViewById(R.id.rvCacChiTieu);
        rvCacChiTieu.setLayoutManager(new LinearLayoutManager(getContext()));
        chiTieuAdapter = new ChiTieuAdapter(getContext());
        rvCacChiTieu.setAdapter(chiTieuAdapter);
    }

    private void lineChart() {
        List<Entry> listChi = new ArrayList<>();
        List<Entry> listThu = new ArrayList<>();
        //Tìm khoản chi
        for (int i = 0; i < listChiTieu.size(); i++) {
            ChiTieu chitieui = listChiTieu.get(i);
            long k = chitieui.getDate().getTime();
            if (chitieui.getDate().getYear() == year) {
                if (chitieui.getType() == 2) {
                    long tongChi = 0;
                    tongChi += Long.parseLong(chitieui.getMoney());
                    for (int j = i + 1; j < listChiTieu.size(); j++) {
                        ChiTieu chitieuj = listChiTieu.get(j);
                        if (chitieui.getDate().equals(chitieuj.getDate()) && chitieui.getType() == chitieuj.getType()) {
                            tongChi += Long.parseLong(chitieuj.getMoney());
                            i = j;
                        }
                    }
                    listChi.add(new Entry(k, tongChi));
                }
            }
        }
        //Tìm khoản thu
        for (int i = 0; i < listChiTieu.size(); i++) {
            ChiTieu chitieui = listChiTieu.get(i);
            long h = chitieui.getDate().getTime();
            if (chitieui.getDate().getYear() == year) {
                if (chitieui.getType() == 1) {
                    long tongThu = 0;
                    tongThu += Long.parseLong(chitieui.getMoney());
                    for (int j = i + 1; j < listChiTieu.size(); j++) {
                        ChiTieu chitieuj = listChiTieu.get(j);
                        if (chitieui.getDate().equals(chitieuj.getDate()) && chitieui.getType() == chitieuj.getType()) {
                            tongThu += Long.parseLong(chitieuj.getMoney());
                            i = j;
                        }
                    }
                    listThu.add(new Entry(h, tongThu));
                }
            }
        }
        LineDataSet chitieu = new LineDataSet(listChi, "Chi tiêu");
        chitieu.setColor(getResources().getColor(R.color.button_cancel));
        LineDataSet thunhap = new LineDataSet(listThu, "Thu nhập");
        thunhap.setColor(getResources().getColor(R.color.button_success));
        lcThongKeThuChi.animateX(200);
        lcThongKeThuChi.getAxisLeft().setAxisMinValue(0f);
        lcThongKeThuChi.getDescription().setEnabled(false);
        lcThongKeThuChi.getAxisRight().setEnabled(false);
        lcThongKeThuChi.getXAxis().setDrawGridLines(false);
        lcThongKeThuChi.getXAxis().setDrawLabels(false);
        LineData lineData = new LineData();
        lineData.addDataSet(thunhap);
        lineData.addDataSet(chitieu);
        lcThongKeThuChi.setData(lineData);
        lcThongKeThuChi.invalidate();
    }

    private void lineChartMonth(long month) {
        List<Entry> listChi = new ArrayList<>();
        List<Entry> listThu = new ArrayList<>();
        //Tìm khoản chi
        for (int i = 0; i < listChiTieu.size(); i++) {
            ChiTieu chitieui = listChiTieu.get(i);
            if (chitieui.getDate().getYear() == year && chitieui.getDate().getMonth() == month) {
                int k = chitieui.getDate().getDate();
                if (chitieui.getType() == 2) {
                    long tongChi = 0;
                    tongChi += Long.parseLong(chitieui.getMoney());
                    for (int j = i + 1; j < listChiTieu.size(); j++) {
                        ChiTieu chitieuj = listChiTieu.get(j);
                        if (chitieui.getDate().equals(chitieuj.getDate()) && chitieui.getType() == chitieuj.getType()) {
                            tongChi += Long.parseLong(chitieuj.getMoney());
                            i = j;
                        }
                    }
                    listChi.add(new Entry(k, tongChi));
                }
            }
        }
        //Tìm khoản thu
        for (int i = 0; i < listChiTieu.size(); i++) {
            ChiTieu chitieui = listChiTieu.get(i);
            if (chitieui.getDate().getYear() == year && chitieui.getDate().getMonth() == month) {
                int k = chitieui.getDate().getDate();
                if (chitieui.getType() == 1) {
                    long tongThu = 0;
                    tongThu += Long.parseLong(chitieui.getMoney());
                    for (int j = i + 1; j < listChiTieu.size(); j++) {
                        ChiTieu chitieuj = listChiTieu.get(j);
                        if (chitieui.getDate().equals(chitieuj.getDate()) && chitieui.getType() == chitieuj.getType()) {
                            tongThu += Long.parseLong(chitieuj.getMoney());
                            i = j;
                        }
                    }
                    listThu.add(new Entry(k, tongThu));
                }
            }
        }
        LineDataSet chitieu = new LineDataSet(listChi, "Chi tiêu");
        chitieu.setColor(getResources().getColor(R.color.button_cancel));
        LineDataSet thunhap = new LineDataSet(listThu, "Thu nhập");
        thunhap.setColor(getResources().getColor(R.color.button_success));
        lcThongKeThuChi.animateX(200);
        lcThongKeThuChi.getAxisLeft().setAxisMinValue(0f);
        lcThongKeThuChi.getDescription().setEnabled(false);
        lcThongKeThuChi.getAxisRight().setEnabled(false);
        LineData lineData = new LineData();
        lineData.addDataSet(thunhap);
        lineData.addDataSet(chitieu);
        lcThongKeThuChi.setData(lineData);
        lcThongKeThuChi.invalidate();
    }

    private void chonThangNam() {
        expMonth = view.findViewById(R.id.expMonth);
        gvPickMonth = view.findViewById(R.id.gvPickMonth);
        txtYear = view.findViewById(R.id.txtYear);
        Calendar calendar = Calendar.getInstance();
        txtYear.setText(String.valueOf(calendar.get(Calendar.YEAR)));
        txtYear.setOnClickListener(this);
        chonThangAdapter = new ChonThangAdapter(getContext());
        gvPickMonth.setAdapter(chonThangAdapter);
        btnLastYear = view.findViewById(R.id.btnLastYear);
        btnNextYear = view.findViewById(R.id.btnNextYear);
        btnLastYear.setOnClickListener(this);
        btnNextYear.setOnClickListener(this);
        gvPickMonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id < 12) {
                    lineChartMonth(id);
                } else {
                    lineChart();
                }
            }
        });
        getYear();
    }

    private void getYear() {
        year = Long.parseLong(txtYear.getText().toString());
    }

    //Nút cuộn lên đầu trang
    private void buttonScrollToTop() {
        ImageButton btnScrollToTop = view.findViewById(R.id.btnScrollToTop);
        ScrollView scrHome = view.findViewById(R.id.scrHome);
        btnScrollToTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrHome.post(new Runnable() {
                    @Override
                    public void run() {
                        scrHome.smoothScrollTo(0, 0);
                    }
                });
            }
        });
        scrHome.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = scrHome.getScrollY();
                if (scrollY > 500) {
                    btnScrollToTop.setVisibility(View.VISIBLE);
                } else {
                    btnScrollToTop.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtYear:
                expMonth.toggle();
                break;
            case R.id.btnLastYear:
                txtYear.setText(Integer.parseInt(txtYear.getText().toString()) - 1 + "");
                getYear();
                lineChart();
                break;
            case R.id.btnNextYear:
                txtYear.setText(Integer.parseInt(txtYear.getText().toString()) + 1 + "");
                getYear();
                lineChart();
                break;

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
