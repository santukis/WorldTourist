package com.david.worldtourist.itemsdetail.presentation.view;


import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.david.worldtourist.R;
import com.david.worldtourist.common.presentation.activity.HomeActivity;
import com.david.worldtourist.permissions.presentation.view.PermissionFragment;
import com.david.worldtourist.common.presentation.navigation.FragmentType;
import com.david.worldtourist.items.domain.model.GeoCoordinate;
import com.david.worldtourist.items.domain.model.ItemDate;
import com.david.worldtourist.items.domain.model.ItemNameFilter;
import com.david.worldtourist.items.domain.model.ItemType;
import com.david.worldtourist.items.domain.model.Photo;
import com.david.worldtourist.items.domain.model.Ticket;
import com.david.worldtourist.itemsdetail.di.components.DaggerItemDetailComponent;
import com.david.worldtourist.itemsdetail.di.components.ItemDetailComponent;
import com.david.worldtourist.itemsdetail.presentation.adapter.ImageAdapter;
import com.david.worldtourist.itemsdetail.presentation.adapter.MessageAdapter;
import com.david.worldtourist.itemsdetail.presentation.boundary.ItemDetailPresenter;
import com.david.worldtourist.itemsdetail.presentation.boundary.ItemDetailView;
import com.david.worldtourist.authentication.domain.model.User;
import com.david.worldtourist.message.domain.model.Message;
import com.david.worldtourist.message.domain.model.MessageActionFilter;
import com.david.worldtourist.permissions.di.modules.PermissionControllerModule;
import com.david.worldtourist.rating.domain.model.Rating;
import com.david.worldtourist.message.presentation.component.MessageClick;
import com.david.worldtourist.utils.AndroidApplication;
import com.david.worldtourist.utils.Constants;
import com.david.worldtourist.utils.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemDetailFragment extends PermissionFragment implements ItemDetailView,
        MessageClick, ViewPager.OnPageChangeListener {

    private View view;
    private View messageView;

    private ItemDetailPresenter<ItemDetailView> presenter;

    private ImageAdapter imageAdapter;
    private MessageAdapter messageAdapter;

    private PopupMenu messageMenu;

    private List<ImageView> dots = new ArrayList<>();

    @BindView(R.id.item_icon) ImageView itemIcon;
    @BindView(R.id.item_name) TextView itemName;
    @BindView(R.id.item_rating) RatingBar itemRating;
    @BindView(R.id.item_rating_value) TextView itemRatingValue;
    @BindView(R.id.item_address) TextView itemAddress;
    @BindView(R.id.item_timetable) TextView itemTimetable;
    @BindView(R.id.item_date) TextView itemDate;
    @BindView(R.id.item_horary) TextView itemTime;
    @BindView(R.id.item_ticket) TextView itemTicket;
    @BindView(R.id.item_telephone) TextView itemPhone;
    @BindView(R.id.item_info) TextView itemInfo;
    @BindView(R.id.item_panoramic) TextView itemPanoramic;
    @BindView(R.id.item_add_item) TextView addItemToUserLists;
    @BindView(R.id.item_description) WebView itemDescription;
    @BindView(R.id.item_add_photos) TextView itemAddPhotos;
    @BindView(R.id.image_gallery) ViewPager imageGallery;
    @BindView(R.id.image_dots_layout) LinearLayout dotsLayout;
    @BindView(R.id.item_message_title) TextView reviewTitle;
    @BindView(R.id.item_edit_message) FloatingActionButton editMessageButton;

    //////////////////////////////Fragment Lifecycle///////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState) {
        super.onCreateView(inflater, container, saveInstanceState);

        view = inflater.inflate(R.layout.fragment_item_detail, container, false);

        ItemDetailComponent component = DaggerItemDetailComponent.builder()
                .applicationComponent(AndroidApplication.get(getActivity()).getApplicationComponent())
                .permissionControllerModule(new PermissionControllerModule(this))
                .build();

        presenter = component.getItemsDetailPresenter();
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
    public void onStop() {
        presenter.onStop();
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        presenter.onDestroy();
        super.onDestroyView();
    }

    ////////////////////////////Menu options//////////////////////////////////
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_detail, menu);
        menu.getItem(0).getIcon().setColorFilter(ContextCompat.getColor(getActivity(),
                android.R.color.white), PorterDuff.Mode.SRC_IN);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                showFilteringPopUpMenu();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFilteringPopUpMenu() {
        PopupMenu popupMenu =
                new PopupMenu(getActivity(), getActivity().findViewById(R.id.menu_filter));
        popupMenu.getMenuInflater().inflate(R.menu.menu_detail_filter, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_share:
                        presenter.shareItem();
                        return true;

                    case R.id.menu_message_write:
                        presenter.editMessage(Message.MESSAGE_EMPTY, MessageActionFilter.CREATE);
                        return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    ////////////////////////////BaseView implementation/////////////////////////////
    @Override
    public void initializeViewComponents() {
        super.initializeViewComponents();
        ButterKnife.bind(this, view);

        RecyclerView messageRecycler = (RecyclerView) view.findViewById(R.id.recycler_view);
        messageAdapter = new MessageAdapter(getActivity().getApplicationContext());
        messageAdapter.setUser(presenter.loadUser());
        messageAdapter.setMenuAction(this);
        messageRecycler.setAdapter(messageAdapter);
        messageRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));

        imageAdapter = new ImageAdapter(getActivity());
        imageGallery.setAdapter(imageAdapter);
        imageGallery.addOnPageChangeListener(this);
    }

    /////////////////////ItemDetailView implementation/////////////////////////
    @Override
    public void share(String name, String info) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, (
                Html.fromHtml(String.format(
                        getString(R.string.item_text),
                        getString(presenter.getItemName(ItemNameFilter.SINGLE))) + " " + name) + " " + info));
        startActivity(Intent.createChooser(intent, getString(R.string.send_to)));
    }

    @Override
    public void showItemName(String itemName) {
        if (itemName.isEmpty()) {
            this.itemName.setVisibility(View.GONE);

        } else {
            this.itemName.setText(itemName);
            ((HomeActivity) getActivity()).setActionBarTitle(itemName);
        }
    }

    @Override
    public void showItemIcon(ItemType itemType) {
        if (itemType == ItemType.NONE) {
            itemIcon.setVisibility(View.GONE);

        } else {
            itemIcon.setImageDrawable(ContextCompat.getDrawable(getActivity(),
                    itemType.getIcon()));
        }
    }

    @Override
    public void showItemAddress(String address, String city) {
        if(!address.isEmpty() || !city.isEmpty()) {
            address = address.concat(" ").concat(city);
            itemAddress.setText(address);
        }
    }

    @Override
    public void showItemTimetable(final String[] itemTimetable) {
        if (itemTimetable.length == 0) {
            this.itemTimetable.setVisibility(View.GONE);

        } else {
            this.itemTimetable.setText(getTimetable(itemTimetable));
            this.itemTimetable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.item_horary)
                            .setItems(itemTimetable, null)
                            .show();
                }
            });
        }
    }

    private String getTimetable(String[] timetable) {
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);

        int timetableIndex = currentDay - 2 > 0 ? currentDay - 2 : 6;
        return timetable[timetableIndex];
    }

    @Override
    public void showItemDate(ItemDate startDate, ItemDate endDate) {
        if (startDate == ItemDate.EMPTY_DATE) {
            itemDate.setVisibility(View.GONE);

        } else {
            String date = StringUtils.getConvertedDate(startDate.getDate());

            if(endDate != ItemDate.EMPTY_DATE){
                date += " - " + StringUtils.getConvertedDate(endDate.getDate());
            }
            itemDate.setText(date);
        }
    }

    @Override
    public void showItemTime(ItemDate startDate) {
        final String HOURS = " h";

        if (startDate.getTime().isEmpty()) {
            itemTime.setVisibility(View.GONE);

        } else {
            itemTime.setText(StringUtils.getFormattedDate(startDate.getTime(),
                    StringUtils.HOUR_MINUTES).concat(HOURS));
        }
    }

    @Override
    public void showItemTicket(Ticket ticket) {
        final String SPACE = " ";

        if(ticket == Ticket.EMPTY_TICKET) {
            itemTicket.setVisibility(View.GONE);

        } else {
            String price = getString(R.string.from)
                    .concat(SPACE)
                    .concat(String.valueOf(ticket.getMinPrice()))
                    .concat(SPACE)
                    .concat(ticket.getCurrency());

            if(ticket.getMaxPrice() != 0F) {
                price += SPACE
                        .concat(getString(R.string.to))
                        .concat(SPACE)
                        .concat(String.valueOf(ticket.getMaxPrice()))
                        .concat(SPACE)
                        .concat(ticket.getCurrency());
            }
            itemTicket.setText(price);
        }
    }

    @Override
    public void showItemPhone(String itemPhone) {
        if (itemPhone.isEmpty()) {
            this.itemPhone.setVisibility(View.GONE);

        } else {
            this.itemPhone.setText(itemPhone);
        }
    }

    @Override
    public void setupPhoneListener(final String itemPhone) {
        this.itemPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    getActivity().startActivity(new Intent(
                            Intent.ACTION_DIAL, Uri.parse("tel:" + itemPhone)));

                } catch (ActivityNotFoundException exception) {
                    showToastMessage(
                            R.string.no_calls_phone,
                            android.R.drawable.ic_dialog_alert);
                }

            }
        });
    }

    @Override
    public void showItemDescription(String itemDescription) {
        if (itemDescription.isEmpty()) {
            this.itemDescription.setVisibility(View.GONE);

        } else {
            this.itemDescription.loadDataWithBaseURL(
                    null, itemDescription, "text/html", "utf-8", null);
        }
    }

    @Override
    public void showItemPhotos(List<Photo> itemPhotos) {
        if (itemPhotos.isEmpty()) {
            imageGallery.setVisibility(View.GONE);

        } else {
            imageAdapter.insertData(itemPhotos);
        }
    }

    @Override
    public void showDotsLayout(int dotsAmount) {
        if (dotsAmount > 0) {
            if(!dots.isEmpty()){
                dots.clear();
                dotsLayout.removeAllViews();
            }
            for (int position = 0; position < dotsAmount; position++) {
                ImageView dot = createDot();
                dotsLayout.addView(dot);
                dots.add(dot);
            }

            dots.get(0).setImageResource(R.drawable.dot_active);
        }
    }

    private ImageView createDot(){
        ImageView dot = new ImageView(getActivity());
        dot.setImageResource(R.drawable.dot_inactive);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(getResources().getDimensionPixelSize(R.dimen.inter_dot_margin), 0, 0, 0);
        dot.setLayoutParams(params);
        return dot;
    }

    @Override
    public void showItemRating(Rating itemRating) {
        if(itemRating.getRatingSize() == 0) {
            itemRatingValue.setVisibility(View.GONE);
            reviewTitle.setVisibility(View.GONE);

            this.itemRating.setRating(0);

        } else {
            itemRatingValue.setVisibility(View.VISIBLE);
            reviewTitle.setVisibility(View.VISIBLE);

            this.itemRating.setRating(itemRating.calculateAverageRating());
            itemRatingValue.setText(itemRating.convertRatingToText());
        }
    }

    @Override
    public void setupMapListener(final GeoCoordinate itemCoordinates) {
        if (itemCoordinates == GeoCoordinate.EMPTY_COORDINATE) {
            itemAddress.setVisibility(View.GONE);

        } else {
            itemAddress.setVisibility(View.VISIBLE);
            itemAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.cacheMapCoordinates(itemCoordinates);
                }
            });
        }
    }

    @Override
    public void setupInfoListener(final String url) {
        if (url.isEmpty()) {
            itemInfo.setVisibility(View.GONE);

        } else {
            itemInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle arguments = new Bundle();
                    arguments.putString(Constants.URL_KEY, url);

                    ((HomeActivity) getActivity()).openFragment(FragmentType.WEB, arguments);
                }
            });
        }
    }

    @Override
    public void setupPanoramicListener(final GeoCoordinate itemCoordinates) {
        if (itemCoordinates == GeoCoordinate.EMPTY_COORDINATE) {
            itemPanoramic.setVisibility(View.GONE);

        } else {
            itemPanoramic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle arguments = new Bundle();
                    arguments.putSerializable(Constants.COORDINATES_KEY, itemCoordinates);

                    ((HomeActivity) getActivity()).openFragment(FragmentType.STREET_VIEW, arguments);
                }
            });
        }
    }

    @Override
    public void showUserListIcon(boolean isShown) {
        addItemToUserLists.setActivated(isShown);
    }

    @Override
    public void setupUserListListener(final String name,
                                      final boolean isFavourite,
                                      final boolean isToVisit) {

        addItemToUserLists.setText(String.format(getString(R.string.add_to_user_list),
                getString(presenter.getItemName(ItemNameFilter.SINGLE))));

        final String itemType = getString(presenter.getItemName(ItemNameFilter.PLURAL));

        addItemToUserLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle(String.format(getString(R.string.add_message), name))
                        .setCancelable(false)
                        .setMultiChoiceItems(
                                new CharSequence[]{
                                        (String.format(getString(R.string.menu_favourite_items), itemType)),
                                        (String.format(getString(R.string.menu_items_to_visit), itemType))},
                                new boolean[]{isFavourite, isToVisit},
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface,
                                                        int whichButton, boolean isChecked) {
                                        presenter.updateItemUserFilter(whichButton, isChecked);
                                    }}
                        )
                        .setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                       presenter.updateItemUserLists();
                                    }
                        })
                        .show();
            }
        });
    }

    @Override
    public void loadView(FragmentType type) {
        ((HomeActivity) getActivity()).openFragment(type, null);
    }

    @Override
    public void setupMessageMenu(boolean isUserMessage, boolean isUserInUserLikesList){
        messageMenu = new PopupMenu(getActivity(), messageView);
        messageMenu.getMenuInflater().inflate(R.menu.menu_message, messageMenu.getMenu());

        messageMenu.getMenu().findItem(R.id.message_edit).setVisible(isUserMessage);
        messageMenu.getMenu().findItem(R.id.message_delete).setVisible(isUserMessage);
        messageMenu.getMenu().findItem(R.id.message_likes).setVisible(!isUserMessage);
        messageMenu.getMenu().findItem(R.id.message_report).setVisible(!isUserMessage);

        if (isUserInUserLikesList) {
            messageMenu.getMenu().findItem(R.id.message_likes).setTitle(R.string.menu_message_no_likes);
        }
    }

    @Override
    public void showMessageMenu(final Message message, final User user) {

        messageMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.message_likes:
                        if (message.getUserLikesIds().contains(user.getId()))
                            presenter.removeLikeToMessage(message, user.getId());
                        else
                            presenter.addLikeToMessage(message, user.getId());
                        return true;

                    case R.id.message_edit:
                        presenter.editMessage(message, MessageActionFilter.UPDATE);
                        return true;

                    case R.id.message_delete:
                        showDeleteMessage(message);
                        return true;

                    case R.id.message_report:
                        presenter.editReport(user, message);

                        ((HomeActivity) getActivity()).openFragment(FragmentType.REPORT, null);
                        return true;
                }
                return false;
            }
        });

        messageMenu.show();
    }

    @Override
    public void setupWriteMessageListener() {
        editMessageButton.setVisibility(View.VISIBLE);
        editMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.editMessage(Message.MESSAGE_EMPTY, MessageActionFilter.CREATE);
            }
        });
    }

    @Override
    public void setupAddPhotosListener() {
        itemAddPhotos.setVisibility(View.GONE);
        itemAddPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO implementar firebase storage
            }
        });
    }

    @Override
    public void addMessage(Message message) {
        messageAdapter.addMessage(message);
    }

    @Override
    public void updateMessage(Message message) {
        messageAdapter.updateMessage(message);
    }

    @Override
    public void deleteMessage(Message message) {
        messageAdapter.removeMessage(message);
    }

    ////////////////////////MessageClick implementation/////////////////////////
    @Override
    public void onMessageClick(View view, final Message message) {
        messageView = view;
        presenter.onMessageMenuClick(message);
    }

    private void showDeleteMessage(final Message message) {
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.delete_message_title)
                .setIcon(android.R.drawable.ic_delete)
                .setMessage(R.string.delete_message_ask)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.deleteMessage(message);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    ////////////////ViewPager.OnPageChangeListener implementation//////////////////
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (!dots.isEmpty()) {
            for(ImageView dot : dots){
                dot.setImageResource(R.drawable.dot_inactive);
            }
            dots.get(position).setImageResource(R.drawable.dot_active);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}