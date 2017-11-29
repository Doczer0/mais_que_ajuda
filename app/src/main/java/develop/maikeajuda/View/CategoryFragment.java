package develop.maikeajuda.View;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import develop.maikeajuda.Application.AppConfig;
import develop.maikeajuda.Application.ApplicationControler;
import develop.maikeajuda.Application.SessionManager;
import develop.maikeajuda.Controller.CategoryAdapter;
import develop.maikeajuda.Model.Category;
import develop.maikeajuda.R;


public class CategoryFragment extends Fragment {
    private JSONArray categoriesResponse;
    private GridView categoryGridView;
    private ProgressDialog progressDialog;
    private List<Category> categoriesSimpleList = new ArrayList<>();

    public CategoryFragment() {
        // Required empty public constructor
    }

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(),"fonts/SourceSansProLight.ttf");

        TextView categoryTitle = view.findViewById(R.id.text_category);
        categoryGridView = view.findViewById(R.id.grid_categories);

        categoryTitle.setTypeface(font);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);

        progressDialog.setMessage("Carregando Categorias...");
        showDialog();
        listAllCategories();

        categoryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent nextActivity = new Intent(getContext(),ExercisesActivity.class);
                nextActivity.putExtra("category_id",categoriesSimpleList.get(position).getCategoryID());
                nextActivity.putExtra("category",categoriesSimpleList.get(position).getCategoryName());
                startActivity(nextActivity);
            }
        });

        return view;
    }

    private void listAllCategories(){
        String CATEGORIES_TAG = "json_obj_categories";

        JsonObjectRequest categoriesRequest = new JsonObjectRequest(Request.Method.GET, AppConfig.URL_CATEGORIES, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject obj = response.getJSONObject("response");
                    categoriesResponse = obj.getJSONArray("categories");
                    for(int n = 0; n < categoriesResponse.length(); n++){
                        try {
                            JSONObject object = categoriesResponse.getJSONObject(n);
                            Category category;
                            category = new Category(
                                    Integer.parseInt(object.getString("id")),
                                    object.getString("category_name"),
                                    "");
                            categoriesSimpleList.add(category);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //if(e.getMessage()){}
                            showToast("Erro: "+e.getMessage());
                        }
                    }
                    CategoryAdapter adapter = new CategoryAdapter(categoriesSimpleList,getActivity(),getLayoutInflater());
                    categoryGridView.setAdapter(adapter);
                    hideDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                    showToast("Falha ao carregar categorias\nErro: "+e.toString());
                    hideDialog();
                }
                hideDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String error = volleyError.toString();
                switch(error) {
                    case "com.android.volley.AuthFailureError":
                        showToast("Erro: Não foi possivel autenticar solicitação");
                        break;
                    case "com.android.volley.NetworkError":
                        showToast("Erro: Não foi possivel executar solicitação");
                        break;
                    case "com.android.volley.NoConnectionError":
                        showToast("Erro: Não foi possivel estabelecer uma conexão com o servidor");
                        break;
                    case "com.android.volley.ParseError":
                        showToast("Erro: Não foi possivel analizar a resposta do servidor");
                        break;
                    case "com.android.volley.ServerError":
                        showToast("Erro: Erro no servidor");
                        break;
                    case "com.android.volley.TimeoutError":
                        showToast("Erro: Sem conexão com o servidor");
                        break;
                }
                hideDialog();
                //getActivity().finish();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SessionManager session = new SessionManager(getContext());
                String token = session.getTokenSession();
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + token);
                return headers;
            }
        };
        ApplicationControler.getInstance().addToRequestQueue(categoriesRequest,CATEGORIES_TAG);
    }

    private void showToast(String message){
        Toast notify;
        notify = Toast.makeText(getContext(),message,Toast.LENGTH_LONG);
        notify.setGravity(Gravity.CENTER,0,0);
        notify.show();
    }

    private void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    private void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

}
