package com.easy.player.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.easy.player.EasyPlayer;
import com.easy.player.entity.POMedia;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by malijie on 2017/7/13.
 */

public class SQLiteHelperOrm extends OrmLiteSqliteOpenHelper{
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "easy_player";

    public SQLiteHelperOrm() {
        super(EasyPlayer.sContext, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, POMedia.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        try {
            TableUtils.dropTable(connectionSource,POMedia.class,true);
            onCreate(sqLiteDatabase,connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
