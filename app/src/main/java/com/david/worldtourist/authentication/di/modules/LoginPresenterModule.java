package com.david.worldtourist.authentication.di.modules;


import com.david.worldtourist.common.domain.UseCaseHandler;
import com.david.worldtourist.authentication.data.boundary.UserRepository;
import com.david.worldtourist.authentication.domain.usecase.AuthResult;
import com.david.worldtourist.authentication.domain.usecase.DoLogin;
import com.david.worldtourist.authentication.domain.usecase.GetUser;
import com.david.worldtourist.authentication.domain.usecase.SaveUser;
import com.david.worldtourist.authentication.presentation.boundary.LoginPresenter;
import com.david.worldtourist.authentication.presentation.boundary.LoginView;
import com.david.worldtourist.authentication.presentation.presenter.LoginPresenterImp;

import dagger.Module;
import dagger.Provides;

@Module(includes = {UserRepositoryModule.class, AuthenticationControllerModule.class})
public class LoginPresenterModule {

    @Provides
    public LoginPresenter<LoginView> loginPresenter(UseCaseHandler useCaseHandler,
                                                    DoLogin doLogin,
                                                    AuthResult authResult,
                                                    GetUser getUser,
                                                    SaveUser saveUser){
        return new LoginPresenterImp<>(
                useCaseHandler,
                doLogin,
                authResult,
                getUser,
                saveUser);
    }

    @Provides
    public GetUser getUser(UserRepository userRepository) {
        return new GetUser(userRepository);
    }

    @Provides
    public SaveUser saveUser(UserRepository userRepository) {
        return new SaveUser(userRepository);
    }
}
