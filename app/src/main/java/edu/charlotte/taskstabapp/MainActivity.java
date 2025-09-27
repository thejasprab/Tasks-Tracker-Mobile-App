package edu.charlotte.taskstabapp;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import edu.charlotte.taskstabapp.models.DataStore;
import edu.charlotte.taskstabapp.models.Task;

public class MainActivity extends AppCompatActivity {
    //Tasks pre-populated from DataStore
    ArrayList<Task> mTasks = DataStore.getTasks();
    ViewPager2 viewPager;
    ViewPageAdapter viewPageAdapter;
    String[] priorityList = {"HIGH", "MEDIUM", "LOW","ALL"};

    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Log.d("demo", "onCreate: " + DataStore.getTasks());

        viewPager = findViewById(R.id.viewPagerTaskFragments);
        viewPageAdapter = new ViewPageAdapter(this);
        viewPager.setAdapter(viewPageAdapter);
        tabLayout = findViewById(R.id.tabLayoutPriority);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int i) {
                tab.setText(priorityList[i]);
            }
        }).attach();
    }

    private ArrayList<Task> filterTaskListByPriority(ArrayList<Task> tasks,String Priority){

        if(Priority.equals("ALL")){
            return tasks;
        }
        List<Task> priorityFilterTask = new ArrayList<>();
        priorityFilterTask = tasks.stream()
                .filter(task -> task.getPriority().equals(Priority))
                .collect(Collectors.toList());
        return new ArrayList<>(priorityFilterTask);
    }
    public class ViewPageAdapter extends FragmentStateAdapter{


        public ViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            ArrayList<Task> tasks = filterTaskListByPriority(mTasks,priorityList[position]);
            return TasksFragment.newInstance(tasks);
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
}