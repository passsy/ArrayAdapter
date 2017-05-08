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

import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

@RunWith(AndroidJUnit4.class)
public class ArrayAdapterTest {

    private static class TestAdapter extends ArrayAdapter<String, RecyclerView.ViewHolder> {

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

    @Test
    public void addNull() throws Exception {
        assertThat(mAdapter.getItemCount()).isEqualTo(0);

        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        mAdapter.registerAdapterDataObserver(observer);

        mAdapter.add(null);
        assertThat(mAdapter.getItemCount()).isEqualTo(1);
        assertThat(mAdapter.getItem(0)).isNull();
        verify(observer).onItemRangeInserted(0, 1);

        mAdapter.add(null);
        assertThat(mAdapter.getItemCount()).isEqualTo(2);
        assertThat(mAdapter.getItem(1)).isNull();
        verify(observer).onItemRangeInserted(1, 1);

        verifyNoMoreInteractions(observer);
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

    @Test
    public void insertNull() throws Exception {
        mAdapter.add("A");
        mAdapter.add("B");
        mAdapter.add("C");

        assertThat(mAdapter.getItemCount()).isEqualTo(3);
        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        mAdapter.registerAdapterDataObserver(observer);
        mAdapter.insert(null, 1);
        assertThat(mAdapter.getItemCount()).isEqualTo(4);
        assertThat(mAdapter.getItem(1)).isEqualTo(null);
        verify(observer).onItemRangeInserted(1, 1);
        verifyNoMoreInteractions(observer);
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

    @Test
    public void removeNull() throws Exception {
        mAdapter.add("A");
        mAdapter.add(null);
        mAdapter.add("B");
        mAdapter.add("C");
        assertThat(mAdapter.getItemCount()).isEqualTo(4);

        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        mAdapter.registerAdapterDataObserver(observer);
        mAdapter.remove(null);
        assertThat(mAdapter.getItemCount()).isEqualTo(3);
        assertThat(mAdapter.getItem(1)).isEqualTo("B");
        verify(observer).onItemRangeRemoved(1, 1);
        verifyNoMoreInteractions(observer);
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
    public void replaceItem() throws Exception {
        mAdapter.add("A");
        mAdapter.add("B");
        mAdapter.add("C");
        assertThat(mAdapter.getItemCount()).isEqualTo(3);

        final RecyclerView.AdapterDataObserver observer =
                mock(RecyclerView.AdapterDataObserver.class);
        mAdapter.registerAdapterDataObserver(observer);
        mAdapter.replaceItem("B", "Z");
        assertThat(mAdapter.getItemCount()).isEqualTo(3);
        assertThat(mAdapter.getItem(1)).isEqualTo("Z");
        verify(observer).onItemRangeRemoved(1, 1);
        verify(observer).onItemRangeInserted(1, 1);
        verifyNoMoreInteractions(observer);
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
        mAdapter.swap(list, new ArrayAdapter.StableIdProvider<String>() {
            @Override
            public Object getId(final String item) {
                return item;
            }
        });
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
        mAdapter.swap(list, new ArrayAdapter.StableIdProvider<String>() {
            @Override
            public Object getId(final String item) {
                return item;
            }
        });
        assertThat(mAdapter.getItemCount()).isEqualTo(3);
        assertThat(mAdapter.getItem(0)).isEqualTo("A");
        assertThat(mAdapter.getItem(1)).isEqualTo("B");
        assertThat(mAdapter.getItem(2)).isEqualTo("C");
        verifyZeroInteractions(observer);
    }
}