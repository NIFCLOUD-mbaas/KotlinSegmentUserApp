package mbaas.com.nifcloud.androidsegmentuserapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.nifcloud.mbaas.core.NCMBException
import com.nifcloud.mbaas.core.NCMBUser
import kotlinx.android.synthetic.main.activity_login.*

/**
 * ユーザーログイン
 */
class LoginActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login.setOnClickListener {
            login()
        }
        link_signup.setOnClickListener {
            // Start the Signup activity
            val intent = Intent(applicationContext, SignupActivity::class.java)
            startActivityForResult(intent, REQUEST_SIGNUP)
        }
    }

    fun login() {
        Log.d(TAG, "Login")

        if (!validate()) {
            onLoginFailed()
            return
        }

        btn_login.isEnabled = false

        val progressDialog = ProgressDialog(this@LoginActivity, R.style.AppTheme_Dark_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Authenticating...")
        progressDialog.show()

        val name = input_name.text.toString()
        val password = input_password.text.toString()

        // TODO: Implement your own authentication logic here.
        //ユーザ名とパスワードを指定してログインを実行
        try {
            var user = NCMBUser()
            user.userName = name
            user.password = password

            user.login(user.userName,user.password)
            android.os.Handler().postDelayed(
                {
                    // On complete call either onLoginSuccess or onLoginFailed
                    onLoginSuccess()
                    progressDialog.dismiss()
                }, 3000)
        } catch (e: NCMBException) {
            //エラー時の処理
            onLoginFailed()
            progressDialog.dismiss()
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == Activity.RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                setResult(Activity.RESULT_OK, intent)
                this.finish()
            }
        }
    }

    override fun onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true)
    }

    fun onLoginSuccess() {
        btn_login.isEnabled = true
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun onLoginFailed() {
        Toast.makeText(baseContext, "Login failed", Toast.LENGTH_LONG).show()

        btn_login.isEnabled = true
    }

    fun validate(): Boolean {
        var valid = true

        val name = input_name.text.toString()
        val password = input_password.text.toString()

        if (name.isEmpty()) {
            input_name.error = "enter username"
            valid = false
        } else {
            input_name.error = null
        }

        if (password.isEmpty() || password.length < 4 || password.length > 10) {
            input_password.error = "between 4 and 10 alphanumeric characters"
            valid = false
        } else {
            input_password.error = null
        }

        return valid
    }

    companion object {
        private val TAG = "LoginActivity"
        private val REQUEST_SIGNUP = 0
    }
}
