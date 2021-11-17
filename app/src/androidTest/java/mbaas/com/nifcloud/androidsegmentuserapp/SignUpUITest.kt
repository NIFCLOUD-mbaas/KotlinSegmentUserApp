package mbaas.com.nifcloud.androidsegmentuserapp

import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
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
class SignUpUITest {

    var ivLogo: ViewInteraction? = null
    var edtName: ViewInteraction? = null
    var edtPass: ViewInteraction? = null
    var btnSignup: ViewInteraction? = null
    var tvLinkLogin: ViewInteraction? = null

    @get:Rule
    val activityRule = ActivityScenarioRule(
            MainActivity::class.java
    )

    @Before
    fun setup() {
        onView(withId(R.id.link_signup)).perform(click())
        ivLogo = onView(withId(R.id.iv_logo))
        edtName = onView(withId(R.id.input_name))
        edtPass = onView(withId(R.id.input_password))
        btnSignup = onView(withId(R.id.btn_signup))
        tvLinkLogin = onView(withId(R.id.link_login))
    }

    @Test
    fun test01_initialScreen() {
        ivLogo!!.check(matches(isDisplayed()))
        edtName!!.check(matches(isDisplayed()))
        edtPass!!.check(matches(isDisplayed()))
        btnSignup!!.check(matches(withText("Create Account")))
        tvLinkLogin!!.check(matches(withText("Already a member? Login")))
    }

    @Test
    fun test02_validate_empty_user_name() {
        edtName!!.perform(typeText(""))
        edtPass!!.perform(typeText("123456"), closeSoftKeyboard())
        btnSignup!!.perform(scrollTo()).perform(click())
        edtName!!.check(matches(hasErrorText("at least 3 characters")))
    }

    @Test
    fun test03_validate_less_user_name_length() {
        edtName!!.perform(typeText("ho"))
        edtPass!!.perform(typeText("123456"), closeSoftKeyboard())
        btnSignup!!.perform(scrollTo()).perform(click())
        edtName!!.check(matches(hasErrorText("at least 3 characters")))
    }

    @Test
    fun test04_validate_empty_pass() {
        edtName!!.perform(typeText("hoge"))
        edtPass!!.perform(typeText(""), closeSoftKeyboard())
        btnSignup!!.perform(scrollTo()).perform(click())
        edtPass!!.check(matches(hasErrorText("between 4 and 10 alphanumeric characters")))
    }

    @Test
    fun test05_validate_less_pass_length() {
        edtName!!.perform(typeText("hoge"))
        edtPass!!.perform(typeText("123"), closeSoftKeyboard())
        btnSignup!!.perform(scrollTo()).perform(click())
        edtPass!!.check(matches(hasErrorText("between 4 and 10 alphanumeric characters")))
    }

    @Test
    fun test06_validate_over_pass_length() {
        edtName!!.perform(typeText("hoge"))
        edtPass!!.perform(typeText("12345678901"), closeSoftKeyboard())
        btnSignup!!.perform(scrollTo()).perform(click())
        edtPass!!.check(matches(hasErrorText("between 4 and 10 alphanumeric characters")))
    }

    @Test
    fun test07_openLogin() {
        tvLinkLogin!!.perform(scrollTo()).perform(click())
        onView(withId(R.id.btn_login)).check(matches(withText("Login")))
    }

    @Test
    fun test08_doLogin() {
        edtName!!.perform(typeText("ANTHOD"))
        edtPass!!.perform(typeText("123456"), closeSoftKeyboard())
        btnSignup!!.perform(click())
        onView(isRoot()).perform(waitFor(5000))
        onView(withText("ログインに成功しました。")).check(matches(isDisplayed()))
    }
}