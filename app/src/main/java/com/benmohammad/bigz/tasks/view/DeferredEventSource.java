package com.benmohammad.bigz.tasks.view;

import com.spotify.mobius.EventSource;
import com.spotify.mobius.disposables.Disposable;
import com.spotify.mobius.functions.Consumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Nonnull;

public class DeferredEventSource<E> implements EventSource<E> {

    private BlockingQueue<E> events = new LinkedBlockingDeque<>();

    @Nonnull
    @Override
    public Disposable subscribe(Consumer<E> eventConsumer) {
        AtomicBoolean run = new AtomicBoolean(true);
        Thread t = new Thread(
                () -> {
                    while (run.get()) {
                        try {
                            E e = events.take();
                            if(run.get()) {
                                eventConsumer.accept(e);
                            }
                        }catch (InterruptedException e) {
                        }
                    }
                });
        t.start();
        return () -> {
            run.set(false);
            t.interrupt();
            };
        }

        public synchronized void notifyEvent(E e) {
            events.offer(e);
        }

    }






