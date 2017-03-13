package edu.utdallas.csdesign.spring17.nutriscope.overview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import edu.utdallas.csdesign.spring17.nutriscope.R;
import edu.utdallas.csdesign.spring17.nutriscope.R2;
import edu.utdallas.csdesign.spring17.nutriscope.addeditfood.AddEditFoodActivity;
import edu.utdallas.csdesign.spring17.nutriscope.data.ConsumedFood;
import edu.utdallas.csdesign.spring17.nutriscope.data.Trackable;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by john on 2/10/17.
 */

public class OverviewFragment extends Fragment implements OverviewContract.View {

    final private static String TAG = "OverviewFragment";

    private RecyclerView overviewRecyclerView;
    private OverviewRecyclerViewAdapter adapter;

    private OverviewContract.Presenter presenter;
    private BottomSheetBehavior behavior;

    @BindView(R2.id.fab_add_overview) FloatingActionButton fab;
    @BindView(R2.id.overview_tabs) TabLayout tabLayout;
    @BindView(R2.id.toolbar) Toolbar toolbar;
    @BindView(R.id.overview_bottom_sheet) LinearLayout bottomSheet;

    public OverviewFragment() {

    }

    public static OverviewFragment newInstance() {
        return new OverviewFragment();
    }


    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void setPresenter(@NonNull OverviewContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        ButterKnife.bind(this, view);


        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_menu_hamburger));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_menu_graph));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_menu_calendar));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_menu_recipe));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_menu_gear));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_search));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tabLayout.getSelectedTabPosition() == 0) {
                    Toast.makeText(getActivity(), "Tab " + tabLayout.getSelectedTabPosition(), Toast.LENGTH_LONG).show();
                } else if (tabLayout.getSelectedTabPosition() == 1) {
                    Toast.makeText(getActivity(), "Tab " + tabLayout.getSelectedTabPosition(), Toast.LENGTH_LONG).show();
                } else if (tabLayout.getSelectedTabPosition() == 2) {
                    Toast.makeText(getActivity(), "Tab " + tabLayout.getSelectedTabPosition(), Toast.LENGTH_LONG).show();
                } else if (tabLayout.getSelectedTabPosition() == 3) {
                    Toast.makeText(getActivity(), "Tab " + tabLayout.getSelectedTabPosition(), Toast.LENGTH_LONG).show();
                } else if (tabLayout.getSelectedTabPosition() == 4) {
                    Toast.makeText(getActivity(), "Tab " + tabLayout.getSelectedTabPosition(), Toast.LENGTH_LONG).show();
                } else if (tabLayout.getSelectedTabPosition() == 5) {
                    Toast.makeText(getActivity(), "Tab " + tabLayout.getSelectedTabPosition(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        overviewRecyclerView = (RecyclerView) view.findViewById(R.id.overview_recycler_view);
        overviewRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        overviewRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

                }

            }
        });


        this.behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN) {
                    fab.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });


        return view;
    }

    @OnClick(R2.id.overview_bottom_sheet_add_food)
    public void submit() {
        Log.d(TAG, "overview fab clicked");
        showAddEditFood();

    }

    @OnClick(R2.id.fab_add_overview)
    public void showBottomSheet() {
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        fab.setVisibility(View.INVISIBLE);

    }

    private void hideBottomSheet() {
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    public void showAddEditFood() {
        hideBottomSheet();
        Intent intent = new Intent(getActivity(), AddEditFoodActivity.class);
        startActivity(intent);

    }

    @Override
    public void showHistory(List<Trackable> list) {

        if (adapter == null) {

            adapter = new OverviewRecyclerViewAdapter(list);
            overviewRecyclerView.setAdapter(adapter);
        } else {
            adapter.setList(list);
            adapter.notifyDataSetChanged();
        }

    }

    class ViewHolderFood extends RecyclerView.ViewHolder {

        @BindView(R.id.food_consumed_name)
        TextView foodName;

        public ViewHolderFood(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }

        public TextView getFoodName() {
            return foodName;
        }

        public void setFoodName(TextView foodName) {
            this.foodName = foodName;
        }

    }


    private class OverviewRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        // The items to display in your RecyclerView
        private List<Trackable> items;

        private final int FOOD = 0;

        // Provide a suitable constructor (depends on the kind of dataset)
        public OverviewRecyclerViewAdapter(List<Trackable> items) {
            this.items = items;
        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return this.items.size();
        }

        //Returns the view type of the item at position for the purposes of view recycling.
        @Override
        public int getItemViewType(int position) {
            if (items.get(position) instanceof ConsumedFood) {
                return FOOD;
            }
            return -1;
        }

        /**
         * This method creates different RecyclerView.ViewHolder objects based on the item view type.\
         *
         * @param viewGroup ViewGroup container for the item
         * @param viewType  type of view to be inflated
         * @return viewHolder to be inflated
         */
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            RecyclerView.ViewHolder viewHolder;
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

            switch (viewType) {
                case FOOD:
                    View v1 = inflater.inflate(R.layout.list_item_food_consumed, viewGroup, false);
                    viewHolder = new ViewHolderFood(v1);
                    break;
                default:
                    viewHolder = null;
                    break;
            }
            return viewHolder;
        }

        /**
         * This method internally calls onBindViewHolder(ViewHolder, int) to update the
         * RecyclerView.ViewHolder contents with the item at the given position
         * and also sets up some private fields to be used by RecyclerView.
         *
         * @param viewHolder The type of RecyclerView.ViewHolder to populate
         * @param position   Item position in the viewgroup.
         */
        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            switch (viewHolder.getItemViewType()) {
                case FOOD:
                    ViewHolderFood vh1 = (ViewHolderFood) viewHolder;
                    configureViewHolderFood(vh1, position);
                    break;
                default:
                    break;
            }
        }


        private void configureViewHolderFood(ViewHolderFood vhf, int position) {
            ConsumedFood food = (ConsumedFood) items.get(position);
            if (food != null) {
                vhf.getFoodName().setText("Name: " + food.getTimeStamp());
            }
        }


        public void setList(List<Trackable> trackable) {
            this.items = trackable;
        }

    }


}
