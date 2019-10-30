package com.example.smar;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import org.w3c.dom.Text;

public class CalendarDialogPopup extends AppCompatDialogFragment {
    CalendarView calendarView;
    String c;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.calendar_dialog_popup);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCanceledOnTouchOutside(true);
        calendarView=dialog.findViewById(R.id.smar_calenderview_calender);

        return dialog;
    }
}
