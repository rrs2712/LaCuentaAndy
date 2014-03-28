/**
 * 
 */
package com.tsis.lacuenta.andy.bo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

	public boolean exportFile() {
//		Log.d(TAG, "Context path: " + this.contexto.getFilesDir().getPath());
		StringBuilder contenido = getFileContent();
		return writeToFile(FILENAME, contenido.toString(), this.contexto);
	}

	private boolean writeToFile(String fileName, String contenido, Context contexto) {
		
		try {
//			File file = new File(contexto.getFilesDir().getPath(), fileName);
//			File file = getPublicDir(fileName);
			
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
//			bw.write(contenido);
			bw.write(contenido.toString());
			bw.close();
			Log.i(TAG, "Archivo escrito correctamente!");
 
		} catch (IOException e) {
			Log.e(TAG, "Error al escribir el archivo: " + e.getMessage(), e);
			return false;
		}

		return true;
	}

//	private boolean writeToFile(String fileName, String data, Context context) {
//		File file = new File(this.context.getFilesDir().getPath(),
//				this.FILENAME);
//
//		try {
//			if (!file.exists()) {
//				file.createNewFile();
//				Log.d(TAG, "El archivo no existia");
//			}
//
//			FileOutputStream outputStream = openFileOutput(fileName,Context.MODE_PRIVATE);
//			outputStream.write(data.getBytes());
//			outputStream.close();
//		} catch (Exception e) {
////			e.printStackTrace();
//			Log.e(TAG, "File write failed: " + e.toString());
//			return false;
//		}
//
//		return true;
//	}
	
	private StringBuilder getFileContent() {
		BDLaCuenta_DAO bd = new BDLaCuenta_DAO(this.contexto);
		List<CtaHistorial_DTO> cuentas = bd.getCtasByDate(new Date(), new Date());
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

	public File getPublicDir(String newDirName) {
	    Log.d(TAG, "Se puede escribir externo? " + isExternalStorageWritable());
		
	    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), newDirName);
	    Log.d(TAG, "Dir que se pretende crear =" + file.getAbsolutePath());
	    
	    if (!file.mkdirs()) {
	        Log.e(TAG, "Directory not created = " + file.getAbsolutePath());
	    }
	    return file;
	}
	
	public String dirMain() {
		Log.d(TAG, "Entra al dir Main: " + this.contexto.getFilesDir().getPath());
		return this.contexto.getFilesDir().getPath();
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
