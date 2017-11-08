package develop.maikeajuda.Application;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import develop.maikeajuda.View.LoginActivity;

/**
 * Created by user on 01/11/2017.
 */

public class SessionManager {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;
    private static final String PREFERENCES_NAME = "";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String KEY_EMAIL = "login";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_TOKEN = "token";

    public SessionManager(Context _context) {
        this._context = _context;
        preferences = _context.getSharedPreferences(PREFERENCES_NAME,PRIVATE_MODE);
        editor = preferences.edit();
    }

    public void createLoginSession(String email, String passwd, String token){
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_PASSWORD, passwd);
        editor.putString(KEY_TOKEN, token);
        editor.commit();
    }

    public String getTokenSession(){
        return preferences.getString(KEY_TOKEN,"");
    }

    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent intent = new Intent(_context, LoginActivity.class);
        }
    }

    public void logoutUser(){
        editor.clear();
        editor.commit();
    }

    public boolean isLoggedIn(){
        return preferences.getBoolean(IS_LOGIN, false);
    }
}
