# 【Android】ユーザー情報を更新してみよう！ for Kotlin

![画像01](/readme-img/001.png)

## 概要
* [ニフクラ mobile backend](https://mbaas.nifcloud.com/) の『会員管理機能』を利用してAndroidアプリにログイン機能を実装し、ユーザー情報を更新するサンプルプロジェクトです
* 簡単な操作ですぐに [ニフクラ mobile backend](https://mbaas.nifcloud.com/) の機能を体験いただけます

## ニフクラ mobile backendって何？？
スマートフォンアプリのバックエンド機能（プッシュ通知・データストア・会員管理・ファイルストア・SNS連携・位置情報検索・スクリプト）が**開発不要**、しかも基本**無料**(注1)で使えるクラウドサービス！

注1：詳しくは[こちら](https://mbaas.nifcloud.com/price.htm)をご覧ください

![画像02](/readme-img/002.png)

## 動作環境
* MacOS Big Sur 11.6 
* Android Studio Arctic Fox 2020.3.1
* Android ver 4x以上
* Android SDK v3
  - SDK v2系だと動作しないので注意

※上記内容で動作確認をしています

## 作業の手順
### 1. ニフクラ mobile backend の会員登録・ログインとアプリの新規作成
* 下記リンクから会員登録（無料）をします
  * https://console.mbaas.nifcloud.com/signup
* 登録ができたら下記リンクからログインします
  * https://console.mbaas.nifcloud.com/
* 下図のように「アプリの新規作成」画面が出るのでアプリを作成します
  * 既に mobile backend を利用したことがある方は左上の「＋新しいアプリ」をクリックすると同じ画面が表示されます

![画像03](/readme-img/003.png)

* アプリ作成されるとAPIキー（アプリケーションキーとクライアントキー）が発行されます
* この２種類のAPIキーはこの後ダウンロードするサンプルアプリと ニフクラ mobile backend を紐付けるため、あとで使います。

![画像04](/readme-img/004.png)

* 動作確認後に会員情報が保存される場所も確認しておきましょう

![画像05](/readme-img/005.png)

### 2. サンプルプロジェクトのダウンロード
* 下記リンクをクリックしてプロジェクトをダウンロードします
 * https://github.com/NiFCloud-mbaas/KotlinSegmentUserApp/archive/master.zip
* ダウンロードしたプロジェクトを解凍します
* AndroidStudio を開きます、「Open an existing Android Studio projct」をクリックして解凍したプロジェクトを選択します
* プロジェクトが開かれます

![画像06](/readme-img/006.png)

### 3. SDKの導入（実装済み）

※このサンプルアプリには既にSDKが実装済み（下記手順）となっています。（ver.3.0.0)<br>　最新版をご利用の場合は入れ替えてご利用ください。

* SDKダウンロード
SDKはここ（[SDK リリースページ](https://github.com/NIFCloud-mbaas/ncmb_android/releases)）から取得してください.
  - NCMB.jarファイルがダウンロードします。
* SDKをインポート
  - app/libsフォルダにNCMB.jarをコピーします
* 設定追加
  - app/build.gradleファイルに以下を追加します
```gradle
dependencies {
    compile 'com.google.code.gson:gson:2.3.1'
    compile files('libs/NCMB.jar')
}
```
  - androidManifestの設定
    - <application>タグの直前に以下のpermissionを追加します
```html
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

### 4. APIキーの設定

* AndroidStudio で MainActivity.kt を開きます
  * ディレクトリはデフォルトで「Android」が選択されていますので、「Project」に切り替えてから探してください

![画像14](/readme-img/014.png)

* APIキー（アプリケーションキーとクライアントキー）の設定をします

![画像07](/readme-img/007.png)

* それぞれ`YOUR_APPLICATION_KEY`と`YOUR_CLIENT_KEY`の部分を書き換えます
   - このとき、ダブルクォーテーション（`"`）を消さないように注意してください！
* 書き換え終わったら保存してください
   * Windowsの場合、Ctrl + Sで保存できます。
   * Macの場合、command + Sで保存できます。

### 5.動作確認
* エミュレーターでアプリをビルドします
 * 失敗する場合は一度「Clean Project」を実行してから再度ビルドしてください
* アプリが起動したら、Login画面が表示されます
* 初回は `No account yet? Create one` リンクをクリックして、新規会員登録を行います

![画像08](/readme-img/008.png)

* `User Name`と`Password`を２つ入力して「CREATE ACCOUNT」ボタンをタップします
* 会員登録が成功するとログインされ、下記ユーザー情報の一覧が表示されます
* SignUpに成功すると ニフクラ mobile backend 上に会員情報が作成されます！

![画像09](/readme-img/009.png)

* ログインに失敗した場合はアラートでログイン失敗を表示します

#### 新しいフィールドの追加
* 新しいフィールドの追加をしてみましょう
  - 例として`"favorite"` というフィールドを作り、中身には `"music"` と入れてみます
* 編集が完了したら更新ボタンをタップして下さい

![画像10](/readme-img/010.png)

* 更新後、リストビューが自動でリロードされ、追加・更新が行われていることが確認できます
* 追加したフィールドは後から編集することも可能です

![画像11](/readme-img/011.png)

* ニフクラ mobile backend の管理画面上で、会員データの更新ができていることを確認してみましょう

![画像12](/readme-img/012.png)

* （参考）ActionBar右上側メニューから「Logout」を押すとログアウトが出来ます

![画像13](/readme-img/013.png)

## ロジック紹介
### 会員登録
`SignupActivity.kt`

```kotlin
//NCMBUserのインスタンスを作成
val user = NCMBUser()
//ユーザ名を設定
user.userName = name
//パスワードを設定
user.setPassword(password)
//設定したユーザ名とパスワードで会員登録を行う
user.signUpInBackground { e ->
    if (e != null) {
        // 新規登録失敗時の処理
    } else {
        // 新規登録成功時の処理
    }
    }
});
```

### ログイン
`LoginActivity.kt`

```kotlin
NCMBUser.loginInBackground(name, password) { user, e ->
    if (e != null) {
        // ログイン失敗時の処理
    } else {
        // ログイン成功時の処理
    }
}
```

### 会員情報の取得
`MainActivity.kt`

```kotlin
// カレントユーザ情報の取得
val userInfo = NCMBUser.getCurrentUser()

userInfo.fetchInBackground { userObject, e ->
    if (e != null) {
        // ユーザー情報の取得が失敗した場合の処理
    } else {
        // ユーザー情報の取得が成功した場合の処理
    }
}
```

## 参考

サンプルコードをカスタマイズすることで、様々な機能を実装できます！
データ保存・データ検索・会員管理・プッシュ通知などの機能を実装したい場合には、以下のドキュメント（for Java）もご参照ください。

* [ドキュメント](https://mbaas.nifcloud.com/doc/current/)
* [ドキュメント・データストア](https://mbaas.nifcloud.com/doc/current/datastore/basic_usage_android.html)
* [ドキュメント・会員管理](https://mbaas.nifcloud.com/doc/current/user/basic_usage_android.html)
* [ドキュメント・プッシュ通知](https://mbaas.nifcloud.com/doc/current/push/basic_usage_android.html)
