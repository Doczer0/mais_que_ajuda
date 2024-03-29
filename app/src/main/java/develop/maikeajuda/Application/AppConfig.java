package develop.maikeajuda.Application;


public class AppConfig {
    public static final String IMAGE_DIRECTORY_NAME = "/MaikeAjuda/";

    //URL's POST
    public static final String URL_LOGIN = "http://191.252.185.111:4000/api/authentication/";
    public static final String URL_REGISTER = "http://191.252.185.111:4000/api/users";
    public static final String URL_RECOVEREMAIL = "http://191.252.185.111:4000/api/forgotpassword";
    public static final String URL_RECOVEREMAILCODE = "http://191.252.185.111:4000/api/validatetoken";
    public static final String URL_CHANGEPASSWORD = "http://191.252.185.111:4000/api/resetpassword";

    //URL's GET
    public static final String URL_CATEGORIES = "http://191.252.185.111:4000/api/categories";
    public static final String URL_EXERCISES = "http://191.252.185.111:4000/api/exercises";
    public static final String URL_STAGES = "http://191.252.185.111:4000/api/steps";
    public static final String URL_EXERCISE_IMG = "http://191.252.185.111:4000/api/comparisons";

    /*
    //URL's POST
    public static final String URL_LOGIN = "http://www.marmitexonline.com.br:4000/api/authentication";
    public static final String URL_REGISTER = "http://www.marmitexonline.com.br:4000/api/users";

    //URL's GET
    public static final String URL_CATEGORIES = "http://www.marmitexonline.com.br:4000/api/categories";
    public static final String URL_EXERCISES = "http://www.marmitexonline.com.br:4000/api/exercises";
    public static final String URL_STAGES = "http://www.marmitexonline.com.br:4000/api/steps";
    public static final String URL_EXERCISE_IMG = "http://www.marmitexonline.com.br:4000/api/comparisons";
    */
}
