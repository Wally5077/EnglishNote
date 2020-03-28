package com.home.englishnote.utils;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.home.englishnote.R;
import com.home.englishnote.models.entities.Word;
import com.home.englishnote.views.activities.DictionaryHomePageActivity;
import com.home.englishnote.views.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.N)
public class PublicWordsAdapter extends RecyclerView.Adapter<PublicWordsAdapter.PublicWordsHolder> {

    private Context context;
    private List<Word> minWordList;
    private List<Word> maxWordList;
    private List<Word> wordList;

    public PublicWordsAdapter(List<Word> wordList) {
        this.maxWordList = wordList;
        this.minWordList = new ArrayList<>(3);
        setMinWordList(wordList);
    }

    private void setMinWordList(List<Word> wordList) {
        for (int index = 0; index < 3; index++) {
            minWordList.add(wordList.get(index));
        }
        this.wordList = minWordList;
    }

    @NonNull
    @Override
    public PublicWordsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = (context == null) ? parent.getContext() : context;
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_word, parent, false);
        return new PublicWordsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PublicWordsHolder holder, int position) {
        holder.setData(wordList.get(position));
    }

    @Override
    public int getItemCount() {
        return wordList.size();
    }

    public class PublicWordsHolder extends RecyclerView.ViewHolder {

        private ImageView wordItemImage;
        private TextView wordItemName;
        private TextView wordItemSynonym;

        public PublicWordsHolder(@NonNull View itemView) {
            super(itemView);
            findViews(itemView);
            itemView.setOnClickListener(this::onWordsRecyclerClick);
        }

        private boolean isWordsRecyclerClick = false;

        private void onWordsRecyclerClick(View v) {
            isWordsRecyclerClick = !isWordsRecyclerClick;
            wordList = (isWordsRecyclerClick) ? maxWordList : minWordList;
            notifyDataSetChanged();
        }

        private void findViews(View view) {
            wordItemImage = view.findViewById(R.id.wordItemImage);
            wordItemName = view.findViewById(R.id.wordItemName);
            wordItemSynonym = view.findViewById(R.id.wordItemSynonym);
        }

        public void setData(Word word) {
            Glide.with(context)
                    .asBitmap()
                    .load(word.getImageUrl())
                    .error(R.drawable.apple)
                    .into(wordItemImage);
            wordItemName.setText(word.getName());
            String wordSynonyms = "( " + word.getSynonyms().get(0) + " )";
            wordItemSynonym.setText(wordSynonyms);
            wordItemImage.setOnClickListener(v -> ((DictionaryHomePageActivity) context)
                    .switchFragment(R.layout.fragment_word,
                            R.id.publicDictionaryPageContainer, word));
        }

    }
}
