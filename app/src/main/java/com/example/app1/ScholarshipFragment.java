package com.example.app1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.app1.dao.AppDatabase;
import com.example.app1.model.Post;
import com.example.app1.model.Profile;

import java.util.List;

public class ScholarshipFragment extends Fragment implements SearchView.OnQueryTextListener {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scholarship, container, false);
        AppDatabase db = Room.databaseBuilder(getContext(), AppDatabase.class, "app-database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        Profile profile = db.profileDao().getProfile();
        if (!(profile != null && "大学以上".equals(profile.grade))) {
            View writePost = view.findViewById(R.id.button7);
            if (writePost != null) {
                writePost.setVisibility(View.INVISIBLE);
            }
        }
        displayPosts(view, "scholarship", null);
        SearchView searchView = view.findViewById(R.id.scholarships_search);
        searchView.setOnQueryTextListener(this);
        return view;
    }

    public boolean onQueryTextSubmit(String query) {
        return onQueryTextChange(query);
    }

    public boolean onQueryTextChange(String newText) {
        ScrollView scrollView = getView().findViewById(R.id.scholarships_scroll);
        scrollView.removeAllViews();
        displayPosts(getView(), "scholarship", newText);
        return false;
    }

    public void displayPosts(View view, @NonNull String category, @Nullable String query) {
        AppDatabase db = Room.databaseBuilder(getContext(), AppDatabase.class, "app-database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        List<Post> posts;
        if (query == null || query.isEmpty()) {
            posts = db.postDao().getPosts(category);
            if (posts.isEmpty()) {
                // Our first time displaying this, there will be no posts.
                // Fill in the examples;
                Post post1 = new Post();
                post1.category = "scholarship";
                post1.title = "公共財団法人ベアー奨学会";
                post1.body = "対象・・・公立高校出身の大学生 \r\n 主な条件・・・公立高校出身であること \r\n 金額(月額)・・・¥11500 \r\n 期間・・・在学期間中 \r\n\n 私たちは、「子どもは歴史の希望である」という基本理念に基づき、子どもたちの最善の利益を目指した活動を行っている公益財団法人です。すべての子ども・若者たちが、自分らしく心と身体が健康に成長できる社会づくりに、様々な視点と角度から取り組んでいきます。" ;
                post1.image = "scholarshipschool";
                db.postDao().insert(post1);
                // add post2, post3, ...
                Post post2 = new Post();
                post2.category = "scholarship";
                post2.title = "のりしお育英会";
                post2.body = "対象・・・公立高校出身の大学生¥n主な条件・・・公立高校出身であること¥n金額(月額)・・・¥11500¥n期間・・・在学期間中";
                post2.image = "scholarshipgroup";
                db.postDao().insert(post2);

                Post post3 = new Post();
                post3.category = "scholarship";
                post3.title = "A塾 特待制度";
                post3.image = "scholarshipjyuku";
                db.postDao().insert(post3);

                Post post4 = new Post();
                post4.category = "scholarship";
                post4.title = "長期留学支援";
                post4.image = "scholarshipryugaku";
                db.postDao().insert(post4);
                // get the posts again
                posts = db.postDao().getPosts(category);
            }
        } else {
            posts = db.postDao().searchPosts(category, query);
        }


        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(32, 32, 32, 0);
        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(params);
        ScrollView scrollView = view.findViewById(R.id.scholarships_scroll);
        scrollView.addView(layout);
        View leftCell = null;  // fill in rows of 2 columns
        for (Post post : posts) {
            // create button for post
            ImageButton image = new ImageButton(getContext());
            String imageName = post.image != null && !post.image.isEmpty() ? post.image : "scholarshipgattkou";
            image.setBackgroundResource(getResources().getIdentifier(imageName, "drawable", "com.example.app1"));
            image.setOnClickListener((View v) -> {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, DisplayPostFragment.newInstance(post.uid)).commit();
            });
            ViewGroup.LayoutParams imageParams = new ViewGroup.LayoutParams(320, 320);
            image.setLayoutParams(imageParams);
            TextView textView = new TextView(getContext());
            textView.setText(post.title);
            LinearLayout cell = new LinearLayout(getContext());
            cell.setOrientation(LinearLayout.VERTICAL);
            cell.setLayoutParams(params);
            cell.addView(image);
            cell.addView(textView);
            LinearLayout save = new LinearLayout(getContext());
            save.setOrientation(LinearLayout.HORIZONTAL);
            TextView saveTextView = new TextView(getContext());
            saveTextView.setText("Save");
            SwitchCompat aSwitch = new SwitchCompat(getContext());
            aSwitch.setChecked(post.saved);
            aSwitch.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
                db.postDao().setSaved(post.uid, isChecked);
            });
            save.addView(saveTextView);
            save.addView(aSwitch);
            cell.addView(save);
            if (leftCell == null) {
                leftCell = cell;
            } else {
                LinearLayout row = new LinearLayout(getContext());
                row.setOrientation(LinearLayout.HORIZONTAL);
                row.setLayoutParams(params);
                row.addView(leftCell);
                row.addView(cell);
                layout.addView(row);
                leftCell = null;
            }
        }
        if (leftCell != null) {
            // last button by itself
            layout.addView(leftCell);
        }
    }


}
