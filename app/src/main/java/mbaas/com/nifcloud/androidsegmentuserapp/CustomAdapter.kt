package mbaas.com.nifcloud.androidsegmentuserapp

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.EditText
import android.widget.TextView
import com.nifcloud.mbaas.core.NCMBObject

/**
 * Customer List view
 */
class CustomAdapter(private val context: Context, userObject: NCMBObject) : BaseAdapter() {
    private lateinit var inflater: LayoutInflater
    /**
     * return userObject
     */
    var userObject: NCMBObject? = null

    init {
        this.userObject = userObject
//        inflater = LayoutInflater.from(context)
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    }

    override fun getCount(): Int {
        return userObject!!.mFields.length();
    }

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    inner class ViewHolder {

        var item_key: TextView? = null
        var item_value: EditText? = null

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var vi = convertView
        val holder: ViewHolder
        val strKey = ""
        val strValue = ""

        //動的にレイアウトxmlからViewを生成
        vi = inflater!!.inflate(R.layout.two_line_list_item, null)

        //View要素設定
        holder = ViewHolder()
        holder.item_key = vi.findViewById<View>(R.id.item_key) as TextView
        holder.item_value = vi.findViewById<View>(R.id.item_value) as EditText

        //Viewにセットする
        vi.tag = holder

        val keyName = userObject!!.mFields.names().getString(position)
        val valName = userObject!!.getString(keyName) as Any

        holder.item_key!!.text = keyName //keyの値
        holder.item_value!!.setText(valName.toString()) //valueの値


        //更新可項目を別途Layout設定
        if (Constant.ignoreKeys.contains(keyName)) {
            //valueの値を更新不可にする
            holder.item_value!!.setBackgroundColor(Color.BLACK)
            holder.item_value!!.setTextColor(Color.WHITE)
            holder.item_value!!.isEnabled = false
        } else {
            //valueの値を更新可能にする
            holder.item_value!!.isEnabled = true
            holder.item_value!!.setBackgroundColor(Color.WHITE)
            holder.item_value!!.setTextColor(Color.BLACK)
            //valueの値を更新後の処理する
            holder.item_value!!.addTextChangedListener(TextWatcherCustom(keyName, holder.item_value!!, position))
        }

        return vi
    }

    ////////////////////////////////////////////////////////////
    // テキストの変更を検知するために必要
    internal inner class TextWatcherCustom// コンストラクタ
    (// 通知するためのエディットボックス
            private val mKeyText: String, private val mValueEditText: EditText, private val mPosition: Int) : TextWatcher {

        // テキスト変更前
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            //処理
        }

        // テキスト変更後
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            //処理
        }

        // テキスト変更後
        override fun afterTextChanged(s: Editable) {
            //List更新データを設定
            userObject!!.put(this.mKeyText, s.toString())
        }

    }
    ////////////////////////////////////////////////////////////

}