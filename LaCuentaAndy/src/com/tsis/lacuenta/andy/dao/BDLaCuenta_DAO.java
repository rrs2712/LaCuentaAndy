package com.tsis.lacuenta.andy.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import android.widget.Toast;

import com.tsis.lacuenta.andy.db.DBHelper;

public class BDLaCuenta_DAO {

	private Context context;
	private static final String bdDateFormat = "yyyy-MM-dd HH:mm:ss";
	
	public BDLaCuenta_DAO(Context context){
		this.context = context;
	}
	
	public boolean insert(double monto, int personas, double propina, double montoPersonal){
		
		DBHelper dbHelper = new DBHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		
		try{
			ContentValues values = new ContentValues();
			values.put(DBHelper.FECHA, getTimeStamp()); //ToDo 
			values.put(DBHelper.MONTO, String.valueOf(monto));
			values.put(DBHelper.PERSONAS, String.valueOf(personas));
			values.put(DBHelper.PROPINA, String.valueOf(propina));
			values.put(DBHelper.MONTO_PERSONAL, String.valueOf(montoPersonal));
			
			db.insert(DBHelper.TABLE_NAME, null, values);
			db.close();
			return true;
		} catch(SQLiteException ex){
			Log.v("Insert into database exception caught: ", ex.getMessage());
			Toast.makeText(context, "Problema de insercion:\n" + ex.getMessage(), Toast.LENGTH_LONG).show();
		}
		
		return false;
	}
	

	public String consultaTodo(){
		String respuesta ="";
		
//      CREAMOS UNA NUEVA CONEXION PARA LEER DATOS DE LA BD
		DBHelper dbHelper = new DBHelper(context);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
//		CREAMOS UN ARREGLO CON LAS COLUMNAS QUE RECUPERAREMOS DE LA TABLA CUENTAS
		String[] columnas = new String[]{
    		  "_id",
    		  DBHelper.FECHA,
    		  DBHelper.MONTO,
    		  DBHelper.PERSONAS,
    		  DBHelper.PROPINA,
    		  DBHelper.MONTO_PERSONAL};
		
//		ESTE CURSOR ES EQUIVALENTE A "SELECT CAMPOS FROM CUENTAS WHERE FECHA=FECHA QUE RECIBIMOS DEL URI"
//		Cursor c = db.query(DBHelper.TABLE_NAME, columnas, DBHelper.LINE_NUMBER + "='L03'", null, null, null, "_id" + " ASC ");//LIMIT 10
		Cursor c = db.query(DBHelper.TABLE_NAME, columnas, null, null, null, null, "_id" + " ASC ");
      
//		RECUPERAMOS LOS INDICES DE LAS COLUMNAS QUE NOS ARROJO LA CONSULTA ANTERIOR
//		int indexID=c.getColumnIndex("_id");
		int indexFecha=c.getColumnIndex(DBHelper.FECHA);
		int indexMonto=c.getColumnIndex(DBHelper.MONTO);
		int indexPersonas=c.getColumnIndex(DBHelper.PERSONAS);
		int indexPropina=c.getColumnIndex(DBHelper.PROPINA);
		int indexMontoPersonal=c.getColumnIndex(DBHelper.MONTO_PERSONAL);
		
		for (c.moveToFirst();!c.isAfterLast();c.moveToNext()){      			
			respuesta=respuesta 
//				+ c.getString(indexID)  + "-"
				+ c.getString(indexFecha) + "*"
				+ c.getString(indexMonto) + "*"
				+ c.getString(indexPersonas) + "*"
				+ c.getString(indexPropina) + "*"
				+ c.getString(indexMontoPersonal) + "\n";
		}
		
//		CERRAMOS LA CONEXION
		db.close();
		
		return respuesta;
	}
	
	public String deleteAll(){
		
//      CREAMOS UNA NUEVA CONEXION PARA LEER DATOS DE LA BD
		DBHelper dbHelper = new DBHelper(context);
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		try {
			db.delete(DBHelper.TABLE_NAME, null, null);
		} catch (Exception e) {
			return "El pex: " + e.getMessage();
		}
		
		return "exito!";
	}
	
	private String getTimeStamp() {
		
		Locale us = new Locale(Locale.US.getLanguage(), Locale.US.getCountry());
		Date currentDate = new Date();
		SimpleDateFormat f = new SimpleDateFormat(bdDateFormat, us);
		return f.format(currentDate);
	}
}
