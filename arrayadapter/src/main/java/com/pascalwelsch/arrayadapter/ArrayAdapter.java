/*
 * Copyright (C) 2017 Pascal Welsch
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
 */

package com.pascalwelsch.arrayadapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Simple adapter implementation analog to {@link android.widget.ArrayAdapter} for {@link
 * android.support.v7.widget.RecyclerView}. Holds to a list of objects of type {@link T}
 *
 * Created by Pascal Welsch on 04.07.14.
 */
public abstract class ArrayAdapter<T, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    /**
     * Lock used to modify the content of {@link #mObjects}. Any write operation
     * performed on the array should be synchronized on this lock.
     */
    private final Object mLock = new Object();

    private List<T> mObjects;

    public ArrayAdapter(@Nullable final List<T> objects) {
        mObjects = objects != null ? objects : new ArrayList<T>();
    }

    public ArrayAdapter() {
        this(null);
    }

    /**
     * Adds the specified object at the end of the array.
     *
     * @param object The object to add at the end of the array.
     */
    public void add(@Nullable final T object) {
        final int position;
        synchronized (mLock) {
            position = getItemCount();
            mObjects.add(object);
        }
        notifyItemInserted(position);
    }

    /**
     * Adds the specified list of objects at the end of the array.
     *
     * @param collection The objects to add at the end of the array.
     */
    public void addAll(@NonNull final Collection<T> collection) {
        final int length = collection.size();
        if (length == 0) {
            return;
        }
        final int position;
        synchronized (mLock) {
            position = getItemCount();
            mObjects.addAll(collection);
        }
        notifyItemRangeInserted(position, length);
    }

    /**
     * Adds the specified items at the end of the array.
     *
     * @param items The items to add at the end of the array.
     */
    public void addAll(T... items) {
        final int length = items.length;
        if (length == 0) {
            return;
        }
        final int position;
        synchronized (mLock) {
            position = getItemCount();
            Collections.addAll(mObjects, items);
        }
        notifyItemRangeInserted(position, length);
    }

    /**
     * Remove all elements from the list.
     */
    public void clear() {
        if (mObjects.isEmpty()) {
            return;
        }
        final int size;
        synchronized (mLock) {
            size = getItemCount();
            mObjects.clear();
        }
        notifyItemRangeRemoved(0, size);
    }

    @Nullable
    public T getItem(final int position) {
        return mObjects.get(position);
    }

    @Override
    public int getItemCount() {
        return mObjects.size();
    }

    /**
     * Return a stable id for an item. The item doesn't have to be part of the underlying data set.
     *
     * If you don't have an id field simply return the {@code item} itself
     *
     * @param item for which a stable id should be generated
     * @return a identifier for the given item
     */
    public abstract Object getItemId(T item);

    /**
     * Returns the position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     * @return The position of the specified item.
     */
    public int getPosition(final T item) {
        return mObjects.indexOf(item);
    }

    /**
     * Inserts the specified object at the specified index in the array.
     *
     * @param object The object to insert into the array.
     * @param index  The index at which the object must be inserted.
     */
    public void insert(@Nullable T object, int index) {
        synchronized (mLock) {
            mObjects.add(index, object);
        }
        notifyItemInserted(index);
    }

    /**
     * Called by the DiffUtil when it wants to check whether two items have the same data.
     * DiffUtil uses this information to detect if the contents of an item has changed.
     * <p>
     * DiffUtil uses this method to check equality instead of {@link Object#equals(Object)}
     * so that you can change its behavior depending on your UI.
     * For example, if you are using DiffUtil with a
     * {@link android.support.v7.widget.RecyclerView.Adapter RecyclerView.Adapter}, you should
     * return whether the items' visual representations are the same.
     * <p>
     * This method is called only if {@link #isItemTheSame(Object, Object)} returns
     * {@code true} for these items.
     *
     * @param oldItem The position of the item in the old list
     * @param newItem The position of the item in the new list which replaces the
     *                oldItem
     * @return True if the contents of the items are the same or false if they are different.
     */
    public boolean isContentTheSame(@Nullable final T oldItem, @Nullable final T newItem) {
        return (oldItem == newItem) || (oldItem != null && oldItem.equals(newItem));
    }

    /**
     * Called by the DiffUtil to decide whether two object represent the same Item.
     * <p>
     * For example, if your items have unique ids, this method should check their id equality.
     *
     * @param oldItem The position of the item in the old list
     * @param newItem The position of the item in the new list
     * @return True if the two items represent the same object or false if they are different.
     */
    public boolean isItemTheSame(@Nullable final T oldItem, @Nullable final T newItem) {

        if (oldItem == null && newItem == null) {
            return true;
        }
        if (oldItem == null || newItem == null) {
            return false;
        }

        final Object oldId = getItemId(oldItem);
        final Object newId = getItemId(newItem);

        return (oldId == newId) || (oldId != null && oldId.equals(newId));
    }

    /**
     * Removes the specified object from the array.
     *
     * @param object The object to remove.
     */
    public void remove(@Nullable T object) {
        final int position;
        final boolean remove;
        synchronized (mLock) {
            position = getPosition(object);
            remove = mObjects.remove(object);
        }
        if (remove) {
            notifyItemRemoved(position);
        }
    }

    public void replaceItem(final T oldObject, final T newObject) {
        final int position = getPosition(oldObject);

        synchronized (mLock) {
            mObjects.remove(position);
            mObjects.add(position, newObject);
        }

        if (isItemTheSame(oldObject, newObject)) {
            if (!isContentTheSame(oldObject, newObject)) {
                // no notify, content did not change
                return;
            }
            notifyItemChanged(position, newObject);
        } else {
            notifyItemRemoved(position);
            notifyItemInserted(position);
        }
    }

    /**
     * Sorts the content of this adapter using the specified comparator.
     *
     * @param comparator The comparator used to sort the objects contained in this adapter.
     */
    public void sort(Comparator<? super T> comparator) {
        final ArrayList<T> copy = new ArrayList<>(mObjects);
        Collections.sort(copy, comparator);
        swap(copy);
    }

    /**
     * replaces the data with the given list
     *
     * @param newObjects new data
     */
    public void swap(final List<T> newObjects) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public boolean areContentsTheSame(final int oldItemPosition,
                    final int newItemPosition) {
                final T oldItem = mObjects.get(oldItemPosition);
                final T newItem = newObjects.get(newItemPosition);
                return isContentTheSame(oldItem, newItem);
            }

            @Override
            public boolean areItemsTheSame(final int oldItemPosition, final int newItemPosition) {
                final T oldItem = mObjects.get(oldItemPosition);
                final T newItem = newObjects.get(newItemPosition);
                return isItemTheSame(oldItem, newItem);
            }

            @Override
            public int getNewListSize() {
                return newObjects != null ? newObjects.size() : 0;
            }

            @Override
            public int getOldListSize() {
                return mObjects != null ? mObjects.size() : 0;
            }
        });
        synchronized (mLock) {
            mObjects = newObjects;
        }
        result.dispatchUpdatesTo(this);
    }
}
