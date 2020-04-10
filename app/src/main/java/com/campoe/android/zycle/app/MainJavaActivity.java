package com.campoe.android.zycle.app;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.campoe.android.zycle.R;
import com.campoe.android.zycle.adapter.Adapter;
import com.campoe.android.zycle.adapter.util.AdapterPositionLookup;
import com.campoe.android.zycle.app.binder.HeaderBinder;
import com.campoe.android.zycle.app.binder.ItemBinder;
import com.campoe.android.zycle.click.ktx.KtxKt;
import com.campoe.android.zycle.condition.Condition;
import com.campoe.android.zycle.mapper.Mapper;
import com.campoe.android.zycle.observablelist.ObservableList;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import kotlin.Unit;

public class MainJavaActivity extends AppCompatActivity {

    private static final int Sections = 5;
    private static final int SectionItemCount = 9;
    private static final int ImageOffset = 160;
    private static final int ItemCount = Sections * SectionItemCount;
    private static final int EmojiOffset = 0x1F60A;
    private static final int GridSpanCount = 3;

    private ObservableList<Object> list = new ObservableList<>();

    private MenuItem menuItemGrid;
    private MenuItem menuItemList;

    private MenuItem menuItemHide;
    private MenuItem menuItemShow;

    private Condition condition = new Condition();

    private HeaderBinder headerBinder = new HeaderBinder();
    private ItemBinder itemBinder = new ItemBinder();

    private RecyclerView recyclerView;
    private AdapterPositionLookup adapterPositionLookup;
    private GridLayoutManager.SpanSizeLookup spanSizeLookup =
            new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int innerPosition = adapterPositionLookup.innerPosition(position);
                    if (list.get(innerPosition) instanceof ItemBinder.Item) {
                        return 1;
                    } else {
                        return GridSpanCount;
                    }
                }
            };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        KtxKt.onClick(itemBinder, (holder, item, position) -> { // TODO: write in a different way
            Snackbar.make(holder.itemView, item.getDescription(), Snackbar.LENGTH_SHORT).show();
            return Unit.INSTANCE; // TODO: don't require return of Unit
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Mapper<Object> mapper = new Mapper.Builder<>()
                .map(HeaderBinder.Header.class, headerBinder)
                .map(ItemBinder.Item.class, itemBinder)
                .build();
        Adapter adapter = new Adapter.Builder()
                .viewsOf(R.layout.header_top)
                .adapterOf(list, mapper)
                .postBuild(it -> it.showIfElse(condition, R.layout.item_empty))
                .viewsOf(R.layout.footer_bottom)
                .build();
        adapterPositionLookup = new AdapterPositionLookup(adapter);
        adapter.attach(recyclerView);

        int section = 1;
        for (int i = 0; i < ItemCount; i += SectionItemCount) {
            list.add(newHeader(section));
            for (int j = 0; j < SectionItemCount; ++j) {
                list.add(newItem(i + j));
            }
            ++section;
        }
    }

    private HeaderBinder.Header newHeader(int i) {
        return new HeaderBinder.Header(
                i,
                "Section " + i
        );
    }

    private ItemBinder.Item newItem(int i) {
        return new ItemBinder.Item(
                i,
                new String(Character.toChars(EmojiOffset + i)),
                "In chislic ut labore ipsum dolore nulla excepteur frankfurter aute shoulder swine.",
                "https://picsum.photos/id/${ImageOffset + i}/320/200"
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menuItemHide = menu.findItem(R.id.hide_list);
        menuItemShow = menu.findItem(R.id.show_list);
        menuItemGrid = menu.findItem(R.id.switch_grid);
        menuItemList = menu.findItem(R.id.switch_list);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.hide_list: {
                item.setVisible(false);
                menuItemShow.setVisible(true);
                condition.assign(false);
                return true;
            }
            case R.id.show_list: {
                item.setVisible(false);
                menuItemHide.setVisible(true);
                condition.assign(false);
                return true;
            }
            case R.id.switch_grid: {
                item.setVisible(false);
                menuItemList.setVisible(true);
                recyclerView.setLayoutManager(
                        itemBinder.switchLayout(1, recyclerView.getContext()));
                return true;
            }
            case R.id.switch_list: {
                item.setVisible(false);
                menuItemGrid.setVisible(true);
                RecyclerView.LayoutManager layoutManager =
                        itemBinder.switchLayout(GridSpanCount, recyclerView.getContext());
                if (layoutManager instanceof GridLayoutManager) {
                    ((GridLayoutManager) layoutManager).setSpanSizeLookup(spanSizeLookup);
                }
                recyclerView.setLayoutManager(layoutManager);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    void scrollToBottom(View view) {
        recyclerView.smoothScrollToPosition(
                Objects.requireNonNull(recyclerView.getAdapter()).getItemCount() - 1);
    }

    void scrollToTop(View view) {
        recyclerView.smoothScrollToPosition(0);
    }

}
