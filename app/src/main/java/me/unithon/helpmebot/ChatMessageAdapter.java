package me.unithon.helpmebot;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import java.util.ArrayList;

import me.unithon.helpmebot.util.ScreenSize;
import me.unithon.helpmebot.vo.Message;

/**
 * Created by Kim on 2017-02-04.
 */

public class ChatMessageAdapter extends RecyclerView.Adapter {
    private static final int TYPE_USER = 0;
    private static final int TYPE_TEXT = 1;
    private static final int TYPE_IMAGE = 2;

    private Context context;
    private ArrayList<Message> arrList;
    private int lastAnimatedPosition = -1;

    private int arrRow[] = {R.layout.row_message_user, R.layout.row_message_text, R.layout.row_message_image};

    public ChatMessageAdapter(Context context, ArrayList<Message> arrList) {
        this.context = context;
        this.arrList = arrList;
    }

    @Override
    public int getItemViewType(int position) {
        return arrList.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(arrRow[viewType], parent, false);

        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType) {
            case TYPE_USER:
            case TYPE_TEXT:
                viewHolder = new MessageViewHolder(itemView);
                break;
            case TYPE_IMAGE:
                viewHolder = new TestViewHolder(itemView);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        setAnimation(holder, position);

        Message item = arrList.get(position);
        switch (holder.getItemViewType()){
            case TYPE_USER:
            case TYPE_TEXT:
                MessageViewHolder mViewHolder = (MessageViewHolder) holder;
                mViewHolder.tvMessage.setText(item.getMessage());
                break;
            case TYPE_IMAGE:
                TestViewHolder tViewHolder = (TestViewHolder) holder;
                break;
        }

    }

    private void setAnimation(RecyclerView.ViewHolder viewHolder, int position) {
        if(viewHolder.getItemViewType() == TYPE_USER) {
            return;
        }

        View view = viewHolder.itemView;
        if(position > lastAnimatedPosition) {
            view.setTranslationY(ScreenSize.getScreenHeight(context));
            view.animate()
                    .translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700)
                    .start();
            lastAnimatedPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return arrList.size();
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView tvMessage;

        public MessageViewHolder(final View itemView) {
            super(itemView);
            tvMessage = (TextView) itemView.findViewById(R.id.tv_message);
        }
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        public ImageViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class TestViewHolder extends RecyclerView.ViewHolder {

        public TestViewHolder(View itemView) {
            super(itemView);
        }
    }
}