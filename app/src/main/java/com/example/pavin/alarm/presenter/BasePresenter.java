package com.example.pavin.alarm.presenter;

public class BasePresenter<V> {

    private V view;

    public void bindView(V view){
        this.view = view;
    }

    public void unbindView(){
        this.view = null;
    }

    public V getView() {
        return view;
    }

    public boolean isViewReady(){
        return view != null;
    }

}
