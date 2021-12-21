package hu.unideb.getnatage.api

data class Nationality(
    val country: List<Country>,
    val name: String
)