package mbaas.com.nifcloud.androidsegmentuserapp

import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import mbaas.com.nifcloud.androidsegmentuserapp.MainActivity
import mbaas.com.nifcloud.androidsegmentuserapp.R
import mbaas.com.nifcloud.androidsegmentuserapp.Utils.waitFor
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginUITest {
    var ivLogo: ViewInteraction? = null
    var edtName: ViewInteraction? = null
    var edtPass: ViewInteraction? = null
    var btnLogin: ViewInteraction? = null
    var tvLinkSignup: ViewInteraction? = null

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
        tvLinkSignup = onView(withId(R.id.link_signup))
    }

    @Test
    fun test01_initialScreen() {
        ivLogo!!.check(matches(isDisplayed()))
        edtName!!.check(matches(isDisplayed()))
        edtPass!!.check(matches(isDisplayed()))
        btnLogin!!.check(matches(withText("Login")))
        tvLinkSignup!!.check(matches(withText("No account yet? Create one")))
    }

    @Test
    fun test02_validate_user_name() {
        edtName!!.perform(typeText(""))
        edtPass!!.perform(typeText("123456"), closeSoftKeyboard())
        btnLogin!!.perform(scrollTo()).perform(click())
        edtName!!.check(matches(hasErrorText("enter username")))
    }

    @Test
    fun test03_validate_empty_pass() {
        edtName!!.perform(typeText("Hoge"))
        edtPass!!.perform(typeText(""), closeSoftKeyboard())
        btnLogin!!.perform(scrollTo()).perform(click())
        edtPass!!.check(matches(hasErrorText("between 4 and 10 alphanumeric characters")))
    }

    @Test
    fun test04_validate_less_pass_length() {
        edtName!!.perform(typeText("Hoge"))
        edtPass!!.perform(typeText("123"), closeSoftKeyboard())
        btnLogin!!.perform(scrollTo()).perform(click())
        edtPass!!.check(matches(hasErrorText("between 4 and 10 alphanumeric characters")))
    }

    @Test
    fun test05_validate_over_pass_length() {
        edtName!!.perform(typeText("Hoge"))
        edtPass!!.perform(typeText("12345678901"), closeSoftKeyboard())
        btnLogin!!.perform(scrollTo()).perform(click())
        edtPass!!.check(matches(hasErrorText("between 4 and 10 alphanumeric characters")))
    }

    @Test
    fun test06_openSignup() {
        tvLinkSignup!!.perform(scrollTo()).perform(click())
        onView(withId(R.id.btn_signup)).check(matches(withText("Create Account")))
    }

    @Test
    fun test07_doLogin() {
        edtName!!.perform(typeText("ANTHOD"))
        edtPass!!.perform(typeText("123456"), closeSoftKeyboard())
        btnLogin!!.perform(scrollTo()).perform(click())
        onView(isRoot()).perform(waitFor(5000))
        onView(withText("ログインに成功しました。")).check(matches(isDisplayed()))
    }
}