package info.gratour.common.utils;

public class ManualResetEvent {

  private final Object monitor = new Object();
  private volatile boolean open = false;

  public ManualResetEvent(boolean isEventSet) {
    this.open = isEventSet;
  }

  public void waitOne() throws InterruptedException {
    synchronized (monitor) {
      while (!open) {
          monitor.wait();
      }
    }
  }

  public void waitThenReset() throws InterruptedException {
    waitOne();
    resetEvent();
  }

  public boolean waitOne(long milliseconds) throws InterruptedException {
    synchronized (monitor) {
      if (open)
        return true;
      monitor.wait(milliseconds);
        return open;
    }
  }

  public void setEvent() {//open start
    synchronized (monitor) {
      open = true;
      monitor.notifyAll();
    }
  }

  public void resetEvent() {//close stop
    open = false;
  }
}
