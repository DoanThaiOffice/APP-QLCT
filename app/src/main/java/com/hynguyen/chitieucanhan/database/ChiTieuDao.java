package com.hynguyen.chitieucanhan.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.hynguyen.chitieucanhan.mdel.ChiTieu;
import com.hynguyen.chitieucanhan.mdel.DanhMuc;

import java.util.List;

@Dao
public interface ChiTieuDao {
    @Insert
    void themChiTieu(ChiTieu... chiTieu);
    @Query("SELECT * FROM tb_expense WHERE exp_id LIKE :uid")
    ChiTieu timChiTieuTheoID(int uid);

    @Delete
    void xoaChiTieu(ChiTieu chiTieu);

    @Update
    void capNhatChiTieu(ChiTieu chiTieu);

    @Insert
    void themNhieuChiTieu(List<ChiTieu> chiTieuList);

    @Query("SELECT * FROM tb_expense ORDER BY exp_date ASC")
    List<ChiTieu> xuatChiTieu();

    @Query("SELECT * FROM tb_expense ORDER BY exp_date ASC")
    LiveData<List<ChiTieu>> xuatChiTieuLive();

    @Query("SELECT * FROM tb_expense ORDER BY exp_date ASC,exp_id ASC limit 5")
    LiveData<List<ChiTieu>> xuatChiTieuLiveLimit5();

    @Query("SELECT * FROM tb_expense WHERE exp_type LIKE 1")
    LiveData<List<ChiTieu>> xuatKhoanThuLive();

    @Query("SELECT * FROM tb_expense WHERE exp_type LIKE 2")
    LiveData<List<ChiTieu>> xuatKhoanChiLive();
}
