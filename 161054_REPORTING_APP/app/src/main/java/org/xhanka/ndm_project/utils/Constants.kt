package org.xhanka.ndm_project.utils

object Constants {
    const val DARK_MODE = "dark_mode"
    const val REQUEST_LOCATION_PERMISSION_CODE = 123

    const val BASE_URL = "http://192.168.43.142:8000/api/"
    // const val BASE_URL = "https://xhanka.pythonanywhere.com/api/"

    const val WEATHER_FOR_ESWATINI = "eswatini_weather/"
    const val FORECAST_FOR_TOWN = "eswatini_forecast/%s/"

    const val CHANNEL_ID = "_id"
    const val CHANNEL_NAME = "_channel_name"

    // databases
    const val DB_STATIONS_COLLECTION = "EMERGENCY_STATIONS"
    const val DB_USERS_COLLECTION = "USERS"
    const val DB_EMERGENCIES_COLLECTION = "LOGGED_EMERGENCIES"
    const val DB_USER_DASHBOARD_COLLECTION = "USER_DASHBOARD"
    const val DB_PUBLIC_NOTICE_COLLECTION = "PUBLIC_NOTICE"
}