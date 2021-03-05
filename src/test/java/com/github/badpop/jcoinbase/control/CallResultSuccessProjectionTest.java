package com.github.badpop.jcoinbase.control;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.control.Option;
import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class CallResultSuccessProjectionTest extends AbstractFunctionalValueTest {

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
  void shouldThrowOnGetOnRightProjectionOffailure() {
    val actual = CallResult.failure(1).success();
    assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(actual::get);
  }

  @Test
  void shouldGetOnRightProjectionOfsuccess() {
    assertThat(CallResult.success(1).success().get()).isEqualTo(1);
  }

  @Test
  void shouldRightProjectionOrElseRightProjection() {
    final CallResult.SuccessProjection<Integer, Integer> elseProjection =
        CallResult.<Integer, Integer>success(2).success();
    assertThat(CallResult.success(1).success().orElse(elseProjection).get()).isEqualTo(1);
    assertThat(CallResult.failure(1).success().orElse(elseProjection).get()).isEqualTo(2);
  }

  @Test
  void shouldRightProjectionOrElseRightProjectionFromSupplier() {
    final CallResult.SuccessProjection<Integer, Integer> elseProjection =
        CallResult.<Integer, Integer>success(2).success();
    assertThat(CallResult.success(1).success().orElse(() -> elseProjection).get()).isEqualTo(1);
    assertThat(CallResult.failure(1).success().orElse(() -> elseProjection).get()).isEqualTo(2);
  }

  @Test
  void shouldReturnRightWhenOrElseOnRightProjectionOfsuccess() {
    final Integer actual = CallResult.<String, Integer>success(1).success().getOrElse(2);
    assertThat(actual).isEqualTo(1);
  }

  @Test
  void shouldReturnOtherWhenOrElseOnRightProjectionOffailure() {
    final Integer actual = CallResult.<String, Integer>failure("1").success().getOrElse(2);
    assertThat(actual).isEqualTo(2);
  }

  @Test
  void shouldReturnRightWhenOrElseGetGivenFunctionOnRightProjectionOfsuccess() {
    final Integer actual = CallResult.<String, Integer>success(1).success().getOrElseGet(l -> 2);
    assertThat(actual).isEqualTo(1);
  }

  @Test
  void shouldReturnOtherWhenOrElseGetGivenFunctionOnRightProjectionOffailure() {
    final Integer actual = CallResult.<String, Integer>failure("1").success().getOrElseGet(l -> 2);
    assertThat(actual).isEqualTo(2);
  }

  @Test
  void shouldReturnRightWhenOrElseRunOnRightProjectionOfsuccess() {
    final boolean[] actual = new boolean[] {true};
    CallResult.<String, Integer>success(1).success().orElseRun(s -> actual[0] = false);
    assertThat(actual[0]).isTrue();
  }

  @Test
  void shouldReturnOtherWhenOrElseRunOnRightProjectionOffailure() {
    final boolean[] actual = new boolean[] {false};
    CallResult.<String, Integer>failure("1").success().orElseRun(s -> actual[0] = true);
    assertThat(actual[0]).isTrue();
  }

  @Test
  void shouldReturnRightWhenOrElseThrowWithFunctionOnRightProjectionOfsuccess() {
    final Integer actual =
        CallResult.<String, Integer>success(1)
            .success()
            .getOrElseThrow((Function<String, RuntimeException>) RuntimeException::new);
    assertThat(actual).isEqualTo(1);
  }

  @Test
  void shouldThrowWhenOrElseThrowWithFunctionOnRightProjectionOffailure() {
    val actual = CallResult.<String, Integer>failure("1").success();
    assertThatExceptionOfType(RuntimeException.class)
        .isThrownBy(() -> actual.getOrElseThrow(i -> new RuntimeException(String.valueOf(i))));
  }

  @Test
  void shouldConvertRightProjectionOfLeftToNone() {
    assertThat(CallResult.failure(0).success().toOption()).isEqualTo(Option.none());
  }

  @Test
  void shouldConvertRightProjectionOfRightToSome() {
    assertThat(CallResult.<Integer, String>success("1").success().toOption())
        .isEqualTo(Option.of("1"));
  }

  @Test
  void shouldConvertRightProjectionOfLeftToCallResult() {
    final CallResult<Integer, String> self = CallResult.failure(1);
    assertThat(self.success().toCallResult()).isEqualTo(self);
  }

  @Test
  void shouldConvertRightProjectionOfRightToCallResult() {
    final CallResult<Integer, String> self = CallResult.success("1");
    assertThat(self.success().toCallResult()).isEqualTo(self);
  }

  @Test
  void shouldConvertRightProjectionOfLeftToJavaOptional() {
    assertThat(CallResult.failure(0).success().toJavaOptional()).isEqualTo(Optional.empty());
  }

  @Test
  void shouldConvertRightProjectionOfRightToJavaOptional() {
    assertThat(CallResult.<Integer, String>success("1").success().toJavaOptional())
        .isEqualTo(Optional.of("1"));
  }

  @Test
  void shouldTransform() {
    final String transformed = of(1).transform(v -> String.valueOf(v.get()));
    assertThat(transformed).isEqualTo("1");
  }

  @Test
  void shouldFilterSomeOnRightProjectionOfRightIfPredicateMatches() {
    final boolean actual =
        CallResult.<String, Integer>success(1).success().filter(i -> true).toOption().isDefined();
    assertThat(actual).isTrue();
  }

  @Test
  void shouldFilterNoneOnRightProjectionOfRightIfPredicateNotMatches() {
    assertThat(CallResult.<String, Integer>success(1).success().filter(i -> false))
        .isEqualTo(Option.none());
  }

  @Test
  void shouldFilterSomeOnRightProjectionOfLeftIfPredicateMatches() {
    final boolean actual =
        CallResult.<String, Integer>failure("1").success().filter(i -> true).isDefined();
    assertThat(actual).isTrue();
  }

  @Test
  void shouldFilterNoneOnRightProjectionOfLeftIfPredicateNotMatches() {
    final boolean actual =
        CallResult.<String, Integer>failure("1").success().filter(i -> false).isDefined();
    assertThat(actual).isTrue();
  }

  @Test
  void shouldFlatMapOnRightProjectionOfsuccess() {
    final CallResult<String, Integer> actual =
        CallResult.<String, Integer>success(1)
            .success()
            .flatMap(i -> CallResult.<String, Integer>success(i + 1).success())
            .toCallResult();
    assertThat(actual).isEqualTo(CallResult.success(2));
  }

  @Test
  void shouldFlatMapOnRightProjectionOffailure() {
    final CallResult<String, Integer> actual =
        CallResult.<String, Integer>failure("1")
            .success()
            .flatMap(i -> CallResult.<String, Integer>success(i + 1).success())
            .toCallResult();
    assertThat(actual).isEqualTo(CallResult.failure("1"));
  }

  @Test
  void shouldFlatMapRightProjectionOfLeftOnRightProjectionOfsuccess() {
    final CallResult<String, String> good = CallResult.success("good");
    final CallResult<String, String> bad = CallResult.failure("bad");
    final CallResult.SuccessProjection<String, Tuple2<String, String>> actual =
        good.success().flatMap(g -> bad.success().map(b -> Tuple.of(g, b)));
    assertThat(actual.toCallResult()).isEqualTo(CallResult.failure("bad"));
  }

  @Test
  void shouldBeAwareOfPropertyThatHoldsExistsOfRightProjectionOfsuccess() {
    assertThat(CallResult.success(1).success().exists(i -> i == 1)).isTrue();
  }

  @Test
  void shouldBeAwareOfPropertyThatNotHoldsExistsOfRightProjectionOfsuccess() {
    assertThat(CallResult.success(1).success().exists(i -> i == 2)).isFalse();
  }

  @Test
  void shouldNotHoldPropertyExistsOfRightProjectionOffailure() {
    assertThat(CallResult.success(1).failure().exists(e -> true)).isFalse();
  }

  @Test
  void shouldBeAwareOfPropertyThatHoldsForAllOfRightProjectionOfsuccess() {
    assertThat(CallResult.success(1).success().forAll(i -> i == 1)).isTrue();
  }

  @Test
  void shouldBeAwareOfPropertyThatNotHoldsForAllOfRightProjectionOfsuccess() {
    assertThat(CallResult.success(1).success().forAll(i -> i == 2)).isFalse();
  }

  @Test
  void shouldNotHoldPropertyForAllOfRightProjectionOffailure() {
    assertThat(CallResult.success(1).failure().forAll(e -> true)).isTrue();
  }

  @Test
  void shouldForEachOnRightProjectionOfsuccess() {
    final List<Integer> actual = new ArrayList<>();
    CallResult.<String, Integer>success(1).success().forEach(actual::add);
    assertThat(actual).isEqualTo(Collections.singletonList(1));
  }

  @Test
  void shouldForEachOnRightProjectionOffailure() {
    final List<Integer> actual = new ArrayList<>();
    CallResult.<String, Integer>failure("1").success().forEach(actual::add);
    assertThat(actual.isEmpty()).isTrue();
  }

  @Test
  void shouldPeekOnRightProjectionOfsuccess() {
    final List<Integer> actual = new ArrayList<>();
    final CallResult<String, Integer> testee =
        CallResult.<String, Integer>success(1).success().peek(actual::add).toCallResult();
    assertThat(actual).isEqualTo(Collections.singletonList(1));
    assertThat(testee).isEqualTo(CallResult.success(1));
  }

  @Test
  void shouldPeekOnRightProjectionOffailure() {
    final List<Integer> actual = new ArrayList<>();
    final CallResult<String, Integer> testee =
        CallResult.<String, Integer>failure("1").success().peek(actual::add).toCallResult();
    assertThat(actual.isEmpty()).isTrue();
    assertThat(testee).isEqualTo(CallResult.<String, Integer>failure("1"));
  }

  @Test
  void shouldMapOnRightProjectionOfsuccess() {
    final CallResult<String, Integer> actual =
        CallResult.<String, Integer>success(1).success().map(i -> i + 1).toCallResult();
    assertThat(actual).isEqualTo(CallResult.success(2));
  }

  @Test
  void shouldMapOnRightProjectionOffailure() {
    final CallResult<String, Integer> actual =
        CallResult.<String, Integer>failure("1").success().map(i -> i + 1).toCallResult();
    assertThat(actual).isEqualTo(CallResult.failure("1"));
  }

  @Test
  void shouldReturnIteratorOfRightOfRightProjection() {
    assertThat((Iterator<Integer>) CallResult.success(1).success().iterator()).isNotNull();
  }

  @Test
  void shouldReturnIteratorOfLeftOfRightProjection() {
    assertThat((Iterator<Object>) CallResult.failure(1).success().iterator()).isNotNull();
  }

  @Test
  void shouldEqualRightProjectionOfRightIfObjectIsSame() {
    final CallResult.SuccessProjection<?, ?> r = CallResult.success(1).success();
    assertThat(r.equals(r)).isTrue();
  }

  @Test
  void shouldEqualRightProjectionOfLeftIfObjectIsSame() {
    final CallResult.SuccessProjection<?, ?> r = CallResult.failure(1).success();
    assertThat(r.equals(r)).isTrue();
  }

  @Test
  void shouldNotEqualRightProjectionOfRightIfObjectIsNull() {
    assertThat(CallResult.success(1).success().equals(null)).isFalse();
  }

  @Test
  void shouldNotEqualRightProjectionOfLeftIfObjectIsNull() {
    assertThat(CallResult.failure(1).success().equals(null)).isFalse();
  }

  @Test
  void shouldNotEqualRightProjectionOfRightIfObjectIsOfDifferentType() {
    assertThat(CallResult.success(1).success().equals(new Object())).isFalse();
  }

  @Test
  void shouldNotEqualRightProjectionOfLeftIfObjectIsOfDifferentType() {
    assertThat(CallResult.failure(1).success().equals(new Object())).isFalse();
  }

  @Test
  void shouldEqualRightProjectionOfsuccess() {
    assertThat(CallResult.success(1).success()).isEqualTo(CallResult.success(1).success());
  }

  @Test
  void shouldEqualRightProjectionOffailure() {
    assertThat(CallResult.failure(1).success()).isEqualTo(CallResult.failure(1).success());
  }

  @Test
  void shouldHashRightProjectionOfsuccess() {
    assertThat(CallResult.success(1).success().hashCode())
        .isEqualTo(Objects.hashCode(CallResult.success(1)));
  }

  @Test
  void shouldHashRightProjectionOffailure() {
    assertThat(CallResult.failure(1).success().hashCode())
        .isEqualTo(Objects.hashCode(CallResult.failure(1)));
  }

  @Test
  void shouldConvertRightProjectionOfLeftToString() {
    assertThat(CallResult.failure(1).success().toString())
        .isEqualTo("CallResult.SuccessProjection(callResult=CallResult.Failure(value=1))");
  }

  @Test
  void shouldConvertRightProjectionOfRightToString() {
    assertThat(CallResult.success(1).success().toString())
        .isEqualTo("CallResult.SuccessProjection(callResult=CallResult.Success(value=1))");
  }

  @Test
  void shouldHaveSizedSpliterator() {
    assertThat(of(1).spliterator().hasCharacteristics(Spliterator.SIZED | Spliterator.SUBSIZED))
        .isTrue();
  }

  @Test
  void shouldHaveOrderedSpliterator() {
    assertThat(of(1).spliterator().hasCharacteristics(Spliterator.ORDERED)).isTrue();
  }

  @Test
  void shouldReturnSizeWhenSpliterator() {
    assertThat(of(1).spliterator().getExactSizeIfKnown()).isEqualTo(1);
  }
}
