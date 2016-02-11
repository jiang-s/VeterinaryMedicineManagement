package com.js.vmm.data;

import java.util.ArrayList;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class Dao {

	public static String dbpath = null;

	public void initDB(SQLiteDatabase db) {
		// ִ��DDL�������ݱ�
		db.execSQL("create table record_inf(_id integer primary key autoincrement,"
				+ " name varchar(255)," + " date varchar(255))");
		for (int i = 0; i < 10; i++) {
			db.execSQL("insert into record_inf values(null , ? , ?)",
					new String[] { "����", "0" });
		}
	}
	
	public void initDB() {
		// ִ��DDL�������ݱ�
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbpath, null);
		db.execSQL("create table record_inf(_id integer primary key autoincrement,"
				+ " name varchar(255)," + " date varchar(255))");
		for (int i = 0; i < 10; i++) {
			db.execSQL("insert into record_inf values(null , ? , ?)",
					new String[] { "����", "0" });
		}
	}

	public void insertData(Record record) {
		// ִ�в������
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbpath, null);
		try {
			db.execSQL("insert into record_inf values(null , ? , ?)",
					new String[] { record.getName(), record.getScore() });
		} catch (SQLiteException se) {
			initDB(db);
			// ִ��insert����������
			db.execSQL("insert into record_inf values(null , ? , ?)",
					new String[] { record.getName(), record.getScore() });
		} finally {
			db.close();
		}
	}

	public ArrayList<Record> getAll() {
		ArrayList<Record> list = new ArrayList<Record>();
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbpath, null);
		try {
			Cursor cur = db.rawQuery("select * from record_inf", null);
			if (cur.moveToFirst() == true)
				for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
					Record record = new Record();
					record.setNum(Integer.parseInt(cur.getString(0)));
					record.setName(cur.getString(1));
					record.setScore(cur.getString(2));
					list.add(record);
				}
		} catch (SQLiteException se) {
			initDB(db);
			Cursor cur = db.rawQuery("select * from record_inf", null);
			if (cur.moveToFirst() == true)
				for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
					Record record = new Record();
					record.setNum(Integer.parseInt(cur.getString(0)));
					record.setName(cur.getString(1));
					record.setScore(cur.getString(2));
					list.add(record);
				}
		} finally {
			db.close();
		}
		return list;
	}

	public void deleteData(String numToDelete) {
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbpath, null);
		db.execSQL("delete from record_inf where _id=?",
				new String[] { numToDelete });
		db.close();
	}

}
