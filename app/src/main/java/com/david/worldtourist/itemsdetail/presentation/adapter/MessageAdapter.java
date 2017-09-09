package com.david.worldtourist.itemsdetail.presentation.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.david.worldtourist.R;
import com.david.worldtourist.authentication.domain.model.User;
import com.david.worldtourist.message.domain.model.Message;
import com.david.worldtourist.message.presentation.component.MessageClick;
import com.david.worldtourist.items.presentation.component.click.EmptyClickAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private static final int MINUTE = 60 * 1000;
    private static final int HOUR = 60 * 60 * 1000;
    private static final int DAY = 24 * 60 * 60 * 1000;
    private static final int WEEK = 7 * 24 * 60 * 60 * 1000;

    private Context context;
    private List<Message> messages = new ArrayList<>();
    private MessageClick actionMenu = new EmptyClickAction();

    private User user = User.EMPTY_USER;

    public MessageAdapter(Context context) {
        this.context = context;
    }

    public void setMenuAction(MessageClick menuAction) {
        actionMenu = menuAction;
    }

    public void setUser(User user) {
        this.user = user;
    }


    /////////////////////////RecyclerView.Adapter<> Implementation//////////////////////////

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.element_message, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Message message = messages.get(position);

        showImageIfAvailable(holder, message);

        showName(holder, message);

        showMessage(holder, message);

        showRating(holder, message);

        showLikes(holder, message);

        showDate(holder, message);

        showMenu(holder);

        holder.messageMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionMenu.onMessageClick(view, messages.get(holder.getAdapterPosition()));
            }
        });
    }

    private void showImageIfAvailable(ViewHolder holder, Message message) {
        if (!message.getUser().getImage().isEmpty()) {
            Glide.with(context)
                    .load(message.getUser().getImage())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .fitCenter()
                    .centerCrop()
                    .into(holder.userImage);
        }
    }

    private void showName(ViewHolder holder, Message message) {
        holder.userName.setText(message.getUser().getName());
    }

    private void showMessage(ViewHolder holder, Message message) {
        holder.userMessage.setText(message.getText());
    }

    private void showRating(ViewHolder holder, Message message) {
        holder.userRating.setRating(message.getUserRating());
        holder.userRatingValue.setText(String.valueOf(String.format(Locale.getDefault(),
                "%.1f", message.getUserRating())));
    }

    private void showLikes(ViewHolder holder, Message message) {
        holder.messageLikes.setText(String.valueOf(message.getLikes()));

        if (message.getUserLikesIds().contains(user.getId())) {
            holder.messageLikes.setCompoundDrawables(setDrawableColor(
                    holder.messageLikes.getCompoundDrawables()[0],
                    R.color.colorAccent), null, null, null);
        } else {
            holder.messageLikes.setCompoundDrawables(setDrawableColor(
                    holder.messageLikes.getCompoundDrawables()[0],
                    android.R.color.darker_gray), null, null, null);

        }
    }

    private void showDate(ViewHolder holder, Message message) {
        holder.messageDate.setText(determineDate(System.currentTimeMillis() - message.getDate()));
    }

    private void showMenu(ViewHolder holder) {
        holder.messageMenu.setImageDrawable(setDrawableColor(ContextCompat.getDrawable(context,
                R.drawable.ic_menu_vertical), android.R.color.darker_gray));
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    public synchronized void addMessage(Message addedMessage) {
        for (Message message : messages) {
            if (message.getMessageId().equals(addedMessage.getMessageId()))
                return;
        }

        messages.add(addedMessage);
        orderMessagesByDate();
        notifyDataSetChanged();
    }

    public synchronized void removeMessage(Message deletedMessage) {
        for (Message message : messages) {
            if (message.getMessageId().equals(deletedMessage.getMessageId())) {
                messages.remove(message);
                notifyDataSetChanged();
                return;
            }
        }
    }

    public synchronized void updateMessage(Message updatedMessage) {
        for (Message message : messages) {
            if (message.getMessageId().equals(updatedMessage.getMessageId())) {
                messages.set(messages.indexOf(message), updatedMessage);
                orderMessagesByDate();
                notifyDataSetChanged();
                return;
            }
        }
    }

    private void orderMessagesByDate(){
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message firstMessage, Message secondMessage) {
                Long date1 = firstMessage.getDate();
                Long date2 = secondMessage.getDate();
                return date2.compareTo(date1);
            }
        });
    }

    private String determineDate(long date) {
        if (date < HOUR) {
            return String.valueOf(date / MINUTE) + " " + context.getString(R.string.minutes);
        } else if (date < DAY) {
            return String.valueOf(date / HOUR) + " " + context.getString(R.string.hour);
        } else if (date < WEEK) {
            return String.valueOf(date / DAY) + " " + context.getString(R.string.day);
        } else {
            return String.valueOf(date / WEEK) + " " + context.getString(R.string.week);
        }
    }

    private Drawable setDrawableColor(Drawable iconDrawable, int colorResource) {
        iconDrawable.setColorFilter(ContextCompat.getColor(context, colorResource),
                PorterDuff.Mode.SRC_IN);
        return iconDrawable;
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.user_image) ImageView userImage;
        @BindView(R.id.user_name) TextView userName;
        @BindView(R.id.user_message) TextView userMessage;
        @BindView(R.id.user_rating) RatingBar userRating;
        @BindView(R.id.user_rating_value) TextView userRatingValue;
        @BindView(R.id.message_likes) TextView messageLikes;
        @BindView(R.id.message_date) TextView messageDate;
        @BindView(R.id.message_menu) ImageView messageMenu;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}