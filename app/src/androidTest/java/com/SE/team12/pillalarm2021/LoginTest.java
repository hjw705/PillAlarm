package com.SE.team12.pillalarm2021;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class LoginTest {

    private static final String MESSAGE = "This is a test";
    @Rule
    public ActivityScenarioRule<Login> activityTestRule = new ActivityScenarioRule<Login>(Login.class);

    @Test
    public void onCreate() throws Exception {

        onView(withId(R.id.login_id)).perform(typeText("aaaa@gmail.com"));
        onView(withId(R.id.login_pw)).perform(typeText("abcd1234"));
        onView(withId(R.id.login_submit)).perform(scrollTo()).perform(click());

        Thread.sleep(1000);

    }

}