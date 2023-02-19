/*
 * ThinkingRock, a project management tool for Personal Computers.
 * Copyright (C) 2006 Avente Pty Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package au.com.trgtd.tr.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;

public class ObservableImplTest {

    private Impl sut;
    private List<String> notifications;

    private final AtomicLong counter = new AtomicLong();
    private final List logs = Collections.synchronizedList(new ArrayList<String>());

    private final Observer fakeObserver1 = fakeObserver(1);
    private final Observer fakeObserver2 = fakeObserver(2);

    @Before
    public void setUp() {
        sut = new Impl();
        notifications = new ArrayList<>();
        counter.set(0);
        logs.clear();
    }

    @Test
    public void callingRemoveObserversOnObservableWithoutObservers() {
        sut.removeObservers();
        assertEquals(0, notifications.size());
    }

    @Test
    public void callingRemoveObserverOnObservableWithoutObservers() {
        sut.removeObserver(fakeObserver1);
        assertEquals(0, notifications.size());
    }

    @Test
    public void callingRemoveObserverWithNullObserver() {
        sut.removeObserver(null);
        assertEquals(0, notifications.size());
    }

    @Test
    public void callingNotifyObserverWithNullObserver() {
        sut.notifyObservers(null);
        assertEquals(0, notifications.size());
    }

    @Test
    public void addingObservableDoesNotNotify() {
        sut.addObserver(fakeObserver1);
        assertEquals(0, notifications.size());
    }

    @Test
    public void givenImplWithObserver_whenNotifiying_notifies() {
        sut.addObserver(fakeObserver1);

        sut.notifyObservers(fakeObservable);

        assertEquals(1, notifications.size());
        assertEquals("fo1 - null", notifications.get(0));
    }

    @Test
    public void givenImplWithObserver_whenNotifiying_notifies2() {
        sut.addObserver(fakeObserver1);

        sut.notifyObservers(fakeObservable, "bar");

        assertEquals(1, notifications.size());
        assertEquals("fo1 - bar", notifications.get(0));
    }

    @Test
    public void givenImplWithTwoObservers_whenNotifyingObservers_notifiesBoth() {
        sut.addObserver(fakeObserver1);
        sut.addObserver(fakeObserver2);

        sut.notifyObservers(fakeObservable);

        assertEquals(2, notifications.size());
    }

    @Test
    public void canConcurrentlyModifyObserversWhileNotifyingAndStillNotifiesAll() throws Exception {
        sut.addObserver(fakeSleepingObserver(1, 20));
        sut.addObserver(fakeSleepingObserver(2, 20));
        sut.addObserver(fakeSleepingObserver(3, 20));

        concurrentlyNotifyAndRemove(sut, 30);

        assertEquals(3, notifications.size());

        logs.forEach(System.out::println);
    }

    private void concurrentlyNotifyAndRemove(Impl sut, int millisBeforeRemove) throws InterruptedException {
        ExecutorService exec = Executors.newFixedThreadPool(5);
        List<Callable<Integer>> tasks = new ArrayList<>();
        tasks.add(() -> {
            var ctr = counter.getAndIncrement();
            logs.add(
                    "[" + Thread.currentThread().getName() + "-"
                    + ctr + "] start removing observers..."
            );
            sleepOrFail(millisBeforeRemove, "observer removal", ctr);

            logs.add(
                    "[" + Thread.currentThread().getName() + "-"
                    + ctr + "] - sut.removeObservers() entering..."
            );
            sut.removeObservers();
            logs.add(
                    "[" + Thread.currentThread().getName() + "-"
                    + ctr + "] - sut.removeObservers() done."
            );
            logs.add(
                    "[" + Thread.currentThread().getName() + "-"
                    + ctr + "] observers removed."
            );
            return 0;
        });
        tasks.add(() -> {
            sut.notifyObservers(fakeObservable);
            return 0;
        });
        exec.invokeAll(tasks);

        exec.shutdown();

        if (!exec.awaitTermination(20, TimeUnit.SECONDS)) {
            exec.shutdownNow();
        }
    }

    private class Impl extends ObservableImpl {
    }

    private Observer fakeObserver(int index) {
        return (Observable obs, Object argument) -> {
            notifications.add(fakeNotification(index, argument));
        };
    }

    private Observer fakeSleepingObserver(int index, int millis) {
        return (Observable obs, Object argument) -> {
            var ctr = counter.getAndIncrement();
            logs.add(
                    "[" + Thread.currentThread().getName()
                    + "-" + ctr + "] start updating observer "
                    + index + "..."
            );
            sleepOrFail(millis, "observer " + index + " update", ctr);
            notifications.add(fakeNotification(index, argument));
            logs.add(
                    "[" + Thread.currentThread().getName()
                    + "-" + ctr + "] observer "
                    + index + " updated (slept for " + millis + " ms)."
            );
        };
    }

    private void sleepOrFail(int millis, String label, long ctr) {
        try {
            logs.add(
                    "[" + Thread.currentThread().getName() + "-"
                    + ctr + "] - " + label + ": sleeping for "
                    + millis + " ms."
            );
            Thread.sleep(millis);
            logs.add(
                    "[" + Thread.currentThread().getName() + "-"
                    + ctr + "] - " + label + ": woken up after "
                    + millis + " ms."
            );
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            fail("Unable to sleep");
        }
    }

    private String fakeNotification(int index, Object argument) {
        return "fo" + index + " - " + (argument != null ? argument.toString() : "null");
    }

    private final Observable fakeObservable = new Observable() {
        @Override
        public void addObserver(Observer observer) {
        }

        @Override
        public void removeObserver(Observer observer) {
        }

        @Override
        public void removeObservers() {
        }

        @Override
        public void resetObservers() {
        }
    };
}
