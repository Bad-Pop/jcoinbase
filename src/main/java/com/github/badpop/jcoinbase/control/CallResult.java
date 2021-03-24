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
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, callResult express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *
 * Modified work in 2021 by Alexis "Bad_Pop" Vachard
 * CallResult is a simplified version of the vavr Either adapted for JCoinbase's needs.
 * For more information, please take a look at the https://www.vavr.io/
 */
package com.github.badpop.jcoinbase.control;

import io.vavr.collection.Iterator;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static lombok.AccessLevel.PRIVATE;

/**
 * Call result is a simplified version of the vavr {@link Either} adapted to the JCoinbase needs.
 * For more information, please take a look at the <a href="https://www.vavr.io/">vavr site</a>
 *
 * <p>CallResult represents a value of two possible types. A CallResult is callResult a {@link
 * Failure} or a {@link Success}.
 *
 * <p>If the given CallResult is a Success and projected to a Failure, the Failure operations have
 * no effect on the Success value.<br>
 * If the given CallResult is a Failure and projected to a Success, the Success operations have no
 * effect on the Failure value.<br>
 * If a Failure is projected to a Failure or a Success is projected to a Success, the operations
 * have an effect.
 *
 * <p><strong>Example:</strong> A compute() function, which results callResult in an Integer value
 * (in the case of success) or in an error message of type String (in the case of failure). By
 * convention the success case is Success and the failure is Failure.
 *
 * <pre>
 * <code>
 * CallResult&lt;String,Integer&gt; value = compute().success().map(i -&gt; i * 2).toCallResult();
 * </code>
 * </pre>
 *
 * If the result of compute() is Success(1), the value is Success(2).<br>
 * If the result of compute() is Failure("error"), the value is Failure("error").
 *
 * @param <L> The type of the Failure value of an CallResult.
 * @param <R> The type of the Success value of an CallResult.
 */
@ToString
@NoArgsConstructor(access = PRIVATE)
@SuppressWarnings({"java:S1948", "java:S1905", "unchecked"})
public abstract class CallResult<L, R> implements Iterable<R>, FunctionalValue<R>, Serializable {

  @Serial private static final long serialVersionUID = 1L;

  private static final String FAILURE_MAPPER_IS_NULL = "failureMapper is null";
  private static final String FUNCTION_IS_NULL = "function is null";
  private static final String OTHER_IS_NULL = "function is null";
  private static final String ACTION_IS_NULL = "function is null";
  private static final String EXCEPTION_FUNCTION_IS_NULL = "exceptionFunction is null";
  private static final String MAPPER_IS_NULL = "mapper is null";
  private static final String PREDICATE_IS_NULL = "predicate is null";
  private static final String SUPPLIER_IS_NULL = "supplier is null";

  /**
   * Constructs a {@link Success}
   *
   * <pre>{@code
   * // Creates CallResult instance initiated with success value 1
   * CallResult<?, Integer> callResult = CallResult.success(1);
   * }</pre>
   *
   * @param success The value.
   * @param <L> Type of failure value.
   * @param <R> Type of success value.
   * @return A new {@code Success} instance.
   */
  public static <L, R> CallResult<L, R> success(R success) {
    return new Success<>(success);
  }

  /**
   * Constructs a {@link Failure}
   *
   * <pre>{@code
   * // Creates CallResult instance initiated with failure value "error message"
   * CallResult<String, ?> callResult = CallResult.failure("error message");
   * }</pre>
   *
   * @param failure The value.
   * @param <L> Type of failure value.
   * @param <R> Type of success value.
   * @return A new {@code Failure} instance.
   */
  public static <L, R> CallResult<L, R> failure(L failure) {
    return new Failure<>(failure);
  }

  /**
   * Narrows a widened {@code CallResult<? extends L, ? extends R>} to {@code CallResult<L, R>} by
   * performing a type-safe cast. This is eligible because immutable/read-only collections are
   * covariant.
   *
   * <pre>{@code
   * // It's ok, Integer inherits from Number
   * CallResult<?, Number> answer = CallResult.success(42);
   *
   * // RuntimeException is an Exception
   * CallResult<Exception, ?> failed = CallResult.failure(new RuntimeException("poetry recital"));
   * }</pre>
   *
   * @param callResult A {@code CallResult}.
   * @param <L> Type of failure value.
   * @param <R> Type of success value.
   * @return the given {@code callResult} instance as narrowed type {@code CallResult<L, R>}.
   */
  public static <L, R> CallResult<L, R> narrow(CallResult<? extends L, ? extends R> callResult) {
    return (CallResult<L, R>) callResult;
  }

  /**
   * Reduces many {@code CallResult}s into a single {@code CallResult} by transforming an {@code
   * Iterable<CallResult<L, R>>} into a {@code CallResult<Seq<L>, Seq<R>>}.
   *
   * <p>If any of the given {@code CallResult}s is a {@link Failure} then {@code sequence} returns a
   * {@link Failure} containing a non-empty {@link Seq} of all failure values.
   *
   * <p>If none of the given {@code CallResult}s is a {@link Failure} then {@code sequence} returns
   * a {@link Success} containing a (possibly empty) {@link Seq} of all success values.
   *
   * <pre>{@code
   * // = Success(Seq())
   * CallResult.sequence(List.empty())
   *
   * // = Success(Seq(1, 2))
   * CallResult.sequence(List.of(CallResult.success(1), CallResult.success(2)))
   *
   * // = Failure(Seq("x"))
   * CallResult.sequence(List.of(CallResult.success(1), CallResult.failure("x")))
   * }</pre>
   *
   * @param callResults An {@link Iterable} of {@code CallResult}s
   * @param <L> closure of all failure types of the given {@code CallResult}s
   * @param <R> closure of all success types of the given {@code CallResult}s
   * @return An {@code CallResult} of a {@link Seq} of failure or success values
   * @throws NullPointerException if {@code callResults} is null
   */
  public static <L, R> CallResult<Seq<L>, Seq<R>> sequence(
      Iterable<? extends CallResult<? extends L, ? extends R>> callResults) {
    Objects.requireNonNull(callResults, "callResults is null");
    return Iterator.ofAll((Iterable<CallResult<L, R>>) callResults)
        .partition(CallResult::isFailure)
        .apply(
            (failurePartition, successPartition) ->
                failurePartition.hasNext()
                    ? CallResult.failure(failurePartition.map(CallResult::getFailure).toVector())
                    : CallResult.success(successPartition.map(CallResult::get).toVector()));
  }

  /**
   * Returns the failure value.
   *
   * <pre>{@code
   * // prints "error"
   * System.out.println(CallResult.failure("error").getFailure());
   *
   * // throws NoSuchElementException
   * System.out.println(CallResult.success(42).getFailure());
   * }</pre>
   *
   * @return The failure value.
   * @throws NoSuchElementException if this is a {@code Success}.
   */
  public abstract L getFailure();

  /**
   * Returns whether this CallResult is a Failure.
   *
   * <pre>{@code
   * // prints "true"
   * System.out.println(CallResult.failure("error").isFailure());
   *
   * // prints "false"
   * System.out.println(CallResult.success(42).isFailure());
   * }</pre>
   *
   * @return true, if this is a Failure, false otherwise
   */
  public abstract boolean isFailure();

  /**
   * Returns whether this CallResult is a Success.
   *
   * <pre>{@code
   * // prints "true"
   * System.out.println(CallResult.success(42).isSuccess());
   *
   * // prints "false"
   * System.out.println(CallResult.failure("error").isSuccess());
   * }</pre>
   *
   * @return true, if this is a Success, false otherwise
   */
  public abstract boolean isSuccess();

  /**
   * Returns a FailureProjection of this CallResult.
   *
   * @return a new FailureProjection of this
   */
  public final CallResult.FailureProjection<L, R> failure() {
    return new CallResult.FailureProjection<>(this);
  }

  /**
   * Returns a SuccessProjection of this CallResult.
   *
   * @return a new SuccessProjection of this
   */
  public final CallResult.SuccessProjection<L, R> success() {
    return new CallResult.SuccessProjection<>(this);
  }

  /**
   * Maps callResult the failure or the success side of this disjunction.
   *
   * <pre>{@code
   * CallResult<?, AtomicInteger> success = CallResult.success(new AtomicInteger(42));
   *
   * // prints "Success(42)"
   * System.out.println(success.bimap(Function1.identity(), AtomicInteger::get));
   *
   * CallResult<Exception, ?> failure = CallResult.failure(new Exception("error"));
   *
   * // prints "Failure(error)"
   * System.out.println(failure.bimap(Exception::getMessage, Function1.identity()));
   * }</pre>
   *
   * @param failureMapper maps the failure value if this is a Failure
   * @param successMapper maps the success value if this is a Success
   * @param <X> The new failure type of the resulting CallResult
   * @param <Y> The new success type of the resulting CallResult
   * @return A new CallResult instance
   */
  public final <X, Y> CallResult<X, Y> bimap(
      Function<? super L, ? extends X> failureMapper,
      Function<? super R, ? extends Y> successMapper) {
    Objects.requireNonNull(failureMapper, FAILURE_MAPPER_IS_NULL);
    Objects.requireNonNull(successMapper, "successMapper is null");
    if (isSuccess()) {
      return new Success<>(successMapper.apply(get()));
    } else {
      return new Failure<>(failureMapper.apply(getFailure()));
    }
  }

  /**
   * Folds callResult the failure or the success side of this disjunction.
   *
   * <pre>{@code
   * CallResult<Exception, Integer> success = CallResult.success(3);
   *
   * // prints "Users updated: 3"
   * System.out.println(success.fold(Exception::getMessage, count -> "Users updated: " + count));
   *
   * CallResult<Exception, Integer> failure = CallResult.failure(new Exception("Failed to update users"));
   *
   * // prints "Failed to update users"
   * System.out.println(failure.fold(Exception::getMessage, count -> "Users updated: " + count));
   * }</pre>
   *
   * @param failureMapper maps the failure value if this is a Failure
   * @param successMapper maps the success value if this is a Success
   * @param <U> type of the folded value
   * @return A value of type U
   */
  public final <U> U fold(
      Function<? super L, ? extends U> failureMapper,
      Function<? super R, ? extends U> successMapper) {
    Objects.requireNonNull(failureMapper, FAILURE_MAPPER_IS_NULL);
    Objects.requireNonNull(successMapper, "successMapper is null");
    if (isSuccess()) {
      return successMapper.apply(get());
    } else {
      return failureMapper.apply(getFailure());
    }
  }

  /**
   * Transforms this {@code CallResult}.
   *
   * <pre>{@code
   * // prints "Answer is 42"
   * System.out.println(CallResult.success(42).<String> transform(e -> "Answer is " + e.get()));
   * }</pre>
   *
   * @param f A transformation
   * @param <U> Type of transformation result
   * @return An instance of type {@code U}
   * @throws NullPointerException if {@code f} is null
   */
  public final <U> U transform(Function<? super CallResult<L, R>, ? extends U> f) {
    Objects.requireNonNull(f, FUNCTION_IS_NULL);
    return f.apply(this);
  }

  /**
   * Gets the Success value or an alternate value, if the projected CallResult is a Failure.
   *
   * <pre>{@code
   * // prints "42"
   * System.out.println(CallResult.success(42).getOrElseGet(l -> -1));
   *
   * // prints "13"
   * System.out.println(CallResult.failure("error message").getOrElseGet(String::length));
   * }</pre>
   *
   * @param other a function which converts a Failure value to an alternative Success value
   * @return the success value, if the underlying CallResult is a Success or else the alternative
   *     Success value provided by {@code other} by applying the Failure value.
   */
  public final R getOrElseGet(Function<? super L, ? extends R> other) {
    Objects.requireNonNull(other, OTHER_IS_NULL);
    if (isSuccess()) {
      return get();
    } else {
      return other.apply(getFailure());
    }
  }

  /**
   * Runs an action in the case this is a projection on a Failure value.
   *
   * <pre>{@code
   * // prints "no value found"
   * CallResult.failure("no value found").orElseRun(System.out::println);
   * }</pre>
   *
   * @param action an action which consumes a Failure value
   */
  public final void orElseRun(Consumer<? super L> action) {
    Objects.requireNonNull(action, ACTION_IS_NULL);
    if (isFailure()) {
      action.accept(getFailure());
    }
  }

  /**
   * Gets the Success value or throws, if the projected CallResult is a Failure.
   *
   * <pre>{@code
   * Function<String, RuntimeException> exceptionFunction = RuntimeException::new;
   * // prints "42"
   * System.out.println(CallResult.<String, Integer>success(42).getOrElseThrow(exceptionFunction));
   *
   * // throws RuntimeException("no value found")
   * CallResult.failure("no value found").getOrElseThrow(exceptionFunction);
   * }</pre>
   *
   * @param <X> a throwable type
   * @param exceptionFunction a function which creates an exception based on a Failure value
   * @return the success value, if the underlying CallResult is a Success or else throws the
   *     exception provided by {@code exceptionFunction} by applying the Failure value.
   * @throws X if the projected CallResult is a Failure
   */
  public final <X extends Throwable> R getOrElseThrow(Function<? super L, X> exceptionFunction)
      throws X {
    Objects.requireNonNull(exceptionFunction, EXCEPTION_FUNCTION_IS_NULL);
    if (isSuccess()) {
      return get();
    } else {
      throw exceptionFunction.apply(getFailure());
    }
  }

  /**
   * Converts a {@code Failure} to a {@code Success} vice versa by wrapping the value in a new type.
   *
   * <pre>{@code
   * // prints "Success(42)"
   * System.out.println(CallResult.failure(42).swap());
   *
   * // prints "Failure(message)"
   * System.out.println(CallResult.success("message").swap());
   * }</pre>
   *
   * @return a new {@code CallResult}
   */
  public final CallResult<R, L> swap() {
    if (isSuccess()) {
      return new Failure<>(get());
    } else {
      return new Success<>(getFailure());
    }
  }

  /**
   * Calls recoveryFunction if the projected CallResult is a Failure, performs no operation if this
   * is a Success. This is similar to {@code getOrElseGet}, but where the fallback method also
   * returns an CallResult.
   *
   * <pre>{@code
   * CallResult<Integer, String> tryGetString() { return CallResult.failure(1); }
   *
   * CallResult<Integer, String> tryGetStringAnotherWay(Integer lvalue) { return CallResult.success("yo " + lvalue); }
   *
   * = Success("yo 1")
   * tryGetString().recover(this::tryGetStringAnotherWay);
   * }</pre>
   *
   * @param recoveryFunction a function which accepts a Failure value and returns an CallResult
   * @return an {@code CallResult<L, R>} instance
   * @throws NullPointerException if the given {@code recoveryFunction} is null
   */
  public final CallResult<L, R> recoverWith(
      Function<? super L, ? extends CallResult<? extends L, ? extends R>> recoveryFunction) {
    Objects.requireNonNull(recoveryFunction, "recoveryFunction is null");
    if (isFailure()) {
      return (CallResult<L, R>) recoveryFunction.apply(getFailure());
    } else {
      return this;
    }
  }

  /**
   * Calls {@code recoveryFunction} if the projected CallResult is a Failure, or returns {@code
   * this} if Success. The result of {@code recoveryFunction} will be projected as a Success.
   *
   * <pre>{@code
   * CallResult<Integer, String> tryGetString() { return CallResult.failure(1); }
   *
   * String getStringAnotherWay() { return "yo"; }
   *
   * = Success("yo")
   * tryGetString().recover(this::getStringAnotherWay);
   * }</pre>
   *
   * @param recoveryFunction a function which accepts a Failure value and returns a Success value
   * @return an {@code CallResult<L, R>} instance
   * @throws NullPointerException if the given {@code recoveryFunction} is null
   */
  public final CallResult<L, R> recover(Function<? super L, ? extends R> recoveryFunction) {
    Objects.requireNonNull(recoveryFunction, "recoveryFunction is null");
    if (isFailure()) {
      return CallResult.success(recoveryFunction.apply(getFailure()));
    } else {
      return this;
    }
  }

  /**
   * FlatMaps this success-biased CallResult.
   *
   * <pre>{@code
   * // prints "Success(42)"
   * System.out.println(CallResult.success(21).flatMap(v -> CallResult.success(v * 2)));
   *
   * // prints "Failure(error message)"
   * System.out.println(CallResult.failure("error message").flatMap(CallResult::success));
   * }</pre>
   *
   * @param mapper A mapper
   * @param <U> Component type of the mapped success value
   * @return this as {@code CallResult<L, U>} if this is a Failure, otherwise the success mapping
   *     result
   * @throws NullPointerException if {@code mapper} is null
   */
  public final <U> CallResult<L, U> flatMap(
      Function<? super R, ? extends CallResult<L, ? extends U>> mapper) {
    Objects.requireNonNull(mapper, MAPPER_IS_NULL);
    if (isSuccess()) {
      return (CallResult<L, U>) mapper.apply(get());
    } else {
      return (CallResult<L, U>) this;
    }
  }

  /**
   * Maps the value of this CallResult if it is a Success, performs no operation if this is a
   * Failure.
   *
   * <pre>{@code
   * // = Success("A")
   * CallResult.success("a").map(String::toUpperCase);
   *
   * // = Failure(1)
   * CallResult.failure(1).map(String::toUpperCase);
   * }</pre>
   *
   * @param mapper A mapper
   * @param <U> Component type of the mapped success value
   * @return a mapped {@code Monad}
   * @throws NullPointerException if {@code mapper} is null
   */
  @Override
  public final <U> CallResult<L, U> map(Function<? super R, ? extends U> mapper) {
    Objects.requireNonNull(mapper, MAPPER_IS_NULL);
    if (isSuccess()) {
      return CallResult.success(mapper.apply(get()));
    } else {
      return (CallResult<L, U>) this;
    }
  }

  /**
   * Maps the value of this CallResult if it is a Failure, performs no operation if this is a
   * Success.
   *
   * <pre>{@code
   * // = Failure(2)
   * CallResult.failure(1).mapLeft(i -> i + 1);
   *
   * // = Success("a")
   * CallResult.success("a").mapLeft(i -> i + 1);
   * }</pre>
   *
   * @param leftMapper A mapper
   * @param <U> Component type of the mapped right value
   * @return a mapped {@code Monad}
   * @throws NullPointerException if {@code mapper} is null
   */
  public final <U> CallResult<U, R> mapLeft(Function<? super L, ? extends U> leftMapper) {
    Objects.requireNonNull(leftMapper, "leftMapper is null");
    if (isFailure()) {
      return CallResult.failure(leftMapper.apply(getFailure()));
    } else {
      return (CallResult<U, R>) this;
    }
  }

  /**
   * Maps the value of this CallResult if it is a Failure, performs no operation if this is a
   * Success.
   *
   * <pre>{@code
   * // = Failure(2)
   * CallResult.failure(1).mapFailure(i -> i + 1);
   *
   * // = Success("a")
   * CallResult.success("a").mapFailure(i -> i + 1);
   * }</pre>
   *
   * @param failureMapper A mapper
   * @param <U> Component type of the mapped success value
   * @return a mapped {@code Monad}
   * @throws NullPointerException if {@code mapper} is null
   */
  public final <U> CallResult<U, R> mapFailure(Function<? super L, ? extends U> failureMapper) {
    Objects.requireNonNull(failureMapper, FAILURE_MAPPER_IS_NULL);
    if (isFailure()) {
      return CallResult.failure(failureMapper.apply(getFailure()));
    } else {
      return (CallResult<U, R>) this;
    }
  }

  /**
   * Filters this success-biased {@code CallResult} by testing a predicate.
   *
   * <p>
   *
   * @param predicate A predicate
   * @return a new {@code Option} instance
   * @throws NullPointerException if {@code predicate} is null
   */
  public final Option<CallResult<L, R>> filter(Predicate<? super R> predicate) {
    Objects.requireNonNull(predicate, PREDICATE_IS_NULL);
    return isFailure() || predicate.test(get()) ? Option.some(this) : Option.none();
  }

  /**
   * Filters this success-biased {@code CallResult} by testing a predicate.
   *
   * @param predicate A predicate
   * @return a new {@code CallResult}
   * @throws NullPointerException if {@code predicate} is null
   */
  public final Option<CallResult<L, R>> filterNot(Predicate<? super R> predicate) {
    Objects.requireNonNull(predicate, PREDICATE_IS_NULL);
    return filter(predicate.negate());
  }

  /**
   * Filters this success-biased {@code CallResult} by testing a predicate. If the {@code
   * CallResult} is a {@code Success} and the predicate doesn't match, the {@code CallResult} will
   * be turned into a {@code Failure} with contents computed by applying the zero function to the
   * {@code CallResult} value.
   *
   * <pre>{@code
   * // = Failure("bad: a")
   * CallResult.success("a").filterOrElse(i -> false, val -> "bad: " + val);
   *
   * // = Success("a")
   * CallResult.success("a").filterOrElse(i -> true, val -> "bad: " + val);
   * }</pre>
   *
   * @param predicate A predicate
   * @param zero A function that turns a success value into a failure value if the success value
   *     does not make it through the filter.
   * @return an {@code CallResult} instance
   * @throws NullPointerException if {@code predicate} is null
   */
  public final CallResult<L, R> filterOrElse(
      Predicate<? super R> predicate, Function<? super R, ? extends L> zero) {
    Objects.requireNonNull(predicate, PREDICATE_IS_NULL);
    Objects.requireNonNull(zero, "zero is null");
    if (isFailure() || predicate.test(get())) {
      return this;
    } else {
      return CallResult.failure(zero.apply(get()));
    }
  }

  @Override
  public final boolean isEmpty() {
    return isFailure();
  }

  public final CallResult<L, R> orElse(CallResult<? extends L, ? extends R> other) {
    Objects.requireNonNull(other, OTHER_IS_NULL);
    return isSuccess() ? this : (CallResult<L, R>) other;
  }

  public final CallResult<L, R> orElse(
      Supplier<? extends CallResult<? extends L, ? extends R>> supplier) {
    Objects.requireNonNull(supplier, SUPPLIER_IS_NULL);
    return isSuccess() ? this : (CallResult<L, R>) supplier.get();
  }

  @Override
  public final Iterator<R> iterator() {
    if (isSuccess()) {
      return Iterator.of(get());
    } else {
      return Iterator.empty();
    }
  }

  /**
   * Performs the given {@code failureAction} on the failure element if this is Failure. Performs
   * the given {@code successAction} on the success element if this is Success.
   *
   * @param failureAction The action that will be performed on the failure element
   * @param successAction The action that will be performed on the success element
   * @return this instance
   */
  public final CallResult<L, R> peek(
      Consumer<? super L> failureAction, Consumer<? super R> successAction) {
    Objects.requireNonNull(failureAction, "failureAction is null");
    Objects.requireNonNull(successAction, "successAction is null");

    if (isFailure()) {
      failureAction.accept(getFailure());
    } else { // this isSuccess() by definition
      successAction.accept(get());
    }

    return this;
  }

  @Override
  public final CallResult<L, R> peek(Consumer<? super R> action) {
    Objects.requireNonNull(action, ACTION_IS_NULL);
    if (isSuccess()) {
      action.accept(get());
    }
    return this;
  }

  public final CallResult<L, R> peekFailure(Consumer<? super L> action) {
    Objects.requireNonNull(action, ACTION_IS_NULL);
    if (isFailure()) {
      action.accept(getFailure());
    }
    return this;
  }

  /**
   * A failure projection of a CallResult.
   *
   * @param <L> The type of the Failure value of a CallResult.
   * @param <R> The type of the Success value of a CallResult.
   */
  @ToString
  public static final class FailureProjection<L, R> implements FunctionalValue<L> {

    private final CallResult<L, R> callResult;

    private FailureProjection(CallResult<L, R> callResult) {
      this.callResult = callResult;
    }

    @Override
    public boolean isEmpty() {
      return callResult.isSuccess();
    }

    /**
     * Gets the {@code Failure} value or throws.
     *
     * @return the failure value, if the underlying {@code CallResult} is a {@code Failure}
     * @throws NoSuchElementException if the underlying {@code CallResult} of this {@code
     *     FailureProjection} is a {@code Success}
     */
    @Override
    public L get() {
      if (callResult.isFailure()) {
        return callResult.getFailure();
      } else {
        throw new NoSuchElementException("FailureProjection.get() on Success");
      }
    }

    public CallResult.FailureProjection<L, R> orElse(
        CallResult.FailureProjection<? extends L, ? extends R> other) {
      Objects.requireNonNull(other, OTHER_IS_NULL);
      return callResult.isFailure() ? this : (CallResult.FailureProjection<L, R>) other;
    }

    public CallResult.FailureProjection<L, R> orElse(
        Supplier<? extends CallResult.FailureProjection<? extends L, ? extends R>> supplier) {
      Objects.requireNonNull(supplier, SUPPLIER_IS_NULL);
      return callResult.isFailure() ? this : (CallResult.FailureProjection<L, R>) supplier.get();
    }

    /**
     * Gets the Failure value or an alternate value, if the projected CallResult is a Success.
     *
     * @param other an alternative value
     * @return the failure value, if the underlying CallResult is a Failure or else {@code other}
     * @throws NoSuchElementException if the underlying callResult of this FailureProjection is a
     *     Success
     */
    @Override
    public L getOrElse(L other) {
      return callResult.isFailure() ? callResult.getFailure() : other;
    }

    /**
     * Gets the Failure value or an alternate value, if the projected CallResult is a Success.
     *
     * @param other a function which converts a Success value to an alternative Failure value
     * @return the failure value, if the underlying CallResult is a Failure or else the alternative
     *     Failure value provided by {@code other} by applying the Success value.
     */
    public L getOrElseGet(Function<? super R, ? extends L> other) {
      Objects.requireNonNull(other, OTHER_IS_NULL);
      if (callResult.isFailure()) {
        return callResult.getFailure();
      } else {
        return other.apply(callResult.get());
      }
    }

    /**
     * Runs an action in the case this is a projection on a Success value.
     *
     * @param action an action which consumes a Success value
     */
    public void orElseRun(Consumer<? super R> action) {
      Objects.requireNonNull(action, ACTION_IS_NULL);
      if (callResult.isSuccess()) {
        action.accept(callResult.get());
      }
    }

    /**
     * Gets the Failure value or throws, if the projected CallResult is a Success.
     *
     * @param <X> a throwable type
     * @param exceptionFunction a function which creates an exception based on a Success value
     * @return the failure value, if the underlying CallResult is a Failure or else throws the
     *     exception provided by {@code exceptionFunction} by applying the Success value.
     * @throws X if the projected CallResult is a Success
     */
    public <X extends Throwable> L getOrElseThrow(Function<? super R, X> exceptionFunction)
        throws X {
      Objects.requireNonNull(exceptionFunction, EXCEPTION_FUNCTION_IS_NULL);
      if (callResult.isFailure()) {
        return callResult.getFailure();
      } else {
        throw exceptionFunction.apply(callResult.get());
      }
    }

    /**
     * Returns the underlying callResult of this projection.
     *
     * @return the underlying callResult
     */
    public CallResult<L, R> toCallResult() {
      return callResult;
    }

    /**
     * Returns {@code Some} value of type L if this is a failure projection of a Failure value and
     * the predicate applies to the underlying value.
     *
     * @param predicate A predicate
     * @return A new Option
     */
    public Option<CallResult.FailureProjection<L, R>> filter(Predicate<? super L> predicate) {
      Objects.requireNonNull(predicate, PREDICATE_IS_NULL);
      return callResult.isSuccess() || predicate.test(callResult.getFailure())
          ? Option.some(this)
          : Option.none();
    }

    /**
     * FlatMaps this FailureProjection.
     *
     * @param mapper A mapper
     * @param <U> Component type of the mapped failure value
     * @return this as {@code FailureProjection<L, U>} if a Success is underlying, otherwise a the
     *     mapping result of the failure value.
     * @throws NullPointerException if {@code mapper} is null
     */
    public <U> CallResult.FailureProjection<U, R> flatMap(
        Function<? super L, ? extends CallResult.FailureProjection<? extends U, R>> mapper) {
      Objects.requireNonNull(mapper, MAPPER_IS_NULL);
      if (callResult.isFailure()) {
        return (CallResult.FailureProjection<U, R>) mapper.apply(callResult.getFailure());
      } else {
        return (CallResult.FailureProjection<U, R>) this;
      }
    }

    /**
     * Maps the failure value if the projected CallResult is a Failure.
     *
     * @param mapper A mapper which takes a failure value and returns a value of type U
     * @param <U> The new type of a Failure value
     * @return A new FailureProjection
     */
    @Override
    public <U> CallResult.FailureProjection<U, R> map(Function<? super L, ? extends U> mapper) {
      Objects.requireNonNull(mapper, MAPPER_IS_NULL);
      if (callResult.isFailure()) {
        return callResult.mapFailure((Function<L, U>) mapper).failure();
      } else {
        return (CallResult.FailureProjection<U, R>) this;
      }
    }

    /**
     * Applies the given action to the value if the projected callResult is a Failure. Otherwise
     * nothing happens.
     *
     * @param action An action which takes a failure value
     * @return this FailureProjection
     */
    @Override
    public CallResult.FailureProjection<L, R> peek(Consumer<? super L> action) {
      Objects.requireNonNull(action, ACTION_IS_NULL);
      if (callResult.isFailure()) {
        action.accept(callResult.getFailure());
      }
      return this;
    }

    /**
     * Transforms this {@code FailureProjection}.
     *
     * @param f A transformation
     * @param <U> Type of transformation result
     * @return An instance of type {@code U}
     * @throws NullPointerException if {@code f} is null
     */
    public <U> U transform(Function<? super CallResult.FailureProjection<L, R>, ? extends U> f) {
      Objects.requireNonNull(f, FUNCTION_IS_NULL);
      return f.apply(this);
    }

    @Override
    public Iterator<L> iterator() {
      if (callResult.isFailure()) {
        return Iterator.of(callResult.getFailure());
      } else {
        return Iterator.empty();
      }
    }

    @Override
    public boolean equals(Object obj) {
      return (obj == this)
          || (obj instanceof CallResult.FailureProjection
              && Objects.equals(callResult, ((CallResult.FailureProjection<?, ?>) obj).callResult));
    }

    @Override
    public int hashCode() {
      return callResult.hashCode();
    }
  }

  /**
   * A success projection of a CallResult.
   *
   * @param <L> The type of the Failure value of a CallResult.
   * @param <R> The type of the Success value of a CallResult.
   */
  @ToString
  public static final class SuccessProjection<L, R> implements FunctionalValue<R> {

    private final CallResult<L, R> callResult;

    private SuccessProjection(CallResult<L, R> callResult) {
      this.callResult = callResult;
    }

    @Override
    public boolean isEmpty() {
      return callResult.isFailure();
    }

    /**
     * Gets the {@code Success} value or throws.
     *
     * @return the success value, if the underlying {@code CallResult} is a {@code Success}
     * @throws NoSuchElementException if the underlying {@code CallResult} of this {@code
     *     SuccessProjection} is a {@code Failure}
     */
    @Override
    public R get() {
      if (callResult.isSuccess()) {
        return callResult.get();
      } else {
        throw new NoSuchElementException("SuccessProjection.get() on Failure");
      }
    }

    public CallResult.SuccessProjection<L, R> orElse(
        CallResult.SuccessProjection<? extends L, ? extends R> other) {
      Objects.requireNonNull(other, OTHER_IS_NULL);
      return callResult.isSuccess() ? this : (CallResult.SuccessProjection<L, R>) other;
    }

    public CallResult.SuccessProjection<L, R> orElse(
        Supplier<? extends CallResult.SuccessProjection<? extends L, ? extends R>> supplier) {
      Objects.requireNonNull(supplier, SUPPLIER_IS_NULL);
      return callResult.isSuccess() ? this : (CallResult.SuccessProjection<L, R>) supplier.get();
    }

    /**
     * Gets the Success value or an alternate value, if the projected CallResult is a Failure.
     *
     * @param other an alternative value
     * @return the success value, if the underlying CallResult is a Success or else {@code other}
     * @throws NoSuchElementException if the underlying callResult of this SuccessProjection is a
     *     Failure
     */
    @Override
    public R getOrElse(R other) {
      return callResult.getOrElse(other);
    }

    /**
     * Gets the Success value or an alternate value, if the projected CallResult is a Failure.
     *
     * <pre>{@code
     * // prints 42
     * System.out.println(CallResult.success(42).getOrElseGet(l -> 2));
     * // prints 0
     * System.out.println(CallResult.failure(42).getOrElseGet(l -> 0));
     * }</pre>
     *
     * @param other a function which converts a Failure value to an alternative Success value
     * @return the success value, if the underlying CallResult is a Success or else the alternative
     *     Success value provided by {@code other} by applying the Failure value.
     */
    public R getOrElseGet(Function<? super L, ? extends R> other) {
      Objects.requireNonNull(other, OTHER_IS_NULL);
      return callResult.getOrElseGet(other);
    }

    /**
     * Runs an action in the case this is a projection on a Failure value.
     *
     * <pre>{@code
     * // nothing is printed
     * CallResult.success(42).orElseRun(System.out::println);
     *
     * // prints "error message"
     * CallResult.failure("error message").orElseRun(System.out::println);
     * }</pre>
     *
     * @param action an action which consumes a Failure value
     */
    public void orElseRun(Consumer<? super L> action) {
      Objects.requireNonNull(action, ACTION_IS_NULL);
      callResult.orElseRun(action);
    }

    /**
     * Gets the Success value or throws, if the projected CallResult is a Failure.
     *
     * <pre>{@code
     * // prints "42"
     * System.out.println(CallResult.<String, Integer> success(42).getOrElseThrow(s -> new RuntimeException(s)));
     *
     * // throws RuntimeException("error message")
     * CallResult.failure("error message").getOrElseThrow(s -> new RuntimeException(s));
     * }</pre>
     *
     * @param <X> a throwable type
     * @param exceptionFunction a function which creates an exception based on a Failure value
     * @return the success value, if the underlying CallResult is a Success or else throws the
     *     exception provided by {@code exceptionFunction} by applying the Failure value.
     * @throws X if the projected CallResult is a Failure
     */
    public <X extends Throwable> R getOrElseThrow(Function<? super L, X> exceptionFunction)
        throws X {
      Objects.requireNonNull(exceptionFunction, EXCEPTION_FUNCTION_IS_NULL);
      return callResult.getOrElseThrow(exceptionFunction);
    }

    /**
     * Returns the underlying callResult of this projection.
     *
     * @return the underlying callResult
     */
    public CallResult<L, R> toCallResult() {
      return callResult;
    }

    /**
     * Returns {@code Some} value of type R if this is a success projection of a Success value and
     * the predicate applies to the underlying value.
     *
     * @param predicate A predicate
     * @return A new Option
     */
    public Option<CallResult.SuccessProjection<L, R>> filter(Predicate<? super R> predicate) {
      Objects.requireNonNull(predicate, PREDICATE_IS_NULL);
      return callResult.isFailure() || predicate.test(callResult.get())
          ? Option.some(this)
          : Option.none();
    }

    /**
     * FlatMaps this SuccessProjection.
     *
     * @param mapper A mapper
     * @param <U> Component type of the mapped success value
     * @return this as {@code SuccessProjection<L, U>} if a Failure is underlying, otherwise a the
     *     mapping result of the success value.
     * @throws NullPointerException if {@code mapper} is null
     */
    public <U> CallResult.SuccessProjection<L, U> flatMap(
        Function<? super R, ? extends CallResult.SuccessProjection<L, ? extends U>> mapper) {
      Objects.requireNonNull(mapper, MAPPER_IS_NULL);
      if (callResult.isSuccess()) {
        return (CallResult.SuccessProjection<L, U>) mapper.apply(callResult.get());
      } else {
        return (CallResult.SuccessProjection<L, U>) this;
      }
    }

    /**
     * Maps the success value if the projected CallResult is a Success.
     *
     * @param mapper A mapper which takes a success value and returns a value of type U
     * @param <U> The new type of a Success value
     * @return A new SuccessProjection
     */
    @Override
    public <U> CallResult.SuccessProjection<L, U> map(Function<? super R, ? extends U> mapper) {
      Objects.requireNonNull(mapper, MAPPER_IS_NULL);
      if (callResult.isSuccess()) {
        return callResult.map((Function<R, U>) mapper).success();
      } else {
        return (CallResult.SuccessProjection<L, U>) this;
      }
    }

    /**
     * Applies the given action to the value if the projected callResult is a Success. Otherwise
     * nothing happens.
     *
     * @param action An action which takes a success value
     * @return this {@code CallResult} instance
     */
    @Override
    public CallResult.SuccessProjection<L, R> peek(Consumer<? super R> action) {
      Objects.requireNonNull(action, ACTION_IS_NULL);
      if (callResult.isSuccess()) {
        action.accept(callResult.get());
      }
      return this;
    }

    /**
     * Transforms this {@code SuccessProjection}.
     *
     * @param f A transformation
     * @param <U> Type of transformation result
     * @return An instance of type {@code U}
     * @throws NullPointerException if {@code f} is null
     */
    public <U> U transform(Function<? super CallResult.SuccessProjection<L, R>, ? extends U> f) {
      Objects.requireNonNull(f, FUNCTION_IS_NULL);
      return f.apply(this);
    }

    @Override
    public Iterator<R> iterator() {
      return callResult.iterator();
    }

    @Override
    public boolean equals(Object obj) {
      return (obj == this)
          || (obj instanceof CallResult.SuccessProjection
              && Objects.equals(callResult, ((CallResult.SuccessProjection<?, ?>) obj).callResult));
    }

    @Override
    public int hashCode() {
      return callResult.hashCode();
    }
  }

  /**
   * The {@code Failure} version of a {@code CallResult}.
   *
   * @param <L> failure component type
   * @param <R> success component type
   */
  @ToString
  @SuppressWarnings("java:S1948")
  public static final class Failure<L, R> extends CallResult<L, R> implements Serializable {

    @Serial private static final long serialVersionUID = 1L;

    private final L value;

    /**
     * Constructs a {@code Failure}.
     *
     * @param value a failure value
     */
    private Failure(L value) {
      this.value = value;
    }

    @Override
    public R get() {
      throw new NoSuchElementException("get() on Failure");
    }

    @Override
    public boolean isSuccess() {
      return false;
    }

    @Override
    public boolean isFailure() {
      return true;
    }

    @Override
    public L getFailure() {
      return value;
    }

    @Override
    public boolean equals(Object obj) {
      return (obj == this)
          || (obj instanceof CallResult.Failure
              && Objects.equals(value, ((Failure<?, ?>) obj).value));
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(value);
    }
  }

  /**
   * The {@code Success} version of a {@code CallResult}.
   *
   * @param <L> failure component type
   * @param <R> success component type
   */
  @ToString
  public static final class Success<L, R> extends CallResult<L, R> implements Serializable {

    @Serial private static final long serialVersionUID = 1L;

    private final R value;

    /**
     * Constructs a {@code Success}.
     *
     * @param value a success value
     */
    private Success(R value) {
      this.value = value;
    }

    @Override
    public R get() {
      return value;
    }

    @Override
    public L getFailure() {
      throw new NoSuchElementException("getFailure() on Success");
    }

    @Override
    public boolean isFailure() {
      return false;
    }

    @Override
    public boolean isSuccess() {
      return true;
    }

    @Override
    public boolean equals(Object obj) {
      return (obj == this)
          || (obj instanceof CallResult.Success
              && Objects.equals(value, ((Success<?, ?>) obj).value));
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(value);
    }
  }
}
