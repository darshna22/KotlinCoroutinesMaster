package th.co.ktb.next.libs.lifecycle

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A lifecycle-aware observable that sends only new updates after subscription, used for events like
 * navigation and Snackbar messages.
 *
 * This avoids a common problem with events: on configuration change (like rotation) an update can
 * be emitted if the observer is active. This LiveData only calls the observable if there's an
 * explicit call to setValue() or call().
 *
 * Note that only one observer is going to be notified of changes.
 */
open class LiveEvent<T> : LiveData<T>() {
  protected val pending = AtomicBoolean()

  override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
    if (this.hasActiveObservers()) {
      throw RuntimeException("Multiple observers registered but only one will be notified of changes")
    }
    super.observe(owner, Observer {
      if (pending.compareAndSet(true, false)) observer.onChanged(it)
    })
  }
}
