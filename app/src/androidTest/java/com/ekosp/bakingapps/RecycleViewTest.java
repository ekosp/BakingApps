package com.ekosp.bakingapps;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by eko.purnomo on 15/08/2017.
 * You can contact me at : ekosetyopurnomo@gmail.com
 * or for more detail at  : http://ekosp.com
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecycleViewTest {

    private static final int ITEM_SELECTED = 1;

    @Rule
    public ActivityTestRule<RecipeListActivity> mRecipeListRule = new ActivityTestRule<>(
            RecipeListActivity.class);


    @Test
    public void clickOnRecycleView() {
        onView(ViewMatchers.withId(R.id.recycler_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition(ITEM_SELECTED, click()));

        // Match the text in an item below the fold and check that it's displayed.
        String itemElementText = mRecipeListRule.getActivity().getResources().getString(
                R.string.text_servings) + String.valueOf(ITEM_SELECTED);
        onView(withText(itemElementText)).check(matches(isDisplayed()));
    }
}
