package com.raywenderlich.android.colorchat.ui.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.raywenderlich.android.colorchat.R
import com.raywenderlich.android.colorchat.ui.afterTextChanged
import com.raywenderlich.android.colorchat.ui.channels.ChannelsActivity
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.intentFor

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()
        setSignUp()
        setUpTextFields()
    }

    private fun setSignUp(){
        signUpButton.setOnClickListener {
            when {
                !isEmailValid(emailSignUpTextView.text.toString()) -> {
                    emailSignUpTextInputLayout.error = "Email not valid"
                }

                !isUsernameValid(usernameSignUpTextView.text.toString()) -> {
                    usernameSignUpInputLayout.error = getString(R.string.not_blank, "Username")
                }

                !isPasswordValid(firstPasswordSignUpTextView.text.toString(),
                    secondPasswordSignUpTextView.text.toString()) -> {
                    firstPasswordSignUpInputLayout.error = getString(R.string.invalid_password)
                }

                !isPasswordMatch(firstPasswordSignUpTextView.text.toString(),
                    secondPasswordSignUpTextView.text.toString()) -> {
                    firstPasswordSignUpInputLayout.error = getString(R.string.password_not_match)
                    secondPasswordSignUpTextInputLayout.error = getString(R.string.password_not_match)
                }
                else -> {
                    signupProgressBar.visibility = View.VISIBLE
                    auth.createUserWithEmailAndPassword(emailSignUpTextView.text.toString().trim(), firstPasswordSignUpTextView.text.toString())
                        .addOnCompleteListener { task ->
                            signupProgressBar.visibility = View.GONE
                            if (task.isComplete && task.isSuccessful) {
                                val updates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(usernameSignUpTextView.text.toString())
                                    .build()
                                auth.currentUser?.updateProfile(updates)
                                startActivity(intentFor<ChannelsActivity>())
                            } else {
                                signupLayout.longSnackbar(task.exception?.localizedMessage.toString())
                            }
                        }
                }
            }

        }
    }

    private fun setUpTextFields() {
        usernameSignUpTextView.afterTextChanged {
            if (it.isNotEmpty()) {
                usernameSignUpInputLayout.error = null
            }
        }

        emailSignUpTextView.afterTextChanged {
            if (it.isNotEmpty() && isEmailValid(it)) {
                emailSignUpTextInputLayout.error = null
            }
        }

        firstPasswordSignUpTextView.afterTextChanged {
            if (it.length >= 8) {
                firstPasswordSignUpInputLayout.error = null
            }
        }

        secondPasswordSignUpTextView.afterTextChanged {
            if (it.length >= 8) {
                secondPasswordSignUpTextInputLayout.error = null
            }
        }
    }


    // A placeholder username validation check
    private fun isEmailValid(email: String): Boolean {
        return email.contains('@') &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
                email.isNotBlank()
    }

    // A placeholder username validation check
    private fun isUsernameValid(username: String): Boolean {
        return username.isNotBlank()
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String, secondPassword: String) = password.length >= 8

    private fun isPasswordMatch(password: String, secondPassword: String) = password == secondPassword


}







