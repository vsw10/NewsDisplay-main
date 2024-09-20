/*
 * Copyright 2016 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.husqvarna.newsapp.realm

import androidx.recyclerview.widget.RecyclerView
import io.realm.OrderedCollectionChangeSet
import io.realm.OrderedRealmCollection
import io.realm.OrderedRealmCollectionChangeListener
import io.realm.RealmList
import io.realm.RealmModel
import io.realm.RealmResults

/**
 * The RealmBaseRecyclerAdapter class is an abstract utility class for binding RecyclerView UI elements to Realm data.
 *
 *
 * This adapter will automatically handle any updates to its data and call `notifyDataSetChanged()`,
 * `notifyItemInserted()`, `notifyItemRemoved()` or `notifyItemRangeChanged()` as appropriate.
 *
 *
 * The RealmAdapter will stop receiving updates if the Realm instance providing the [OrderedRealmCollection] is
 * closed.
 *
 *
 * If the adapter contains Realm model classes with a primary key that is either an `int` or a `long`, call
 * `setHasStableIds(true)` in the constructor and override [.getItemId] as described by the Javadoc in that method.
 *
 * @param <T> type of [RealmModel] stored in the adapter.
 * @param <S> type of RecyclerView.ViewHolder used in the adapter.
 * @see RecyclerView.Adapter.setHasStableIds
 * @see RecyclerView.Adapter.getItemId
</S></T> */
abstract class RealmRecyclerViewAdapter<T : RealmModel?, S : RecyclerView.ViewHolder?>
@JvmOverloads constructor(
    data: OrderedRealmCollection<T>?, autoUpdate: Boolean,
    updateOnModification: Boolean = true
) : RecyclerView.Adapter<S>() {
    private val hasAutoUpdates: Boolean
    private val updateOnModification: Boolean
    private val listener: OrderedRealmCollectionChangeListener<*>?

    /**
     * Returns data associated with this adapter.
     *
     * @return adapter data.
     */
    var data: OrderedRealmCollection<T>?
        private set

    private fun createListener(): OrderedRealmCollectionChangeListener<*> {
        return OrderedRealmCollectionChangeListener<Any?> { collection, changeSet ->
            if (changeSet.state == OrderedCollectionChangeSet.State.INITIAL) {
                notifyDataSetChanged()
                return@OrderedRealmCollectionChangeListener
            }
            // For deletions, the adapter has to be notified in reverse order.
            val deletions = changeSet.deletionRanges
            for (i in deletions.indices.reversed()) {
                val range = deletions[i]
                notifyItemRangeRemoved(range.startIndex + dataOffset(), range.length)
            }

            val insertions = changeSet.insertionRanges
            for (range in insertions) {
                notifyItemRangeInserted(range.startIndex + dataOffset(), range.length)
            }

            if (!updateOnModification) {
                return@OrderedRealmCollectionChangeListener
            }

            val modifications = changeSet.changeRanges
            for (range in modifications) {
                notifyItemRangeChanged(range.startIndex + dataOffset(), range.length)
            }
        }
    }

    /**
     * Returns the number of header elements before the Realm collection elements. This is needed so
     * all indexes reported by the [OrderedRealmCollectionChangeListener] can be adjusted
     * correctly. Failing to override can cause the wrong elements to animate and lead to runtime
     * exceptions.
     *
     * @return The number of header elements in the RecyclerView before the collection elements. Default is `0`.
     */
    fun dataOffset(): Int {
        return 0
    }

    /**
     * @param data collection data to be used by this adapter.
     * @param autoUpdate when it is `false`, the adapter won't be automatically updated when collection data
     * changes.
     * @param updateOnModification when it is `true`, this adapter will be updated when deletions, insertions or
     * modifications happen to the collection data. When it is `false`, only
     * deletions and insertions will trigger the updates. This param will be ignored if
     * `autoUpdate` is `false`.
     */
    /**
     * This is equivalent to `RealmRecyclerViewAdapter(data, autoUpdate, true)`.
     *
     * @param data collection data to be used by this adapter.
     * @param autoUpdate when it is `false`, the adapter won't be automatically updated when collection data
     * changes.
     * @see .RealmRecyclerViewAdapter
     */
    init {
        check(!(data != null && !data.isManaged)) {
            "Only use this adapter with managed RealmCollection, " +
                    "for un-managed lists you can just use the BaseRecyclerViewAdapter"
        }
        this.data = data
        this.hasAutoUpdates = autoUpdate
        this.listener = if (hasAutoUpdates) createListener() else null
        this.updateOnModification = updateOnModification
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        if (hasAutoUpdates && isDataValid) {
            addListener(data!!)
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        if (hasAutoUpdates && isDataValid) {
            removeListener(data!!)
        }
    }

    override fun getItemCount(): Int {
        return if (isDataValid) data!!.size else 0
    }

    /**
     * Returns the item in the underlying data associated with the specified position.
     *
     * This method will return `null` if the Realm instance has been closed or the index
     * is outside the range of valid adapter data (which e.g. can happen if [.getItemCount]
     * is modified to account for header or footer views.
     *
     * Also, this method does not take into account any header views. If these are present, modify
     * the `index` parameter accordingly first.
     *
     * @param index index of the item in the original collection backing this adapter.
     * @return the item at the specified position or `null` if the position does not exists or
     * the adapter data are no longer valid.
     */
    fun getItem(index: Int): T? {
        require(index >= 0) { "Only indexes >= 0 are allowed. Input was: $index" }

        // To avoid exception, return null if there are some extra positions that the
        // child adapter is adding in getItemCount (e.g: to display footer view in recycler view)
        if (data != null && index >= data!!.size) return null
        return if (isDataValid) data!![index] else null
    }

    /**
     * Updates the data associated to the Adapter. Useful when the query has been changed.
     * If the query does not change you might consider using the automaticUpdate feature.
     *
     * @param data the new [OrderedRealmCollection] to display.
     */
    fun updateData(data: OrderedRealmCollection<T>?) {
        if (hasAutoUpdates) {
            if (isDataValid) {
                removeListener(this.data!!)
            }
            if (data != null) {
                addListener(data)
            }
        }

        this.data = data
        notifyDataSetChanged()
    }

    private fun addListener(data: OrderedRealmCollection<T>) {
        if (data is RealmResults<*>) {
            val results = data as RealmResults<T>
            results.addChangeListener(listener as OrderedRealmCollectionChangeListener<RealmResults<T>>)
        } else if (data is RealmList<*>) {
            val list = data as RealmList<T>
            list.addChangeListener(listener as OrderedRealmCollectionChangeListener<RealmList<T>>)
        } else {
            throw IllegalArgumentException("RealmCollection not supported: " + data.javaClass)
        }
    }

    private fun removeListener(data: OrderedRealmCollection<T>) {
        if (data is RealmResults<*>) {
            val results = data as RealmResults<T>
            results.removeChangeListener(listener as OrderedRealmCollectionChangeListener<RealmResults<T>>)
        } else if (data is RealmList<*>) {
            val list = data as RealmList<T>
            list.removeChangeListener(listener as OrderedRealmCollectionChangeListener<RealmList<T>>)
        } else {
            throw IllegalArgumentException("RealmCollection not supported: " + data.javaClass)
        }
    }

    private val isDataValid: Boolean
        get() = data != null && data!!.isValid
}