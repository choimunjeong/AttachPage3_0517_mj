package Page3_1_1;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attachpage3_200428_mj.R;

import java.util.ArrayList;
import java.util.Collections;

public class Page3_1_1_adapter extends RecyclerView.Adapter<Page3_1_1_adapter.MyViewHolder> implements SwipeAndDragHelper.ActionCompletionContract{
    private ArrayList<Page3_1_1_dargData> mDataset;
    private ItemTouchHelper touchHelper;
    boolean touchDrag = false;


    //content 뷰 홀더
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView name, number;
        public Button delete, add;
        public RelativeLayout dragline;
        public ImageView circle;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.page3_1_1_name);
            number = (TextView) itemView.findViewById(R.id.page3_1_1_number);
            delete = (Button) itemView.findViewById(R.id.page3_1_1_delete_btn);
            add = (Button) itemView.findViewById(R.id.page3_1_1_listAdd);
            dragline = (RelativeLayout) itemView.findViewById(R.id.page3_1_1_dragLine);
            circle = (ImageView) itemView.findViewById(R.id.page3_1_1_circle);
        }
    }

    public Page3_1_1_adapter(ArrayList<Page3_1_1_dargData> myData){
        this.mDataset = myData;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.page3_1_1_item, parent,false);
            return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.name.setText(mDataset.get(position).getName());

        if(mDataset.get(position).getNumber() == 0){
            holder.number.setText("01");
        }
        else if (mDataset.get(position).getNumber() == mDataset.size()-1){
            if(mDataset.get(position).getNumber() < 9) {
                holder.number.setText("0" + Integer.toString(mDataset.size()) );
            } else {
                holder.number.setText( Integer.toString(mDataset.size()) );
            }
        }
        else {
            if(mDataset.get(position).getNumber() < 9) {
                holder.number.setText("0" + Integer.toString(mDataset.get(position).getNumber()+1) );
            } else {
                holder.number.setText( Integer.toString(mDataset.get(position).getNumber()+1) );
            }
        }


        //줄3개 누르면 드래그 됨.
        holder.dragline.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    touchHelper.startDrag(holder);
                }
                return false;
            }
        });


        //삭제 버튼을 누르면 삭제 됨.
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("언제 잘 수 있을까", Integer.toString(position));
                mDataset.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mDataset.size());
            }
        });
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    @Override
    public void onViewMoved(int oldPosition, int newPosition) {
        Collections.swap(mDataset, oldPosition, newPosition);
        notifyItemMoved(oldPosition, newPosition);
        notifyItemRangeChanged(0, mDataset.size());
    }


    public void setTouchHelper(ItemTouchHelper touchHelper) {
        this.touchHelper = touchHelper;
    }
}
