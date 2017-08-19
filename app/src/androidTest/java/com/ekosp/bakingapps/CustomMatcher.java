package com.ekosp.bakingapps;

import android.content.res.Resources;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.view.View;
import android.widget.TextView;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Created by eko.mct on 19-Aug-17.
 */

public class CustomMatcher {

    /**
     * Original source from Espresso library, modified to handle spanned fields
     *
     * Returns a matcher that matches a descendant of {@link TextView} that is
     * displaying the string associated with the given resource id.
     *
     * @param resourceId
     *            the string resource the text view is expected to hold.
     */
    public static Matcher<View> withText(final int resourceId) {

        return new BoundedMatcher<View, TextView>(TextView.class) {
            private String resourceName = null;
            private String expectedText = null;

            @Override
            public void describeTo(Description description) {
                description.appendText("with string from resource id: ");
                description.appendValue(resourceId);
                if (null != this.resourceName) {
                    description.appendText("[");
                    description.appendText(this.resourceName);
                    description.appendText("]");
                }
                if (null != this.expectedText) {
                    description.appendText(" value: ");
                    description.appendText(this.expectedText);
                }
            }

            @Override
            public boolean matchesSafely(TextView textView) {
                if (null == this.expectedText) {
                    try {
                        this.expectedText = textView.getResources().getString(
                                resourceId);
                        this.resourceName = textView.getResources()
                                .getResourceEntryName(resourceId);
                    } catch (Resources.NotFoundException ignored) {
                    /*
                     * view could be from a context unaware of the resource
                     * id.
                     */
                    }
                }
                if (null != this.expectedText) {
                    return this.expectedText.equals(textView.getText()
                            .toString());
                } else {
                    return false;
                }
            }
        };
    }


}
