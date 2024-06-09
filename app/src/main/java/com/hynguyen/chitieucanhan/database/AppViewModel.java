package com.hynguyen.chitieucanhan.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hynguyen.chitieucanhan.mdel.ChiTieu;
import com.hynguyen.chitieucanhan.mdel.DanhMuc;
import com.hynguyen.chitieucanhan.mdel.LoaiTienTe;
import com.hynguyen.chitieucanhan.mdel.ViTien;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AppViewModel extends AndroidViewModel {
    private AppRepository repository;

    //Danh mục
    private DanhMuc timDanhMucTheoId;
    private LiveData<List<DanhMuc>> tatCaDanhMuc;
    private LiveData<List<DanhMuc>> tatCaDanhMucThu;
    private LiveData<List<DanhMuc>> tatCaDanhMucChi;
    private List<DanhMuc> xuatDanhMuc;
    //Loại tiền tệ
    private LiveData<LoaiTienTe> xuatLoaiTienTe;
    private LoaiTienTe loaiTienTe;

    //Vi Tiền
    private ViTien viTien;
    private List<ViTien> xuatViTien;
    private LiveData<List<ViTien>> tatCaViTien;

    //Chi Tiêu
    private List<ChiTieu> xuatChiTieu;
    private LiveData<List<ChiTieu>> tatCaChiTieu;
    private LiveData<List<ChiTieu>> tatCaChiTieuLimit5;
    private LiveData<List<ChiTieu>> tatCaKhoanChi;
    private LiveData<List<ChiTieu>> tatCaKhoanThu;

    public AppViewModel(@NonNull @NotNull Application application) {
        super(application);
        repository = new AppRepository(application);

        //Danh mục
        xuatDanhMuc = repository.xuatDanhMuc();
        tatCaDanhMuc = repository.xuatDanhMucLive();
        tatCaDanhMucThu = repository.xuatDanhMucThu();
        tatCaDanhMucChi = repository.xuatDanhMucChi();

        //Loại tiền tệ
        xuatLoaiTienTe = repository.xuatLoaiTienTe();
        loaiTienTe = repository.loaiTienTe();

        //Ví Tiền
        xuatViTien = repository.xuatViTien();
        tatCaViTien = repository.xuatViTienLive();

        //Chi tiêu
        xuatChiTieu = repository.xuatChiTieu();
        tatCaChiTieu = repository.xuatChiTieuLive();
        tatCaChiTieuLimit5 = repository.xuatChiTieuLiveLimit5();
        tatCaKhoanChi = repository.xuatKhoanThuLive();
        tatCaKhoanThu = repository.xuatKhoanChiLive();
    }

    //Danh mục
    public void themDanhMuc(DanhMuc danhMuc) {
        repository.themDanhMuc(danhMuc);
    }

    public void capNhatDanhMuc(DanhMuc danhMuc) {
        repository.capNhatDanhMuc(danhMuc);
    }

    public void xoaDanhMuc(DanhMuc danhMuc) {
        repository.xoaDanhMuc(danhMuc);
    }

    public DanhMuc timDanhMucTheoId(int id) {
        return repository.timDanhMucTheoId(id);
    }

    public List<DanhMuc> xuatDanhMuc() {
        return xuatDanhMuc;
    }

    public LiveData<List<DanhMuc>> tatCaDanhMuc() {
        return tatCaDanhMuc;
    }

    public LiveData<List<DanhMuc>> tatCaDanhMucThu() {
        return tatCaDanhMucThu;
    }

    public LiveData<List<DanhMuc>> tatCaDanhMucChi() {
        return tatCaDanhMucChi;
    }

    //Loại tiền tệ
    public void themLoaiTienTe(LoaiTienTe loaiTienTe) {
        repository.themLoaiTienTe(loaiTienTe);
    }

    public void capNhatLoaiTienTe(LoaiTienTe loaiTienTe) {
        repository.capNhatLoaiTienTe(loaiTienTe);
    }

    public LiveData<LoaiTienTe> xuatLoaiTienTe() {
        return xuatLoaiTienTe;
    }

    public LoaiTienTe loaiTienTe() {
        return loaiTienTe;
    }

    //Ví Tiền
    public void themViTien(ViTien viTien) {
        repository.themViTien(viTien);
    }

    public void capNhatViTien(ViTien viTien) {
        repository.capNhatViTien(viTien);
    }

    public void xoaViTien(ViTien viTien) {
        repository.xoaViTien(viTien);
    }

    public ViTien timViTienTheoId(int uID) {
        return repository.timViTienTheoId(uID);
    }

    public List<ViTien> xuatViTien() {
        return xuatViTien;
    }

    public LiveData<List<ViTien>> tatCaViTien() {
        return tatCaViTien;
    }

    //Thu Chi
    public void themChiTieu(ChiTieu chiTieu) {
        repository.themChiTieu(chiTieu);
    }

    public void capNhatChiTieu(ChiTieu chiTieu) {
        repository.capNhatChiTieu(chiTieu);
    }

    public void xoaChiTieu(ChiTieu chiTieu) {
        repository.xoaChiTieu(chiTieu);
    }

    public LiveData<List<ChiTieu>> tatCaChiTieu() {
        return tatCaChiTieu;
    }
    public LiveData<List<ChiTieu>> tatCaChiTieuLimit5() {
        return tatCaChiTieuLimit5;
    }

    public List<ChiTieu> xuatChiTieu() {
        return xuatChiTieu;
    }

    public LiveData<List<ChiTieu>> tatCaKhoanThu() {
        return tatCaKhoanThu;
    }

    public LiveData<List<ChiTieu>> tatCaKhoanChi() {
        return tatCaKhoanChi;
    }
}