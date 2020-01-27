package es.guillemburnleesviada.ejercicio2_firebase_examen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import es.guillemburnleesviada.ejercicio2_firebase_examen.pojo.Deudor;

public class MainActivity extends AppCompatActivity {

    private TextView txtDeudores, txtDeudaTotal;
    private EditText txtNombreDeudor, txtCantidadDeuda;
    private Button btnAddDeudor;

    private FirebaseDatabase database;
    private DatabaseReference mainRef;

    private ArrayList<Deudor> deudoresList;
    private Map<String, Deudor> mapDeudores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtCantidadDeuda = findViewById(R.id.txtDeudaMain);
        txtDeudaTotal = findViewById(R.id.txtDeudaAcumMain);
        txtNombreDeudor = findViewById(R.id.txtNombreDeudorMain);
        txtDeudores = findViewById(R.id.txtDeudoresMain);
        btnAddDeudor = findViewById(R.id.btnAddDeudaMain);

        deudoresList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();
        mainRef = database.getReference("deudores");


        mainRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                GenericTypeIndicator<Map<String, Deudor>> typeIndicator = new GenericTypeIndicator<Map<String, Deudor>>() {};

                mapDeudores = dataSnapshot.getValue(typeIndicator);
                deudoresList.clear();

                if (mapDeudores != null) {
                    deudoresList.addAll(mapDeudores.values());
                    Log.d("FIREBASE", mapDeudores.values().toString());

                    txtDeudores.setText("Deudores: "+deudoresList.size()+"");

                    float total=0;

                    for (int i = 0; i < deudoresList.size(); i++) {
                        total += deudoresList.get(i).getDeuda();
                    }

                    txtDeudaTotal.setText("Deuda total: "+total);

                }else{
                    txtDeudores.setText("Deudores: "+0);
                    txtDeudaTotal.setText("Deuda total: "+0);
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FIREBASE", "Failed to read value.", error.toException());
            }
        });


        btnAddDeudor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (!txtNombreDeudor.getText().toString().isEmpty() || !txtCantidadDeuda.getText().toString().isEmpty()){

                    Deudor deudor = new Deudor(txtNombreDeudor.getText().toString(), Float.parseFloat(txtCantidadDeuda.getText().toString()), mainRef);

                    for (int i = 0; i < deudoresList.size(); i++) {
                        if (deudor.getNombre().equals(deudoresList.get(i).getNombre())){
                            float temp = Float.parseFloat(txtCantidadDeuda.getText().toString())+deudoresList.get(i).getDeuda();
                            deudor.setDeuda(temp);
                            deudoresList.get(i).setDeuda(temp);
                            Log.d("FIREBASE", deudor.getNombre()+" se ha añadido "+Integer.parseInt(txtCantidadDeuda.getText().toString()));
                        }
                    }

                    guardarEnDB(deudor);
                    limpiarTxt();
                }else
                    Toast.makeText(MainActivity.this, "Faltan datos", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void guardarEnDB(Deudor deudor){

        deudor.getRef().child(deudor.getNombre()).child("nombre").setValue(deudor.getNombre());
        deudor.getRef().child(deudor.getNombre()).child("deuda").setValue(deudor.getDeuda());
        deudoresList.add(deudor);

    }

    private void limpiarTxt(){
        txtNombreDeudor.setText("");
        txtCantidadDeuda.setText("");
    }


}
