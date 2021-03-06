package com.example.pavin.alarm.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.pavin.alarm.R;
import com.example.pavin.alarm.model.Sound;

import java.util.ArrayList;

public class DialogSound extends DialogFragment implements DialogInterface.OnClickListener {

    private OnSoundChooseListener listener;
    private ListView listView;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View v = View.inflate(getActivity(), R.layout.dialog_sound, null);
        listView = v.findViewById(R.id.listSound);
        ArrayList<Sound> soundList = listener.getAdapterData();
        ArrayAdapter<Sound> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_single_choice,android.R.id.text1, soundList);
        listView.setAdapter(adapter);
        listView.setItemChecked(soundList.indexOf(listener.getSelectedAudio()), true);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(v)
                .setNegativeButton(getString(R.string.cancel), this)
                .setPositiveButton(getString(R.string.ok), this);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnSoundChooseListener){
            listener = (OnSoundChooseListener)context;
        }
        else throw new ClassCastException(context.toString() + " should implement OnSoundChooseListener");
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i){
            case Dialog.BUTTON_POSITIVE:
                listener.onSoundSelected(listView.getCheckedItemPosition());
                break;
            case Dialog.BUTTON_NEGATIVE:
                dismiss();
                break;
        }
    }

    public interface OnSoundChooseListener{
        void onSoundSelected(int position);
        ArrayList<Sound> getAdapterData();
        Sound getSelectedAudio();
    }

}
