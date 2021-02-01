package th.co.ktb.next.libs.testutils;

import androidx.annotation.NonNull;
import org.threeten.bp.Clock;
import org.threeten.bp.Instant;
import org.threeten.bp.ZoneId;
import org.threeten.bp.temporal.TemporalAmount;

import java.util.Objects;

import static androidx.core.util.ObjectsCompat.hash;
import static java.lang.String.format;

/**
 * A <strong>mutable</strong> clock providing access to the current instant, date and time using a
 * time-zone.
 *
 * Starting from Java 8 developers are suggested to treat time as a dependency, and thus developers
 * are encouraged to inject a {@link java.time.Clock} instance into things that require access to
 * time.
 *
 * This makes the testing of time much more manageable. However, one caveat is that Clock instances
 * are immutable by default, and as such it is incredibly difficult to simulate the passing of time.
 * To remedy this, this class overrides a {@code Clock} to make it easily mutable.
 *
 * @see "https://github.com/robfletcher/test-clock"
 */
public class MutableClock extends Clock {

  private Instant instant;
  private ZoneId zone;

  public MutableClock() {
    this(Instant.now(), ZoneId.systemDefault());
  }

  public MutableClock(@NonNull Instant instant) {
    this(instant, ZoneId.systemDefault());
  }

  public MutableClock(@NonNull ZoneId zone) {
    this(Instant.now(), zone);
  }

  public MutableClock(@NonNull Instant instant, @NonNull ZoneId zone) {
    this.instant = instant;
    this.zone = zone;
  }

  @Override
  @NonNull
  public Clock withZone(@NonNull ZoneId zone) {
    return new MutableClock(instant, zone);
  }

  @Override
  @NonNull
  public ZoneId getZone() {
    return zone;
  }

  @Override
  @NonNull
  public Instant instant() {
    return instant;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || !(o instanceof Clock)) return false;
    Clock that = (Clock) o;
    return Objects.equals(instant, that.instant()) &&
        Objects.equals(zone, that.getZone());
  }

  @Override
  public int hashCode() {
    return hash(super.hashCode(), instant, zone);
  }

  @Override
  public String toString() {
    return format("MutableClock[%s,%s]", instant, zone);
  }

  public void advanceBy(@NonNull TemporalAmount amount) {
    instant = instant.plus(amount);
  }

  public void instant(@NonNull Instant newInstant) {
    instant = newInstant;
  }

  public Clock toFixed() {
    return Clock.fixed(instant, zone);
  }
}
