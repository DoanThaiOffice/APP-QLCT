package com.hynguyen.chitieucanhan.mdel;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tb_wallet")
public class ViTien {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "wallet_id")
    private int id;

    @ColumnInfo(name = "wallet_img")
    private String img;
    @ColumnInfo(name = "wallet_name")
    private String name;

    @ColumnInfo(name = "wallet_money")
    private String money;

    public ViTien() {
    }

    public ViTien(String img, String name, String money) {
        this.img = img;
        this.name = name;
        this.money = money;
    }

    public ViTien(int id, String img, String name, String money) {
        this.id = id;
        this.img = img;
        this.name = name;
        this.money = money;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
