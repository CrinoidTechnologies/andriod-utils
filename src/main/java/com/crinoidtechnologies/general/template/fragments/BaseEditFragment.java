package com.crinoidtechnologies.general.template.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.crinoidtechnologies.general.R;
import com.crinoidtechnologies.general.models.BaseModel;
import com.crinoidtechnologies.general.utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ${Vivek} on 8/8/2016 for MyVoice.Be careful
 */

public abstract class BaseEditFragment<T extends BaseModel> extends BaseNetworkFragment {

    protected T data;
    protected boolean isEditing = false;
    protected boolean is24Time = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected void hideKeyboard() {
        View view = this.getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    protected abstract void setData();

    protected void endActivity(boolean isOk) {
        if (isOk) {
            getActivity().setResult(Activity.RESULT_OK);
        }
        getActivity().finish();
    }

    protected void showDateOnEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v, Math.max(System.currentTimeMillis(), getMinDateForEditText((EditText) v)));
            }
        });

//        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus){
//                    hideKeyboard();
//                    showDatePickerDialog(v, Math.max(System.currentTimeMillis(), getMinDateForEditText((EditText) v)));
//                }
//            }
//        });
    }

    protected void showTimeOnEditText(EditText editText, final boolean is24Time) {
        editText.setFocusable(false);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog(v, Math.max(System.currentTimeMillis(), getMinTimeForEditText((EditText) v)), is24Time);
            }
        });
    }

    protected long getMinTimeForEditText(EditText v) {
        return System.currentTimeMillis();
    }

    protected long getMinDateForEditText(EditText v) {
        return System.currentTimeMillis();

    }

    protected void configureAutoCompleteTextView(final AutoCompleteTextView textView, List<String> items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_dropdown_item_1line, items);
        textView.setAdapter(adapter);
        textView.setThreshold(1);
        textView.setFocusable(false);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.showDropDown();
                hideKeyboard();
            }
        });
    }

    protected void updateAutoCompleteTextViewAdapter(AutoCompleteTextView textView, List<String> items) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) textView.getAdapter();
        if (adapter != null) {
            adapter.clear();
            adapter.addAll(items);
            adapter.notifyDataSetChanged();
            if (textView.isPopupShowing()) {
                textView.showDropDown();
            }
        }
    }

    protected void showDatePickerDialog(final View v, long minTime) {
        hideKeyboard();
        Calendar myCalendar = Calendar.getInstance();

        if (v instanceof EditText) {
            String date = ((EditText) v).getText().toString();
            if (!date.isEmpty()) {
                myCalendar.setTime(DateUtils.stringToDate(DateUtils.defaultDateFormat, date));
            }
        }

        DatePickerDialog dialog = new DatePickerDialog(getContext(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                if (v instanceof EditText) {
                    ((EditText) v).setText(DateUtils.defaultDateFormat.format(myCalendar.getTime()));
                    ((EditText) v).setError(null);
                }
            }

        }, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));

        dialog.getDatePicker().setMinDate(minTime);
        dialog.show();
    }

    protected void showTimePickerDialog(final View v, final long minTime) {
        showTimePickerDialog(v, minTime, is24Time);
    }

    protected SimpleDateFormat getTimeFormat(boolean is24) {
        return is24 ? DateUtils.TIME_FORMAT_24 : DateUtils.TIME_FORMAT;
    }

    protected void showTimePickerDialog(final View v, final long minTime, final boolean is24Time) {
        hideKeyboard();
        Calendar myCalendar = Calendar.getInstance();

        if (minTime > 0) {
            myCalendar.setTimeInMillis(minTime);
        }

        if (v instanceof EditText) {
            String date = ((EditText) v).getText().toString();
            if (!date.isEmpty()) {
                myCalendar.setTime(DateUtils.stringToDate(getTimeFormat(is24Time), date));
            }
        }

        TimePickerDialog dialog = new TimePickerDialog(getContext(), R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.HOUR, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);

                if (v instanceof EditText) {
                    ((EditText) v).setText(is24Time ? DateUtils.TIME_FORMAT_24.format(myCalendar.getTime()) : DateUtils.TIME_FORMAT.format(myCalendar.getTime()));
                    ((EditText) v).setError(null);
                }
            }
        }, is24Time ? myCalendar.get(Calendar.HOUR_OF_DAY) : myCalendar.get(Calendar.HOUR), myCalendar.get(Calendar.MINUTE), is24Time);
        dialog.show();
    }

    public void onSaveButtonClick() {
        if (isValidUi()) {
            updateDataFromUi();
            if (isEditing) {
                updateItem();
            } else {
                addItem();
            }
        }
    }

    protected abstract void addItem();

    protected abstract void updateItem();

    protected abstract boolean isValidUi();

    protected abstract void updateDataFromUi();

}
