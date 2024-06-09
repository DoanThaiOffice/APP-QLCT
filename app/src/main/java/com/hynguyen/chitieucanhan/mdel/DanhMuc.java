package com.hynguyen.chitieucanhan.mdel;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tb_category")
public class DanhMuc {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "cat_id")
    private int id;
    @ColumnInfo(name = "cat_name")
    private String tenDanhMuc;
    @ColumnInfo(name = "cat_img")
    private String hinhAnh;
    @ColumnInfo(name = "cat_type")
    private int loaiDanhMuc;

    public DanhMuc() {
    }

    public DanhMuc(int id, String tenDanhMuc, String hinhAnh, int loaiDanhMuc) {
        this.id = id;
        this.tenDanhMuc = tenDanhMuc;
        this.hinhAnh = hinhAnh;
        this.loaiDanhMuc = loaiDanhMuc;
    }

    public DanhMuc(String tenDanhMuc, String hinhAnh, int loaiDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
        this.hinhAnh = hinhAnh;
        this.loaiDanhMuc = loaiDanhMuc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenDanhMuc() {
        return tenDanhMuc;
    }

    public void setTenDanhMuc(String tenDanhMuc) {
        this.tenDanhMuc = tenDanhMuc;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public int getLoaiDanhMuc() {
        return loaiDanhMuc;
    }

    public void setLoaiDanhMuc(int loaiDanhMuc) {
        this.loaiDanhMuc = loaiDanhMuc;
    }
}
