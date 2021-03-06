package com.li.pro.present.home;

import com.li.pro.bean.home.BeanHomeBase;
import com.li.pro.bean.home.BeanHomeResults;
import com.li.pro.model.home.impl.FragmentCAllImpl;
import com.li.pro.view.ifragment.home.IFragmentCAllView;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Mingwei Li on 2016/11/11 0011.
 */

public class FragmentCAllPrecent {
    private FragmentCAllImpl fragmentCAllimpl;
    private IFragmentCAllView iFragmentCAllView;
    private static FragmentCAllPrecent fragmentCAllPrecent;

    private FragmentCAllPrecent() {
    }

    public synchronized static FragmentCAllPrecent getInstance() {
        if (null == fragmentCAllPrecent) {
            fragmentCAllPrecent = new FragmentCAllPrecent();
        }
        return fragmentCAllPrecent;
    }

    public FragmentCAllPrecent with(IFragmentCAllView iFragmentCAllView) {
        fragmentCAllimpl = new FragmentCAllImpl();
        this.iFragmentCAllView = iFragmentCAllView;
        return this;
    }

    /**
     * 获得All分类内容
     *
     * @param count
     * @param page
     */
    public void getFragmentCAllData(int count, int page) {
        fragmentCAllimpl.
                getFragmentCAll(count, page).
                unsubscribeOn(Schedulers.io()).
                flatMap(new Func1<BeanHomeBase, Observable<BeanHomeResults>>() {
                    @Override
                    public Observable<BeanHomeResults> call(BeanHomeBase beanHomeBase) {
                        return Observable.from(beanHomeBase.getResults());
                    }
                }).
                map(new Func1<BeanHomeResults, BeanHomeResults>() {
                    @Override
                    public BeanHomeResults call(BeanHomeResults beanHomeResults) {
                        return beanHomeResults;
                    }
                }).
                subscribeOn(Schedulers.newThread()).
                observeOn(AndroidSchedulers.mainThread()).
                subscribe(new Subscriber<BeanHomeResults>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        iFragmentCAllView.getFragmentCAllStart();
                    }

                    @Override
                    public void onCompleted() {
                        iFragmentCAllView.getFragmentCAllComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        iFragmentCAllView.getFragmentCAllError();
                    }

                    @Override
                    public void onNext(BeanHomeResults beanHomeResults) {
                        iFragmentCAllView.getFragmentCAll(beanHomeResults);
                    }
                })
        ;
    }

}
