package hu.unideb.getnatage

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.inputmethodservice.InputMethodService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import hu.unideb.getnatage.api.Apiurl
import hu.unideb.getnatage.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.requestButton.setOnClickListener {
            if(binding.InputG.text.isNotEmpty()){
                when(binding.bottomNavigation.selectedItemId){
                    R.id.genderOption -> {
                        getGenderAPI(binding.InputG.text.toString().lowercase().trim())
                        binding.genderTv.visibility = View.VISIBLE
                        binding.infoG.visibility = View.VISIBLE
                        binding.titleG.visibility = View.VISIBLE
                        binding.ageTv.visibility = View.GONE
                        binding.infoA.visibility = View.GONE
                        binding.titleA.visibility = View.GONE
                        binding.natTv.visibility = View.GONE
                        binding.infoN.visibility = View.GONE
                        binding.titleN.visibility = View.GONE
                    }
                    R.id.natOption -> {
                        getNationalityAPI(binding.InputG.text.toString().lowercase().trim())
                        binding.genderTv.visibility = View.GONE
                        binding.infoG.visibility = View.GONE
                        binding.titleG.visibility = View.GONE
                        binding.ageTv.visibility = View.GONE
                        binding.infoA.visibility = View.GONE
                        binding.titleA.visibility = View.GONE
                        binding.natTv.visibility = View.VISIBLE
                        binding.infoN.visibility = View.VISIBLE
                        binding.titleN.visibility = View.VISIBLE
                    }
                    R.id.ageOption -> {
                        getAgeAPI(binding.InputG.text.toString().lowercase().trim())
                        binding.genderTv.visibility = View.GONE
                        binding.infoG.visibility = View.GONE
                        binding.titleG.visibility = View.GONE
                        binding.ageTv.visibility = View.VISIBLE
                        binding.infoA.visibility = View.VISIBLE
                        binding.titleA.visibility = View.VISIBLE
                        binding.natTv.visibility = View.GONE
                        binding.infoN.visibility = View.GONE
                        binding.titleN.visibility = View.GONE
                    }
                }
            }
            closeKeyBoard()
        }
    }

    private fun closeKeyBoard(){
        val view = this.currentFocus
        if (view != null){
            val hideMe = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            hideMe.hideSoftInputFromWindow(view.windowToken,0)
        }
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    @SuppressLint("SetTextI18n")
    @DelicateCoroutinesApi
    private fun getGenderAPI(name : String) {
        val api = Retrofit.Builder().baseUrl(Apiurl.GENDER_URL).addConverterFactory(GsonConverterFactory.create()).build().create(APIRequest::class.java)
        // Fut az async
        GlobalScope.launch(Dispatchers.IO) {
                val respond = api.getGender(name)
            // UI megváltoztatom
            withContext(Dispatchers.Main){
                binding.genderTv.text = "${respond.gender} - ${respond.probability * 100}%"
            }
        }
    }

    @SuppressLint("SetTextI18n")
    @DelicateCoroutinesApi
    private fun getAgeAPI(name : String) {
        val api = Retrofit.Builder().baseUrl(Apiurl.AGE_URL).addConverterFactory(GsonConverterFactory.create()).build().create(APIRequest::class.java)
        // Fut az async
        GlobalScope.launch(Dispatchers.IO) {
            val respond = api.getAge(name)
            // UI megváltoztatom
            withContext(Dispatchers.Main){
                binding.ageTv.text = "${respond.age} years old."
            }
        }
    }

    @DelicateCoroutinesApi
    private fun getNationalityAPI(name : String) {
        val api = Retrofit.Builder().baseUrl(Apiurl.NATION_URL).addConverterFactory(GsonConverterFactory.create()).build().create(APIRequest::class.java)
        // Fut az async
        GlobalScope.launch(Dispatchers.IO) {
            val respond = api.getNationality(name)
            // UI megváltoztatom
            withContext(Dispatchers.Main){
                var ans = ""
                respond.country.forEach {
                    ans += (it.country_id + " - " + String.format("%.2f",it.probability*100) + "%" + "\n")
                }
                binding.natTv.text = ans
            }
        }
    }

}