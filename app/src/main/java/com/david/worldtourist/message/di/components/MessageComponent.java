package com.david.worldtourist.message.di.components;

import com.david.worldtourist.common.di.components.ApplicationComponent;
import com.david.worldtourist.items.di.scopes.FragmentScope;
import com.david.worldtourist.message.presentation.boundary.MessageView;
import com.david.worldtourist.message.di.modules.MessagePresenterModule;
import com.david.worldtourist.message.presentation.boundary.MessagePresenter;

import dagger.Component;

@FragmentScope
@Component(modules = MessagePresenterModule.class,
        dependencies = {ApplicationComponent.class})
public interface MessageComponent {

    MessagePresenter<MessageView> getMessagePresenter();
}
