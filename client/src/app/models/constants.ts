export class Constants {
  public static CLIENT_DOMAIN = process.env.NG_APP_ENV_CLIENT_DOMAIN;
  public static SERVER_DOMAIN = process.env.NG_APP_ENV_SERVER_DOMAIN;
  public static API_URL = Constants.SERVER_DOMAIN + process.env.NG_APP_ENV_API_BASE_URI;
  public static AUTH_URL = Constants.SERVER_DOMAIN + process.env.NG_APP_ENV_AUTH_BASE_URI;
  public static OAUTH2_REDIRECT_URI = Constants.CLIENT_DOMAIN + '/#/login';
  public static SESSION_STORAGE_JWT_KEY = 'make_my_day_user_jwt';
  public static SESSION_STORAGE_USER_KEY = 'make_my_day_user';
}
