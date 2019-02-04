package mekotlinapps.dnyaneshwar.`in`.restdemo.acitivty

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Patterns
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Button

import android.widget.EditText
import butterknife.BindView
import com.jakewharton.rxbinding2.widget.RxTextView


import kotlinx.android.synthetic.main.activity_signup.*
import mekotlinapps.dnyaneshwar.`in`.restdemo.R

import java.util.concurrent.TimeUnit
import java.util.regex.Matcher
import android.widget.Toast
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import org.jetbrains.anko.toast
import java.security.MessageDigest
import java.util.*


class Signup: AppCompatActivity(), View.OnClickListener {
    val callbackManager = CallbackManager.Factory.create()

    var sUsername: String? = "fgdf"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        //  loginButton.setReadPermissions("public_profile")
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile,email,user_birthday,user_friends,user_location"));
        generateHashKey()

        if (AccessToken.getCurrentAccessToken() == null) {
        } else {

            /*if your logged in then you will get access token here*/
            val accessToken = AccessToken.getCurrentAccessToken()
        }

        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onError(error: FacebookException?) {
            }

            override fun onSuccess(result: LoginResult?) {

                if (AccessToken.getCurrentAccessToken() != null) {
                    val profile = Profile.getCurrentProfile()
                    if (profile != null) {
                        val name = profile.name
                        profile.firstName
                        Log.i("@@name",profile.firstName);
                        toast("Thank you $name")
                    }

                }
            }

            override fun onCancel() {
            }
        })
//Respond to text change events in enterEmail//
        val btnSignUp = findViewById<View>(R.id.btnSignUp) as Button

        btnSignUp.setOnClickListener(this)

        RxTextView.afterTextChangeEvents(enterEmail)

//Skip enterEmail’s initial, empty state//

                .skipInitialValue()
                .map {
                    emailError.error = null
                    it.view().text.toString()
                }

//Ignore all emissions that occur within a 400 milliseconds timespan//

                .debounce(400,

//Make sure we’re in Android’s main UI thread//

                        TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())

//Apply the validateEmailAddress transformation function//

                .compose(validateEmailAddress)
                .compose(retryWhenError {
                    emailError.error = it.message
                })
                .subscribe()

    }

//If the app encounters an error, then try again//

    private inline fun retryWhenError(crossinline onError: (ex: Throwable) -> Unit): ObservableTransformer<String, String> = ObservableTransformer { observable ->
        observable.retryWhen { errors ->

            ///Use the flatmap() operator to flatten all emissions into a single Observable//

            errors.flatMap {
                onError(it)
                Observable.just("")
            }

        }
    }


//Define an ObservableTransformer, where we’ll perform the email validation//

    private val validateEmailAddress = ObservableTransformer<String, String> { observable ->
        observable.flatMap {
            Observable.just(it).map { it.trim() }
//Check whether the user input matches Android’s email pattern//

                    .filter {
                        Patterns.EMAIL_ADDRESS.matcher(it).matches()

                    }

//If the user’s input doesn’t match the email pattern, then throw an error//

                    .singleOrError()
                    .onErrorResumeNext {
                        if (it is NoSuchElementException) {
                            Single.error(Exception("Please enter a valid email address"))
                        } else {
                            Single.error(it)

                        }
                    }
                    .toObservable()
        }
    }

    fun isNullOrEmpty(str: String?): Boolean {
        if (str != null && !str.isEmpty())
            return false
        return true
    }


    override fun onClick(v: View?) {
        if (v != null) {
            when (v.id) {
                R.id.btnSignUp -> {
                    var etage=findViewById<View>(R.id.etAge) as EditText
                    var etPhoneNumber=findViewById<View>(R.id.etPhoneNumber)as EditText
                    var etGender=findViewById<View>(R.id.etGener)as EditText
                    var usernameEditText = findViewById<View>(R.id.editUsername) as EditText
                    val msg: String = usernameEditText.text.toString()
                    //check if the EditText have values or not
                  /*  if (msg.trim().length > 0
                            && etage.text.toString().length>0
                    &&etPhoneNumber.text.toString().length>0
                    &&etGender.text.toString().length>0) {
                        val i = Intent(this,MainActivity::class.java)
                        startActivity(i)
                        Toast.makeText(applicationContext, "Message : " + msg, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, "Please enter some message! ", Toast.LENGTH_SHORT).show()
                    }*/
                    if(usernameEditText.text.toString().isEmpty()){
                        Toast.makeText(applicationContext, "Please enter some message! ", Toast.LENGTH_SHORT).show()

                    }else if(etage.text.toString().isEmpty()){
                        Toast.makeText(applicationContext, "Please enter some message! ", Toast.LENGTH_SHORT).show()

                    }else if(etPhoneNumber.text.toString().isEmpty()){
                        Toast.makeText(applicationContext, "Please enter some message! ", Toast.LENGTH_SHORT).show()

                    }else if(etGender.text.toString().isEmpty()){
                        Toast.makeText(applicationContext, "Please enter some message! ", Toast.LENGTH_SHORT).show()

                    }else{
                        val i = Intent(this,MainActivity::class.java)
                        startActivity(i)
                        Toast.makeText(applicationContext, "Message : " + msg, Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }

    }
    /*generateHashKey gives you hash key to save facebook dev side*/
    fun generateHashKey() {
        val info = packageManager.getPackageInfo("mekotlinapps.dnyaneshwar.in.restdemo", PackageManager.GET_SIGNATURES)

        for (signature in info.signatures) {
            val md = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            Log.i("HashKey :", Base64.encodeToString(md.digest(), Base64.DEFAULT));
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}