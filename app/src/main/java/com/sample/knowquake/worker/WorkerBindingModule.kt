package com.sample.knowquake.worker

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface WorkerBindingModule {

    @Binds
    @IntoMap
    @WorkerKey(NewQuakeUpdateJob::class)
    abstract fun newQuakeUpdateWorker(factory: NewQuakeUpdateJob.Factory):
            DaggerWorkerFactory.ChildWorkerFactory
}