/**
 * 
 */
package com.tsis.lacuenta.andy.bo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import com.tsis.lacuenta.andy.dao.BDLaCuenta_DAO;
import com.tsis.lacuenta.core.dto.CtaHistorial_DTO;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

/**
 * @author asus
 * 
 */
public class Export extends Activity {

	private final String TAG = Export.class.getName();
	private final String FILENAME = "LaCuenta.csv";
	private final String FOLDER = "/LaCuenta/";
	private final String CABECERO = "FECHA, CUENTA, PERSONAS, PROPINA, CTA. INDIVIDUAL,";
	private final StringBuilder SALTO_LINEA = new StringBuilder("\n");
	private final StringBuilder NUEVA_CELDA = new StringBuilder(",");
	private Context contexto; //NOTA! No usar el contexto de esta actividad sino la del main que se pide en el constructor

	public Export(Context contexto) {
		this.contexto = contexto;
	}

	public boolean exportByCurrentMonth() {
		Calendar ini = Calendar.getInstance();
		ini.set(Calendar.DAY_OF_MONTH, ini.getActualMinimum(Calendar.DAY_OF_MONTH));
		ini.set(Calendar.HOUR_OF_DAY, 0);
		ini.set(Calendar.MINUTE, 0);
		ini.set(Calendar.SECOND, 0);
		
		Calendar fin = Calendar.getInstance();
		fin.set(Calendar.DAY_OF_MONTH, fin.getActualMaximum(Calendar.DAY_OF_MONTH));
		fin.set(Calendar.HOUR_OF_DAY, 23);
		fin.set(Calendar.MINUTE, 59);
		fin.set(Calendar.SECOND, 59);
		
		
		StringBuilder contenido = getFileContent(ini.getTime(), fin.getTime());
		return writeToFile(FILENAME, contenido.toString(), this.contexto);
	}
	
	public boolean exportByDate(Calendar ini) {
		ini.set(Calendar.HOUR_OF_DAY, 0);
		ini.set(Calendar.MINUTE, 0);
		ini.set(Calendar.SECOND, 0);
		
		Calendar fin = new GregorianCalendar();
		fin.setTime(ini.getTime());
		fin.add(Calendar.MONTH, 1);
		fin.set(Calendar.HOUR_OF_DAY, 23);
		fin.set(Calendar.MINUTE, 59);
		fin.set(Calendar.SECOND, 59);
		
		StringBuilder contenido = getFileContent(ini.getTime(), fin.getTime());
		return writeToFile(FILENAME, contenido.toString(), this.contexto);
	}

	private boolean writeToFile(String fileName, String contenido, Context contexto) {
		
		try {
			
			File outputPath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + FOLDER);
			
			if (!outputPath.exists()) {
			    Log.d(TAG, "No existe el dir: " + outputPath.getAbsolutePath());
				outputPath.mkdir();
				Log.d(TAG, "Pero ya se creo : " + outputPath.getAbsolutePath());
			}
			
			File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + FOLDER, fileName);
 
			if (!file.exists()) {
				file.createNewFile();
				Log.d(TAG, "El Archivo no existia, asi que se creo:" + file.getAbsolutePath());
			}
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(contenido);
			bw.close();
			Log.i(TAG, "Archivo escrito correctamente!");
 
		} catch (IOException e) {
			Log.e(TAG, "Error al escribir el archivo: " + e.getMessage(), e);
			return false;
		}

		return true;
	}
	
	private StringBuilder getFileContent(Date fechaInicio, Date fechaFin) {
		BDLaCuenta_DAO bd = new BDLaCuenta_DAO(this.contexto);
		List<CtaHistorial_DTO> cuentas = bd.getCtasByDate(fechaInicio, fechaFin);
		Log.i(TAG, "Se obtienen " + cuentas.size() + " cuentas de la BD");
		
		StringBuilder contenido = new StringBuilder(CABECERO);
		
		Locale us = new Locale(Locale.US.getLanguage(), Locale.US.getCountry());
		SimpleDateFormat f = new SimpleDateFormat(BDLaCuenta_DAO.bdDateFormat, us);
		
		for (CtaHistorial_DTO cuenta : cuentas) {
			contenido.append(SALTO_LINEA);
			contenido.append(f.format(cuenta.getFecha()));
			contenido.append(NUEVA_CELDA);
			contenido.append(String.valueOf(cuenta.getMontoCta()));
			contenido.append(NUEVA_CELDA);
			contenido.append(String.valueOf(cuenta.getPersonas()));
			contenido.append(NUEVA_CELDA);
			contenido.append(String.valueOf(cuenta.getPropina()));
			contenido.append(NUEVA_CELDA);
			contenido.append(String.valueOf(cuenta.getMontoxPersona()));
		}
		return contenido;
	}
	
	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

}
