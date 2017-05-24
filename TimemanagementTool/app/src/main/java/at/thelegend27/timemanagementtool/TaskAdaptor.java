package at.thelegend27.timemanagementtool;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by a1 on 20.05.17.
 */

public class TaskAdaptor extends RecyclerView.Adapter<TaskAdaptor.UserViewFolder> {

    private List<Task> list;
    private Listener listener;
    public TaskAdaptor(List<Task> list) {
        this.list = list;
    }


    public static interface Listener {
        public void onClick(int position);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public UserViewFolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new UserViewFolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.tasks_view, parent, false));
    }

    @Override
    public void onBindViewHolder(final UserViewFolder holder, final int position) {
        Task task = list.get(position);
        holder.textTaskName.setText(task.taskname);
        holder.textDate.setText(task.date);
        holder.textAuthor.setText(task.author);
        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(holder.getAdapterPosition(), 0, 0, "Delete");
                menu.add(holder.getAdapterPosition(), 1, 0, "Change");
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null) {
                    listener.onClick(position);
                }
            }
        });
    }



    @Override
    public int getItemCount() { return list.size(); }

    public class UserViewFolder extends RecyclerView.ViewHolder{
        TextView textAuthor, textTaskName, textDate;

        public UserViewFolder(View itemView) {
            super(itemView);
            textAuthor = (TextView)itemView.findViewById(R.id.text_author);
            textDate = (TextView)itemView.findViewById(R.id.text_date);
            textTaskName = (TextView)itemView.findViewById(R.id.text_task_name);
        }
    }

    public void removeAt(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }



}
