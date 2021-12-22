package mbaas.com.nifcloud.androidsegmentuserapp

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.TextView
import com.nifcloud.mbaas.core.*

class MainActivity : AppCompatActivity() {
    private lateinit var customAdapter: CustomAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //**************** APIキーの設定とSDKの初期化 **********************
        NCMB.initialize(this, "YOUR_APPLICATION_KEY", "YOUR_CLIENT_KEY");

        //ユーザーログイン呼ぶ
        val intent = Intent(this, LoginActivity::class.java)
        startActivityForResult(intent, REQUEST_LOGIN)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        // ログアウト処理
        if (id == R.id.action_logout) {
            var user = NCMBUser()
            try{
                user.logout()
            }
            catch(e: NCMBException){
            }
            //ユーザーログイン呼ぶ
            val intent = Intent(this, LoginActivity::class.java)
            startActivityForResult(intent, REQUEST_LOGIN)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                //進行動作状況表示
                setDispProcess("ログインに成功しました。")

                //ログインに成功場合はーザー情報を表示させる
                doCurrentUserInfo()
            }
        }
    }

    //ログインに成功場合はユーザー情報を表示させる
    fun doCurrentUserInfo() {

        // カレントユーザ情報の取得
        val user = NCMBUser()
        val userInfo = user.getCurrentUser()

        //新規更新
        val _txtNewKey = findViewById<View>(R.id.txtNewKey) as TextView
        val _txtNewValue = findViewById<View>(R.id.txtNewValue) as TextView
        _txtNewKey.text = "" //初期化
        _txtNewValue.text = "" //初期化

        try {
            var rs = userInfo.fetch();
            customAdapter = CustomAdapter(applicationContext, rs)
            val listView1 = findViewById<View>(R.id.listViewUser) as ListView
            listView1.adapter = customAdapter
            setListviewHeight(listView1)
        } catch(e:NCMBException){

        }
    }

    //更新Btn押す処理
    fun doUpdateUserInfo(view: View) {

        //入力値のチェック
        val strCheckError = validateNewData()
        if (!strCheckError.isEmpty()) {
            //入力チェックエラーの場合
            setDispProcess(strCheckError)
            return
        } else {
            try {
                //更新処理開始
                customAdapter!!.userObject!!.save();
                setDispProcess("更新に成功しました。")
                doCurrentUserInfo()
            } catch(e:NCMBException){
                setDispProcess("更新失敗しました!\n" + e.message)
            }
        }
    }

    //新規更新入力チェック
    fun validateNewData(): String {
        var errMsg = ""

        //新規入力のチェック
        val _txtNewKey = findViewById<View>(R.id.txtNewKey) as TextView
        val _txtNewValue = findViewById<View>(R.id.txtNewValue) as TextView

        //新規入力Key値
        val newKey = _txtNewKey.text.toString()
        //新規入力Value値
        val newValue = _txtNewValue.text.toString()

        if (newKey.isEmpty() && !newValue.isEmpty()) {
            errMsg = "新規key値を入力ください!"
        } else if (Constant.ignoreKeys.contains(newKey.trim { it <= ' ' })) {
            errMsg = "key値は更新対象名ではありません!"
        } else if (!newKey.isEmpty() && !newKey.trim { it <= ' ' }.matches("^[0-9a-zA-Z]+$".toRegex())) {
            errMsg = "key値は半角英数字のみです!"
        } else {
            //新規更新データを作成
            if (newKey.isEmpty() && newValue.isEmpty()) {
                //更新対象ではない
            } else {
                //更新データを設定
                customAdapter!!.userObject!!.put(newKey.trim { it <= ' ' }, newValue.trim { it <= ' ' })
            }
        }

        return errMsg
    }

    //進行動作状況表示
    private fun setDispProcess(strProcess: String) {

        if (!strProcess.isEmpty()) {
            val txtProcess = findViewById<View>(R.id.txtProcess) as TextView
            txtProcess.text = strProcess
        }

    }

    //リスト表示用ListView高さを設定
    private fun setListviewHeight(listView: ListView) {

        val dm = Resources.getSystem().displayMetrics
        val winH = dm.heightPixels

        //ListView高さを設定(端末サイズによるScrollする為)
        val params = listView.layoutParams

        if (winH > 1000) {
            params.height = winH / 2
        } else if (winH < 500) {
            params.height = 100
        } else {
            params.height = 350
        }
        listView.layoutParams = params

        listView.requestLayout()
    }

    companion object {

        private val REQUEST_LOGIN = 0
    }

}
