package com.igor040897.moneytrackerpro;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.igor040897.moneytrackerpro.API.AddResult;
import com.igor040897.moneytrackerpro.API.Item;
import com.igor040897.moneytrackerpro.API.LSApi;
import com.igor040897.moneytrackerpro.API.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static android.app.Activity.RESULT_OK;
import static com.igor040897.moneytrackerpro.AddItemActivity.RC_ADD_ITEM;

/**
 * Created by fanre on 6/27/2017.
 */

public class ItemsFragment extends Fragment {
    public static final int LODER_ITEMS = 0;
    public static final int LODER_ADD = 1;
    public static final int LODER_REMOVE = 2;

    public static final String ARG_TYPE = "type";
    private ItemsAdapter adapter = new ItemsAdapter();

    private String type;
    private LSApi api;
    private FloatingActionButton add;
    private ActionMode actionMode;
    private SwipeRefreshLayout refresh;
    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.items, menu);
            add.hide();
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_remove:
                    new AlertDialog.Builder(getContext())
                            .setTitle(R.string.app_name)
                            .setMessage(R.string.confirm_remove)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int id) {
                                    for (int i = adapter.getSelectedItems().size() - 1; i >= 0; i--)
                                        removeItem(adapter.remove(adapter.getSelectedItems().get(i)));
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, null)
                            .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    actionMode.finish();
                                }
                            })
                            .show();
                    return true;
                case R.id.menu_repeat:
                    for (int i = adapter.getSelectedItems().size() - 1; i >= 0; i--) {
                        addItem(adapter.addRepeat(adapter.getSelectedItems().get(i)));
                    }//виправить
                    actionMode.finish();
                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            adapter.clearSelections();
            add.show();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.items, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RecyclerView items = (RecyclerView) view.findViewById(R.id.items);
        items.setAdapter(adapter);

        final GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public void onLongPress(MotionEvent e) {
                if (actionMode == null) {
                    actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
                    toggleSelection(e, items);
                }
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (actionMode != null) {
                    toggleSelection(e, items);
                }
                return super.onSingleTapConfirmed(e);
            }
        });
        items.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }

        });

        refresh = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadItems();
            }
        });

        add = (FloatingActionButton) view.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddItemActivity.class);
                intent.putExtra(AddItemActivity.EXTRA_TYPE, type);
                startActivityForResult(intent, RC_ADD_ITEM);
            }
        });
        type = getArguments().getString(ARG_TYPE);
        api = ((LSApp) getActivity().getApplication()).api();

        loadItems();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter.getItemCount() == 0 && type.equals(Item.TYPE_INCOME)) {
            loadItems();
        }
    }

    private void toggleSelection(MotionEvent e, RecyclerView items) {
        adapter.toggleSelection(items.getChildLayoutPosition(items.findChildViewUnder(e.getX(), e.getY())));
        actionMode.setTitle(adapter.getSelectedItems().size() + " выбрано");
    }

    private void addItem(final Item item) {
        Call<AddResult> call = api.add(item.name, item.price, item.type);
        call.enqueue(new Callback<AddResult>() {
            @Override
            public void onResponse(Call<AddResult> call, retrofit2.Response<AddResult> response) {
                response.body();
                adapter.add(item);
            }

            @Override
            public void onFailure(Call<AddResult> call, Throwable t) {
            }
        });
    }

    private void loadItems() {
        Call<List<Item>> call = api.items(type);
        call.enqueue(new Callback<List<Item>>() {
            @Override
            public void onResponse(Call<List<Item>> call, retrofit2.Response<List<Item>> response) {
                adapter.clear();
                adapter.addAll(response.body());
                refresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
            }
        });
    }

    private void removeItem(final Item selectedItemId) {
        Call<Result> call = api.remove(selectedItemId.id);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, retrofit2.Response<Result> response) {
                response.body();
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_ADD_ITEM && resultCode == RESULT_OK) {
            final Item item = (Item) data.getParcelableExtra(AddItemActivity.RESULT_ITEM);
            addItem(item);
            Toast.makeText(getContext(), item.name, Toast.LENGTH_LONG).show();

        }
    }
}
