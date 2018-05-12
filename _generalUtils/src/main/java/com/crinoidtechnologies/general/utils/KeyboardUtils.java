package com.crinoidtechnologies.general.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtils {

    public static void showKeyboard(Context activity) {
        InputMethodManager inputManager = (InputMethodManager) activity.
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.toggleSoftInput(0, InputMethodManager.SHOW_IMPLICIT);
        }

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity.
                getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * Registers listener for soft keyboard state changes.<br/>
     * The state is computed based on rootView height changes.<br/>
     * Note: In AndroidManifest corresponding activity should have <code>android:windowSoftInputMode</code>
     * set to <code>adjustResize</code>.
     *
     * @param rootView should be deepest full screen view, i.e. root of the layout passed to
     *                 Activity.setContentView(...) or view returned by Fragment.onCreateView(...)
     * @param listener Keyboard state listener
     */
    public static void addKeyboardShowListener(final View rootView, final OnKeyboardShowListener listener) {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            private boolean mIsKeyboardShown;
            private int mInitialHeightsDiff = -1;

            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);

                int heightDiff = rootView.getRootView().getHeight() - (r.bottom - r.top);
                if (mInitialHeightsDiff == -1) {
                    mInitialHeightsDiff = heightDiff;
                }
                heightDiff -= mInitialHeightsDiff;

                if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
                    if (!mIsKeyboardShown) {
                        mIsKeyboardShown = true;
                        listener.onKeyboardShow(true);
                    }
                } else if (heightDiff < 50) {
                    if (mIsKeyboardShown) {
                        mIsKeyboardShown = false;
                        listener.onKeyboardShow(false);
                    }
                }
            }
        });
    }

    public static interface OnKeyboardShowListener {
        void onKeyboardShow(boolean show);
    }

//    public static boolean isKeyboardPresent(Activity activity){
//        InputMethodManager inputManager = (InputMethodManager) activity.
//                getSystemService(Context.INPUT_METHOD_SERVICE);
//        return inputManager.isAcceptingText();
//    }
}