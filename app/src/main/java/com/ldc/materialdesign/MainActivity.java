package com.ldc.materialdesign;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.DimType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;

import java.util.Random;
//炫酷的弹出菜单

public class MainActivity extends AppCompatActivity implements BoomMenuButton.OnSubButtonClickListener, BoomMenuButton.AnimatorListener {

    private View mCustomView;
    private BoomMenuButton boomMenuButtonInActionBar;
    private BoomMenuButton boomMenuButton;
    private BoomMenuButton boomInfo;
    private boolean isInit=false;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;

        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.title_text);
        mTitleTextView.setText(R.string.app_name);

        boomMenuButtonInActionBar = (BoomMenuButton) mCustomView.findViewById(R.id.boom);

        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);

        ((Toolbar) mCustomView.getParent()).setContentInsetsAbsolute(0,0);
        boomMenuButton = (BoomMenuButton)findViewById(R.id.boom);
        boomInfo = (BoomMenuButton)mCustomView.findViewById(R.id.info);
        boomMenuButton.setDuration(2000);
        boomInfo.setDuration(2000);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!isInit) {
            initBoom();
//            initInfoBoom();
        }
        isInit = true;
    }

    //region initInfoBoom
    private void initInfoBoom() {
        Drawable[] drawables = new Drawable[3];
        int[] drawablesResource = new int[]{
                R.mipmap.boom,
                R.mipmap.java,
                R.mipmap.github
        };
        for (int i = 0; i < 3; i++)
            drawables[i] = ContextCompat.getDrawable(mContext, drawablesResource[i]);

        int[][] colors = new int[3][2];
        for (int i = 0; i < 3; i++) {
            colors[i][1] = ContextCompat.getColor(mContext, R.color.material_white);
            colors[i][0] = Util.getInstance().getPressedColor(colors[i][1]);
        }

        // Now with Builder, you can init BMB more convenient
        new BoomMenuButton.Builder()
                .subButtons(drawables, colors, new String[]{"BoomMenuButton", "View source code", "Follow me"})
                .button(ButtonType.HAM)
                .boom(BoomType.PARABOLA_2)
                .place(PlaceType.HAM_3_1)
                .subButtonsShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
                .subButtonTextColor(ContextCompat.getColor(mContext, R.color.black))
                .onSubButtonClick(new BoomMenuButton.OnSubButtonClickListener() {
                    @Override
                    public void onClick(int buttonIndex) {
                        if (buttonIndex == 0) {
                            Toast.makeText(mContext, "Boom!", Toast.LENGTH_SHORT).show();
                        } else if (buttonIndex == 1) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                                    "https://github.com/Nightonke/BoomMenu")));
                        } else if (buttonIndex == 2) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(
                                    "https://github.com/Nightonke")));
                        }
                    }
                })
                .init(boomInfo);
    }
    //endregion
    //region initBoom
    private void initBoom(){
        int number = 7;
        Drawable[] drawables = new Drawable[number];
        int[] drawablesResource = new int[]{
                R.mipmap.mark,
                R.mipmap.refresh,
                R.mipmap.copy,
                R.mipmap.heart,
                R.mipmap.info,
                R.mipmap.like,
                R.mipmap.record,
                R.mipmap.search,
                R.mipmap.settings
        };

        for (int i = 0;i< number; i++){
            drawables[i] = ContextCompat.getDrawable(mContext,drawablesResource[i]);

        }
        String[] STRINGS = new String[]{
                "Mark",
                "Refresh",
                "Copy",
                "Heart",
                "Info",
                "Like",
                "Record",
                "Search",
                "Settings"
        };
        String[] strings = new String[number];
        for (int i = 0; i < number; i++)
            strings[i] = STRINGS[i];

        int[][] colors = new int[number][2];
        for (int i = 0; i < number; i++) {
            colors[i][1] = GetRandomColor();
            colors[i][0] = Util.getInstance().getPressedColor(colors[i][1]);
        }
        ButtonType buttonType = ButtonType.CIRCLE;
        new BoomMenuButton.Builder()
                .subButtons(drawables,colors,strings)
                .button(buttonType)
                .boom(BoomType.HORIZONTAL_THROW)
                .place(PlaceType.CIRCLE_7_1)
                .boomButtonShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
                .subButtonsShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
                .onSubButtonClick(this)
                .animator(this)
                .init(boomMenuButton);

        // Now with Builder, you can init BMB more convenient
        new BoomMenuButton.Builder()
                .subButtons(drawables, colors, strings)
                .button(buttonType)
                .boom(BoomType.HORIZONTAL_THROW)
                .place(PlaceType.CIRCLE_7_1)
                .subButtonsShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
                .onSubButtonClick(this)
                .animator(this)
                .dim(DimType.DIM_0)
                .init(boomMenuButtonInActionBar);
    }
    private String[] Colors = {
            "#F44336",
            "#E91E63",
            "#9C27B0",
            "#2196F3",
            "#03A9F4",
            "#00BCD4",
            "#009688",
            "#4CAF50",
            "#8BC34A",
            "#CDDC39",
            "#FFEB3B",
            "#FFC107",
            "#FF9800",
            "#FF5722",
            "#795548",
            "#9E9E9E",
            "#607D8B"};
    public int GetRandomColor() {
        Random random = new Random();
        int p = random.nextInt(Colors.length);
        return Color.parseColor(Colors[p]);
    }
    //endregion

    //region 实现接口方法
    @Override
    public void onClick(int buttonIndex) {
        switch (buttonIndex){
            case 0:
                startActivity(new Intent(MainActivity.this,BLEActivity.class));
                break;
        }
    }

    @Override
    public void toShow() {

    }

    @Override
    public void showing(float fraction) {

    }

    @Override
    public void showed() {

    }

    @Override
    public void toHide() {

    }

    @Override
    public void hiding(float fraction) {

    }

    @Override
    public void hided() {

    }

    //endregion
}
