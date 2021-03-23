package com.github.badpop.jcoinbase.control;

import io.vavr.control.Option;
import lombok.val;
import org.assertj.core.api.*;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertThrows;

abstract class AbstractFunctionalValueTest {

  protected <T> IterableAssert<T> assertThat(Iterable<T> actual) {
    return new IterableAssert<T>(actual) {};
  }

  protected <T> ObjectAssert<T> assertThat(T actual) {
    return new ObjectAssert<T>(actual) {};
  }

  protected <T> ObjectArrayAssert<T> assertThat(T[] actual) {
    return new ObjectArrayAssert<T>(actual) {};
  }

  protected BooleanAssert assertThat(Boolean actual) {
    return new BooleanAssert(actual) {};
  }

  protected DoubleAssert assertThat(Double actual) {
    return new DoubleAssert(actual) {};
  }

  protected IntegerAssert assertThat(Integer actual) {
    return new IntegerAssert(actual) {};
  }

  protected LongAssert assertThat(Long actual) {
    return new LongAssert(actual) {};
  }

  protected StringAssert assertThat(String actual) {
    return new StringAssert(actual) {};
  }

  protected abstract <T> FunctionalValue<T> empty();

  protected abstract <T> FunctionalValue<T> of(T element);

  @SuppressWarnings("unchecked")
  protected abstract <T> FunctionalValue<T> of(T... elements);

  protected abstract boolean useIsEqualToInsteadOfIsSameAs();

  protected abstract int getPeekNonNilPerformingAnAction();

  @Test
  void shouldGetEmpty() {
    val empty = empty();
    assertThrows(NoSuchElementException.class, empty::get);
  }

  @Test
  void shouldContains() {
    val actual = of(1);
    assertThat(actual.contains(1)).isTrue();
  }

  @Test
  void shouldNotContains() {
    val actual = of(1);
    assertThat(actual.contains(2)).isFalse();
  }

  @Test
  void shouldNotBeAsync() {
    assertThat(empty().isAsync()).isFalse();
  }

  @Test
  void shouldNotBeLazy() {
    assertThat(empty().isLazy()).isFalse();
  }

  @Test
  void shouldBesINGLEvalued() {
    assertThat(empty().isSingleValued()).isTrue();
  }

  @Test
  void shouldGetNonEmpty() {
    assertThat(of(1).get()).isEqualTo(1);
  }

  @Test
  void shouldCalculateGetOrElseWithNull() {
    assertThat(this.<Integer>empty().getOrElse((Integer) null)).isEqualTo(null);
    assertThat(of(1).getOrElse((Integer) null)).isEqualTo(1);
  }

  @Test
  void shouldCalculateGetOrElseWithNonNull() {
    assertThat(empty().getOrElse(1)).isEqualTo(1);
    assertThat(of(1).getOrElse(2)).isEqualTo(1);
  }

  @Test
  void shouldThrowOnGetOrElseWithNullSupplier() {
    final Supplier<?> supplier = null;
    val empty = empty();
    assertThrows(NullPointerException.class, () -> empty.getOrElse(supplier));
  }

  @Test
  void shouldCalculateGetOrElseWithSupplier() {
    assertThat(empty().getOrElse(() -> 1)).isEqualTo(1);
    assertThat(of(1).getOrElse(() -> 2)).isEqualTo(1);
  }

  @Test
  void shouldThrowOnGetOrElseThrowIfEmpty() {
    val empty = empty();
    assertThrows(ArithmeticException.class, () -> empty.getOrElseThrow(ArithmeticException::new));
  }

  @Test
  void shouldNotThrowOnGetOrElseThrowIfNonEmpty() {
    assertThat(of(1).getOrElseThrow(ArithmeticException::new)).isEqualTo(1);
  }

  @Test
  void shouldReturnUnderlyingValueWhenCallingGetOrElseTryOnNonEmptyValue() {
    assertThat(of(1).getOrElseTry(() -> 2)).isEqualTo(1);
  }

  @Test
  void shouldReturnAlternateValueWhenCallingGetOrElseTryOnEmptyValue() {
    assertThat(empty().getOrElseTry(() -> 2)).isEqualTo(2);
  }

  @Test
  void shouldThrowWhenCallingGetOrElseTryOnEmptyValueAndTryIsAFailure() {
    val empty = empty();
    assertThrows(
        Error.class,
        () ->
            empty.getOrElseTry(
                () -> {
                  throw new Error();
                }));
  }

  @Test
  void shouldReturnNullWhenGetOrNullOfEmpty() {
    assertThat(empty().getOrNull()).isEqualTo(null);
  }

  @Test
  void shouldReturnValueWhenGetOrNullOfNonEmpty() {
    assertThat(of(1).getOrNull()).isEqualTo(1);
  }

  @Test
  void shouldPerformsActionOnEachElement() {
    final int[] consumer = new int[1];
    final FunctionalValue<Integer> value = of(1, 2, 3);
    value.forEach(i -> consumer[0] += i);
    assertThat(consumer[0]).isEqualTo(value.isSingleValued() ? 1 : 6);
  }

  @Test
  void shouldCalculateIsEmpty() {
    assertThat(empty().isEmpty()).isTrue();
    assertThat(of(1).isEmpty()).isFalse();
  }

  @Test
  void shouldPeekNil() {
    assertThat(empty().peek(t -> {})).isEqualTo(empty());
  }

  @Test
  void shouldPeekNonNilPerformingNoAction() {
    assertThat(of(1).peek(t -> {})).isEqualTo(of(1));
  }

  @Test
  void shouldPeekSingleValuePerformingAnAction() {
    final int[] effect = {0};
    final FunctionalValue<Integer> actual = of(1).peek(i -> effect[0] = i);
    assertThat(actual).isEqualTo(of(1));
    assertThat(effect[0]).isEqualTo(1);
  }

  @Test
  void shouldPeekNonNilPerformingAnAction() {
    final int[] effect = {0};
    final FunctionalValue<Integer> actual = of(1, 2, 3).peek(i -> effect[0] = i);
    assertThat(actual).isEqualTo(of(1, 2, 3)); // traverses all elements in the lazy case
    assertThat(effect[0]).isEqualTo(getPeekNonNilPerformingAnAction());
  }

  @Test
  void shouldConvertToOption() {
    assertThat(empty().toOption()).isSameAs(Option.none());
    assertThat(of(1).toOption()).isEqualTo(Option.of(1));
  }

  @Test
  void shouldConvertToCallResult() {
    assertThat(empty().toCallResult("test")).isEqualTo(CallResult.failure("test"));
    assertThat(empty().toCallResult(() -> "test")).isEqualTo(CallResult.failure("test"));
    assertThat(of(1).toCallResult("test")).isEqualTo(CallResult.success(1));
  }

  @Test
  void shouldConvertToJavaOptional() {
    assertThat(of(1, 2, 3).toJavaOptional()).isEqualTo(Optional.of(1));
  }

  @Test
  void shouldBeAwareOfExistingElement() {
    final FunctionalValue<Integer> value = of(1, 2);
    if (value.isSingleValued()) {
      assertThat(value.exists(i -> i == 1)).isTrue();
    } else {
      assertThat(value.exists(i -> i == 2)).isTrue();
    }
  }

  @Test
  void shouldBeAwareOfNonExistingElement() {
    assertThat(this.<Integer>empty().exists(i -> i == 1)).isFalse();
  }

  @Test
  void shouldBeAwareOfPropertyThatHoldsForAll() {
    assertThat(of(2, 4).forAll(i -> i % 2 == 0)).isTrue();
  }

  @Test
  void shouldBeAwareOfPropertyThatNotHoldsForAll() {
    assertThat(of(1, 2).forAll(i -> i % 2 == 0)).isFalse();
  }

  @Test
  void shouldHaveAReasonableToString() {
    final FunctionalValue<Integer> value = of(1, 2);
    final String actual = value.toString();

    if (value.isSingleValued()) {
      assertThat(actual).contains("1");
    } else {
      assertThat(actual).contains("1", "2");
    }
  }

  @Test
  void shouldRecognizeSameObject() {
    final FunctionalValue<Integer> v = of(1);
    //noinspection EqualsWithItself
    assertThat(v.equals(v)).isTrue();
  }

  @Test
  void shouldRecognizeEqualObjects() {
    final FunctionalValue<Integer> v1 = of(1);
    final FunctionalValue<Integer> v2 = of(1);
    assertThat(v1.equals(v2)).isTrue();
  }

  @Test
  void shouldRecognizeUnequalObjects() {
    final FunctionalValue<Integer> v1 = of(1);
    final FunctionalValue<Integer> v2 = of(2);
    assertThat(v1.equals(v2)).isFalse();
  }
}
