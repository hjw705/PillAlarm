package com.SE.team12.pillalarm2021;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MainActivityTest {

    private static final String MESSAGE = "This is a test";
    private SettingAlarm s;

    @Rule
    public ActivityScenarioRule<MainActivity> activityTestRule = new ActivityScenarioRule<MainActivity>(MainActivity.class);
    @Test
    public void onCreate() throws Exception {
        onView(withId(R.id.fab)).perform(click());

        onView(withId(R.id.reminder_title)).perform(typeText("Tylenol"));
        onView(withId(R.id.tgbtn_mon_repeat)).perform(click());
        onView(withId(R.id.test)).perform(click());
        Thread.sleep(1000);
    }
}