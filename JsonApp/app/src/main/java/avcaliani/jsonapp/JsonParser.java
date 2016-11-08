package avcaliani.jsonapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by avcaliani on 17/09/16.
 */
public class JsonParser {

    /*
     * JSON Padrao
     * [
     *  {"name":"anthony", "age":19},
     *  {"name":"ze", "age":18}
     * ]
     *  */

    public static ArrayList<User> parse(String json) throws Exception{

        if (json == null || json.trim().matches(""))
            throw new Exception("JSONParser Error: json está nulo!");

        ArrayList<User> users = new ArrayList<User>(); // Lista de Users

        try {

            JSONArray userArray = new JSONArray(json); // Pegando o Array de Objetos
            JSONObject user;

            for (int i = 0; i < userArray.length(); i++) { // Enquanto o JSONArray tem item, faça

                user = userArray.getJSONObject(i); // Pegando os objetos do array

                // Se o JSON contem as chaves "name" e "age", prossiga
                if (user.has("name") && user.has("age")){

                    // Criando um obj User, passando o nome e a idade que foi
                    //  recebido pelo JSON
                    User objetoUser = new User(user.getString("name"), user.getInt("age"));

                    // Adicionando o obj a lista de Users
                    users.add(objetoUser);
                }

                // Caso o JSON venha sem algum valor que eu pedi eu nao vou
                //  adicionar o obj a lista
                Log.i("JSONTeste", "Erro no JSON: Falta alguma key no JSON!");

            } // End-for

        } catch (JSONException e) {

            // Caso o JSON venha com sintaxe ou com o padrão basico errado
            throw new Exception("JSONParser Error: Formato de JSON inválido!");

        }

        return users;
    } // End-Function

} // End-Class
