/*
 *  Copyright (c) 2020 Razeware LLC
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 *  distribute, sublicense, create a derivative work, and/or sell copies of the
 *  Software in any work that is designed, intended, or marketed for pedagogical or
 *  instructional purposes related to programming, coding, application development,
 *  or information technology.  Permission for such use, copying, modification,
 *  merger, publication, distribution, sublicensing, creation of derivative works,
 *  or sale is expressly withheld.
 *
 *  This project and source code may use libraries or frameworks that are
 *  released under various Open-Source licenses. Use of those libraries and
 *  frameworks are governed by their own individual licenses.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 *
 */

package com.raywenderlich.android.colorchat.ui.login

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.raywenderlich.android.colorchat.R
import com.raywenderlich.android.colorchat.ui.afterTextChanged
import com.raywenderlich.android.colorchat.ui.channels.ChannelsActivity
import com.raywenderlich.android.colorchat.ui.signup.SignUpActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.longToast

class LoginActivity : AppCompatActivity() {

  private lateinit var auth: FirebaseAuth

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    auth = FirebaseAuth.getInstance()
    if (auth.currentUser != null){
      startActivity(intentFor<ChannelsActivity>())
    }
    setUpLogin()
    setUpTextFields()
  }

  private fun setUpLogin(){
    loginButton.setOnClickListener {
      when {
        !isEmailValid(emailTextView.text.toString()) -> {
          emailSignUpTextInputLayout.error = getString(R.string.email_not_valid)
        }
        !isPasswordValid(passwordEditText.text.toString()) -> {
          passwordTextInputLayout.error = getString(R.string.invalid_password)
        }
        else -> {
          loading.visibility = View.VISIBLE
          auth.signInWithEmailAndPassword(emailTextView.text.toString(), passwordEditText.text.toString()).addOnCompleteListener { task ->
            if (task.isComplete && task.isSuccessful) {
              startActivity(intentFor<ChannelsActivity>())
              longToast("Welcome ${auth.currentUser?.displayName}!")
            } else {
              loading.visibility = View.GONE
              container.longSnackbar(task.exception?.localizedMessage.toString())
            }
          }

        }
      }

    }
  }

  fun startSignUpActivity(view: View) = startActivity(intentFor<SignUpActivity>())

  fun forgotPassword(view: View){
    if (!isEmailValid(emailTextView.text.toString())) {
      emailSignUpTextInputLayout.error = getString(R.string.email_not_valid)
    }else{
      auth.sendPasswordResetEmail(emailTextView.text.toString()).addOnCompleteListener {
        if (it.isComplete && it.isSuccessful) {
          container.longSnackbar(getString(R.string.recovery_email_sent))
        } else {
          container.longSnackbar(it.exception?.message.toString())
        }
      }
    }
  }

  private fun setUpTextFields(){
    emailTextView.afterTextChanged {
      if (it.isNotEmpty()) {
        emailSignUpTextInputLayout.error = null
      }
    }

    passwordEditText.afterTextChanged {
      if (it.length >= 8) {
        passwordTextInputLayout.error = null
      }
    }

  }

  private fun isEmailValid(email: String): Boolean {
    return email.contains('@') && email.isNotBlank()
  }

  private fun isPasswordValid(password: String): Boolean {
    return password.length >= 8
  }

}