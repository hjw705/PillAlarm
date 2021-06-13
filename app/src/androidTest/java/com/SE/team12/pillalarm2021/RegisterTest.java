package com.SE.team12.pillalarm2021;

import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;



@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterTest {

    @Rule
    public ActivityScenarioRule<Register> activityTestRule = new ActivityScenarioRule<Register>(Register.class);

    @Test
    public void onCreate() throws Exception {

        onView(withId(R.id.register_id)).perform(typeText("iiii@gmail.com"));
        onView(withId(R.id.register_pw)).perform(typeText("abcd1234"));
        onView(withId(R.id.register)).perform(scrollTo()).perform(click());

        onView(withId(R.id.login_layout)).check(ViewAssertions.matches(isDisplayed()));

    }

}
