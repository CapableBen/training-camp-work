package org.geektimes.reactive.streams;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class DefaultSubscriber<T> implements Subscriber<T> {

    private Subscription subscription;

    private int count = 0;

    @Override
    public void onSubscribe(Subscription s) {
        this.subscription = s;
    }

    @Override
    public void onNext(Object o) {
        if (count == 3) { // 当到达数据阈值时，取消 Publisher 给当前 Subscriber 发送数据
            subscription.cancel();
            // 当第三次数据发布，订阅者满足取消阈值时，
            // 由于在（判断当前 subscriber 是否 cancel 数据发送）在发布者中进行
            // 所以
            //      方案1： 在订阅者中添加提示
            //      方案2： 在发布者中进行判断
            System.err.println("本次数据发布已忽略，数据为：" + o);
            return;
        }
        count++;
        System.out.println("收到数据：" + o);
    }

    @Override
    public void onError(Throwable t) {
        System.out.println("遇到异常：" + t);
    }

    @Override
    public void onComplete() {
        System.out.println("收到数据完成");
    }
}
