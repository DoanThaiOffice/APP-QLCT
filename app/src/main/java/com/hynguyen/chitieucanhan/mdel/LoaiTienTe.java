package com.hynguyen.chitieucanhan.mdel;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tb_money_type")
public class LoaiTienTe {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "money_id")
    private int id;
    @ColumnInfo(name = "money_name")
    private String name;

    public LoaiTienTe() {
    }

    public LoaiTienTe(String name) {
        this.name = name;
    }

    public LoaiTienTe(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
