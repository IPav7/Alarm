package com.example.pavin.alarm.presenter;

import com.example.pavin.alarm.view.AlarmView;

public class AlarmPresenter extends BasePresenter<AlarmView> {


    public void onSoundChoose(int position) {
      //  getView().setSoundName(position);
    }

    public void onClickChoose(String lol) {
        getView().showSoundDialog();
    }
}
