package com.home.englishnote.views.fragments.secondary.dictionary;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.home.englishnote.R;
import com.home.englishnote.models.entities.Dictionary;
import com.home.englishnote.presenters.PublicDictionariesPresenter;
import com.home.englishnote.utils.Global;
import com.home.englishnote.views.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import static com.home.englishnote.presenters.PublicDictionariesPresenter.*;

public class PublicDictionariesFragment extends BaseFragment implements PublicDictionaryView {

    private SwipeRefreshLayout publicDictionariesSwipeRefreshLayout;
    private RecyclerView publicDictionariesRecycler;
    private PublicDictionariesAdapter publicDictionariesAdapter;
    private PublicDictionariesPresenter publicDictionariesPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater
                .inflate(R.layout.fragment_public_dictionaries, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        init();
    }

    private void findViews(View view) {
        publicDictionariesSwipeRefreshLayout = view.findViewById(R.id.publicDictionariesSwipeRefreshLayout);
        publicDictionariesRecycler = view.findViewById(R.id.publicDictionariesRecycler);
    }

    private void init() {
        publicDictionariesPresenter = new PublicDictionariesPresenter(
                this, Global.dictionaryRepository(), Global.threadExecutor());
        setUpDictionariesRecycler();
        setUpDictionariesSwipeRefreshLayout();
    }

    @Override
    public void onResume() {
        super.onResume();
        dictionaryList.clear();
        queryDictionaryList();
    }

    private List<Dictionary> dictionaryList = new ArrayList<>();

    private void setUpDictionariesRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(dictionaryHomePageActivity);
        publicDictionariesRecycler.setHasFixedSize(true);
        publicDictionariesRecycler.setLayoutManager(linearLayoutManager);
        publicDictionariesAdapter = new PublicDictionariesAdapter(dictionaryList);
        publicDictionariesRecycler.setAdapter(publicDictionariesAdapter);
        publicDictionariesRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView,
                                             int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int lastVisibleItemPosition =
                        linearLayoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition + 1 == publicDictionariesAdapter.getItemCount()) {
                    if (!isPullToRefreshTriggered) {
                        isPullToRefreshTriggered = true;
                        queryDictionaryList();
                    }
                }
            }
        });
    }

    private void setUpDictionariesSwipeRefreshLayout() {
        publicDictionariesSwipeRefreshLayout.measure(0, 0);
        publicDictionariesSwipeRefreshLayout.setProgressViewOffset(true, 80, 90);
        publicDictionariesSwipeRefreshLayout.setOnRefreshListener(this::queryDictionaryList);
    }

    private static final int PAGE_LIMIT = 10;

    private void queryDictionaryList() {
        setDictionariesSwipeRefreshLayoutEnable(true);
        int dictionaryListSize = dictionaryList.size();
        publicDictionariesPresenter.getDictionaries(dictionaryListSize, PAGE_LIMIT);
    }

    private boolean isPullToRefreshTriggered = false;

    @Override
    public void onGetDictionariesSuccessfully(List<Dictionary> dictionaryList) {
        isPullToRefreshTriggered = false;
        setDictionariesSwipeRefreshLayoutEnable(false);
        this.dictionaryList.addAll(dictionaryList);
        publicDictionariesAdapter.notifyDataSetChanged();
    }

    private void setDictionariesSwipeRefreshLayoutEnable(boolean enable) {
        publicDictionariesSwipeRefreshLayout.setEnabled(enable);
        publicDictionariesSwipeRefreshLayout.setRefreshing(enable);
    }

    public void onVocabSearchClick(View view) {
        // Todo show vocabSearch
    }

    public void onDictionaryListSearchClick(View view) {
        // Todo show dictionaryListSearch
    }

    public class PublicDictionariesAdapter extends Adapter<PublicDictionariesAdapter.PublicDictionariesHolder> {

        private List<Dictionary> dictionaryList;

        public PublicDictionariesAdapter(List<Dictionary> dictionaryList) {
            this.dictionaryList = dictionaryList;
        }

        @NonNull
        @Override
        public PublicDictionariesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_public_dictionary, parent, false);
            return new PublicDictionariesHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PublicDictionariesHolder holder, int position) {
            Dictionary dictionary = dictionaryList.get(position);
            holder.setData(dictionary);
            holder.setExploreButtonClick(dictionary);
        }

        @Override
        public int getItemCount() {
            return dictionaryList.size();
        }

        protected class PublicDictionariesHolder extends ViewHolder {

            private TextView dictionaryTitle, dictionaryDescription;
            private ImageView dictionaryImage1, dictionaryImage2, dictionaryImage3;
            private Button exploreButton;

            public PublicDictionariesHolder(@NonNull View itemView) {
                super(itemView);
                findViews(itemView);
            }

            private void findViews(View itemView) {
                dictionaryTitle = itemView.findViewById(R.id.dictionaryTitle);
                dictionaryDescription = itemView.findViewById(R.id.dictionaryDescription);
                dictionaryImage1 = itemView.findViewById(R.id.dictionaryImage1);
                dictionaryImage2 = itemView.findViewById(R.id.dictionaryImage2);
                dictionaryImage3 = itemView.findViewById(R.id.dictionaryImage3);
                exploreButton = itemView.findViewById(R.id.exploreButton);
            }

            private void setExploreButtonClick(Dictionary dictionary) {
                exploreButton.setOnClickListener(
                        (v) -> switchFragment(R.layout.fragment_public_word_groups, dictionary));
            }

            private void setData(Dictionary dictionary) {
                dictionaryTitle.setText(dictionary.getTitle());
                dictionaryDescription.setText(dictionary.getDescription());
                dictionaryImage1.setImageResource(
                        R.drawable.pear_hydrosol_pyrus_communis_hydrolat_product_pic);
                dictionaryImage2.setImageResource(R.drawable.guava_1);
                dictionaryImage3.setImageResource(
                        R.drawable.apple);
            }
        }
    }
}
