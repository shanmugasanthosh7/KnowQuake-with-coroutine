package com.sample.knowquake.rx

import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.TestScheduler


class TestSchedulerProvider(private val testScheduler: TestScheduler) : SchedulerProvider {

    override fun executorIo(): Scheduler = testScheduler

    override fun io(): Scheduler = testScheduler

    override fun ui(): Scheduler = testScheduler

    override fun newThread(): Scheduler = testScheduler

    override fun computation(): Scheduler = testScheduler
}