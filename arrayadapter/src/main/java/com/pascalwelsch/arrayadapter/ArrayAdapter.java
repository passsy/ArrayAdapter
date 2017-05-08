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

    public interface StableIdProvider<T> {

        Object getId(T item);
    }

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
        notifyItemRangeRemoved(position, 1);
        notifyItemRangeInserted(position, 1);
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
     * @param objects new data
     */
    public void swap(final List<T> objects) {
        swap(objects, new StableIdProvider<T>() {
            @Override
            public Object getId(final T item) {
                // id is unknown, at least return the item itself as id so it doesn't get
                // notified when it doesn't change at all.
                return item;
            }
        });
    }

    /**
     * replaces the data with the given list
     *
     * @param newObjects new data
     * @param idProvider function to determine an identification for each element to detect same
     *                   (but updated) items
     */
    public void swap(final List<T> newObjects, final StableIdProvider<T> idProvider) {
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public boolean areContentsTheSame(final int oldItemPosition,
                    final int newItemPosition) {
                final T oldItem = mObjects.get(oldItemPosition);
                final T newItem = newObjects.get(newItemPosition);
                return (oldItem == newItem) || (oldItem != null && oldItem.equals(newItem));
            }

            @Override
            public boolean areItemsTheSame(final int oldItemPosition, final int newItemPosition) {
                final T oldItem = mObjects.get(oldItemPosition);
                final T newItem = newObjects.get(newItemPosition);

                if (oldItem == null && newItem == null) {
                    return true;
                }
                if (oldItem == null || newItem == null) {
                    return false;
                }

                final Object oldId = idProvider.getId(oldItem);
                final Object newId = idProvider.getId(newItem);

                return (oldId == newId) || (oldId != null && oldId.equals(newId));
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
