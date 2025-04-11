/*
 * (C) Copyright 2025 Boni Garcia (https://bonigarcia.github.io/)
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
package es.uc3m.android.test

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Rule
import org.junit.Test

class AddRecipeTest {

    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun addRecipeTest() {
        // Exercise: click on +
        val add = context.getString(R.string.add)
        composeTestRule.onNodeWithContentDescription(add).performClick()

        // Exercise: add recipe
        composeTestRule.onNode(hasText(context.getString(R.string.name)))
            .performTextInput("My recipe")
        composeTestRule.onNode(hasText(context.getString(R.string.ingredients)))
            .performTextInput("My ingredients")
        val accept = context.getString(R.string.accept)
        composeTestRule.onNodeWithText(accept).performClick()

        // Verify: we're back to home
        composeTestRule.onNodeWithContentDescription(accept).assertIsNotDisplayed()
        composeTestRule.onNodeWithContentDescription(add).assertIsDisplayed()
    }

}