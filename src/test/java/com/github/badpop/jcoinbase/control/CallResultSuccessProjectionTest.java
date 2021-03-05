package com.github.badpop.jcoinbase.control;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Option;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class CallResultSuccessProjectionTest extends AbstractFunctionalValueTest {

  @Override
  protected <T> CallResult.SuccessProjection<?, T> empty() {
    return CallResult.<T, T>failure(null).success();
  }

  @Override
  protected <T> CallResult.SuccessProjection<?, T> of(T element) {
    return CallResult.<T, T>success(element).success();
  }

  @SafeVarargs
  @Override
  protected final <T> CallResult.SuccessProjection<?, T> of(T... elements) {
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
  public void shouldThrowOnGetOnRightProjectionOffailure() {
    assertThatExceptionOfType(NoSuchElementException.class)
        .isThrownBy(() -> CallResult.failure(1).success().get());
  }

  @Test
  public void shouldGetOnRightProjectionOfsuccess() {
    assertThat(CallResult.success(1).success().get()).isEqualTo(1);
  }

  @Test
  public void shouldRightProjectionOrElseRightProjection() {
    final CallResult.SuccessProjection<Integer, Integer> elseProjection =
        CallResult.<Integer, Integer>success(2).success();
    assertThat(CallResult.success(1).success().orElse(elseProjection).get()).isEqualTo(1);
    assertThat(CallResult.failure(1).success().orElse(elseProjection).get()).isEqualTo(2);
  }

  @Test
  public void shouldRightProjectionOrElseRightProjectionFromSupplier() {
    final CallResult.SuccessProjection<Integer, Integer> elseProjection =
        CallResult.<Integer, Integer>success(2).success();
    assertThat(CallResult.success(1).success().orElse(() -> elseProjection).get()).isEqualTo(1);
    assertThat(CallResult.failure(1).success().orElse(() -> elseProjection).get()).isEqualTo(2);
  }

  @Test
  public void shouldReturnRightWhenOrElseOnRightProjectionOfsuccess() {
    final Integer actual = CallResult.<String, Integer>success(1).success().getOrElse(2);
    assertThat(actual).isEqualTo(1);
  }

  @Test
  public void shouldReturnOtherWhenOrElseOnRightProjectionOffailure() {
    final Integer actual = CallResult.<String, Integer>failure("1").success().getOrElse(2);
    assertThat(actual).isEqualTo(2);
  }

  @Test
  public void shouldReturnRightWhenOrElseGetGivenFunctionOnRightProjectionOfsuccess() {
    final Integer actual = CallResult.<String, Integer>success(1).success().getOrElseGet(l -> 2);
    assertThat(actual).isEqualTo(1);
  }

  @Test
  public void shouldReturnOtherWhenOrElseGetGivenFunctionOnRightProjectionOffailure() {
    final Integer actual = CallResult.<String, Integer>failure("1").success().getOrElseGet(l -> 2);
    assertThat(actual).isEqualTo(2);
  }

  @Test
  public void shouldReturnRightWhenOrElseRunOnRightProjectionOfsuccess() {
    final boolean[] actual = new boolean[] {true};
    CallResult.<String, Integer>success(1).success().orElseRun(s -> actual[0] = false);
    assertThat(actual[0]).isTrue();
  }

  @Test
  public void shouldReturnOtherWhenOrElseRunOnRightProjectionOffailure() {
    final boolean[] actual = new boolean[] {false};
    CallResult.<String, Integer>failure("1").success().orElseRun(s -> actual[0] = true);
    assertThat(actual[0]).isTrue();
  }

  @Test
  public void shouldReturnRightWhenOrElseThrowWithFunctionOnRightProjectionOfsuccess() {
    final Integer actual =
        CallResult.<String, Integer>success(1)
            .success()
            .getOrElseThrow((Function<String, RuntimeException>) RuntimeException::new);
    assertThat(actual).isEqualTo(1);
  }

  @Test
  public void shouldThrowWhenOrElseThrowWithFunctionOnRightProjectionOffailure() {
    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(
            () ->
                CallResult.<String, Integer>failure("1")
                    .success()
                    .getOrElseThrow(i -> new RuntimeException(String.valueOf(i))));
  }

  @Test
  public void shouldConvertRightProjectionOfLeftToNone() {
    assertThat(CallResult.failure(0).success().toOption()).isEqualTo(Option.none());
  }

  @Test
  public void shouldConvertRightProjectionOfRightToSome() {
    assertThat(CallResult.<Integer, String>success("1").success().toOption())
        .isEqualTo(Option.of("1"));
  }

  @Test
  public void shouldConvertRightProjectionOfLeftToCallResult() {
    final CallResult<Integer, String> self = CallResult.failure(1);
    assertThat(self.success().toCallResult()).isEqualTo(self);
  }

  @Test
  public void shouldConvertRightProjectionOfRightToCallResult() {
    final CallResult<Integer, String> self = CallResult.success("1");
    assertThat(self.success().toCallResult()).isEqualTo(self);
  }

  @Test
  public void shouldConvertRightProjectionOfLeftToJavaOptional() {
    assertThat(CallResult.failure(0).success().toJavaOptional()).isEqualTo(Optional.empty());
  }

  @Test
  public void shouldConvertRightProjectionOfRightToJavaOptional() {
    assertThat(CallResult.<Integer, String>success("1").success().toJavaOptional())
        .isEqualTo(Optional.of("1"));
  }

  @Test
  public void shouldTransform() {
    final String transformed = of(1).transform(v -> String.valueOf(v.get()));
    assertThat(transformed).isEqualTo("1");
  }

  @Test
  public void shouldFilterSomeOnRightProjectionOfRightIfPredicateMatches() {
    final boolean actual =
        CallResult.<String, Integer>success(1).success().filter(i -> true).toOption().isDefined();
    assertThat(actual).isTrue();
  }

  @Test
  public void shouldFilterNoneOnRightProjectionOfRightIfPredicateNotMatches() {
    assertThat(CallResult.<String, Integer>success(1).success().filter(i -> false))
        .isEqualTo(Option.none());
  }

  @Test
  public void shouldFilterSomeOnRightProjectionOfLeftIfPredicateMatches() {
    final boolean actual =
        CallResult.<String, Integer>failure("1").success().filter(i -> true).isDefined();
    assertThat(actual).isTrue();
  }

  @Test
  public void shouldFilterNoneOnRightProjectionOfLeftIfPredicateNotMatches() {
    final boolean actual =
        CallResult.<String, Integer>failure("1").success().filter(i -> false).isDefined();
    assertThat(actual).isTrue();
  }

  @Test
  public void shouldFlatMapOnRightProjectionOfsuccess() {
    final CallResult<String, Integer> actual =
        CallResult.<String, Integer>success(1)
            .success()
            .flatMap(i -> CallResult.<String, Integer>success(i + 1).success())
            .toCallResult();
    assertThat(actual).isEqualTo(CallResult.success(2));
  }

  @Test
  public void shouldFlatMapOnRightProjectionOffailure() {
    final CallResult<String, Integer> actual =
        CallResult.<String, Integer>failure("1")
            .success()
            .flatMap(i -> CallResult.<String, Integer>success(i + 1).success())
            .toCallResult();
    assertThat(actual).isEqualTo(CallResult.failure("1"));
  }

  @Test
  public void shouldFlatMapRightProjectionOfLeftOnRightProjectionOfsuccess() {
    final CallResult<String, String> good = CallResult.success("good");
    final CallResult<String, String> bad = CallResult.failure("bad");
    final CallResult.SuccessProjection<String, Tuple2<String, String>> actual =
        good.success().flatMap(g -> bad.success().map(b -> Tuple.of(g, b)));
    assertThat(actual.toCallResult()).isEqualTo(CallResult.failure("bad"));
  }

  @Test
  public void shouldBeAwareOfPropertyThatHoldsExistsOfRightProjectionOfsuccess() {
    assertThat(CallResult.success(1).success().exists(i -> i == 1)).isTrue();
  }

  @Test
  public void shouldBeAwareOfPropertyThatNotHoldsExistsOfRightProjectionOfsuccess() {
    assertThat(CallResult.success(1).success().exists(i -> i == 2)).isFalse();
  }

  @Test
  public void shouldNotHoldPropertyExistsOfRightProjectionOffailure() {
    assertThat(CallResult.success(1).failure().exists(e -> true)).isFalse();
  }

  @Test
  public void shouldBeAwareOfPropertyThatHoldsForAllOfRightProjectionOfsuccess() {
    assertThat(CallResult.success(1).success().forAll(i -> i == 1)).isTrue();
  }

  @Test
  public void shouldBeAwareOfPropertyThatNotHoldsForAllOfRightProjectionOfsuccess() {
    assertThat(CallResult.success(1).success().forAll(i -> i == 2)).isFalse();
  }

  @Test
  public void shouldNotHoldPropertyForAllOfRightProjectionOffailure() {
    assertThat(CallResult.success(1).failure().forAll(e -> true)).isTrue();
  }

  @Test
  public void shouldForEachOnRightProjectionOfsuccess() {
    final List<Integer> actual = new ArrayList<>();
    CallResult.<String, Integer>success(1).success().forEach(actual::add);
    assertThat(actual).isEqualTo(Collections.singletonList(1));
  }

  @Test
  public void shouldForEachOnRightProjectionOffailure() {
    final List<Integer> actual = new ArrayList<>();
    CallResult.<String, Integer>failure("1").success().forEach(actual::add);
    assertThat(actual.isEmpty()).isTrue();
  }

  @Test
  public void shouldPeekOnRightProjectionOfsuccess() {
    final List<Integer> actual = new ArrayList<>();
    final CallResult<String, Integer> testee =
        CallResult.<String, Integer>success(1).success().peek(actual::add).toCallResult();
    assertThat(actual).isEqualTo(Collections.singletonList(1));
    assertThat(testee).isEqualTo(CallResult.success(1));
  }

  @Test
  public void shouldPeekOnRightProjectionOffailure() {
    final List<Integer> actual = new ArrayList<>();
    final CallResult<String, Integer> testee =
        CallResult.<String, Integer>failure("1").success().peek(actual::add).toCallResult();
    assertThat(actual.isEmpty()).isTrue();
    assertThat(testee).isEqualTo(CallResult.<String, Integer>failure("1"));
  }

  @Test
  public void shouldMapOnRightProjectionOfsuccess() {
    final CallResult<String, Integer> actual =
        CallResult.<String, Integer>success(1).success().map(i -> i + 1).toCallResult();
    assertThat(actual).isEqualTo(CallResult.success(2));
  }

  @Test
  public void shouldMapOnRightProjectionOffailure() {
    final CallResult<String, Integer> actual =
        CallResult.<String, Integer>failure("1").success().map(i -> i + 1).toCallResult();
    assertThat(actual).isEqualTo(CallResult.failure("1"));
  }

  @Test
  public void shouldReturnIteratorOfRightOfRightProjection() {
    assertThat((Iterator<Integer>) CallResult.success(1).success().iterator()).isNotNull();
  }

  @Test
  public void shouldReturnIteratorOfLeftOfRightProjection() {
    assertThat((Iterator<Object>) CallResult.failure(1).success().iterator()).isNotNull();
  }

  @Test
  public void shouldEqualRightProjectionOfRightIfObjectIsSame() {
    final CallResult.SuccessProjection<?, ?> r = CallResult.success(1).success();
    assertThat(r.equals(r)).isTrue();
  }

  @Test
  public void shouldEqualRightProjectionOfLeftIfObjectIsSame() {
    final CallResult.SuccessProjection<?, ?> r = CallResult.failure(1).success();
    assertThat(r.equals(r)).isTrue();
  }

  @Test
  public void shouldNotEqualRightProjectionOfRightIfObjectIsNull() {
    assertThat(CallResult.success(1).success().equals(null)).isFalse();
  }

  @Test
  public void shouldNotEqualRightProjectionOfLeftIfObjectIsNull() {
    assertThat(CallResult.failure(1).success().equals(null)).isFalse();
  }

  @Test
  public void shouldNotEqualRightProjectionOfRightIfObjectIsOfDifferentType() {
    assertThat(CallResult.success(1).success().equals(new Object())).isFalse();
  }

  @Test
  public void shouldNotEqualRightProjectionOfLeftIfObjectIsOfDifferentType() {
    assertThat(CallResult.failure(1).success().equals(new Object())).isFalse();
  }

  @Test
  public void shouldEqualRightProjectionOfsuccess() {
    assertThat(CallResult.success(1).success()).isEqualTo(CallResult.success(1).success());
  }

  @Test
  public void shouldEqualRightProjectionOffailure() {
    assertThat(CallResult.failure(1).success()).isEqualTo(CallResult.failure(1).success());
  }

  @Test
  public void shouldHashRightProjectionOfsuccess() {
    assertThat(CallResult.success(1).success().hashCode())
        .isEqualTo(Objects.hashCode(CallResult.success(1)));
  }

  @Test
  public void shouldHashRightProjectionOffailure() {
    assertThat(CallResult.failure(1).success().hashCode())
        .isEqualTo(Objects.hashCode(CallResult.failure(1)));
  }

  @Test
  public void shouldConvertRightProjectionOfLeftToString() {
    assertThat(CallResult.failure(1).success().toString()).isEqualTo("CallResult.SuccessProjection(callResult=CallResult.Failure(value=1))");
  }

  @Test
  public void shouldConvertRightProjectionOfRightToString() {
    assertThat(CallResult.success(1).success().toString()).isEqualTo("CallResult.SuccessProjection(callResult=CallResult.Success(value=1))");
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
