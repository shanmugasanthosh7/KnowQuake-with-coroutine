package com.sample.knowquake.rx

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.Executors

class AppSchedulerProvider : SchedulerProvider {

    companion object {
        private const val THREAD_POOL_LIMIT = 3
    }

    override fun executorIo(): Scheduler =
            Schedulers.from(Executors.newFixedThreadPool(THREAD_POOL_LIMIT))

    override fun io(): Scheduler = Schedulers.io()

    override fun ui(): Scheduler = AndroidSchedulers.mainThread()

    override fun newThread(): Scheduler = Schedulers.newThread()

    override fun computation(): Scheduler = Schedulers.computation()
}