// (c)2016 Flipboard Inc, All Rights Reserved.

package com.example.kotlindemo.token_4;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.kotlindemo.BaseFragment;
import com.example.kotlindemo.R;
import com.example.kotlindemo.model.FakeThing;
import com.example.kotlindemo.model.FakeToken;
import com.example.kotlindemo.network.Network;
import com.example.kotlindemo.network.api.FakeApi;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class TokenFragment extends BaseFragment {

    @BindView(R.id.tokenTv)
    TextView tokenTv;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @OnClick(R.id.requestBt)
    void upload() {
        swipeRefreshLayout.setRefreshing(true);
        unsubscribe();
        final FakeApi fakeApi = Network.getFakeApi();
        disposable = fakeApi.getFakeToken("fake_auth_code")
                .flatMap(new Function<FakeToken, Observable<FakeThing>>() {
                    @Override
                    public Observable<FakeThing> apply(FakeToken fakeToken) {
                        return fakeApi.getFakeData(fakeToken);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FakeThing>() {
                    @Override
                    public void accept(FakeThing fakeData) {
                        swipeRefreshLayout.setRefreshing(false);
                        tokenTv.setText(getString(R.string.got_data, fakeData.id, fakeData.name));
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), R.string.loading_failed, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_token, container, false);
        ButterKnife.bind(this, view);
        swipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        swipeRefreshLayout.setEnabled(false);
        return view;
    }

    @Override
    protected int getDialogRes() {
        return R.layout.dialog_token;
    }

    @Override
    protected int getTitleRes() {
        return R.string.title_token;
    }
}
