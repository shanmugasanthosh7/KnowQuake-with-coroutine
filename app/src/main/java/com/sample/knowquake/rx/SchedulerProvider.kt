package com.sample.knowquake.rx

import io.reactivex.rxjava3.core.Scheduler


interface SchedulerProvider {

    fun executorIo(): Scheduler

    fun io(): Scheduler

    fun ui(): Scheduler

    fun newThread(): Scheduler

    fun computation(): Scheduler
}