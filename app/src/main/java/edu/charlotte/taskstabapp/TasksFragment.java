package edu.charlotte.taskstabapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import edu.charlotte.taskstabapp.databinding.FragmentTasksBinding;
import edu.charlotte.taskstabapp.models.Task;

public class TasksFragment extends Fragment {

    private static final String TASK_FRAGMENTS_KEY = "Tasks";

    private ArrayList<Task> tasks;

    public TasksFragment() {
        // Required empty public constructor
    }

    public static TasksFragment newInstance(ArrayList<Task> incomingTasks) {
        TasksFragment fragment = new TasksFragment();
        Bundle args = new Bundle();
        args.putSerializable(TASK_FRAGMENTS_KEY, incomingTasks);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tasks = (ArrayList<Task>) getArguments().getSerializable(TASK_FRAGMENTS_KEY);
        }
    }

    FragmentTasksBinding binding;

    private void fillCardViewInfo(){
        int size = tasks.size();
        if (size == 0) return;

        Task t = tasks.get(taskIndex);
        binding.textViewTaskName.setText(t.getTitle());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDateString = formatter.format(t.getDate());
        binding.textViewTaskDate.setText(formattedDateString);
        binding.textViewTaskPriority.setText(t.getPriority());
        binding.textViewTaskOutOf.setText("Task " + (taskIndex + 1) + " of " + size);
        binding.textViewTasksCount.setText("You have " + size + " tasks");
        binding.cardViewTask.setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTasksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    int taskIndex=0;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("Demo", "onViewCreated: "+tasks.get(0).toString());
        if (tasks.isEmpty()){
            binding.textViewTasksCount.setText("You have 0 tasks");
        }
        Collections.sort(tasks, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return -1*o1.getDate().compareTo(o2.getDate());
            }
        });
        fillCardViewInfo();
        binding.imageViewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = tasks.size();
                if(size<=1) {
                    return;
                }
                taskIndex = (taskIndex+1)%size;
                fillCardViewInfo();
            }
        });
        binding.imageViewPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = tasks.size();
                if (size <=1) {
                    return;
                }
                taskIndex = (taskIndex-1+size)%size;
                fillCardViewInfo();
            }
        });
        binding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tasks.isEmpty()){
                    return;
                }
                tasks.remove(taskIndex);
                if (tasks.isEmpty()) {
                    taskIndex = 0;
                    binding.textViewTasksCount.setText("You have 0 tasks");
                    binding.cardViewTask.setVisibility(View.INVISIBLE);  // requirement
                    return;
                }

                // If we deleted the last item, step back to new last index
                if (taskIndex >= tasks.size()) {
                    taskIndex = tasks.size() - 1;
                }

                fillCardViewInfo();
            }
        });
    }


}