package com.hynguyen.chitieucanhan.mdel;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "tb_expense")
public class ChiTieu {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "exp_id")
    private int id;

    @ColumnInfo(name = "exp_note")
    private String note;

    @ColumnInfo(name = "exp_money")
    private String money;

    @ColumnInfo(name = "exp_date")
    private Date date;

    @ColumnInfo(name = "exp_type")
    private int type;

    @ColumnInfo(name = "exp_cat")
    private int category;

    @ColumnInfo(name = "exp_wallet")
    private int wallet;

    public ChiTieu() {
    }

    public ChiTieu(String note, String money, Date date, int type, int category, int wallet) {
        this.note = note;
        this.money = money;
        this.date = date;
        this.type = type;
        this.category = category;
        this.wallet = wallet;
    }

    public ChiTieu(int id, String note, String money, Date date, int type, int category, int wallet) {
        this.id = id;
        this.note = note;
        this.money = money;
        this.date = date;
        this.type = type;
        this.category = category;
        this.wallet = wallet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }
}
