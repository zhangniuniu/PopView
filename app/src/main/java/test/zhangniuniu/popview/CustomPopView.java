package test.zhangniuniu.popview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Author：zhangyong on 2017/5/22 20:14
 * Email：zhangyonglncn@gmail.com
 * Description：PopWindow效果从底部弹出，背景渐变
 */

public class CustomPopView {
    private Context mContext;

    protected ViewGroup contentContainer;
    private ViewGroup decorView;//activity的根View
    private ViewGroup rootView;//附加View 的 根View


    private boolean isShowing;
    private boolean dismissing;

    private final FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM
    );
    private int contentContainerHeight;

    public CustomPopView(Context context) {
        this.mContext = context;
        initViews();
    }

    private void initViews() {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        decorView = (ViewGroup) ((Activity) mContext).getWindow().getDecorView().findViewById(android.R.id.content);
        //将控件添加到decorView中
        rootView = (ViewGroup) layoutInflater.inflate(R.layout.layout_custom_pop, decorView, false);
        rootView.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));
        //这个是真正要加载时间选取器的父布局
        contentContainer = (ViewGroup) rootView.findViewById(R.id.content_container);
        contentContainer.setLayoutParams(params);

        //设置外部点击消失
        contentContainer.setClickable(true);
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setKeyBackCancelable(true);
    }

    public CustomPopView setChildShowView(View showView) {
        contentContainer.removeAllViews();
        contentContainer.addView(showView);
        contentContainer.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        contentContainerHeight = contentContainer.getMeasuredHeight();

        return this;
    }


    /**
     * 添加这个View到Activity的根视图
     */
    public void show() {

        if (isShowing()) {
            return;
        }
        isShowing = true;
        onAttached(rootView);
        rootView.requestFocus();

    }

    public void dismiss() {
        if (dismissing) {
            return;
        }
        dismissing = true;


        final FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) contentContainer.getLayoutParams();
        ValueAnimator animator = ValueAnimator.ofInt(0, contentContainerHeight);
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int curValue = (int) animation.getAnimatedValue();
                layoutParams.bottomMargin = -curValue;
                contentContainer.setLayoutParams(layoutParams);
                float alpha = curValue * 1.0f / contentContainerHeight;
                int alphaInt = (int) (((1 - alpha) * 0.5f) * 255);
                int argb = Color.argb(alphaInt, 0, 0, 0);

                rootView.setBackgroundColor(argb);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                dismissImmediately();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }


    public CustomPopView setKeyBackCancelable(boolean isCancelable) {

        rootView.setFocusable(isCancelable);
        rootView.setFocusableInTouchMode(isCancelable);
        if (isCancelable) {
            rootView.setOnKeyListener(onKeyBackListener);
        } else {
            rootView.setOnKeyListener(null);
        }
        return this;
    }

    private View.OnKeyListener onKeyBackListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == MotionEvent.ACTION_DOWN
                    && isShowing()) {
                dismiss();
                return true;
            }
            return false;
        }
    };

    private void dismissImmediately() {
        //从activity根视图移除
        decorView.removeView(rootView);
        isShowing = false;
        dismissing = false;

    }

    /**
     * 检测该View是不是已经添加到根视图
     *
     * @return 如果视图已经存在该View返回true
     */
    public boolean isShowing() {
        return rootView.getParent() != null || isShowing;

    }

    /**
     * show的时候调用
     *
     * @param view 这个View
     */
    private void onAttached(View view) {
        decorView.addView(view);

        final FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) contentContainer.getLayoutParams();
        ValueAnimator animator = ValueAnimator.ofInt(contentContainerHeight, 0);
        animator.setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int curValue = (int) animation.getAnimatedValue();
                layoutParams.bottomMargin = -curValue;
                contentContainer.setLayoutParams(layoutParams);
                float alpha = 1 - curValue * 1.0f / contentContainerHeight;
                int alphaInt = (int) ((alpha * 0.5f) * 255);
                int argb = Color.argb(alphaInt, 0, 0, 0);
                rootView.setBackgroundColor(argb);
            }
        });
        animator.start();
    }


}
