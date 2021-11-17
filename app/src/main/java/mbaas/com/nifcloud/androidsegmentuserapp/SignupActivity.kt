package mbaas.com.nifcloud.androidsegmentuserapp

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.nifcloud.mbaas.core.NCMBException
import com.nifcloud.mbaas.core.NCMBUser
import kotlinx.android.synthetic.main.activity_signup.*

/**
 * 新規ユーザー登録
 */
class SignupActivity : AppCompatActivity() {

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        btn_signup.setOnClickListener { signup() }

        link_login.setOnClickListener {
            // Finish the registration screen and return to the Login activity
            setResult(Activity.RESULT_CANCELED, intent)
            finish()
        }
    }

    fun signup() {
        Log.d(TAG, "Signup")

        if (!validate()) {
            onSignupFailed()
            return
        }

        btn_signup.isEnabled = false

        val progressDialog = ProgressDialog(this@SignupActivity,
                R.style.AppTheme_Dark_Dialog)
        progressDialog.isIndeterminate = true
        progressDialog.setMessage("Creating Account...")
        progressDialog.show()

        val name = input_name.text.toString()
        val password = input_password.text.toString()

        // TODO: Implement your own signup logic here.
        //NCMBUserのインスタンスを作成
        val user = NCMBUser()
        //ユーザ名を設定
        user.userName = name
        //パスワードを設定
        user.password = password
        //設定したユーザ名とパスワードで会員登録を行う
        try {
            user.signUp()
            android.os.Handler().postDelayed(
                {
                    // On complete call either onSignupSuccess or onSignupFailed
                    // depending on success
                    onSignupSuccess()
                    // onSignupFailed();
                    progressDialog.dismiss()
                }, 3000)
        }
        catch(e: NCMBException){
            //会員登録時にエラーが発生した場合の処理
            onSignupFailed()
            progressDialog.dismiss()
        }
    }


    fun onSignupSuccess() {
        btn_signup.isEnabled = true
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun onSignupFailed() {
        Toast.makeText(baseContext, "Login failed", Toast.LENGTH_LONG).show()

        btn_signup.isEnabled = true
    }

    fun validate(): Boolean {
        var valid = true

        val name = input_name.text.toString()
        val password = input_password.text.toString()

        if (name.isEmpty() || name.length < 3) {
            input_name.error = "at least 3 characters"
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
        private val TAG = "SignupActivity"
    }
}
