package com.github.badpop.jcoinbase.control;

import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.collection.Vector;
import io.vavr.control.Option;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class CallResultTest extends AbstractFunctionalValueTest {

  @Override
  protected <T> CallResult<?, T> empty() {
    return CallResult.<T, T>failure(null);
  }

  @Override
  protected <T> CallResult<?, T> of(T element) {
    return CallResult.<T, T>success(element);
  }

  @SafeVarargs
  @Override
  protected final <T> CallResult<?, T> of(T... elements) {
    return of(elements[0]);
  }

  @Override
  protected boolean useIsEqualToInsteadOfIsSameAs() {
    return true;
  }

  @Override
  protected int getPeekNonNilPerformingAnAction() {
    return 1;
  }

  @Test
  public void shouldBimapLeft() {
    final CallResult<Integer, String> actual =
        CallResult.<Integer, String>failure(1).bimap(i -> i + 1, s -> s + "1");
    final CallResult<Integer, String> expected = CallResult.failure(2);
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void shouldBimapRight() {
    final CallResult<Integer, String> actual =
        CallResult.<Integer, String>success("1").bimap(i -> i + 1, s -> s + "1");
    final CallResult<Integer, String> expected = CallResult.success("11");
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void shouldFoldLeft() {
    final String value = CallResult.failure("L").fold(l -> l + "+", r -> r + "-");
    assertThat(value).isEqualTo("L+");
  }

  @Test
  public void shouldFoldRight() {
    final String value = CallResult.success("R").fold(l -> l + "-", r -> r + "+");
    assertThat(value).isEqualTo("R+");
  }

  @Test
  public void shouldThrowWhenSequencingNull() {
    assertThatThrownBy(() -> CallResult.sequence(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("callResults is null");
  }

  @Test
  public void shouldSequenceEmptyIterableOfEither() {
    final Iterable<CallResult<Integer, String>> callresults = List.empty();
    final CallResult<Seq<Integer>, Seq<String>> actual = CallResult.sequence(callresults);
    final CallResult<Seq<Integer>, Seq<String>> expected = CallResult.success(Vector.empty());
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void shouldSequenceNonEmptyIterableOfRight() {
    final Iterable<CallResult<Integer, String>> callresults =
        List.of(CallResult.success("a"), CallResult.success("b"), CallResult.success("c"));
    final CallResult<Seq<Integer>, Seq<String>> actual = CallResult.sequence(callresults);
    final CallResult<Seq<Integer>, Seq<String>> expected =
        CallResult.success(Vector.of("a", "b", "c"));
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void shouldSequenceNonEmptyIterableOfLeft() {
    final Iterable<CallResult<Integer, String>> callResults =
        List.of(CallResult.failure(1), CallResult.failure(2), CallResult.failure(3));
    final CallResult<Seq<Integer>, Seq<String>> actual = CallResult.sequence(callResults);
    final CallResult<Seq<Integer>, Seq<String>> expected = CallResult.failure(Vector.of(1, 2, 3));
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void shouldSequenceNonEmptyIterableOfMixedEither() {
    final Iterable<CallResult<Integer, String>> callResults =
        List.of(
            CallResult.success("a"),
            CallResult.failure(1),
            CallResult.success("c"),
            CallResult.failure(3));
    final CallResult<Seq<Integer>, Seq<String>> actual = CallResult.sequence(callResults);
    final CallResult<Seq<Integer>, Seq<String>> expected = CallResult.failure(Vector.of(1, 3));
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  public void shouldThrowExceptionOnNullTransformFunction() {
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> CallResult.success(1).transform(null));
  }

  @Test
  public void shouldApplyTransformFunctionToRight() {
    final CallResult<?, Integer> callResult = CallResult.success(1);
    final Function<CallResult<?, Integer>, String> f =
        e -> e.get().toString().concat("-transformed");
    assertThat(callResult.transform(f)).isEqualTo("1-transformed");
  }

  @Test
  public void shouldHandleTransformOnLeft() {
    assertThat(CallResult.failure(0).<String>transform(self -> self.isEmpty() ? "ok" : "failed"))
        .isEqualTo("ok");
  }

  @Test
  public void shouldReturnSameWhenCallingMapOnLeft() {
    final CallResult<Integer, Object> actual = CallResult.failure(1);
    assertThat(
            actual.map(
                v -> {
                  throw new IllegalStateException();
                }))
        .isSameAs(actual);
  }

  @Test
  public void shouldThrowIfRightGetLeft() {
    assertThatExceptionOfType(NoSuchElementException.class)
        .isThrownBy(() -> CallResult.success(1).getFailure());
  }

  @Test
  public void shouldThrowIfLeftGet() {
    assertThatExceptionOfType(NoSuchElementException.class)
        .isThrownBy(() -> CallResult.failure(1).get());
  }

  @Test
  public void shouldSwapLeft() {
    assertThat(CallResult.failure(1).swap()).isEqualTo(CallResult.success(1));
  }

  @Test
  public void shouldSwapRight() {
    assertThat(CallResult.success(1).swap()).isEqualTo(CallResult.failure(1));
  }

  @Test
  public void shouldRecoverWithLeftToRightEither() {
    assertThat(CallResult.failure(1).recoverWith(lvalue -> CallResult.success(lvalue + 1)))
        .isEqualTo(CallResult.success(2));
  }

  @Test
  public void shouldRecoverWithLeftToLeftEither() {
    assertThat(CallResult.failure(1).recoverWith(lvalue -> CallResult.failure(lvalue + 1)))
        .isEqualTo(CallResult.failure(2));
  }

  @Test
  public void shouldRecoverWithRight() {
    final CallResult<String, String> value =
        CallResult.<String, String>success("R").recoverWith(lvalue -> CallResult.failure("L"));
    assertThat(value).isEqualTo(CallResult.success("R"));
  }

  @Test
  public void shouldRecoverLeft() {
    assertThat(CallResult.failure(1).recover(lvalue -> "R")).isEqualTo(CallResult.success("R"));
  }

  @Test
  public void shouldRecoverRightWithoutInvokingRecovery() {
    // Recover function should not be invoked, so hardcode it to fail
    Function<Object, String> recoveryFunction =
        $ -> {
          throw new RuntimeException("Lazy recovery function should not be invoked for a Right!");
        };

    assertThat(CallResult.success("R").recover(recoveryFunction))
        .isEqualTo(CallResult.success("R"));
  }

  @Test
  public void shouldNarrowRightEither() {
    CallResult<String, Integer> either = CallResult.success(42);
    CallResult<CharSequence, Number> narrow = CallResult.narrow(either);
    assertThat(narrow.get()).isEqualTo(42);
  }

  @Test
  public void shouldNarrowLeftEither() {
    CallResult<String, Integer> either = CallResult.failure("vavr");
    CallResult<CharSequence, Number> narrow = CallResult.narrow(either);
    assertThat(narrow.getFailure()).isEqualTo("vavr");
  }

  @Test
  public void shouldEitherOrElseEither() {
    assertThat(CallResult.success(1).orElse(CallResult.success(2)).get()).isEqualTo(1);
    assertThat(CallResult.failure(1).orElse(CallResult.success(2)).get()).isEqualTo(2);
  }

  @Test
  public void shouldEitherOrElseSupplier() {
    assertThat(CallResult.success(1).orElse(() -> CallResult.success(2)).get()).isEqualTo(1);
    assertThat(CallResult.failure(1).orElse(() -> CallResult.success(2)).get()).isEqualTo(2);
  }

  @Test
  public void shouldReturnTrueWhenCallingIsLeftOnLeft() {
    assertThat(CallResult.failure(1).isFailure()).isTrue();
  }

  @Test
  public void shouldReturnFalseWhenCallingIsRightOnLeft() {
    assertThat(CallResult.failure(1).isSuccess()).isFalse();
  }

  @Test
  public void shouldFilterRight() {
    CallResult<String, Integer> either = CallResult.success(42);
    assertThat(either.filter(i -> true).get()).isSameAs(either);
    assertThat(either.filter(i -> false)).isSameAs(Option.none());
  }

  @Test
  public void shouldFilterLeft() {
    CallResult<String, Integer> either = CallResult.failure("vavr");
    assertThat(either.filter(i -> true).get()).isSameAs(either);
    assertThat(either.filter(i -> false).get()).isSameAs(either);
  }

  @Test
  public void shouldFilterNotRight() {
    CallResult<String, Integer> either = CallResult.success(42);
    assertThat(either.filterNot(i -> false).get()).isSameAs(either);
    assertThat(either.filterNot(i -> true)).isSameAs(Option.none());
  }

  @Test
  public void shouldFilterNotLeft() {
    CallResult<String, Integer> either = CallResult.failure("vavr");
    assertThat(either.filterNot(i -> false).get()).isSameAs(either);
    assertThat(either.filterNot(i -> true).get()).isSameAs(either);
  }

  @Test
  public void shouldThrowWhenNullPredicate() {
    assertThatThrownBy(() -> CallResult.failure(42).filterNot(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessage("predicate is null");
  }

  @Test
  public void shouldFilterOrElseRight() {
    CallResult<String, Integer> either = CallResult.success(42);
    assertThat(either.filterOrElse(i -> true, Object::toString)).isSameAs(either);
    assertThat(either.filterOrElse(i -> false, Object::toString))
        .isEqualTo(CallResult.failure("42"));
  }

  @Test
  public void shouldFilterOrElseLeft() {
    CallResult<String, Integer> either = CallResult.failure("vavr");
    assertThat(either.filterOrElse(i -> true, Object::toString)).isSameAs(either);
    assertThat(either.filterOrElse(i -> false, Object::toString)).isSameAs(either);
  }

  @Test
  public void shouldFlatMapRight() {
    CallResult<String, Integer> either = CallResult.success(42);
    assertThat(either.flatMap(v -> CallResult.success("ok")).get()).isEqualTo("ok");
  }

  @Test
  public void shouldFlatMapLeft() {
    CallResult<String, Integer> either = CallResult.failure("vavr");
    assertThat(either.flatMap(v -> CallResult.success("ok"))).isSameAs(either);
  }

  @Test
  public void shouldPeekLeftNil() {
    assertThat(empty().peekFailure(t -> {})).isEqualTo(empty());
  }

  @Test
  public void shouldPeekLeftForLeft() {
    final int[] effect = {0};
    final CallResult<Integer, ?> actual = CallResult.failure(1).peekFailure(i -> effect[0] = i);
    assertThat(actual).isEqualTo(CallResult.failure(1));
    assertThat(effect[0]).isEqualTo(1);
  }

  @Test
  public void shouldNotPeekLeftForRight() {
    CallResult.success(1)
        .peekFailure(
            i -> {
              throw new IllegalStateException();
            });
  }

  @Test
  public void shouldEqualLeftIfObjectIsSame() {
    final CallResult<Integer, ?> left = CallResult.failure(1);
    assertThat(left.equals(left)).isTrue();
  }

  @Test
  public void shouldNotEqualLeftIfObjectIsNull() {
    assertThat(CallResult.failure(1).equals(null)).isFalse();
  }

  @Test
  public void shouldNotEqualLeftIfObjectIsOfDifferentType() {
    assertThat(CallResult.failure(1).equals(new Object())).isFalse();
  }

  @Test
  public void shouldEqualLeft() {
    assertThat(CallResult.failure(1)).isEqualTo(CallResult.failure(1));
  }

  @Test
  public void shouldHashLeft() {
    assertThat(CallResult.failure(1).hashCode()).isEqualTo(Objects.hashCode(1));
  }

  @Test
  public void shouldConvertLeftToString() {
    assertThat(CallResult.failure(1).toString()).isEqualTo("CallResult.Failure(value=1)");
  }

  @Test
  public void shouldReturnTrueWhenCallingIsRightOnRight() {
    assertThat(CallResult.success(1).isSuccess()).isTrue();
  }

  @Test
  public void shouldReturnFalseWhenCallingIsLeftOnRight() {
    assertThat(CallResult.success(1).isFailure()).isFalse();
  }

  @Test
  public void shouldEqualRightIfObjectIsSame() {
    final CallResult<?, ?> right = CallResult.success(1);
    assertThat(right.equals(right)).isTrue();
  }

  @Test
  public void shouldNotEqualRightIfObjectIsNull() {
    assertThat(CallResult.success(1).equals(null)).isFalse();
  }

  @Test
  public void shouldNotEqualRightIfObjectIsOfDifferentType() {
    assertThat(CallResult.success(1).equals(new Object())).isFalse();
  }

  @Test
  public void shouldEqualRight() {
    assertThat(CallResult.success(1)).isEqualTo(CallResult.success(1));
  }

  @Test
  public void shouldHashRight() {
    assertThat(CallResult.success(1).hashCode()).isEqualTo(Objects.hashCode(1));
  }

  @Test
  public void shouldConvertRightToString() {
    assertThat(CallResult.success(1).toString()).isEqualTo("CallResult.Success(value=1)");
  }

  @Test
  public void shouldHaveSizedSpliterator() {
    assertThat(of(1).spliterator().hasCharacteristics(Spliterator.SIZED | Spliterator.SUBSIZED))
        .isTrue();
  }

  @Test
  public void shouldHaveOrderedSpliterator() {
    assertThat(of(1).spliterator().hasCharacteristics(Spliterator.ORDERED)).isTrue();
  }

  @Test
  public void shouldReturnSizeWhenSpliterator() {
    assertThat(of(1).spliterator().getExactSizeIfKnown()).isEqualTo(1);
  }
}
