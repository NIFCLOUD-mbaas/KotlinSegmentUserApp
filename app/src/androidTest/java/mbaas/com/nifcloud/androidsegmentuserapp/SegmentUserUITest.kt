package mbaas.com.nifcloud.androidsegmentuserapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import mbaas.com.nifcloud.androidsegmentuserapp.Utils.waitFor
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class SegmentUserUITest {
    var ivLogo: ViewInteraction? = null
    var edtName: ViewInteraction? = null
    var edtPass: ViewInteraction? = null
    var btnLogin: ViewInteraction? = null
    var edtNewKey: ViewInteraction? = null
    var edtNewValue: ViewInteraction? = null
    var btnUpdate: ViewInteraction? = null

    @get:Rule
    val activityRule = ActivityScenarioRule(
            MainActivity::class.java
    )

    @Before
    fun setup() {
        ivLogo = onView(withId(R.id.iv_logo))
        edtName = onView(withId(R.id.input_name))
        edtPass = onView(withId(R.id.input_password))
        btnLogin = onView(withId(R.id.btn_login))
        edtNewKey = onView(withId(R.id.txtNewKey))
        edtNewValue = onView(withId(R.id.txtNewValue))
        btnUpdate = onView(withId(R.id.btnUpdate))

        edtName!!.perform(typeText("ANTHOD"))
        edtPass!!.perform(typeText("123456"), closeSoftKeyboard())
        btnLogin!!.perform(scrollTo()).perform(click())
        onView(isRoot()).perform(waitFor(5000))
        onView(withText("ログインに成功しました。")).check(matches(isDisplayed()))
    }

    @Test
    fun validate_empty_key() {
        edtNewKey!!.perform(typeText(""), closeSoftKeyboard())
        edtNewValue!!.perform(typeText("values"), closeSoftKeyboard())
        btnUpdate!!.check(matches(isDisplayed())).perform(click())
        onView(isRoot()).perform(waitFor(5000))
        onView(withText("新規key値を入力ください!")).check(matches(isDisplayed()))
    }

    @Test
    fun validate_special_key() {
        edtNewKey!!.perform(typeText("key key"), closeSoftKeyboard())
        edtNewValue!!.perform(typeText("values"), closeSoftKeyboard())
        btnUpdate!!.check(matches(isDisplayed())).perform(click())
        onView(isRoot()).perform(waitFor(5000))
        onView(withText("key値は半角英数字のみです!")).check(matches(isDisplayed()))
    }

    @Test
    fun validate_same_default_key() {
        edtNewKey!!.perform(typeText("acl"), closeSoftKeyboard())
        edtNewValue!!.perform(typeText("values"), closeSoftKeyboard())
        btnUpdate!!.check(matches(isDisplayed())).perform(click())
        onView(isRoot()).perform(waitFor(5000))
        onView(withText("key値は更新対象名ではありません!")).check(matches(isDisplayed()))
    }

    @Test
    fun updateSuccess() {
        edtNewKey!!.perform(typeText("keyName"), closeSoftKeyboard())
        edtNewValue!!.perform(typeText("values"), closeSoftKeyboard())
        btnUpdate!!.check(matches(isDisplayed())).perform(click())
        onView(isRoot()).perform(waitFor(5000))
        onView(withText("更新に成功しました。")).check(matches(isDisplayed()))
    }
}