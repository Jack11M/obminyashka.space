package space.obminyashka.items_exchange.rest.api;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ApiKey {
    public static final String API = "/api/v1";
    // Advertisement API
    public static final String ADV = API + "/adv";
    public static final String ADV_SEARCH_PAGINATED = ADV + "/search/{keyword}";
    public static final String ADV_SEARCH_PAGINATED_REQUEST_PARAMS = ADV_SEARCH_PAGINATED + "?page={page}&size={size}";
    public static final String ADV_DEFAULT_IMAGE = ADV + "/default-image/{advertisementId}/{imageId}";
    public static final String ADV_ID = ADV + "/{advertisement_id}";
    public static final String ADV_FILTER = ADV + "/filter";
    public static final String ADV_TOTAL = ADV + "/total-amount";
    // Authorization API
    public static final String AUTH = API + "/auth";
    public static final String AUTH_REGISTER = AUTH + "/register";
    public static final String AUTH_LOGIN = AUTH + "/login";
    public static final String AUTH_LOGOUT = AUTH + "/logout";
    public static final String AUTH_REFRESH_TOKEN = AUTH + "/refresh/token";
    public static final String AUTH_OAUTH2_SUCCESS = AUTH + "/oauth2/success";
    // Category API
    public static final String CATEGORY = API + "/category";
    public static final String CATEGORY_NAMES = CATEGORY + "/names";
    public static final String CATEGORY_ALL = CATEGORY + "/all";
    public static final String CATEGORY_ID = CATEGORY + "/{category_id}";
    public static final String CATEGORY_SIZES = CATEGORY_ID + "/sizes";
    // Email API
    public static final String EMAIL = API + "/email";
    public static final String EMAIL_VALIDATE_CODE = EMAIL + "/validate/{code}";
    // Image API
    public static final String IMAGE = API + "/image";
    public static final String IMAGE_BY_ADV_ID = IMAGE + "/{advertisement_id}";
    public static final String IMAGE_RESOURCE = IMAGE_BY_ADV_ID + "/resource";
    public static final String IMAGE_IN_ADV_COUNT = IMAGE_BY_ADV_ID + "/total";
    // Location API
    public static final String LOCATION = API + "/location";
    public static final String LOCATION_ID = LOCATION + "/{location_id}";
    public static final String LOCATION_ALL = LOCATION + "/all";
    public static final String LOCATION_AREA = LOCATION + "/area";
    public static final String LOCATION_DISTRICT = LOCATION + "/district";
    public static final String LOCATION_CITY = LOCATION + "/city";
    public static final String LOCATIONS_INIT = LOCATION + "/locations-init";
    // Subcategory API
    public static final String SUBCATEGORY = API + "/subcategory";
    public static final String SUBCATEGORY_ID = SUBCATEGORY + "/{subcategory_id}";
    public static final String SUBCATEGORY_NAMES = SUBCATEGORY + "/{category_id}/names";
    // User API
    public static final String USER = API + "/user";
    public static final String USER_MY_INFO = USER + "/my-info";
    public static final String USER_MY_ADV = USER + "/my-adv";
    public static final String USER_MY_FAVORITE = USER_MY_ADV + "/favorite";
    public static final String USER_MY_FAVORITE_ADV = USER_MY_FAVORITE + "/{advertisementId}";
    public static final String USER_CHILD = USER + "/child";
    public static final String USER_SERVICE = USER + "/service";
    public static final String USER_SERVICE_CHANGE_PASSWORD = USER_SERVICE + "/pass";
    public static final String USER_SERVICE_CHANGE_EMAIL = USER_SERVICE + "/email";
    public static final String USER_SERVICE_CHANGE_AVATAR = USER_SERVICE + "/avatar";
    public static final String USER_SERVICE_DELETE = USER_SERVICE + "/delete";
    public static final String USER_SERVICE_RESTORE = USER_SERVICE + "/restore";
    public static final String USER_SERVICE_RESET_PASSWORD = USER_SERVICE_CHANGE_PASSWORD + "/reset";
    public static final String USER_SERVICE_PASSWORD_CONFIRM = USER_SERVICE_CHANGE_PASSWORD + "/confirm/{code}";
    // OAuth2 API
    public static final String OAUTH2 = "/oauth2/**";
    public static final String OAUTH2_LOGIN = "/login" + OAUTH2;
    public static final String OAUTH2_SUCCESS = "/oauth-success";
    // Chat API
    public static final String CHAT = API + "/chat";
    public static final String CHAT_ID = CHAT + "/hello/{chatId}";
    // Front API from routeConstants
    public static final String FRONT_ADV_ADD = "/add-good/**";
    public static final String FRONT_LOGIN = "/login";
    public static final String FRONT_SIGN = FRONT_LOGIN + "/sign";
    public static final String FRONT_USER = "/user/**";
    public static final String FRONT_PRODUCT = "/product-page/**";
    public static final String FRONT_FILTER = "/filter/**";
}
