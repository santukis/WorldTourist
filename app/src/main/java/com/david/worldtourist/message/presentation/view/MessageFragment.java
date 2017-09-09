package com.david.worldtourist.message.presentation.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.david.worldtourist.R;
import com.david.worldtourist.permissions.presentation.view.PermissionFragment;
import com.david.worldtourist.message.presentation.boundary.MessageView;
import com.david.worldtourist.message.di.components.DaggerMessageComponent;
import com.david.worldtourist.message.di.components.MessageComponent;
import com.david.worldtourist.message.presentation.boundary.MessagePresenter;
import com.david.worldtourist.permissions.di.modules.PermissionControllerModule;
import com.david.worldtourist.utils.AndroidApplication;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MessageFragment extends PermissionFragment implements MessageView {

    private View view;

    private MessagePresenter<MessageView> presenter;

    private InputMethodManager inputManager;

    private MenuItem sendMenuButton;

    @BindView(R.id.user_image) ImageView userImage;
    @BindView(R.id.user_name) TextView userName;
    @BindView(R.id.user_rating) RatingBar userRating;
    @BindView(R.id.user_message) EditText userMessage;
    @BindView(R.id.send_button) ImageButton sendButton;
    @BindView(R.id.user_rating_value) TextView ratingValue;

    //////////////////////////////Fragment Lifecycle///////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState) {
        super.onCreateView(inflater, container, saveInstanceState);

        view = inflater.inflate(R.layout.fragment_message, container, false);

        MessageComponent component = DaggerMessageComponent.builder()
                .applicationComponent(AndroidApplication.get(getActivity()).getApplicationComponent())
                .permissionControllerModule(new PermissionControllerModule(this))
                .build();

        presenter = component.getMessagePresenter();
        presenter.setView(this);
        presenter.onCreate();

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle saveInstanceSate) {
        super.onActivityCreated(saveInstanceSate);

        View decorView = getActivity().getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.onStart();
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    ///////////////////////////////////////////////////////////////////////////////
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_write_message, menu);
        sendMenuButton = menu.getItem(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_publish_message:
                presenter.sendMessage(userMessage.getText().toString(),
                        userRating.getRating());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    ///////////////////////////BaseView implementation///////////////////////////////
    @Override
    public void initializeViewComponents() {
        super.initializeViewComponents();
        ButterKnife.bind(this, view);

        inputManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    @Override
    public void initializeViewListeners() {

        userRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (fromUser) {
                    userRating.setRating(rating);
                    ratingValue.setText(String.valueOf(rating));
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.sendMessage(userMessage.getText().toString(),
                        userRating.getRating());
            }
        });
    }

    @Override
    public void closeView() {
        try {
            inputManager.hideSoftInputFromWindow(getActivity()
                    .getCurrentFocus()
                    .getWindowToken(), 0);

        } catch (NullPointerException exception){

        } finally {
            super.closeView();
        }
    }

    ///////////////////////////MessageView implementation///////////////////////////////
    @Override
    public void showUserName(String name) {
        userName.setText(name);
    }

    @Override
    public void showUserPhoto(String photoUrl) {
        Glide.with(getActivity())
                .load(photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .fitCenter()
                .centerCrop()
                .into(userImage);
    }

    @Override
    public void showMessageRating(float rating) {
        ratingValue.setText(String.valueOf(rating));
        userRating.setRating(rating);
    }

    @Override
    public void showMessageText(String text) {
        userMessage.setText(text);
        userMessage.requestFocus();
    }

    @Override
    public void enableButtons() {
        sendMenuButton.setEnabled(true);
        sendButton.setEnabled(true);
    }

    @Override
    public void disableButtons() {
        sendMenuButton.setEnabled(false);
        sendButton.setEnabled(false);
    }
}