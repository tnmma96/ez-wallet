package io.tnmma.ezwallet.base.event;

import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Used as a wrapper for data that is exposed via a LiveData that represents an event.
 *
 * @see <a
 *     href="https://stackoverflow.com/questions/56071990/android-architecture-singleliveevent-and-eventobserver-practicle-example-in-java">
 *     Android Architecture SingleLiveEvent and EventObserver Practicle Example in Java - Stack
 *     Overflow</a>
 *     <p><a href="https://commonsware.com/Jetpack/pages/chap-arch-005.html">States and Events</a>
 */
public class EventWrapper<T> {

    private final AtomicBoolean hasBeenHandled = new AtomicBoolean(false);

    private final T content;

    public EventWrapper(T content) {
        this.content = content;
    }

    public AtomicBoolean hasBeenHandled() {
        return hasBeenHandled;
    }

    /** Returns the content and prevents its use again. */
    public T getContentIfNotHandled() {
        if (hasBeenHandled.get()) {
            return null;
        } else {
            hasBeenHandled.set(true);
            return content;
        }
    }

    /** Returns the content, even if it's already been handled. */
    public T peekContent() {
        return content;
    }

    private void handle(EventWrapper.Handler<T> handler) {
        if (!hasBeenHandled.get()) {
            hasBeenHandled.set(true);
            handler.handle(content);
        }
    }

    public interface Handler<T> {

        void handle(T content);
    }

    /**
     * An {@link androidx.lifecycle.Observer Observer} for {@link EventWrapper Event}, simplifying
     * the pattern of checking if the {@link EventWrapper Event}'s content has already been handled.
     *
     * <p>{@link Handler#handle(Object) handle(T content)} is *only* called if the {@link
     * EventWrapper Event}'s contents has not been handled.
     */
    public static class EventObserver<T> implements Observer<EventWrapper<T>> {

        private final EventWrapper.Handler<T> handler;

        public EventObserver(Handler<T> handler) {
            this.handler = handler;
        }

        @Override
        public void onChanged(EventWrapper<T> eventWrapper) {
            if (eventWrapper != null) {
                eventWrapper.handle(handler);
            }
        }
    }
}
