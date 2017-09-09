package com.david.worldtourist.itemsmap.presentation.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.david.worldtourist.R;
import com.david.worldtourist.itemsmap.domain.usecase.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.ViewHolder> {


    private Context context;
    private List<Step> steps = new ArrayList<>();

    public RouteAdapter(Context context) {
        this.context = context;
    }

    /////////////////////////RecyclerView.Adapter<> Implementation//////////////////////////

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.element_route, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Step step = steps.get(position);

        holder.stepInstructions.setText(Html.fromHtml(step.getInstructions()));
        holder.stepDistance.setText(step.getDistance());
        holder.stepDuration.setText(step.getDuration());
    }


    @Override
    public int getItemCount() {
        return steps.size();
    }

    public void addSteps(List<Step> steps) {
        this.steps.clear();
        this.steps.addAll(steps);
        notifyDataSetChanged();
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.step_instructions) TextView stepInstructions;
        @BindView(R.id.step_distance) TextView stepDistance;
        @BindView(R.id.step_duration) TextView stepDuration;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
