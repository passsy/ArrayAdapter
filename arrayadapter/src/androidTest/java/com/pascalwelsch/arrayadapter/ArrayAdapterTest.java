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


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(AndroidJUnit4.class)
public class ArrayAdapterTest {

    private static class TestAdapter extends ArrayAdapter<String, RecyclerView.ViewHolder> {

        TestAdapter(@Nullable final List<String> objects) {
            super(objects);
        }

        public TestAdapter() {
            super();
        }

        @Override
        public Object getItemId(@NonNull final String item) {
            return item;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent,
                final int viewType) {
            return null;
        }

    }

    private class User {

        private final String id;

        private final String name;

        private User(final String name, final String id) {
            this.name = name;
            this.id = id;
        }

        @SuppressWarnings("SimplifiableIfStatement")
        @Override
        public boolean equals(final Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof User)) {
                return false;
            }

            final User user = (User) o;

            if (name != null ? !name.equals(user.name) : user.name != null) {
                return false;
            }
            return id != null ? id.equals(user.id) : user.id == null;

        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (id != null ? id.hashCode() : 0);
            return result;
        }
    }

    private class UserAdapter extends ArrayAdapter<User, RecyclerView.ViewHolder> {

        @Override
        public Object getItemId(@NonNull final User item) {
            // return id, not item
            return item.id;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent,
                final int viewType) {
            return null;
        }
    }

    private TestAdapter mAdapter;

    @Test
    public void addAllEmptyList() throws Exception {
        assertThat(mAdapter.getItemCount()).isEqualTo(0);

        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        mAdapter.registerAdapterDataObserver(observer);
        mAdapter.addAll(new ArrayList<String>());
        assertThat(mAdapter.getItemCount()).isEqualTo(0);
        verifyZeroInteractions(observer);
    }

    @Test
    public void addAllListContainingNull() throws Exception {
        final ArrayList<String> nullList = new ArrayList<>();
        nullList.add("A");
        nullList.add(null);
        nullList.add("C");
        final TestAdapter adapter = new TestAdapter();
        try {
            adapter.addAll(nullList);
            fail("did no throw");
        } catch (IllegalStateException e) {
            assertThat(e).hasMessageContaining("null");
        }
    }

    @Test
    public void addAllMultiple() throws Exception {
        mAdapter.add("A");
        mAdapter.add("B");
        assertThat(mAdapter.getItemCount()).isEqualTo(2);

        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        mAdapter.registerAdapterDataObserver(observer);
        final ArrayList<String> list = new ArrayList<>();
        list.add("C");
        list.add("D");
        mAdapter.addAll(list);
        assertThat(mAdapter.getItemCount()).isEqualTo(4);
        assertThat(mAdapter.getItem(2)).isEqualTo("C");
        assertThat(mAdapter.getItem(3)).isEqualTo("D");
        verify(observer).onItemRangeInserted(2, 2);
        verifyNoMoreInteractions(observer);
    }

    @Test
    public void addAllSingle() throws Exception {
        mAdapter.add("A");
        assertThat(mAdapter.getItemCount()).isEqualTo(1);

        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        mAdapter.registerAdapterDataObserver(observer);
        final ArrayList<String> list = new ArrayList<>();
        list.add("B");
        mAdapter.addAll(list);
        assertThat(mAdapter.getItemCount()).isEqualTo(2);
        assertThat(mAdapter.getItem(1)).isEqualTo("B");
        verify(observer).onItemRangeInserted(1, 1);
        verifyNoMoreInteractions(observer);
    }

    @Test
    public void addAllVarargsEmptyList() throws Exception {
        assertThat(mAdapter.getItemCount()).isEqualTo(0);

        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        mAdapter.registerAdapterDataObserver(observer);
        mAdapter.addAll();
        assertThat(mAdapter.getItemCount()).isEqualTo(0);
        verifyZeroInteractions(observer);
    }

    @Test
    public void addAllVarargsListContainingNull() throws Exception {
        final TestAdapter adapter = new TestAdapter();
        try {
            adapter.addAll("A", null, "C");
            fail("did no throw");
        } catch (IllegalStateException e) {
            assertThat(e).hasMessageContaining("null");
        }
    }

    @Test
    public void addAllVarargsMultiple() throws Exception {
        mAdapter.add("A");
        mAdapter.add("B");
        assertThat(mAdapter.getItemCount()).isEqualTo(2);

        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        mAdapter.registerAdapterDataObserver(observer);
        mAdapter.addAll("C", "D");
        assertThat(mAdapter.getItemCount()).isEqualTo(4);
        assertThat(mAdapter.getItem(2)).isEqualTo("C");
        assertThat(mAdapter.getItem(3)).isEqualTo("D");
        verify(observer).onItemRangeInserted(2, 2);
        verifyNoMoreInteractions(observer);
    }

    @Test
    public void addAllVarargsSingle() throws Exception {
        mAdapter.add("A");
        assertThat(mAdapter.getItemCount()).isEqualTo(1);

        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        mAdapter.registerAdapterDataObserver(observer);
        mAdapter.addAll("B");
        assertThat(mAdapter.getItemCount()).isEqualTo(2);
        assertThat(mAdapter.getItem(1)).isEqualTo("B");
        verify(observer).onItemRangeInserted(1, 1);
        verifyNoMoreInteractions(observer);
    }

    @Test
    public void addMultiple() throws Exception {
        mAdapter.add("A");
        mAdapter.add("B");
        mAdapter.add("C");
        mAdapter.add("D");

        assertThat(mAdapter.getItemCount()).isEqualTo(4);

        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        mAdapter.registerAdapterDataObserver(observer);
        mAdapter.add("E");
        assertThat(mAdapter.getItemCount()).isEqualTo(5);
        assertThat(mAdapter.getItem(4)).isEqualTo("E");

        verify(observer).onItemRangeInserted(4, 1);
        verifyNoMoreInteractions(observer);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void addNull() throws Exception {
        try {
            mAdapter.add(null);
            fail("did not throw");
        } catch (IllegalStateException e) {
            assertThat(e).hasMessageContaining("null");
        }
    }

    @Test
    public void addToEmpty() throws Exception {
        assertThat(mAdapter.getItemCount()).isEqualTo(0);

        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        mAdapter.registerAdapterDataObserver(observer);
        mAdapter.add("A");
        assertThat(mAdapter.getItemCount()).isEqualTo(1);
        assertThat(mAdapter.getItem(0)).isEqualTo("A");
        verify(observer).onItemRangeInserted(0, 1);
        verifyNoMoreInteractions(observer);
    }

    @Test
    public void clear() throws Exception {
        mAdapter.add("A");
        mAdapter.add("B");

        assertThat(mAdapter.getItemCount()).isEqualTo(2);

        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        mAdapter.registerAdapterDataObserver(observer);
        mAdapter.clear();
        assertThat(mAdapter.getItemCount()).isEqualTo(0);
        verify(observer).onItemRangeRemoved(0, 2);
        verifyNoMoreInteractions(observer);
    }

    @Test
    public void clearEmpty() throws Exception {
        assertThat(mAdapter.getItemCount()).isEqualTo(0);

        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        mAdapter.registerAdapterDataObserver(observer);
        mAdapter.clear();
        assertThat(mAdapter.getItemCount()).isEqualTo(0);
        verifyZeroInteractions(observer);
    }

    @Test
    public void constructorEmptyList() throws Exception {
        final TestAdapter testAdapter = new TestAdapter(new ArrayList<String>());
        assertThat(testAdapter.getItemCount()).isEqualTo(0);
    }

    @Test
    public void constructorNull() throws Exception {
        try {
            new TestAdapter(null);
            fail("did no throw");
        } catch (IllegalStateException e) {
            assertThat(e).hasMessageContaining("null");
        }
    }

    @Test
    public void constructorWithList() throws Exception {
        final ArrayList<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        final TestAdapter testAdapter = new TestAdapter(list);
        assertThat(testAdapter.getItemCount()).isEqualTo(3);
    }

    @Test
    public void constructorWithNullValueInList() throws Exception {
        final ArrayList<String> nullList = new ArrayList<>();
        nullList.add("A");
        nullList.add(null);
        nullList.add("C");
        try {
            new TestAdapter(nullList);
            fail("did no throw");
        } catch (IllegalStateException e) {
            assertThat(e).hasMessageContaining("null");
        }
    }

    @Test
    public void constructorWithoutArgs() throws Exception {
        final TestAdapter testAdapter = new TestAdapter();
        assertThat(testAdapter.getItemCount()).isEqualTo(0);
    }

    @Test
    public void getItemNotFoundReturnsNull() throws Exception {
        assertThat(mAdapter.getItem(0)).isNull();
        assertThat(mAdapter.getItem(5)).isNull();
        assertThat(mAdapter.getItem(-1)).isNull();

        mAdapter.add("A");
        assertThat(mAdapter.getItem(0)).isEqualTo("A");
        assertThat(mAdapter.getItem(5)).isNull();
        assertThat(mAdapter.getItem(-1)).isNull();
    }

    @Test
    public void insert() throws Exception {
        mAdapter.add("A");
        mAdapter.add("B");
        mAdapter.add("C");

        assertThat(mAdapter.getItemCount()).isEqualTo(3);
        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        mAdapter.registerAdapterDataObserver(observer);
        mAdapter.insert("Z", 1);
        assertThat(mAdapter.getItemCount()).isEqualTo(4);
        assertThat(mAdapter.getItem(1)).isEqualTo("Z");
        verify(observer).onItemRangeInserted(1, 1);
        verifyNoMoreInteractions(observer);
    }

    @Test
    public void insertEmpty() throws Exception {
        assertThat(mAdapter.getItemCount()).isEqualTo(0);
        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        mAdapter.registerAdapterDataObserver(observer);
        mAdapter.insert("Z", 0);
        assertThat(mAdapter.getItemCount()).isEqualTo(1);
        assertThat(mAdapter.getItem(0)).isEqualTo("Z");
        verify(observer).onItemRangeInserted(0, 1);
        verifyNoMoreInteractions(observer);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void insertNullThrows() throws Exception {
        mAdapter.add("A");
        mAdapter.add("B");
        mAdapter.add("C");

        try {
            mAdapter.insert(null, 1);
            fail("did not throw");
        } catch (IllegalStateException e) {
            assertThat(e).hasMessageContaining("null");
        }
    }

    @Test
    public void isContentTheSame() throws Exception {
        assertThat(mAdapter.isContentTheSame(null, null)).isTrue();
        assertThat(mAdapter.isContentTheSame(null, "A")).isFalse();
        assertThat(mAdapter.isContentTheSame("A", null)).isFalse();
        assertThat(mAdapter.isContentTheSame("A", "B")).isFalse();
        assertThat(mAdapter.isContentTheSame("A", "A")).isTrue();
    }

    @Test
    public void isItemTheSame() throws Exception {
        assertThat(mAdapter.isItemTheSame(null, null)).isTrue();
        assertThat(mAdapter.isItemTheSame(null, "A")).isFalse();
        assertThat(mAdapter.isItemTheSame("A", null)).isFalse();
        assertThat(mAdapter.isItemTheSame("A", "A")).isTrue();
        assertThat(mAdapter.isItemTheSame("A", "B")).isFalse();

        mAdapter = new TestAdapter() {
            @SuppressWarnings({"UnnecessaryBoxing", "UseValueOf"})
            @Override
            public Object getItemId(@NonNull final String item) {
                // equal id, not same object
                return new Integer(1);
            }
        };
        // always the same when the same id gets returned
        assertThat(mAdapter.isItemTheSame("A", "B")).isTrue();

        mAdapter = new TestAdapter() {
            @Override
            public Object getItemId(@NonNull final String item) {
                if ("nullItemId".equals(item)) {
                    return null;
                }
                return item;
            }
        };
        assertThat(mAdapter.isItemTheSame("nullItemId", "B")).isFalse();
        assertThat(mAdapter.isItemTheSame("B", "nullItemId")).isFalse();
    }

    @Test
    public void removeEmpty() throws Exception {
        assertThat(mAdapter.getItemCount()).isEqualTo(0);
        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        mAdapter.registerAdapterDataObserver(observer);
        mAdapter.remove("B");
        assertThat(mAdapter.getItemCount()).isEqualTo(0);
        verifyZeroInteractions(observer);
    }

    @Test
    public void removeMultiple() throws Exception {
        mAdapter.add("A");
        mAdapter.add("B");
        mAdapter.add("C");
        mAdapter.add("D");

        assertThat(mAdapter.getItemCount()).isEqualTo(4);

        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        mAdapter.registerAdapterDataObserver(observer);
        mAdapter.remove("D");
        assertThat(mAdapter.getItemCount()).isEqualTo(3);
        verify(observer).onItemRangeRemoved(3, 1);
        mAdapter.remove("C");
        mAdapter.remove("B");
        assertThat(mAdapter.getItemCount()).isEqualTo(1);
        assertThat(mAdapter.getItem(0)).isEqualTo("A");
        verify(observer).onItemRangeRemoved(2, 1);
        verify(observer).onItemRangeRemoved(1, 1);
        verifyNoMoreInteractions(observer);
    }

    @Test
    public void removeNonExisting() throws Exception {
        mAdapter.add("A");
        mAdapter.add("B");
        mAdapter.add("C");
        assertThat(mAdapter.getItemCount()).isEqualTo(3);

        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        mAdapter.registerAdapterDataObserver(observer);
        mAdapter.remove("X");
        assertThat(mAdapter.getItemCount()).isEqualTo(3);
        verifyZeroInteractions(observer);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void removeNull() throws Exception {
        mAdapter.add("A");
        mAdapter.add("B");
        mAdapter.add("C");
        assertThat(mAdapter.getItemCount()).isEqualTo(3);

        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        mAdapter.registerAdapterDataObserver(observer);

        // does nothing
        mAdapter.remove(null);
        assertThat(mAdapter.getItemCount()).isEqualTo(3);
        verifyZeroInteractions(observer);
    }

    @Test
    public void removeSingle() throws Exception {
        mAdapter.add("A");
        mAdapter.add("B");
        mAdapter.add("C");
        mAdapter.add("D");

        assertThat(mAdapter.getItemCount()).isEqualTo(4);

        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        mAdapter.registerAdapterDataObserver(observer);
        mAdapter.remove("B");
        assertThat(mAdapter.getItemCount()).isEqualTo(3);
        assertThat(mAdapter.getItem(1)).isEqualTo("C");

        verify(observer).onItemRangeRemoved(1, 1);
        verifyNoMoreInteractions(observer);
    }

    @Test
    public void replaceItemByUpdatingIt() throws Exception {
        final UserAdapter adapter = new UserAdapter();
        adapter.add(new User("A", "1"));
        final User userB = new User("B", "2");
        adapter.add(userB);
        adapter.add(new User("C", "3"));
        assertThat(adapter.getItemCount()).isEqualTo(3);

        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        adapter.registerAdapterDataObserver(observer);
        final User newUser = new User("Z", "2");
        adapter.replaceItem(userB, newUser);
        assertThat(adapter.getItemCount()).isEqualTo(3);
        assertThat(adapter.getItem(1)).isEqualTo(newUser);
        verify(observer).onItemRangeChanged(1, 1, newUser);
        verifyNoMoreInteractions(observer);
    }

    @Test
    public void replaceItemUiUnchanged() throws Exception {
        final UserAdapter adapter = new UserAdapter();
        adapter.add(new User("A", "1"));
        final User userB = new User("B", "2");
        adapter.add(userB);
        adapter.add(new User("C", "3"));
        assertThat(adapter.getItemCount()).isEqualTo(3);

        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        adapter.registerAdapterDataObserver(observer);
        final User newUserB = new User("B", "2");
        adapter.replaceItem(userB, newUserB);
        assertThat(adapter.getItemCount()).isEqualTo(3);
        assertThat(adapter.getItem(1)).isEqualTo(newUserB);
        verifyZeroInteractions(observer);
    }

    @Test
    public void replaceItemWithDifferentItem() throws Exception {
        final UserAdapter adapter = new UserAdapter();
        adapter.add(new User("A", "1"));
        adapter.add(new User("B", "2"));
        adapter.add(new User("C", "3"));
        assertThat(adapter.getItemCount()).isEqualTo(3);

        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        adapter.registerAdapterDataObserver(observer);
        final User newUser = new User("Z", "otherId");
        adapter.replaceItem(new User("B", "2"), newUser);
        assertThat(adapter.getItemCount()).isEqualTo(3);
        assertThat(adapter.getItem(1)).isEqualTo(newUser);
        verify(observer).onItemRangeRemoved(1, 1);
        verify(observer).onItemRangeInserted(1, 1);
        verifyNoMoreInteractions(observer);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void replaceItemWithNullThrows() throws Exception {
        final TestAdapter adapter = new TestAdapter();
        try {
            adapter.replaceItem("A", null);
            fail("did no throw");
        } catch (IllegalStateException e) {
            assertThat(e).hasMessageContaining("null");
        }
    }

    @Test
    public void replaceNonExistentItem() throws Exception {
        final UserAdapter adapter = new UserAdapter();
        adapter.add(new User("A", "1"));
        adapter.add(new User("B", "2"));
        adapter.add(new User("C", "3"));
        assertThat(adapter.getItemCount()).isEqualTo(3);

        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        adapter.registerAdapterDataObserver(observer);
        adapter.replaceItem(new User("X", "10"), new User("Y", "11"));
        assertThat(adapter.getItemCount()).isEqualTo(3);
        assertThat(adapter.getItem(0)).isEqualTo(new User("A", "1"));
        assertThat(adapter.getItem(1)).isEqualTo(new User("B", "2"));
        assertThat(adapter.getItem(2)).isEqualTo(new User("C", "3"));
        verifyZeroInteractions(observer);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void replaceNullWithItemThrow() throws Exception {
        final TestAdapter adapter = new TestAdapter();
        try {
            adapter.replaceItem(null, "A");
            fail("did no throw");
        } catch (IllegalStateException e) {
            assertThat(e).hasMessageContaining("null");
        }
    }

    @Before
    public void setUp() throws Exception {
        mAdapter = new TestAdapter();
    }

    @Test
    public void sort() throws Exception {
        mAdapter.add("C");
        mAdapter.add("A");
        mAdapter.add("B");
        assertThat(mAdapter.getItemCount()).isEqualTo(3);

        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        mAdapter.registerAdapterDataObserver(observer);
        mAdapter.sort(new Comparator<String>() {
            @Override
            public int compare(final String o1, final String o2) {
                return o1.compareTo(o2);
            }
        });
        assertThat(mAdapter.getItem(0)).isEqualTo("A");
        assertThat(mAdapter.getItem(1)).isEqualTo("B");
        assertThat(mAdapter.getItem(2)).isEqualTo("C");
        verify(observer).onItemRangeMoved(0, 2, 1);
        verifyNoMoreInteractions(observer);
    }

    @Test
    public void sortSortedList() throws Exception {
        mAdapter.add("A");
        mAdapter.add("B");
        mAdapter.add("C");
        assertThat(mAdapter.getItemCount()).isEqualTo(3);

        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        mAdapter.registerAdapterDataObserver(observer);
        mAdapter.sort(new Comparator<String>() {
            @Override
            public int compare(final String o1, final String o2) {
                return o1.compareTo(o2);
            }
        });
        assertThat(mAdapter.getItem(0)).isEqualTo("A");
        assertThat(mAdapter.getItem(1)).isEqualTo("B");
        assertThat(mAdapter.getItem(2)).isEqualTo("C");
        verifyZeroInteractions(observer);
    }

    @Test
    public void swap() throws Exception {
        mAdapter.add("A");
        mAdapter.add("B");
        mAdapter.add("C");

        assertThat(mAdapter.getItemCount()).isEqualTo(3);
        assertThat(mAdapter.getItem(0)).isEqualTo("A");
        assertThat(mAdapter.getItem(1)).isEqualTo("B");
        assertThat(mAdapter.getItem(2)).isEqualTo("C");

        List<String> list = new ArrayList<>();
        list.add("D");
        list.add("E");
        list.add("F");
        list.add("G");
        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        mAdapter.registerAdapterDataObserver(observer);
        mAdapter.swap(list);
        assertThat(mAdapter.getItemCount()).isEqualTo(4);
        assertThat(mAdapter.getItem(0)).isEqualTo("D");
        assertThat(mAdapter.getItem(1)).isEqualTo("E");
        assertThat(mAdapter.getItem(2)).isEqualTo("F");
        assertThat(mAdapter.getItem(3)).isEqualTo("G");
        verify(observer).onItemRangeRemoved(0, 3);
        verify(observer).onItemRangeInserted(0, 4);
        verifyNoMoreInteractions(observer);
    }

    @Test
    public void swapListWithOneUnchanged() throws Exception {
        mAdapter.add("A");
        mAdapter.add("B");
        mAdapter.add("C");

        assertThat(mAdapter.getItemCount()).isEqualTo(3);
        assertThat(mAdapter.getItem(0)).isEqualTo("A");
        assertThat(mAdapter.getItem(1)).isEqualTo("B");
        assertThat(mAdapter.getItem(2)).isEqualTo("C");

        List<String> list = new ArrayList<>();
        list.add("D");
        list.add("B");
        list.add("F");
        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        mAdapter.registerAdapterDataObserver(observer);
        mAdapter.swap(list);
        assertThat(mAdapter.getItemCount()).isEqualTo(3);
        assertThat(mAdapter.getItem(0)).isEqualTo("D");
        assertThat(mAdapter.getItem(1)).isEqualTo("B");
        assertThat(mAdapter.getItem(2)).isEqualTo("F");
        verify(observer).onItemRangeRemoved(0, 1);
        verify(observer).onItemRangeRemoved(2, 1);
        verify(observer).onItemRangeInserted(0, 1);
        verify(observer).onItemRangeInserted(2, 1);
        verifyNoMoreInteractions(observer);
    }

    @Test
    public void swapNullClears() throws Exception {
        mAdapter.add("A");
        mAdapter.add("B");
        mAdapter.add("C");

        assertThat(mAdapter.getItemCount()).isEqualTo(3);

        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        mAdapter.registerAdapterDataObserver(observer);
        mAdapter.swap(null);
        assertThat(mAdapter.getItemCount()).isEqualTo(0);
        verify(observer).onItemRangeRemoved(0, 3);
        verifyNoMoreInteractions(observer);
    }

    @Test
    public void swapSameList() throws Exception {
        mAdapter.add("A");
        mAdapter.add("B");
        mAdapter.add("C");

        assertThat(mAdapter.getItemCount()).isEqualTo(3);
        assertThat(mAdapter.getItem(0)).isEqualTo("A");
        assertThat(mAdapter.getItem(1)).isEqualTo("B");
        assertThat(mAdapter.getItem(2)).isEqualTo("C");

        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        mAdapter.registerAdapterDataObserver(observer);
        mAdapter.swap(list);
        assertThat(mAdapter.getItemCount()).isEqualTo(3);
        assertThat(mAdapter.getItem(0)).isEqualTo("A");
        assertThat(mAdapter.getItem(1)).isEqualTo("B");
        assertThat(mAdapter.getItem(2)).isEqualTo("C");
        verifyZeroInteractions(observer);
    }

    @Test
    public void swap_getItemIdNotStable() throws Exception {
        mAdapter = new TestAdapter() {
            int i = 0;

            @Override
            public Object getItemId(@NonNull final String item) {
                // always return a different id
                return i++;
            }
        };

        mAdapter.add("A");
        mAdapter.add("B");
        mAdapter.add("C");

        assertThat(mAdapter.getItemCount()).isEqualTo(3);
        assertThat(mAdapter.getItem(0)).isEqualTo("A");
        assertThat(mAdapter.getItem(1)).isEqualTo("B");
        assertThat(mAdapter.getItem(2)).isEqualTo("C");

        List<String> list = new ArrayList<>();
        list.add("A");
        list.add("B");
        list.add("C");
        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        mAdapter.registerAdapterDataObserver(observer);
        mAdapter.swap(list);
        assertThat(mAdapter.getItemCount()).isEqualTo(3);
        assertThat(mAdapter.getItem(0)).isEqualTo("A");
        assertThat(mAdapter.getItem(1)).isEqualTo("B");
        assertThat(mAdapter.getItem(2)).isEqualTo("C");

        verify(observer).onItemRangeRemoved(0, 3);
        verify(observer).onItemRangeInserted(0, 3);
        verifyNoMoreInteractions(observer);
    }

    @Test
    public void swap_onlyDataChanged() throws Exception {
        final UserAdapter adapter = new UserAdapter();
        adapter.add(new User("A", "1"));
        adapter.add(new User("B", "2"));
        adapter.add(new User("C", "3"));

        assertThat(adapter.getItemCount()).isEqualTo(3);
        assertThat(adapter.getItem(0)).isEqualTo(new User("A", "1"));
        assertThat(adapter.getItem(1)).isEqualTo(new User("B", "2"));
        assertThat(adapter.getItem(2)).isEqualTo(new User("C", "3"));

        final List<User> list = new ArrayList<>();
        // change items, not the id
        list.add(new User("A'", "1"));
        list.add(new User("B'", "2"));
        list.add(new User("C'", "3"));
        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        adapter.registerAdapterDataObserver(observer);
        adapter.swap(list);
        assertThat(adapter.getItemCount()).isEqualTo(3);
        assertThat(adapter.getItem(0)).isEqualTo(new User("A'", "1"));
        assertThat(adapter.getItem(1)).isEqualTo(new User("B'", "2"));
        assertThat(adapter.getItem(2)).isEqualTo(new User("C'", "3"));

        verify(observer).onItemRangeChanged(0, 3, null);
        verifyNoMoreInteractions(observer);
    }
}