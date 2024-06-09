package com.hynguyen.chitieucanhan.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.hynguyen.chitieucanhan.mdel.ChiTieu;
import com.hynguyen.chitieucanhan.mdel.DanhMuc;
import com.hynguyen.chitieucanhan.mdel.LoaiTienTe;
import com.hynguyen.chitieucanhan.mdel.ViTien;

import java.util.List;

public class AppRepository {
    //Danh mục
    private DanhMucDao danhMucDao;
    private DanhMuc timDanhMucTheoId;
    private List<DanhMuc> xuatDanhMuc;
    private LiveData<List<DanhMuc>> xuatDanhMucLive;
    private LiveData<List<DanhMuc>> xuatDanhMucThu;
    private LiveData<List<DanhMuc>> xuatDanhMucChi;

    //Loại tiền tệ
    private LoaiTienTeDao loaiTienTeDao;
    private LiveData<LoaiTienTe> xuatLoaiTienTe;
    private LoaiTienTe loaiTienTe;

    //Ví Tiền
    private ViTienDao viTienDao;
    private ViTien timViTienTheoId;
    private List<ViTien> xuatViTien;
    private LiveData<List<ViTien>> xuatViTienLive;

    //Chi Tiêu
    private ChiTieuDao chiTieuDao;
    private List<ChiTieu> xuatChiTieu;
    private LiveData<List<ChiTieu>> xuatChiTieuLive;
    private LiveData<List<ChiTieu>> xuatChiTieuLiveLimit5;
    private LiveData<List<ChiTieu>> xuatKhoanThuLive;
    private LiveData<List<ChiTieu>> xuatKhoanChiLive;

    public AppRepository(Application application) {
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        //Danh mục
        danhMucDao = appDatabase.danhMucDao();
        xuatDanhMuc = danhMucDao.xuatDanhMuc();
        xuatDanhMucLive = danhMucDao.xuatDanhMucLive();
        xuatDanhMucThu = danhMucDao.xuatDanhMucThu();
        xuatDanhMucChi = danhMucDao.xuatDanhMucChi();

        //Loại tiền tệ
        loaiTienTeDao = appDatabase.loaiTienTeDao();
        xuatLoaiTienTe = loaiTienTeDao.xuatLoaiTienTe();
        loaiTienTe = loaiTienTeDao.loaiTienTe();

        //Ví Tiền
        viTienDao = appDatabase.viTienDao();
        xuatViTien = viTienDao.xuatViTien();
        xuatViTienLive = viTienDao.xuatViTienLive();

        //Chi Tiêu
        chiTieuDao = appDatabase.chiTieuDao();
        xuatChiTieu = chiTieuDao.xuatChiTieu();
        xuatChiTieuLive = chiTieuDao.xuatChiTieuLive();
        xuatChiTieuLiveLimit5 = chiTieuDao.xuatChiTieuLiveLimit5();
        xuatKhoanThuLive = chiTieuDao.xuatKhoanThuLive();
        xuatKhoanChiLive = chiTieuDao.xuatKhoanChiLive();
    }

    //Danh mục
    public void themDanhMuc(DanhMuc danhMuc) {
        new ThemDanhMucAsyncTask(danhMucDao).execute(danhMuc);
    }

    private static class ThemDanhMucAsyncTask extends AsyncTask<DanhMuc, Void, Void> {
        private DanhMucDao danhMucDao;

        private ThemDanhMucAsyncTask(DanhMucDao danhMucDao) {
            this.danhMucDao = danhMucDao;
        }

        @Override
        protected Void doInBackground(DanhMuc... danhMucs) {
            danhMucDao.themDanhMuc(danhMucs[0]);
            return null;
        }
    }

    public void capNhatDanhMuc(DanhMuc danhMuc) {
        new CapNhatDanhMucAsyncTask(danhMucDao).execute(danhMuc);
    }

    private static class CapNhatDanhMucAsyncTask extends AsyncTask<DanhMuc, Void, Void> {
        private DanhMucDao danhMucDao;

        private CapNhatDanhMucAsyncTask(DanhMucDao danhMucDao) {
            this.danhMucDao = danhMucDao;
        }

        @Override
        protected Void doInBackground(DanhMuc... danhMucs) {
            danhMucDao.capNhatDanhMuc(danhMucs[0]);
            return null;
        }
    }

    public void xoaDanhMuc(DanhMuc danhMuc) {
        new XoaDanhMucAsyncTask(danhMucDao).execute(danhMuc);
    }

    private static class XoaDanhMucAsyncTask extends AsyncTask<DanhMuc, Void, Void> {
        private DanhMucDao danhMucDao;

        private XoaDanhMucAsyncTask(DanhMucDao danhMucDao) {
            this.danhMucDao = danhMucDao;
        }

        @Override
        protected Void doInBackground(DanhMuc... danhMucs) {
            danhMucDao.xoaDanhMuc(danhMucs[0]);
            return null;
        }
    }

    public DanhMuc timDanhMucTheoId(int id) {
        return danhMucDao.timDanhMucTheoID(id);
    }

    public List<DanhMuc> xuatDanhMuc() {
        return xuatDanhMuc;
    }

    public LiveData<List<DanhMuc>> xuatDanhMucLive() {
        return xuatDanhMucLive;
    }

    public LiveData<List<DanhMuc>> xuatDanhMucThu() {
        return xuatDanhMucThu;
    }

    public LiveData<List<DanhMuc>> xuatDanhMucChi() {
        return xuatDanhMucChi;
    }

    //Loại tiền tệ
    public void capNhatLoaiTienTe(LoaiTienTe loaiTienTe) {
        new CapNhatLoaiTienTeAsyncTask(loaiTienTeDao).execute(loaiTienTe);
    }

    private static class CapNhatLoaiTienTeAsyncTask extends AsyncTask<LoaiTienTe, Void, Void> {
        private LoaiTienTeDao loaiTienTeDao;

        private CapNhatLoaiTienTeAsyncTask(LoaiTienTeDao loaiTienTeDao) {
            this.loaiTienTeDao = loaiTienTeDao;
        }

        @Override
        protected Void doInBackground(LoaiTienTe... loaiTienTes) {
            loaiTienTeDao.capNhatLoaiTienTe(loaiTienTes[0]);
            return null;
        }

    }

    public LiveData<LoaiTienTe> xuatLoaiTienTe() {
        return xuatLoaiTienTe;
    }

    public LoaiTienTe loaiTienTe() {
        return loaiTienTe;
    }

    public void themLoaiTienTe(LoaiTienTe loaiTienTe) {
        new ThemLoaiTienTeAsyncTask(loaiTienTeDao).execute(loaiTienTe);
    }

    private static class ThemLoaiTienTeAsyncTask extends AsyncTask<LoaiTienTe, Void, Void> {
        private LoaiTienTeDao loaiTienTeDao;

        private ThemLoaiTienTeAsyncTask(LoaiTienTeDao loaiTienTeDao) {
            this.loaiTienTeDao = loaiTienTeDao;
        }

        @Override
        protected Void doInBackground(LoaiTienTe... loaiTienTes) {
            loaiTienTeDao.themTienTe(loaiTienTes[0]);
            return null;
        }
    }

    //Ví Tiền
    public void themViTien(ViTien viTien) {
        new ThemViTienAsyncTask(viTienDao).execute(viTien);
    }

    private static class ThemViTienAsyncTask extends AsyncTask<ViTien, Void, Void> {
        private ViTienDao viTienDao;

        private ThemViTienAsyncTask(ViTienDao viTienDao) {
            this.viTienDao = viTienDao;
        }

        @Override
        protected Void doInBackground(ViTien... viTiens) {
            viTienDao.themViTien(viTiens[0]);
            return null;
        }
    }

    public void capNhatViTien(ViTien viTien) {
        new CapNhatViTienAsyncTask(viTienDao).execute(viTien);
    }

    private static class CapNhatViTienAsyncTask extends AsyncTask<ViTien, Void, Void> {
        private ViTienDao viTienDao;

        private CapNhatViTienAsyncTask(ViTienDao viTienDao) {
            this.viTienDao = viTienDao;
        }

        @Override
        protected Void doInBackground(ViTien... viTiens) {
            viTienDao.capNhatViTien(viTiens[0]);
            return null;
        }
    }

    public void xoaViTien(ViTien viTien) {
        new XoaViTienAsyncTask(viTienDao).execute(viTien);
    }

    private static class XoaViTienAsyncTask extends AsyncTask<ViTien, Void, Void> {
        private ViTienDao viTienDao;

        private XoaViTienAsyncTask(ViTienDao viTienDao) {
            this.viTienDao = viTienDao;
        }

        @Override
        protected Void doInBackground(ViTien... viTiens) {
            viTienDao.xoaViTien(viTiens[0]);
            return null;
        }
    }

    public ViTien timViTienTheoId(int uID) {
        return viTienDao.timViTienTheoID(uID);
    }

    public List<ViTien> xuatViTien() {
        return xuatViTien;
    }

    public LiveData<List<ViTien>> xuatViTienLive() {
        return xuatViTienLive;
    }

    //Chi Tiêu
    public void themChiTieu(ChiTieu chiTieu) {
        new ThemChiTieuAsyncTask(chiTieuDao).execute(chiTieu);
    }

    private static class ThemChiTieuAsyncTask extends AsyncTask<ChiTieu, Void, Void> {
        private ChiTieuDao chiTieuDao;

        private ThemChiTieuAsyncTask(ChiTieuDao chiTieuDao) {
            this.chiTieuDao = chiTieuDao;
        }

        @Override
        protected Void doInBackground(ChiTieu... chiTieus) {
            chiTieuDao.themChiTieu(chiTieus[0]);
            return null;
        }
    }

    public void capNhatChiTieu(ChiTieu chiTieu) {
        new CapNhatChiTieuAsyncTask(chiTieuDao).execute(chiTieu);
    }

    private static class CapNhatChiTieuAsyncTask extends AsyncTask<ChiTieu, Void, Void> {
        private ChiTieuDao chiTieuDao;

        private CapNhatChiTieuAsyncTask(ChiTieuDao chiTieuDao) {
            this.chiTieuDao = chiTieuDao;
        }

        @Override
        protected Void doInBackground(ChiTieu... chiTieus) {
            chiTieuDao.capNhatChiTieu(chiTieus[0]);
            return null;
        }
    }

    public void xoaChiTieu(ChiTieu chiTieu) {
        new XoaChiTieuAsyncTask(chiTieuDao).execute(chiTieu);
    }

    private static class XoaChiTieuAsyncTask extends AsyncTask<ChiTieu, Void, Void> {
        private ChiTieuDao chiTieuDao;

        private XoaChiTieuAsyncTask(ChiTieuDao chiTieuDao) {
            this.chiTieuDao = chiTieuDao;
        }

        @Override
        protected Void doInBackground(ChiTieu... chiTieus) {
            chiTieuDao.xoaChiTieu(chiTieus[0]);
            return null;
        }
    }

    public List<ChiTieu> xuatChiTieu() {
        return xuatChiTieu;
    }
    public LiveData<List<ChiTieu>> xuatChiTieuLiveLimit5() {
        return xuatChiTieuLiveLimit5;
    }
    public LiveData<List<ChiTieu>> xuatChiTieuLive() {
        return xuatChiTieuLive;
    }

    public LiveData<List<ChiTieu>> xuatKhoanChiLive() {
        return xuatKhoanChiLive;
    }

    public LiveData<List<ChiTieu>> xuatKhoanThuLive() {
        return xuatKhoanThuLive;
    }
}
