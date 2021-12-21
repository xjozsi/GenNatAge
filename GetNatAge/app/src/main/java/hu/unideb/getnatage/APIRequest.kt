package hu.unideb.getnatage

import hu.unideb.getnatage.api.Age
import hu.unideb.getnatage.api.Apiurl
import hu.unideb.getnatage.api.Gender
import hu.unideb.getnatage.api.Nationality
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIRequest {
    @GET(Apiurl.GENDER_URL)
    suspend fun getGender(@Query("name") name : String) : Gender

    @GET(Apiurl.AGE_URL)
    suspend fun getAge(@Query("name") name : String) : Age

    @GET(Apiurl.NATION_URL)
    suspend fun getNationality(@Query("name") name : String) : Nationality
}