package com.github.badpop.jcoinbase.control;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Option;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class CallResultFailureProjectionTest extends AbstractFunctionalValueTest {

  @Override
  protected <T> CallResult.FailureProjection<T, ?> empty() {
    return CallResult.<T, T>success(null).failure();
  }

  @Override
  protected <T> CallResult.FailureProjection<T, ?> of(T element) {
    return CallResult.<T, T>failure(element).failure();
  }

  @SafeVarargs
  @Override
  protected final <T> CallResult.FailureProjection<T, ?> of(T... elements) {
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
  public void shouldThrowOnGetOnLeftProjectionOfsuccess() {
    assertThatExceptionOfType(NoSuchElementException.class)
        .isThrownBy(() -> CallResult.success(1).failure().get());
  }

  @Test
  public void shouldGetOnLeftProjectionOffailure() {
    assertThat(CallResult.failure(1).failure().get()).isEqualTo(1);
  }

  @Test
  public void shouldLeftProjectionOrElseLeftProjection() {
    final CallResult.FailureProjection<Integer, Integer> elseProjection =
        CallResult.<Integer, Integer>failure(2).failure();
    assertThat(CallResult.failure(1).failure().orElse(elseProjection).get()).isEqualTo(1);
    assertThat(CallResult.success(1).failure().orElse(elseProjection).get()).isEqualTo(2);
  }

  @Test
  public void shouldLeftProjectionOrElseLeftProjectionFromSupplier() {
    final CallResult.FailureProjection<Integer, Integer> elseProjection =
        CallResult.<Integer, Integer>failure(2).failure();
    assertThat(CallResult.failure(1).failure().orElse(() -> elseProjection).get()).isEqualTo(1);
    assertThat(CallResult.success(1).failure().orElse(() -> elseProjection).get()).isEqualTo(2);
  }

  @Test
  public void shouldReturnLeftWhenOrElseOnLeftProjectionOffailure() {
    final Integer actual = CallResult.failure(1).failure().getOrElse(2);
    assertThat(actual).isEqualTo(1);
  }

  @Test
  public void shouldReturnOtherWhenOrElseOnLeftProjectionOfsuccess() {
    final Integer actual = CallResult.<Integer, String>success("1").failure().getOrElse(2);
    assertThat(actual).isEqualTo(2);
  }

  @Test
  public void shouldReturnLeftWhenOrElseGetGivenFunctionOnLeftProjectionOffailure() {
    final Integer actual = CallResult.failure(1).failure().getOrElseGet(r -> 2);
    assertThat(actual).isEqualTo(1);
  }

  @Test
  public void shouldReturnOtherWhenOrElseGetGivenFunctionOnLeftProjectionOfsuccess() {
    final Integer actual = CallResult.<Integer, String>success("1").failure().getOrElseGet(r -> 2);
    assertThat(actual).isEqualTo(2);
  }

  @Test
  public void shouldReturnLeftWhenOrElseRunOnLeftProjectionOffailure() {
    final boolean[] actual = new boolean[] {true};
    CallResult.failure(1).failure().orElseRun(s -> actual[0] = false);
    assertThat(actual[0]).isTrue();
  }

  @Test
  public void shouldReturnOtherWhenOrElseRunOnLeftProjectionOfsuccess() {
    final boolean[] actual = new boolean[] {false};
    CallResult.success("1")
        .failure()
        .orElseRun(
            s -> {
              actual[0] = true;
            });
    assertThat(actual[0]).isTrue();
  }

  @Test
  public void shouldReturnLeftWhenOrElseThrowWithFunctionOnLeftProjectionOffailure() {
    final Integer actual =
        CallResult.<Integer, String>failure(1)
            .failure()
            .getOrElseThrow((Function<String, RuntimeException>) RuntimeException::new);
    assertThat(actual).isEqualTo(1);
  }

  @Test
  public void shouldThrowWhenOrElseThrowWithFunctionOnLeftProjectionOfsuccess() {
    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(
            () ->
                CallResult.success("1")
                    .failure()
                    .getOrElseThrow((Function<String, RuntimeException>) RuntimeException::new));
  }
  
  @Test
  public void shouldConvertLeftProjectionOfLeftToSome() {
    assertThat(CallResult.failure(1).failure().toOption()).isEqualTo(Option.of(1));
  }

  @Test
  public void shouldConvertLeftProjectionOfRightToNone() {
    assertThat(CallResult.success("x").failure().toOption()).isEqualTo(Option.none());
  }

  @Test
  public void shouldConvertLeftProjectionOfLeftToEither() {
    final CallResult<Integer, String> self = CallResult.failure(1);
    assertThat(self.failure().toCallResult()).isEqualTo(self);
  }

  @Test
  public void shouldConvertLeftProjectionOfRightToEither() {
    final CallResult<Integer, String> self = CallResult.success("1");
    assertThat(self.failure().toCallResult()).isEqualTo(self);
  }

  @Test
  public void shouldConvertLeftProjectionOfLeftToJavaOptional() {
    assertThat(CallResult.failure(1).failure().toJavaOptional()).isEqualTo(Optional.of(1));
  }

  @Test
  public void shouldConvertLeftProjectionOfRightToJavaOptional() {
    assertThat(CallResult.<Integer, String>success("x").failure().toJavaOptional())
        .isEqualTo(Optional.empty());
  }

  @Test
  public void shouldFilterSomeOnLeftProjectionOfLeftIfPredicateMatches() {
    final boolean actual = CallResult.failure(1).failure().filter(i -> true).toOption().isDefined();
    assertThat(actual).isTrue();
  }

  @Test
  public void shouldFilterNoneOnLeftProjectionOfLeftIfPredicateNotMatches() {
    assertThat(CallResult.failure(1).failure().filter(i -> false)).isEqualTo(Option.none());
  }

  @Test
  public void shouldFilterSomeOnLeftProjectionOfRightIfPredicateMatches() {
    final boolean actual = CallResult.success("1").failure().filter(i -> true).isDefined();
    assertThat(actual).isTrue();
  }

  @Test
  public void shouldFilterNoneOnLeftProjectionOfRightIfPredicateNotMatches() {
    final boolean actual = CallResult.success("1").failure().filter(i -> false).isDefined();
    assertThat(actual).isTrue();
  }

  @Test
  public void shouldFlatMapOnLeftProjectionOffailure() {
    final CallResult<Integer, String> actual =
        CallResult.<Integer, String>failure(1)
            .failure()
            .flatMap(i -> CallResult.<Integer, String>failure(i + 1).failure())
            .toCallResult();
    assertThat(actual).isEqualTo(CallResult.failure(2));
  }

  @Test
  public void shouldFlatMapOnLeftProjectionOfsuccess() {
    final CallResult<Integer, String> actual =
        CallResult.<Integer, String>success("1")
            .failure()
            .flatMap(i -> CallResult.<Integer, String>failure(i + 1).failure())
            .toCallResult();
    assertThat(actual).isEqualTo(CallResult.success("1"));
  }

  @Test
  public void shouldFlatMapLeftProjectionOfRightOnLeftProjectionOffailure() {
    final CallResult<String, String> good = CallResult.failure("good");
    final CallResult<String, String> bad = CallResult.success("bad");
    final CallResult.FailureProjection<Tuple2<String, String>, String> actual =
        good.failure().flatMap(g -> bad.failure().map(b -> Tuple.of(g, b)));
    assertThat(actual.toCallResult()).isEqualTo(CallResult.success("bad"));
  }

  @Test
  public void shouldBeAwareOfPropertyThatHoldsExistsOfLeftProjectionOffailure() {
    assertThat(CallResult.failure(1).failure().exists(i -> i == 1)).isTrue();
  }

  @Test
  public void shouldBeAwareOfPropertyThatNotHoldsExistsOfLeftProjectionOffailure() {
    assertThat(CallResult.failure(1).failure().exists(i -> i == 2)).isFalse();
  }

  @Test
  public void shouldNotHoldPropertyExistsOfLeftProjectionOfsuccess() {
    assertThat(CallResult.failure(1).success().exists(e -> true)).isFalse();
  }

  @Test
  public void shouldBeAwareOfPropertyThatHoldsForAllOfLeftProjectionOffailure() {
    assertThat(CallResult.failure(1).failure().forAll(i -> i == 1)).isTrue();
  }

  @Test
  public void shouldBeAwareOfPropertyThatNotHoldsForAllOfLeftProjectionOffailure() {
    assertThat(CallResult.failure(1).failure().forAll(i -> i == 2)).isFalse();
  }

  @Test
  public void shouldNotHoldPropertyForAllOfLeftProjectionOfsuccess() {
    assertThat(CallResult.failure(1).success().forAll(e -> true)).isTrue();
  }

  @Test
  public void shouldForEachOnLeftProjectionOffailure() {
    final List<Integer> actual = new ArrayList<>();
    CallResult.failure(1).failure().forEach(actual::add);
    assertThat(actual).isEqualTo(Collections.singletonList(1));
  }

  @Test
  public void shouldForEachOnLeftProjectionOfsuccess() {
    final List<Integer> actual = new ArrayList<>();
    CallResult.<Integer, String>success("1").failure().forEach(actual::add);
    assertThat(actual.isEmpty()).isTrue();
  }

  @Test
  public void shouldPeekOnLeftProjectionOffailure() {
    final List<Integer> actual = new ArrayList<>();
    final CallResult<Integer, String> testee =
        CallResult.<Integer, String>failure(1).failure().peek(actual::add).toCallResult();
    assertThat(actual).isEqualTo(Collections.singletonList(1));
    assertThat(testee).isEqualTo(CallResult.failure(1));
  }

  @Test
  public void shouldPeekOnLeftProjectionOfsuccess() {
    final List<Integer> actual = new ArrayList<>();
    final CallResult<Integer, String> testee =
        CallResult.<Integer, String>success("1").failure().peek(actual::add).toCallResult();
    assertThat(actual.isEmpty()).isTrue();
    assertThat(testee).isEqualTo(CallResult.success("1"));
  }

  @Test
  public void shouldMapOnLeftProjectionOffailure() {
    final CallResult<Integer, String> actual =
        CallResult.<Integer, String>failure(1).failure().map(i -> i + 1).toCallResult();
    assertThat(actual).isEqualTo(CallResult.failure(2));
  }

  @Test
  public void shouldMapOnLeftProjectionOfsuccess() {
    final CallResult<Integer, String> actual =
        CallResult.<Integer, String>success("1").failure().map(i -> i + 1).toCallResult();
    assertThat(actual).isEqualTo(CallResult.success("1"));
  }

  @Test
  public void shouldReturnIteratorOfLeftOfLeftProjection() {
    assertThat((Iterator<Integer>) CallResult.failure(1).failure().iterator()).isNotNull();
  }

  @Test
  public void shouldReturnIteratorOfRightOfLeftProjection() {
    assertThat((Iterator<Object>) CallResult.success(1).failure().iterator()).isNotNull();
  }

  @Test
  public void shouldEqualLeftProjectionOfLeftIfObjectIsSame() {
    final CallResult.FailureProjection<?, ?> l = CallResult.failure(1).failure();
    assertThat(l.equals(l)).isTrue();
  }

  @Test
  public void shouldEqualLeftProjectionOfRightIfObjectIsSame() {
    final CallResult.FailureProjection<?, ?> l = CallResult.success(1).failure();
    assertThat(l.equals(l)).isTrue();
  }

  @Test
  public void shouldNotEqualLeftProjectionOfLeftIfObjectIsNull() {
    assertThat(CallResult.failure(1).failure().equals(null)).isFalse();
  }

  @Test
  public void shouldNotEqualLeftProjectionOfRightIfObjectIsNull() {
    assertThat(CallResult.success(1).failure().equals(null)).isFalse();
  }

  @Test
  public void shouldNotEqualLeftProjectionOfLeftIfObjectIsOfDifferentType() {
    assertThat(CallResult.failure(1).failure().equals(new Object())).isFalse();
  }

  @Test
  public void shouldNotEqualLeftProjectionOfRightIfObjectIsOfDifferentType() {
    assertThat(CallResult.success(1).failure().equals(new Object())).isFalse();
  }

  @Test
  public void shouldEqualLeftProjectionOffailure() {
    assertThat(CallResult.failure(1).failure()).isEqualTo(CallResult.failure(1).failure());
  }

  @Test
  public void shouldEqualLeftProjectionOfsuccess() {
    assertThat(CallResult.success(1).failure()).isEqualTo(CallResult.success(1).failure());
  }

  @Test
  public void shouldHashLeftProjectionOffailure() {
    assertThat(CallResult.failure(1).failure().hashCode())
        .isEqualTo(Objects.hashCode(CallResult.success(1)));
  }

  @Test
  public void shouldHashLeftProjectionOfsuccess() {
    assertThat(CallResult.success(1).failure().hashCode())
        .isEqualTo(Objects.hashCode(CallResult.failure(1)));
  }

  @Test
  public void shouldConvertLeftProjectionOfLeftToString() {
    assertThat(CallResult.failure(1).failure().toString()).isEqualTo("CallResult.FailureProjection(callResult=CallResult.Failure(value=1))");
  }

  @Test
  public void shouldConvertLeftProjectionOfRightToString() {
    assertThat(CallResult.success(1).failure().toString()).isEqualTo("CallResult.FailureProjection(callResult=CallResult.Success(value=1))");
  }

  @Test
  public void shouldTransform() {
    final String transformed = of(1).transform(v -> String.valueOf(v.get()));
    assertThat(transformed).isEqualTo("1");
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
