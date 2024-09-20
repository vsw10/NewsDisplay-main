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

import android.widget.BaseAdapter
import io.realm.OrderedRealmCollection
import io.realm.OrderedRealmCollectionChangeListener
import io.realm.RealmChangeListener
import io.realm.RealmList
import io.realm.RealmModel
import io.realm.RealmResults

/**
 * The RealmBaseAdapter class is an abstract utility class for binding UI elements to Realm data, much like an
 * [android.widget.CursorAdapter].
 *
 *
 * This adapter will automatically handle any updates to its data and call [.notifyDataSetChanged] as
 * appropriate.
 *
 *
 * The RealmAdapter will stop receiving updates if the Realm instance providing the [io.realm.RealmResults] is
 * closed. Trying to access Realm objects will at this point also result in a `IllegalStateException`.
 */
abstract class RealmBaseAdapter<T : RealmModel?>(data: OrderedRealmCollection<T>?) : BaseAdapter() {
    protected var adapterData: OrderedRealmCollection<T>?
    private val listener: RealmChangeListener<OrderedRealmCollection<T>>?

    init {
        check(!(data != null && !data.isManaged)) {
            "Only use this adapter with managed list, " +
                    "for un-managed lists you can just use the BaseAdapter"
        }
        this.adapterData = data
        this.listener = RealmChangeListener { notifyDataSetChanged() }

        if (isDataValid) {
            addListener(data!!)
        }
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

    /**
     * Returns how many items are in the data set.
     *
     * @return the number of items.
     */
    override fun getCount(): Int {
        return if (isDataValid) adapterData!!.size else 0
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     * data set.
     * @return The data at the specified position.
     */
    override fun getItem(position: Int): T? {
        return if (isDataValid) adapterData!![position] else null
    }

    /**
     * Get the row id associated with the specified position in the list. Note that item IDs are not stable so you
     * cannot rely on the item ID being the same after [.notifyDataSetChanged] or
     * [.updateData] has been called.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    override fun getItemId(position: Int): Long {
        // TODO: find better solution once we have unique IDs
        return position.toLong()
    }

    /**
     * Updates the data associated with the Adapter.
     *
     * Note that RealmResults and RealmLists are "live" views, so they will automatically be updated to reflect the
     * latest changes. This will also trigger `notifyDataSetChanged()` to be called on the adapter.
     *
     * This method is therefore only useful if you want to display data based on a new query without replacing the
     * adapter.
     *
     * @param data the new [OrderedRealmCollection] to display.
     */
    fun updateData(data: OrderedRealmCollection<T>?) {
        if (listener != null) {
            if (isDataValid) {
                removeListener(adapterData!!)
            }
            if (data != null && data.isValid) {
                addListener(data)
            }
        }

        this.adapterData = data
        notifyDataSetChanged()
    }

    private val isDataValid: Boolean
        get() = adapterData != null && adapterData!!.isValid
}