package Models.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;

import Models.Entities.MRTDRecord;

/**
 * Created by billy on 24-5-2017.
 */

public class DbHelper extends SQLiteOpenHelper{
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "PassportScanner";
    // Contacts table name
    private static final String TABLE_MRTDRECORDS = "MRTDRecords";

    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_FIRSTNAME = "firstname";
    private static final String KEY_LASTNAME = "lastname";
    private static final String KEY_ISSUER = "issuer";
    private static final String KEY_NATIONALITY = "nationality";
    private static final String KEY_DATEOFBIRTH = "dateofbirth";
    private static final String KEY_DOCUMENTNUMBER = "documentnumber";
    private static final String KEY_SEX = "sex";
    private static final String KEY_DOCUMENTCODE = "documentcode";
    private static final String KEY_DATEOFEXPIRY = "dateofexpiry";
    private static final String KEY_OPT1 = "opt1";
    private static final String KEY_OPT2 = "opt2";
    private static final String KEY_MRZTEXT = "mrztext";
    private static final String KEY_DATETIMESTAMP = "datetimestamp";
    private static final String KEY_NOTE = "note";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_MRTDRECORDS_TABLE = "CREATE TABLE " + TABLE_MRTDRECORDS + "("
        + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_FIRSTNAME + " TEXT,"
                + KEY_LASTNAME + " TEXT,"
                + KEY_ISSUER + " TEXT,"
                + KEY_NATIONALITY + " TEXT,"
                + KEY_DATEOFBIRTH + " TEXT,"
                + KEY_DOCUMENTNUMBER + " TEXT,"
                + KEY_SEX + " TEXT,"
                + KEY_DOCUMENTCODE + " TEXT,"
                + KEY_DATEOFEXPIRY + " TEXT,"
                + KEY_OPT1 + " TEXT,"
                + KEY_OPT2 + " TEXT,"
                + KEY_MRZTEXT + " TEXT,"
                + KEY_NOTE + " TEXT,"
        + KEY_DATETIMESTAMP + " TEXT" + ")";
        db.execSQL(CREATE_MRTDRECORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MRTDRECORDS);
        // Creating tables again
        onCreate(db);
    }

    public long addMrtdRecord (MRTDRecord mrtdRecord){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_DATEOFBIRTH, mrtdRecord.getDateOfBirth());
        values.put(KEY_DATEOFEXPIRY, mrtdRecord.getDateOfExpiry());
        values.put(KEY_DOCUMENTCODE, mrtdRecord.getDocumentCode());
        values.put(KEY_DOCUMENTNUMBER, mrtdRecord.getDocumentNumber());
        values.put(KEY_FIRSTNAME, mrtdRecord.getFirstName());
        values.put(KEY_ISSUER, mrtdRecord.getIssuer());
        values.put(KEY_LASTNAME, mrtdRecord.getLastName());
        values.put(KEY_MRZTEXT, mrtdRecord.getMrzText());
        values.put(KEY_NATIONALITY, mrtdRecord.getNationality());
        values.put(KEY_SEX, mrtdRecord.getSex());
        values.put(KEY_OPT1, mrtdRecord.getOpt1());
        values.put(KEY_OPT2, mrtdRecord.getOpt2());
        values.put(KEY_MRZTEXT, mrtdRecord.getMrzText());
        values.put(KEY_DATETIMESTAMP, mrtdRecord.getDateTimeStamp().getTime());

        long id = db.insert(TABLE_MRTDRECORDS, null, values);
        db.close();
        return id;
    }

    public MRTDRecord getMrtdRecord(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MRTDRECORDS, new String[] {KEY_ID, KEY_FIRSTNAME, KEY_LASTNAME }, KEY_ID + "=?",
                new String[] {String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        MRTDRecord mrtdRecord = new MRTDRecord(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));

        return mrtdRecord;
    }

    public int getMrtdRecordsCount() {
    String countQuery = "select * from " + TABLE_MRTDRECORDS;
    SQLiteDatabase db = this.getReadableDatabase();
    Cursor cursor = db.rawQuery(countQuery, null);
    int count = cursor.getCount();
    cursor.close();
        return count;
    }

    public ArrayList<MRTDRecord> getAllMrtdRecords() {
        ArrayList<MRTDRecord> scannedList = new ArrayList<MRTDRecord>();

        String selectQuery = "SELECT * FROM " + TABLE_MRTDRECORDS;
        SQLiteDatabase db = this.getWritableDatabase();
        this.deleteExpiredRecords();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                MRTDRecord mrtdRecord = new MRTDRecord();
                mrtdRecord.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                mrtdRecord.setFirstName(cursor.getString(cursor.getColumnIndex(KEY_FIRSTNAME)));
                mrtdRecord.setLastName(cursor.getString(cursor.getColumnIndex(KEY_LASTNAME)));
                mrtdRecord.setDateOfBirth(cursor.getString(cursor.getColumnIndex(KEY_DATEOFBIRTH)));
                mrtdRecord.setDateOfExpiry(cursor.getString(cursor.getColumnIndex(KEY_DATEOFEXPIRY)));
                mrtdRecord.setDocumentCode(cursor.getString(cursor.getColumnIndex(KEY_DOCUMENTCODE)));
                mrtdRecord.setDocumentNumber(cursor.getString(cursor.getColumnIndex(KEY_DOCUMENTNUMBER)));
                mrtdRecord.setIssuer(cursor.getString(cursor.getColumnIndex(KEY_ISSUER)));
                mrtdRecord.setMrzText(cursor.getString(cursor.getColumnIndex(KEY_MRZTEXT)));
                mrtdRecord.setNationality(cursor.getString(cursor.getColumnIndex(KEY_NATIONALITY)));
                mrtdRecord.setOpt1(cursor.getString(cursor.getColumnIndex(KEY_OPT1)));
                mrtdRecord.setOpt2(cursor.getString(cursor.getColumnIndex(KEY_OPT2)));
                mrtdRecord.setSex(cursor.getString(cursor.getColumnIndex(KEY_SEX)));
                Date date = new Date(cursor.getLong(cursor.getColumnIndex(KEY_DATETIMESTAMP)));
                mrtdRecord.setDateTimeStamp(date);
                mrtdRecord.setNote(cursor.getString(cursor.getColumnIndex(KEY_NOTE)));

                scannedList.add(mrtdRecord);
            } while (cursor.moveToNext());
        }
        Collections.sort(scannedList);
        return scannedList;

    }

    public int updateMrtdRecord(MRTDRecord mrtdRecord) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DATEOFBIRTH, mrtdRecord.getDateOfBirth());
        values.put(KEY_DATEOFEXPIRY, mrtdRecord.getDateOfExpiry());
        values.put(KEY_DOCUMENTCODE, mrtdRecord.getDocumentCode());
        values.put(KEY_DOCUMENTNUMBER, mrtdRecord.getDocumentNumber());
        values.put(KEY_FIRSTNAME, mrtdRecord.getFirstName());
        values.put(KEY_ISSUER, mrtdRecord.getIssuer());
        values.put(KEY_LASTNAME, mrtdRecord.getLastName());
        values.put(KEY_MRZTEXT, mrtdRecord.getMrzText());
        values.put(KEY_NATIONALITY, mrtdRecord.getNationality());
        values.put(KEY_SEX, mrtdRecord.getSex());
        values.put(KEY_OPT1, mrtdRecord.getOpt1());
        values.put(KEY_OPT2, mrtdRecord.getOpt2());
        values.put(KEY_MRZTEXT, mrtdRecord.getMrzText());
        values.put(KEY_DATETIMESTAMP, mrtdRecord.getDateTimeStamp().getTime());
        values.put(KEY_NOTE, mrtdRecord.getNote());
        return db.update(TABLE_MRTDRECORDS, values, KEY_ID + "= ?", new String[]{String.valueOf(mrtdRecord.getId())});
    }

    public void deleteMrtdRecord(MRTDRecord mrtdRecord) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_MRTDRECORDS, KEY_ID + " = ?", new String[] {String.valueOf(mrtdRecord.getId())});
        db.close();
    }

    public void deleteExpiredRecords() {
        SQLiteDatabase db = getWritableDatabase();
        String sql = "DELETE FROM " + TABLE_MRTDRECORDS + " WHERE " + KEY_DATETIMESTAMP + " < '" + (System.currentTimeMillis() - 64800000) + "'";
        db.execSQL(sql);
    }

    public String getNote(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MRTDRECORDS, new String[] {KEY_ID, KEY_NOTE }, KEY_ID + "=?",
                new String[] {String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        String note = cursor.getString(1);

        return note;
    }
}

