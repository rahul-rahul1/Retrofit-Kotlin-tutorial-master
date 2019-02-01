package mekotlinapps.dnyaneshwar.`in`.restdemo.acitivty

import android.os.Bundle
import android.util.Patterns
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
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
import org.jetbrains.anko.toast


class Signup: AppCompatActivity(), View.OnClickListener {

    var sUsername: String? = "fgdf"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
//Respond to text change events in enterEmail//
        val btnSignUp = findViewById<View>(R.id.btnSignUp) as Button
        var usernameEditText = findViewById<View>(R.id.editUsername) as EditText
         sUsername = usernameEditText.text.toString()
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
                    if (isNullOrEmpty(sUsername))
                        toast(isNullOrEmpty(sUsername).toString())

                }
            }
        }
    }

}