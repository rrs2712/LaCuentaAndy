package com.tsis.lacuenta.andy.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{

//	DATABASE
	private static final String DATABASE_NAME="LaCuenta";
	private static final int DATABASE_VERSION=2; //AddressMe v16 old data base
	
//	TABLA CUENTAS
	public static final String TABLE_NAME="Cuentas";
	public static final String FECHA="Fecha";
	public static final String MONTO="Monto";
	public static final String PERSONAS="Personas";
	public static final String PROPINA="Propina";
	public static final String MONTO_PERSONAL="TipoCTa";
	
//	CADENA PARA CREAR TABLA LINEAS
	private static final String 
		CREATE_CUENTAS_TABLE="CREATE TABLE " 	+
		TABLE_NAME 	+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
		FECHA + " TEXT, " +
		MONTO + " TEXT, " +
		MONTO_PERSONAL  + " TEXT, " +
		PERSONAS	+ " TEXT, " +
		PROPINA 	+ " TEXT );";
	
	public DBHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

//	AQUI SE VA A CREAR LA BASE DE DATOS SI NO EXISTE,
//	TAMBIEN LAS TABLAS, INDICES,ETC.
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_CUENTAS_TABLE);
	}

//	ESTE METODO SE LLAMA CUANDO SE ACTUALIZA LA VERSION
//	DE LA BASE DE DATOS
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.v(TABLE_NAME, "Upgrading from version " + oldVersion + " to " + newVersion + ", wich will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME + "." + TABLE_NAME);
		onCreate(db);
	}

}
