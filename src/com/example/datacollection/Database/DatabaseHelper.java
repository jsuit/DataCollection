package com.example.datacollection.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	public static final String KEY_COL_MEANX = "MEANX";
	public static final String KEY_COL_MEANY = "MEANY";
	public static final String KEY_COL_MEANZ = "MEANZ";
	public static final String KEY_COL_VARIANCEX = "VARIANCEX";
	public static final String KEY_COL_VARIANCEY = "VARIANCEY";
	public static final String KEY_COL_VARIANCEZ = "VARIANCEZ";
	public static final String KEY_COL_CORRX = "CORRX";
	public static final String KEY_COL_CORRY = "CORRY";
	public static final String KEY_COL_CORRZ = "CORRZ";
	public static final String KEY_COL_TIME = "timestamp";
	public static final String DATABASE_NAME = "DATA_DATABASE";
	public static final String KEY_ROW_ID = "id";
	public static final String KEY_USER_ID = "user_id";
	public static final String KEY_PASSWORD = "key_password";

	private static final int DATABASE_VERSION = 1;
	public static final String DATABASE_TABLE_USERS = "users_table";
	public static final String DATABASE_TEST_FEATURES = "feature_test_table";
	public static final String DATABASE_TRAIN_FEATURES = "feature_train_table";
	public static final String DATABASE_RAW_DATA = "raw_data";
	public static final String DATABASE_COL_Activity = "Activity";

	public static final String KEY_X = "raw_x";
	public static final String KEY_Y = "raw_Y";
	public static final String KEY_Z = "raw_Z";

	private static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS "
			+ DATABASE_TABLE_USERS + "(" + KEY_ROW_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_USER_ID
			+ " TEXT NOT NULL," + KEY_PASSWORD + " TEXT NOT NULL);";

	private static final String CREATE_TEST_DATABASE = "CREATE TABLE IF NOT EXISTS "
			+ DATABASE_TEST_FEATURES + "(" + KEY_ROW_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_USER_ID
			+ " TEXT NOT NULL," + KEY_COL_TIME + " REAL NOT NULL,"
			+ KEY_COL_MEANX + " REAL NOT NULL," + KEY_COL_MEANY
			+ " REAL NOT NULL," + KEY_COL_MEANZ + " REAL NOT NULL,"
			+ KEY_COL_VARIANCEX + " REAL NOT NULL," + KEY_COL_VARIANCEY
			+ " REAL NOT NULL," + KEY_COL_VARIANCEZ + " REAL NOT NULL,"
			+ KEY_COL_CORRX + " REAL NOT NULL," + KEY_COL_CORRY
			+ " REAL NOT NULL," + KEY_COL_CORRZ + " REAL NOT NULL,"
			+ DATABASE_COL_Activity + " TEXT NOT NULL);";

	private static final String CREATE_TRAIN_DATABASE = "CREATE TABLE IF NOT EXISTS "
			+ DATABASE_TRAIN_FEATURES + "(" + KEY_ROW_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_USER_ID
			+ " TEXT NOT NULL," + KEY_COL_TIME + " REAL NOT NULL,"
			+ KEY_COL_MEANX + " REAL NOT NULL," + KEY_COL_MEANY
			+ " REAL NOT NULL," + KEY_COL_MEANZ + " REAL NOT NULL,"
			+ KEY_COL_VARIANCEX + " REAL NOT NULL," + KEY_COL_VARIANCEY
			+ " REAL NOT NULL," + KEY_COL_VARIANCEZ + " REAL NOT NULL,"
			+ KEY_COL_CORRX + " REAL NOT NULL," + KEY_COL_CORRY
			+ " REAL NOT NULL," + KEY_COL_CORRZ + " REAL NOT NULL,"
			+ DATABASE_COL_Activity + " TEXT NOT NULL);";

	private static final String CREATE_RAW_DATABASE = "CREATE TABLE IF NOT EXISTS "
			+ DATABASE_TEST_FEATURES + "(" + KEY_ROW_ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_USER_ID
			+ " TEXT NOT NULL," + KEY_COL_TIME + " REAL NOT NULL," + KEY_X + " REAL NOT NULL," + KEY_Y
			+ " REAL NOT NULL," + KEY_Z + " REAL NOT NULL);";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	
//	int a=	this.getWritableDatabase().delete(DATABASE_TABLE_USERS, null, null);
//	int b=	this.getWritableDatabase().delete(DATABASE_TEST_FEATURES, null, null);
//	int c=	this.getWritableDatabase().delete(DATABASE_TRAIN_FEATURES, null, null);
	//int d = this.getWritableDatabase().delete(DATABASE_RAW_DATA, null, null);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(DATABASE_CREATE);
		db.execSQL(CREATE_TEST_DATABASE);
		db.execSQL(CREATE_TRAIN_DATABASE);
		db.execSQL(CREATE_RAW_DATABASE);
		Log.i("Oncreate for datatabse helper called", "oncreate");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(DatabaseHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_USERS);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_RAW_DATA);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TEST_FEATURES);
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TRAIN_FEATURES);
		onCreate(db);

	}

}
