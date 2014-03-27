package com.tsis.lacuenta.andy.main;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.tsis.lacuenta.andy.dao.BDLaCuenta_DAO;
import com.tsis.lacuenta.core.main.Cuenta;

public class MainActivity extends ActionBarActivity {

	private EditText montoEd, propinaEd;
	private Spinner personasSpn,propinaSpn;
	private ToggleButton redondeoTgg;
	private TextView resulTv;
	
	private double monto=0;
	private int personas=1;
	private double propina=0;
	private int tipoCta=Cuenta.CtaTipProporcional;
	private boolean redondear=false;
	
	private int maxPersonas=100;
	private final double noMonto=0;
	private final double noPropina=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
//		setContentView(R.layout.activity_main);
		setContentView(R.layout.fragment_main);

		addItemsToSpinners();
		addMontoListener();
		addPersonasListener();
		addPropinaPorListener();
		addPropinaFijListener();
		addRedondeoListener();
		
		
//		if (savedInstanceState == null) {
//			getSupportFragmentManager().beginTransaction()
//					.add(R.id.container, new PlaceholderFragment()).commit();
//		}

	}
	
	private void addRedondeoListener() {
		redondeoTgg = (ToggleButton) findViewById(R.id.redondeo_tgg);
		redondeoTgg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				redondear = (redondear) ? false:true;
				CalularCta();
			}
		});	
		
	}

	private void addPropinaFijListener() {
		propinaEd = (EditText) findViewById(R.id.propina_ed);
		
		propinaEd.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				propinaSpn = (Spinner) findViewById(R.id.propina_spn);
				propinaSpn.setSelection(0);
				
				String text = propinaEd.getText().toString();
				propina = (text.equals("")) ? noPropina : Double.valueOf(text.toString());
				tipoCta = Cuenta.CtaTipMontoFijo;
				CalularCta();
				return false;
			}
		});
		
		propinaEd.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				propina = (s.toString().equals("")) ? noPropina : Double.valueOf(s.toString());
				tipoCta = Cuenta.CtaTipMontoFijo;
				CalularCta();
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {}
		});
		
		
		
	}

	private void addPropinaPorListener() {
		propinaSpn = (Spinner) findViewById(R.id.propina_spn);
		propinaSpn.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
//				propina = Double.valueOf(propinaSpn.getSelectedItem().toString());
				String tipTemp = propinaSpn.getSelectedItem().toString();
				propina = (tipTemp.equals("")) ? 0 : Double.valueOf(tipTemp);
				tipoCta = Cuenta.CtaTipProporcional;
				CalularCta();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		
		propinaSpn.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				propinaEd = (EditText) findViewById(R.id.propina_ed);
				propinaEd.setText("");
				
//				propina = Double.valueOf(propinaSpn.getSelectedItem().toString());
				String tipTemp = propinaSpn.getSelectedItem().toString();
				propina = (tipTemp.equals("")) ? 0 : Double.valueOf(tipTemp);
				tipoCta = Cuenta.CtaTipProporcional;
				CalularCta();
				return false;
			}
		});
		
	}

	private void addItemsToSpinners() {
		personasSpn = (Spinner) findViewById(R.id.people_spn);
		List<String> list = new ArrayList<String>();
		for (int i = 1; i <= maxPersonas; i++) {
			list.add(String.valueOf(i));
		}
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		personasSpn.setAdapter(dataAdapter);
		
		
		propinaSpn = (Spinner) findViewById(R.id.propina_spn);
		List<String> list2 = new ArrayList<String>();
		list2.add("");
		list2.add("0");
		list2.add("5");
		list2.add("10");
		list2.add("15");
		list2.add("20");
		ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this,
			android.R.layout.simple_spinner_item, list2);
		dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		propinaSpn.setAdapter(dataAdapter2);
		propinaSpn.setSelection(dataAdapter2.getPosition("10"));
		
	}

	private void addPersonasListener() {
		personasSpn = (Spinner) findViewById(R.id.people_spn);
		personasSpn.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				personas = Integer.valueOf(personasSpn.getSelectedItem().toString());
				CalularCta();
				
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
	}

	private void addMontoListener() {
		montoEd = (EditText) findViewById(R.id.monto_ed);
		montoEd.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
//				monto = Double.valueOf(s.toString());
				monto = (s.toString().equals("")) ? noMonto : Double.valueOf(s.toString());
				CalularCta();
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	public void CalularCta() {
		resulTv = (TextView) findViewById(R.id.resultado_tv);
		Cuenta cuenta = new Cuenta(monto, personas, propina, tipoCta);
		resulTv.setText("$" + cuenta.getCtaIndividual(redondear));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
		
		// Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_activity_actions, menu);
	    return super.onCreateOptionsMenu(menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
//		int id = item.getItemId();
//		if (id == R.id.action_settings) {
//			return true;
//		}
//		return super.onOptionsItemSelected(item);
		
		 // Handle presses on the action bar items
	    switch (item.getItemId()) {
	        case R.id.action_save:
	            saveCta();
	            return true;
	        case R.id.action_consulta:
	            showCta();
	            return true;
	        case R.id.action_export:
	        	borrarTabla();
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }

	}
	
	public void saveCta(){
		TextView resul = (TextView) findViewById(R.id.resultado_tv);
		BDLaCuenta_DAO bd = new BDLaCuenta_DAO(MainActivity.this);
		Cuenta cuenta = new Cuenta(monto, personas, propina, tipoCta);
		bd.insert(monto, personas, propina,cuenta.getCtaIndividual(redondear));
		resul.setText("Guardado!");
	}
	
	public void showCta(){
		TextView resul = (TextView) findViewById(R.id.resultado_tv);
		BDLaCuenta_DAO bd = new BDLaCuenta_DAO(MainActivity.this);
		resul.setText(bd.consultaTodo());
	}
	
	public void borrarTabla(){
		TextView resul = (TextView) findViewById(R.id.resultado_tv);
		BDLaCuenta_DAO bd = new BDLaCuenta_DAO(MainActivity.this);
		resul.setText(bd.deleteAll());
	}

//	/**
//	 * A placeholder fragment containing a simple view.
//	 */
//	public static class PlaceholderFragment extends Fragment {
//		
//		public PlaceholderFragment() {
//		}
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//			View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//
//			return rootView;
//		}
//		
//	}

}
