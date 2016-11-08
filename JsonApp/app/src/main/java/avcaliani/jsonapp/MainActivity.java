package avcaliani.jsonapp;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = (TextView) findViewById(R.id.textView);
        ArrayList<User> users; // Minha lista de Usuarios
        StringBuilder json = new StringBuilder(); // Meu JSON
        String allUsers = ""; // Resultado da Op

        /* Construindo o JSON */

        json.append("["); // Inicio do Array

        // Objeto 1
        json.append("{");
        json.append("\"name\":\"Anthony\",");
        json.append("\"age\":19");
        json.append("},");

        // Objeto 2
        json.append("{");
        json.append("\"name\":\"Breno\",");
        json.append("\"age\":20");
        json.append("}");

        /*
        // Objetos para testar!

        // Objeto 3 - chave errada
        json.append("{");
        json.append("\"vish\":\"Julia\",");
        json.append("\"age\":21");
        json.append("}");

        // Objeto 4 - Sintaxe errada
        json.append("{");
        json.append("name = Julia");
        json.append("age = 21");
        json.append("}");
        */

        json.append("]"); // Fim do Array


        try {
            users = JsonParser.parse(json.toString());

            for (int i = 0; i < users.size(); i++)
                allUsers += users.get(i).toString() + "\n";

            tv.setText(allUsers);
        } catch (Exception e){
            showDialog(e.getMessage());
            tv.setText("Ops! Algo deu errado :(");
        }
    } // End-Functon

    protected void showDialog(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //do things
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    } // End-Functon
} // End-Class
