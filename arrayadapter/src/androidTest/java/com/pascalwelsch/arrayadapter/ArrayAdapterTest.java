package com.pascalwelsch.arrayadapter;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

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

    @Before
    public void setUp() throws Exception {
        mAdapter = new TestAdapter();
    }
}