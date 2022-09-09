package com.franzandel.dicodingintermediatesubmission.custom

import android.content.res.Resources
import android.content.res.Resources.NotFoundException
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher


/**
 * Created by Franz Andel
 * on 08 September 2022.
 */

class RecyclerViewMatcher(private val recyclerViewId: Int) {

    fun atPosition(position: Int): Matcher<View> {
        return atPositionOnView(position, UNSPECIFIED)
    }

    fun atPositionOnView(position: Int, targetViewId: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            var resources: Resources? = null
            var recycler: RecyclerView? = null
            var holder: RecyclerView.ViewHolder? = null
            override fun describeTo(description: Description) {
                if (recycler == null) {
                    description.appendText("RecyclerView with " + getResourceName(recyclerViewId))
                    return
                }
                if (holder == null) {
                    description.appendText(
                        String.format(
                            "in RecyclerView (%s) at position %s",
                            getResourceName(recyclerViewId), position
                        )
                    )
                    return
                }
                if (targetViewId == UNSPECIFIED) {
                    description.appendText(
                        String.format(
                            "in RecyclerView (%s) at position %s",
                            getResourceName(recyclerViewId), position
                        )
                    )
                    return
                }
                description.appendText(
                    String.format(
                        "in RecyclerView (%s) at position %s and with %s",
                        getResourceName(recyclerViewId),
                        position,
                        getResourceName(targetViewId)
                    )
                )
            }

            private fun getResourceName(id: Int): String {
                return try {
                    "R.id." + resources?.getResourceEntryName(id)
                } catch (ex: NotFoundException) {
                    String.format("resource id %s - name not found", id)
                }
            }

            public override fun matchesSafely(view: View): Boolean {
                resources = view.resources
                recycler = view.rootView.findViewById(recyclerViewId)
                if (recycler == null) return false
                holder = recycler?.findViewHolderForAdapterPosition(position)
                if (holder == null) return false
                return if (targetViewId == UNSPECIFIED) {
                    view === holder?.itemView
                } else {
                    view === holder?.itemView?.findViewById<View>(targetViewId)
                }
            }
        }
    }

    companion object {
        const val UNSPECIFIED = -1
    }
}
