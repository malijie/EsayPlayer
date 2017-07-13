package com.easy.player.database;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by malijie on 2017/7/13.
 */

public class DBHelper<T> {

    public void create(T po){
        SQLiteHelperOrm db = new SQLiteHelperOrm();
        Dao dao = null;
        try {
            dao = db.getDao(po.getClass());
            dao.create(po);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(dao != null){
                db.close();
                db = null;
            }
        }
    }

    public void remove(T po){
        SQLiteHelperOrm db = new SQLiteHelperOrm();
        Dao dao = null;
        try {
            dao = db.getDao(po.getClass());
            dao.delete(po);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(dao != null){
                db.close();
                dao = null;
            }
        }
    }

    public void update(T po){
        SQLiteHelperOrm db = new SQLiteHelperOrm();
        Dao dao = null;
        try {
            dao = db.getDao(po.getClass());
            dao.update(po);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(dao != null){
                db.close();
                dao = null;
            }
        }
    }

    public List<T> queryForAll(Class<T> c){
        SQLiteHelperOrm db = new SQLiteHelperOrm();
        List<T> list = null;
        Dao dao = null;
        try {
            dao = db.getDao(c);
            list = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if(dao != null){
                db.close();
                db = null;
            }
        }

        return list;
    }

    public boolean exists(T po, Map<String, Object> where) {
        SQLiteHelperOrm db = new SQLiteHelperOrm();
        try {
            Dao dao = db.getDao(po.getClass());
            if (dao.queryForFieldValues(where).size() > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (db != null)
                db.close();
        }
        return false;
    }

}
