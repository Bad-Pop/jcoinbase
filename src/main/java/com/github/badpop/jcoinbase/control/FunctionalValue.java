/*
 * Original work Copyright 2020 Vavr, http://vavr.io
 * Original licence :
 * Copyright 2020 Vavr, http://vavr.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * Modified work in 2021 by Alexis "Bad_Pop" Vachard
 * FunctionalValue is a really simplified version of the vavr Value adapted for JCoinbase's needs.
 * For more information, please take a look at the https://www.vavr.io/
 */
package com.github.badpop.jcoinbase.control;

import io.vavr.CheckedFunction0;
import io.vavr.Lazy;
import io.vavr.collection.Iterator;
import io.vavr.collection.Stream;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;

import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Functional programming is all about values and transformation of values using functions. The
 * {@code FunctionalValue} type reflects the values in a functional setting. It can be seen as the result of a
 * partial function application. Hence the result may be undefined. If a value is undefined, we say
 * it is empty.
 *
 * <p>How the empty state is interpreted depends on the context, i.e. it may be <em>undefined</em>,
 * <em>failed</em>, <em>no elements</em>, etc.
 *
 * <p>Basic operations:
 *
 * <ul>
 *   <li>{@link #get()}
 *   <li>{@link #getOrElse(Object)}
 *   <li>{@link #getOrElse(Supplier)}
 *   <li>{@link #getOrElseThrow(Supplier)}
 *   <li>{@link #getOrElseTry(CheckedFunction0)}
 *   <li>{@link #getOrNull()}
 *   <li>{@link #map(Function)}
 * </ul>
 *
 * Iterable extensions:
 *
 * <ul>
 *   <li>{@link #contains(Object)}
 *   <li>{@link #exists(Predicate)}
 *   <li>{@link #forAll(Predicate)}
 *   <li>{@link #forEach(Consumer)}
 *   <li>{@link #iterator()}
 * </ul>
 *
 * Side-effects:
 *
 * <ul>
 *   <li>{@link #peek(Consumer)}
 * </ul>
 *
 * Tests:
 *
 * <ul>
 *   <li>{@link #isEmpty()}
 * </ul>
 *
 * @param <T> The type of the wrapped value.
 */
public interface FunctionalValue<T> extends Iterable<T> {

  /**
   * Shortcut for {@code exists(e -> Objects.equals(e, element))}, tests if the given {@code
   * element} is contained.
   *
   * @param element An Object of type A, may be null.
   * @return true, if element is contained, false otherwise.
   */
  default boolean contains(T element) {
    return exists(e -> Objects.equals(e, element));
  }

  /**
   * Checks, if an element exists such that the predicate holds.
   *
   * @param predicate A Predicate
   * @return true, if predicate holds for one or more elements, false otherwise
   * @throws NullPointerException if {@code predicate} is null
   */
  default boolean exists(Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate, "predicate is null");
    for (T t : this) {
      if (predicate.test(t)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if this {@code Value} is asynchronously (short: async) computed.
   * <p>
   * Methods of a {@code Value} instance that operate on the underlying value may block the current thread
   * until the value is present and the computation can be performed.
   *
   * @return true if this {@code Value} is async (like {@link io.vavr.concurrent.Future}), false otherwise.
   */
  boolean isAsync();


  /**
   * Checks if this {@code Value} is lazily evaluated.
   *
   * @return true if this {@code Value} is lazy (like {@link Lazy} and {@link Stream}), false otherwise.
   */
  boolean isLazy();

  /**
   * States whether this is a single-valued type.
   *
   * @return {@code true} if this is single-valued, {@code false} otherwise.
   */
  boolean isSingleValued();

  /**
   * Checks, if the given predicate holds for all elements.
   *
   * @param predicate A Predicate
   * @return true, if the predicate holds for all elements, false otherwise
   * @throws NullPointerException if {@code predicate} is null
   */
  default boolean forAll(Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate, "predicate is null");
    return !exists(predicate.negate());
  }

  /**
   * Gets the underlying functionalValue or throws if no functionalValue is present.
   *
   * <p><strong>IMPORTANT! This method will throw an undeclared {@link Throwable} if {@code
   * isEmpty() == true} is true.</strong>
   *
   * <p>Because the 'empty' state indicates that there is no functionalValue present that can be
   * returned, {@code get()} has to throw in such a case. Generally, implementing classes should
   * throw a {@link java.util.NoSuchElementException} if {@code isEmpty()} returns true.
   *
   * <p>However, there exist use-cases, where implementations may throw other exceptions. See {@link
   * Try#get()}.
   *
   * <p><strong>Additional note:</strong> Dynamic proxies will wrap an undeclared exception in a
   * {@link java.lang.reflect.UndeclaredThrowableException}.
   *
   * @return the underlying functionalValue if this is not empty, otherwise {@code get()} throws a
   *     {@code Throwable}
   */
  T get();

  /**
   * Returns the underlying functionalValue if present, otherwise {@code other}.
   *
   * @param other An alternative functionalValue.
   * @return A functionalValue of type {@code T}
   */
  default T getOrElse(T other) {
    return isEmpty() ? other : get();
  }

  /**
   * Returns the underlying functionalValue if present, otherwise {@code other}.
   *
   * @param supplier An alternative functionalValue supplier.
   * @return A functionalValue of type {@code T}
   * @throws NullPointerException if supplier is null
   */
  default T getOrElse(Supplier<? extends T> supplier) {
    Objects.requireNonNull(supplier, "supplier is null");
    return isEmpty() ? supplier.get() : get();
  }

  /**
   * Returns the underlying functionalValue if present, otherwise throws {@code supplier.get()}.
   *
   * @param <X> a Throwable type
   * @param supplier An exception supplier.
   * @return A functionalValue of type {@code T}.
   * @throws NullPointerException if supplier is null
   * @throws X if no functionalValue is present
   */
  default <X extends Throwable> T getOrElseThrow(Supplier<X> supplier) throws X {
    Objects.requireNonNull(supplier, "supplier is null");
    if (isEmpty()) {
      throw supplier.get();
    } else {
      return get();
    }
  }

  /**
   * Returns the underlying functionalValue if present, otherwise returns the result of {@code
   * Try.of(supplier).get()}.
   *
   * @param supplier An alternative functionalValue supplier.
   * @return A functionalValue of type {@code T}.
   * @throws NullPointerException if supplier is null
   */
  default T getOrElseTry(CheckedFunction0<? extends T> supplier) {
    Objects.requireNonNull(supplier, "supplier is null");
    return isEmpty() ? Try.of(supplier).get() : get();
  }

  /**
   * Returns the underlying functionalValue if present, otherwise {@code null}.
   *
   * @return A functionalValue of type {@code T} or {@code null}.
   */
  default T getOrNull() {
    return isEmpty() ? null : get();
  }

  /**
   * Checks, this {@code Value} is empty, i.e. if the underlying functionalValue is absent.
   *
   * @return false, if no underlying functionalValue is present, true otherwise.
   */
  boolean isEmpty();

  /**
   * Maps the underlying functionalValue to a different component type.
   *
   * @param mapper A mapper
   * @param <U> The new component type
   * @return A new functionalValue
   */
  <U> FunctionalValue<U> map(Function<? super T, ? extends U> mapper);

  /**
   * Performs the given {@code action} on the first element if this is an <em>eager</em>
   * implementation. Performs the given {@code action} on all elements (the first immediately,
   * successive deferred), if this is a <em>lazy</em> implementation.
   *
   * @param action The action that will be performed on the element(s).
   * @return this instance
   */
  FunctionalValue<T> peek(Consumer<? super T> action);

  /**
   * Returns a rich {@code io.vavr.collection.Iterator}.
   *
   * @return A new Iterator
   */
  @Override
  Iterator<T> iterator();

  /**
   * Converts this to an {@link java.util.Optional}.
   *
   * <pre>{@code
   * // = Optional.empty
   * Future.of(() -> { throw new Error(); })
   *       .toJavaOptional()
   *
   * // = Optional[ok]
   * Try.of(() -> "ok")
   *     .toJavaOptional()
   *
   * // = Optional[1]
   * List.of(1, 2, 3)
   *     .toJavaOptional()
   * }</pre>
   *
   * @return A new {@link java.util.Optional}.
   */
  default Optional<T> toJavaOptional() {
    return isEmpty() ? Optional.empty() : Optional.ofNullable(get());
  }

  /**
   * Converts this to a {@link CallResult}.
   *
   * @param leftSupplier A {@link Supplier} for the failure value for the {@link CallResult}
   * @param <L> Validation error component type
   * @return A new {@link CallResult}.
   */
  default <L> CallResult<L, T> toCallResult(Supplier<? extends L> leftSupplier) {
    Objects.requireNonNull(leftSupplier, "leftSupplier is null");
    if (this instanceof Either) {
      return ((CallResult<?, T>) this).mapLeft(ignored -> leftSupplier.get());
    } else {
      return isEmpty() ? CallResult.failure(leftSupplier.get()) : CallResult.success(get());
    }
  }

  /**
   * Converts this to a {@link CallResult}.
   *
   * @param left A failure value for the {@link CallResult}
   * @param <L> CallResult failure component type
   * @return A new {@link CallResult}.
   */
  default <L> CallResult<L, T> toCallResult(L left) {
    if (this instanceof Either) {
      return ((CallResult<?, T>) this).mapLeft(ignored -> left);
    } else {
      return isEmpty() ? CallResult.failure(left) : CallResult.success(get());
    }
  }

  /**
   * Converts this to an {@link Option}.
   *
   * @return A new {@link Option}.
   */
  @SuppressWarnings("unchecked")
  default Option<T> toOption() {
    if (this instanceof Option) {
      return (Option<T>) this;
    } else {
      return isEmpty() ? Option.none() : Option.some(get());
    }
  }

  @Override
  default Spliterator<T> spliterator() {
    return Spliterators.spliterator(
        iterator(),
        isEmpty() ? 0 : 1,
        Spliterator.IMMUTABLE | Spliterator.ORDERED | Spliterator.SIZED | Spliterator.SUBSIZED);
  }

  /**
   * Clarifies that functionalValues have a proper equals() method implemented.
   *
   * <p>See <a
   * href="https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#equals-java.lang.Object-">Object.equals(Object)</a>.
   *
   * @param o An object
   * @return true, if this equals o, false otherwise
   */
  @Override
  boolean equals(Object o);

  /**
   * Clarifies that functionalValues have a proper hashCode() method implemented.
   *
   * <p>See <a
   * href="https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#hashCode--">Object.hashCode()</a>.
   *
   * @return The hashcode of this object
   */
  @Override
  int hashCode();

  /**
   * Clarifies that functionalValues have a proper toString() method implemented.
   *
   * <p>See <a
   * href="https://docs.oracle.com/javase/8/docs/api/java/lang/Object.html#toString--">Object.toString()</a>.
   *
   * @return A String representation of this object
   */
  @Override
  String toString();
}
