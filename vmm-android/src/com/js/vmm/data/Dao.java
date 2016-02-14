package com.js.vmm.data;

import java.util.ArrayList;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class Dao {

	public static String dbpath = null;

	public void initDB(SQLiteDatabase db) {
		// 执行DDL创建数据表
		db.execSQL("create table record_inf(_id integer primary key autoincrement,"
				+ " name varchar(255),"
				+ " date varchar(255),"
				+ " synState varchar(255),"
				+ " barcode varchar(255),"
				+ " inprice INTEGER,"
				+ " outprice INTEGER,"
				+ " specification varchar(255)," + " jsonDesc TEXT)");
	}

	/*
	 * public void initDB() { // 执行DDL创建数据表 SQLiteDatabase db =
	 * SQLiteDatabase.openOrCreateDatabase(dbpath, null);
	 * db.execSQL("create table record_inf(_id integer primary key autoincrement,"
	 * + " name varchar(255)," + " date varchar(255))"); for (int i = 0; i < 10;
	 * i++) { db.execSQL("insert into record_inf values(null , ? , ?)", new
	 * String[] { "匿名", "0" }); } }
	 */

	public void saveData(Record record) {
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbpath, null);
		Cursor cur = db.rawQuery(
				"select * from record_inf where barCode=?",
				new String[] { record.getBarcode() });
		if (cur.moveToFirst() == true && cur.isAfterLast()) {
			insertData(record);
		} else {
			ContentValues cv = new ContentValues();
			cv.put("synState", String.valueOf(record.isSynState()));
			cv.put("inprice", record.getInprice());
			cv.put("outprice", record.getOutprice());
			cv.put("specification", record.getSpecification());
			db.update("record_inf", cv, "barCode = ?", new String[]{record.getBarcode()});
			Log.d("jiangshan", "update");
		}
	}
	
	public int insertData(Record record) {
		int position = -1;
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbpath, null);
		try {
			db.execSQL(
					"insert into record_inf values(null, ?, ?, ?, ?, ?, ?, ?, ?)",
					new String[] { record.getName(), record.getDate(),
							String.valueOf(record.isSynState()),
							record.getBarcode(),
							String.valueOf(record.getInprice()),
							String.valueOf(record.getOutprice()),
							record.getSpecification(), record.getJsonDesc() });

			Cursor cur = db.rawQuery(
					"select * from record_inf where barCode=?",
					new String[] { record.getBarcode() });
			if (cur.moveToFirst() == true) {
				position = Integer.parseInt(cur.getString(0));
				Log.d("jiangshan", "position is: " + position);
			}
		} catch (SQLiteException se) {
			initDB(db);
			// 执行insert语句插入数据
			db.execSQL(
					"insert into record_inf values(null, ?, ?, ?, ?, ?, ?, ?, ?)",
					new String[] { record.getName(), record.getDate(),
							String.valueOf(record.isSynState()),
							record.getBarcode(),
							String.valueOf(record.getInprice()),
							String.valueOf(record.getOutprice()),
							record.getSpecification(), record.getJsonDesc() });
			Cursor cur = db.rawQuery(
					"select * from record_inf where barCode=?",
					new String[] { record.getBarcode() });
			if (cur.moveToFirst() == true) {
				position = Integer.parseInt(cur.getString(0));
				Log.d("jiangshan", "position is: " + position);
			}
		} finally {
			db.close();
		}
		return position;
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
					record.setDate(cur.getString(2));
					record.setSynState(Boolean.getBoolean(cur.getString(3)));
					record.setBarcode(cur.getString(4));
					record.setInprice(cur.getInt(5));
					record.setOutprice(cur.getInt(6));
					record.setSpecification(cur.getString(7));
					record.setJsonDesc(cur.getString(8));
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
					record.setDate(cur.getString(2));
					record.setSynState(Boolean.getBoolean(cur.getString(3)));
					record.setBarcode(cur.getString(4));
					record.setInprice(cur.getInt(5));
					record.setOutprice(cur.getInt(6));
					record.setSpecification(cur.getString(7));
					record.setJsonDesc(cur.getString(8));
					list.add(record);
				}
		} finally {
			db.close();
		}
		return list;
	}

	public void deleteData(String barCode) {
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbpath, null);
		db.execSQL("delete from record_inf where barcode=?",
				new String[] { barCode });
		db.close();
	}

	/*
	 * public void deleteData(String numToDelete) { SQLiteDatabase db =
	 * SQLiteDatabase.openOrCreateDatabase(dbpath, null);
	 * db.execSQL("delete from record_inf where _id=?", new String[] {
	 * numToDelete }); db.close(); }
	 */

	public void updateSynByNum(String barCode) {
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbpath, null);
		db.execSQL("update record_inf set synState='true' where barCode=?",
				new String[] { barCode });
		Log.d("jiangshan", "updateSynByNum");
		
		Cursor cur = db.rawQuery(
				"select * from record_inf where barCode=?",
				new String[] { barCode });
		if (cur.moveToFirst() == true) {
			Log.d("jiangshan", "select by barCode: " + cur.getString(3));
		}
		db.close();
	}

	public Record getRecordByNum(int num) {
		Record record = new Record();
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbpath, null);
		try {
			Cursor cur = db.rawQuery("select * from record_inf where _id=?",
					new String[] { String.valueOf(num) });
			if (cur.moveToFirst() == true) {
				record.setNum(Integer.parseInt(cur.getString(0)));
				record.setName(cur.getString(1));
				record.setDate(cur.getString(2));
				record.setSynState(cur.getString(3).equals("true"));
				record.setBarcode(cur.getString(4));
				record.setInprice(cur.getInt(5));
				record.setOutprice(cur.getInt(6));
				record.setSpecification(cur.getString(7));
				record.setJsonDesc(cur.getString(8));
			}
		} catch (SQLiteException se) {
			initDB(db);
			Cursor cur = db.rawQuery("select * from record_inf where _id=?",
					new String[] { String.valueOf(num) });
			if (cur.moveToFirst() == true) {
				record.setNum(Integer.parseInt(cur.getString(0)));
				record.setName(cur.getString(1));
				record.setDate(cur.getString(2));
				record.setSynState(Boolean.getBoolean(cur.getString(3)));
				record.setBarcode(cur.getString(4));
				record.setInprice(cur.getInt(5));
				record.setOutprice(cur.getInt(6));
				record.setSpecification(cur.getString(7));
				record.setJsonDesc(cur.getString(8));
			}
		} finally {
			db.close();
		}
		return record;
	}

}
