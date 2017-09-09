package com.david.worldtourist.report.presentation.view;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.david.worldtourist.R;
import com.david.worldtourist.common.presentation.activity.HomeActivity;
import com.david.worldtourist.common.presentation.boundary.FragmentView;
import com.david.worldtourist.permissions.di.modules.PermissionControllerModule;
import com.david.worldtourist.utils.AndroidApplication;
import com.david.worldtourist.permissions.presentation.view.PermissionFragment;
import com.david.worldtourist.report.di.components.DaggerReportComponent;
import com.david.worldtourist.report.di.components.ReportComponent;
import com.david.worldtourist.report.domain.model.ReportFilter;
import com.david.worldtourist.report.presentation.boundary.ReportPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReportFragment extends PermissionFragment {

    private View view;
    private ReportPresenter<FragmentView> presenter;

    @BindView(R.id.radio_group) RadioGroup radioGroup;
    @BindView(R.id.send_button)TextView sendButton;

    //////////////////////////////Fragment Lifecycle///////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle saveInstanceState){
        super.onCreateView(inflater, container, saveInstanceState);

        view = inflater.inflate(R.layout.fragment_report, container, false);

        ReportComponent component = DaggerReportComponent.builder()
                .applicationComponent(AndroidApplication.get(getActivity()).getApplicationComponent())
                .permissionControllerModule(new PermissionControllerModule(this))
                .build();

        presenter = component.getReportPresenter();
        presenter.setView(this);
        presenter.onCreate();

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
    public void onDestroy(){
        presenter.onDestroy();
        super.onDestroy();
    }

    ///////////////////////////BaseView implementation///////////////////////////////
    @Override
    public void initializeViewComponents(){
        super.initializeViewComponents();
        ButterKnife.bind(this, view);

        ((HomeActivity) getActivity()).setActionBarTitle(getString(R.string.report));
    }

    @Override
    public void initializeViewListeners() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radio_spam:
                        presenter.setReportedProblem(ReportFilter.SPAM);
                        break;
                    case R.id.radio_violent:
                        presenter.setReportedProblem(ReportFilter.VIOLENCE);
                        break;
                    case R.id.radio_hate:
                        presenter.setReportedProblem(ReportFilter.HATE);
                        break;
                    case R.id.radio_image:
                        presenter.setReportedProblem(ReportFilter.WRONG_INFO);
                        break;
                    case R.id.radio_something_else:
                        presenter.setReportedProblem(ReportFilter.SOMETHING_ELSE);
                        addExtraInformation();
                        break;
                }

                sendButton.setEnabled(true);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.sendReport();
            }
        });
    }

    private void addExtraInformation() {
        final EditText editText = new EditText(getActivity());
        editText.setInputType(InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE);

        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.report_extra_info_title)
                .setMessage(R.string.report_extra_info_message)
                .setView(editText)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String extraInfo = editText.getText().toString();
                        presenter.setExtraInformation(extraInfo);
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }
}
