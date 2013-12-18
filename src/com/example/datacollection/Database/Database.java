package com.example.datacollection.Database;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Database {
	private static DatabaseHelper my_helper;
	private static SQLiteDatabase database;
	private Cursor cursor;

	/**
	 * Constructor for Database. Constructor sets up my_helper, which is static
	 * 
	 * @param context
	 */
	public Database(Context context) {
		if (my_helper == null) {
			my_helper = new DatabaseHelper(context);
		}

	}

	/**
	 * If the database is null or is not open, then open it. Else do nothing
	 */
	public void open() {

		if (database == null || !database.isOpen())
			database = my_helper.getWritableDatabase();
	}

	/**
	 * If the database is not null and is open, then close it. Else do nothing.
	 */
	public void close() {
		if (database != null && database.isOpen())
			my_helper.close();
	}

	/**
	 * Function sees if there is a user that has email. Note: this function does
	 * the open and closes of the database. So user does not need to do it.
	 * Note: parameter is all lowercase
	 * 
	 * @param String
	 *            email, represents userId
	 * @return true if user exists, false otherwise.
	 */
	public boolean seeIfUserExists(String email) {
		email = email.toLowerCase();
		open();
		Cursor c = database.query(DatabaseHelper.DATABASE_TABLE_USERS,
				new String[] { DatabaseHelper.KEY_USER_ID },
				DatabaseHelper.KEY_USER_ID + " = ?", new String[] { email },
				null, null, null);
		boolean returnValue = c.moveToFirst();
		c.close();
		close();

		return returnValue;
	}

	/**
	 * Checks if password is correct for user. Database is open and closed in
	 * this function. If there exists a user with email and password
	 * 
	 * @param email
	 *            : String password : String
	 * @return True if email is associated with password. False otherwise
	 */
	public boolean ableToLogin(String email, String password) {
		open();
		email = email.toLowerCase();
		String[] columns = new String[] { DatabaseHelper.KEY_USER_ID,
				DatabaseHelper.KEY_PASSWORD };
		Cursor c = database.query(DatabaseHelper.DATABASE_TABLE_USERS, columns,
				DatabaseHelper.KEY_USER_ID + " = ? AND "
						+ DatabaseHelper.KEY_PASSWORD + " = ?", new String[] {
						email, password }, null, null, null);
		boolean value = c.moveToFirst();
		boolean t = seeIfUserExists(email);
		c.close();
		close();
		return value;
	}

	/**
	 * Saves a user to database. Returns -1 if error, else returns RowID.
	 * 
	 * @param email
	 * @param password
	 * @return long
	 */
	public long saveUser(String email, String password) {
		open();
		email.toLowerCase();
		ContentValues values = new ContentValues();

		String[] user_data = { email, password };
		// ORDER MATTERS
		String[] columns = { DatabaseHelper.KEY_USER_ID,
				DatabaseHelper.KEY_PASSWORD };

		// put user_data into values so we can save it to database.
		int i = 0;
		for (String string : columns) {
			values.put(columns[i], user_data[i]);
			++i;
		}

		long id = database.insert(DatabaseHelper.DATABASE_TABLE_USERS, null,
				values);
		close();
		return id;

	}

	/**
	 * Deletes user and user's data from all the tables. If user does not exist
	 * return true. If user exists, then we delete them. If the user has no
	 * features, then we return true. If the user exists and has data, we delete
	 * data and user. Return true if the number of rows deleted > 0.
	 * 
	 * @param email
	 * @param password
	 */
	public boolean deleteUser(String email, String password) {
		// first case: user doesn't exist
		if (!ableToLogin(email, password)) {
			return true;
		}
		open();
		String[] where = { email.toLowerCase(), password };
		int user_deleted = database.delete(DatabaseHelper.DATABASE_TABLE_USERS,
				DatabaseHelper.KEY_USER_ID + "= ? AND "
						+ DatabaseHelper.KEY_PASSWORD + " = ?", where);
		close();
		if (user_deleted == 0) {
			return false;
		} else {
			if (!hasData(email)) {
				// if user exists but has not trained or tested
				// Note: hasData function handles the open and close of
				// database.
				return true;
			}
			open();
			int test_rows_deleted = database.delete(
					DatabaseHelper.DATABASE_TEST_FEATURES,
					DatabaseHelper.KEY_USER_ID + "= ?", where);
			int train_rows_deleted = database.delete(
					DatabaseHelper.DATABASE_TRAIN_FEATURES,
					DatabaseHelper.KEY_USER_ID + "= ?", where);
			int raw_rows_deleted = database.delete(
					DatabaseHelper.DATABASE_RAW_DATA,
					DatabaseHelper.KEY_USER_ID + "= ?", where);
			close();
			// if the number of total rows deleted == 0, return false.
			return (test_rows_deleted + train_rows_deleted + raw_rows_deleted == 0);
		}

	}

	public boolean hasData(String email) {

		return (hasTestData(email) || hasTrainData(email));

	}

	private boolean hasTrainData(String email) {

		email = email.toLowerCase();
		open();
		String[] columns = new String[] { DatabaseHelper.KEY_USER_ID };
		Cursor c = database.query(DatabaseHelper.DATABASE_TRAIN_FEATURES,
				columns, DatabaseHelper.KEY_USER_ID + " = ?",
				new String[] { email }, null, null, null);
		boolean value = c.moveToFirst();
		c.close();
		close();
		return value;

	}

	private boolean hasTestData(String email) {
		email = email.toLowerCase();
		open();
		String[] columns = new String[] { DatabaseHelper.KEY_USER_ID };
		Cursor c = database.query(DatabaseHelper.DATABASE_TEST_FEATURES,
				columns, DatabaseHelper.KEY_USER_ID + " = ?",
				new String[] { email }, null, null, null);
		boolean value = c.moveToFirst();
		c.close();
		close();
		return value;

	}

	/**
	 * Returns the password as a string. Returns null if no password to be
	 * found.
	 * 
	 * @param email
	 * @return
	 */
	public String getPassword(String email) {
		email = email.toLowerCase();
		open();
		Cursor c = database.query(DatabaseHelper.DATABASE_TABLE_USERS,
				new String[] { DatabaseHelper.KEY_USER_ID,
						DatabaseHelper.KEY_PASSWORD },
				DatabaseHelper.KEY_USER_ID + " = ?", new String[] { email },
				null, null, null);

		if (c.moveToFirst()) {
			int c_index = c.getColumnIndex(DatabaseHelper.KEY_PASSWORD);
			String password = c.getString(c_index);
			c.close();
			close();
			return password;
		} else {
			c.close();
			close();
			return null;
		}

	}

	/**
	 * Same as deleteUser(email, password)
	 * 
	 * @param email
	 * @return
	 */
	public boolean deleteUser(String email) {
		String password = null;
		if ((password = getPassword(email)) == null) {
			return true;
		} else {
			return deleteUser(email, password);
		}
	}

	/**
	 * Save a list of features which is identified by userID, the email. Table
	 * can be either the testing or training table. Handles test, training table
	 * or raw table. Features does not include Activity or TimeStamp.
	 * 
	 * @param email
	 * @param features
	 * @param table
	 * @return -1 if error, else long
	 */
	public long saveData(String email, List<Float> features, String Activity,
			String table, long time) {
		if (features == null || Activity == null) {
			return -1;
		}
		open();
		email.toLowerCase();
		ContentValues values = new ContentValues();
		String[] cols = setupCols(table);

		values.put(cols[0], email);
		values.put(cols[1], time);
		int i = 2;
		for (Float reading : features) {
			values.put(cols[i], reading);
			++i;
		}

		values.put(cols[cols.length - 1], Activity);
		long id = database.insert(table, null, values);
		close();
		return id;

	}

	private String[] setupCols(String table) {
		if (DatabaseHelper.DATABASE_RAW_DATA.compareTo(table) != 0) {
			String[] columns = { DatabaseHelper.KEY_USER_ID,
					DatabaseHelper.KEY_COL_TIME, DatabaseHelper.KEY_COL_MEANX,
					DatabaseHelper.KEY_COL_MEANY, DatabaseHelper.KEY_COL_MEANZ,
					DatabaseHelper.KEY_COL_VARIANCEX,
					DatabaseHelper.KEY_COL_VARIANCEY,
					DatabaseHelper.KEY_COL_VARIANCEZ,
					DatabaseHelper.KEY_COL_CORRX, DatabaseHelper.KEY_COL_CORRY,
					DatabaseHelper.KEY_COL_CORRZ,
					DatabaseHelper.DATABASE_COL_Activity };
			return columns;
		} else {

			String[] columns = { DatabaseHelper.KEY_USER_ID,
					DatabaseHelper.KEY_COL_TIME, DatabaseHelper.KEY_X,
					DatabaseHelper.KEY_Y, DatabaseHelper.KEY_Z,
					DatabaseHelper.DATABASE_COL_Activity };
			return columns;
		}
	}

	public List<String[]> retrieveRows(String email, String table) {
		String[] cols = setupCols(table);
		open();
		Cursor cursor = database.query(table, cols, DatabaseHelper.KEY_USER_ID
				+ " = ?", new String[] { DatabaseHelper.KEY_USER_ID }, null,
				null, DatabaseHelper.KEY_COL_TIME + "ASC");
		if (cursor == null)
			return null;
		if (cursor.moveToFirst()) {
			String[] colNames = cursor.getColumnNames();
			List<String[]> listOfRows = null;
			int colLength = cursor.getColumnCount();
			while (!cursor.isAfterLast()) {
				String[] row = new String[colLength];
				for (int i = 0; i < colLength; i++) {
					row[i] = cursor.getString(i);
				}
				listOfRows.add(row);
				cursor.moveToNext();
			}
			close();
			cursor.close();
			return listOfRows;
		} else {
			List<String[]> list = new ArrayList<String[]>(1);
			list.add(new String[] { "NO DATA" });
			close();
			cursor.close();
			return list;
		}
		/*
		 * StringBuffer buffer = new StringBuffer(); NumberFormat formatter =
		 * NumberFormat.getInstance(); formatter.setGroupingUsed(false);
		 * formatter.setMinimumFractionDigits(7);
		 */
		/*
		 * if (cursor.moveToFirst()) { int i = cursor.getColumnCount(); while
		 * (cursor.isAfterLast() == false) { // start at 1 because the very
		 * first element is actually the row // number for (int j = 1; j < i -
		 * 1; j++) { buffer.append(formatter.format(cursor.getFloat(j)));
		 * buffer.append(","); } // this appends the classification
		 * buffer.append(cursor.getString(i - 1)); buffer.append("\n");
		 * cursor.moveToNext(); index++; } }
		 */

	}

	public void changeUserNameAndPwd(String newEmail, String newPwd,
			String oldEmail) {

		String oldPwd = getPassword(oldEmail);

		updateId(newEmail, oldEmail);
		updatePwd(newPwd, newEmail, oldPwd);

	}

	public void updateId(String newEmail, String oldEmail) {
		String[] tables = { DatabaseHelper.DATABASE_TABLE_USERS,
				DatabaseHelper.DATABASE_TEST_FEATURES,
				DatabaseHelper.DATABASE_TRAIN_FEATURES };

		ContentValues cv = new ContentValues();
		cv.put(DatabaseHelper.KEY_USER_ID, newEmail);
		open();
		for (String table : tables) {
			database.update(table, cv, DatabaseHelper.KEY_USER_ID + " = ?",
					new String[] { oldEmail });
		}
		close();
	}

	public void updatePwd(String newPwd, String newEmail, String oldPwd) {
		if (oldPwd == null) {
			return;
		}
		ContentValues cv = new ContentValues();
		cv.put(DatabaseHelper.KEY_PASSWORD, newPwd);
		open();

		database.update(DatabaseHelper.DATABASE_TABLE_USERS, cv,
				DatabaseHelper.KEY_USER_ID + " = ? AND "
						+ DatabaseHelper.KEY_PASSWORD + " = ?", new String[] {
						newEmail, oldPwd });

		close();
	}
}
