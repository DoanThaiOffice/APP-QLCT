package com.hynguyen.chitieucanhan.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.hynguyen.chitieucanhan.mdel.ChiTieu;
import com.hynguyen.chitieucanhan.mdel.DanhMuc;
import com.hynguyen.chitieucanhan.mdel.LoaiTienTe;
import com.hynguyen.chitieucanhan.mdel.ViTien;

@Database(entities = {DanhMuc.class, LoaiTienTe.class, ViTien.class, ChiTieu.class}, version = 1, exportSchema = false)
@TypeConverters(Converter.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DanhMucDao danhMucDao();

    public abstract LoaiTienTeDao loaiTienTeDao();

    public abstract ViTienDao viTienDao();

    public abstract ChiTieuDao chiTieuDao();

    private static volatile AppDatabase INSTANCE;

    static AppDatabase getInstance(Context context) {

        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "database")
                            .createFromAsset("database/database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
