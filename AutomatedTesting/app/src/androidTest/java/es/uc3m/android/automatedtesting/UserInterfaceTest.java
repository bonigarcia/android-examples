/*
 * (C) Copyright 2023 Boni Garcia (https://bonigarcia.github.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package es.uc3m.android.automatedtesting;

import android.view.View;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserInterfaceTest {

    @Before
    public void setup() {
        ActivityScenario.launch(MainActivity.class); // Launch MainActivity
    }

    // Alternative way to launch MainActivity
    // @Rule
    // public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void basicUITest() {
        // 1. Exercise activity
        // 1.1 Type "john" in edit text
        Matcher<View> view1 = ViewMatchers.withId(R.id.editText);
        ViewAction action1 = ViewActions.typeText("John");
        Espresso.onView(view1).perform(action1);

        // 1.2 Click on button
        Matcher<View> view2 = ViewMatchers.withId(R.id.button);
        ViewAction action2 = ViewActions.click();
        Espresso.onView(view2).perform(action2);

        // 2. Verify outcome
        Matcher<View> view3 = ViewMatchers.withId(R.id.textView);
        ViewAssertion assertion1 = ViewAssertions.matches(
                ViewMatchers.withText(CoreMatchers.containsString("Hello, John!")));
        Espresso.onView(view3).check(assertion1);
    }

}