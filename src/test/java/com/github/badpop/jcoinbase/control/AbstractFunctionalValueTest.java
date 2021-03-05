package com.github.badpop.jcoinbase.control;

import io.vavr.control.Option;
import org.assertj.core.api.*;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public abstract class AbstractFunctionalValueTest {

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
  public void shouldGetEmpty() {
    assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(() -> empty().get());
  }

  @Test
  public void shouldGetNonEmpty() {
    assertThat(of(1).get()).isEqualTo(1);
  }

  @Test
  public void shouldCalculateGetOrElseWithNull() {
    assertThat(this.<Integer>empty().getOrElse((Integer) null)).isEqualTo(null);
    assertThat(of(1).getOrElse((Integer) null)).isEqualTo(1);
  }

  @Test
  public void shouldCalculateGetOrElseWithNonNull() {
    assertThat(empty().getOrElse(1)).isEqualTo(1);
    assertThat(of(1).getOrElse(2)).isEqualTo(1);
  }

  @Test
  public void shouldThrowOnGetOrElseWithNullSupplier() {
    final Supplier<?> supplier = null;
    assertThatExceptionOfType(NullPointerException.class)
        .isThrownBy(() -> empty().getOrElse(supplier));
  }

  @Test
  public void shouldCalculateGetOrElseWithSupplier() {
    assertThat(empty().getOrElse(() -> 1)).isEqualTo(1);
    assertThat(of(1).getOrElse(() -> 2)).isEqualTo(1);
  }

  @Test
  public void shouldThrowOnGetOrElseThrowIfEmpty() {
    assertThatExceptionOfType(ArithmeticException.class)
        .isThrownBy(() -> empty().getOrElseThrow(ArithmeticException::new));
  }

  @Test
  public void shouldNotThrowOnGetOrElseThrowIfNonEmpty() {
    assertThat(of(1).getOrElseThrow(ArithmeticException::new)).isEqualTo(1);
  }

  @Test
  public void shouldReturnUnderlyingValueWhenCallingGetOrElseTryOnNonEmptyValue() {
    assertThat(of(1).getOrElseTry(() -> 2)).isEqualTo(1);
  }

  @Test
  public void shouldReturnAlternateValueWhenCallingGetOrElseTryOnEmptyValue() {
    assertThat(empty().getOrElseTry(() -> 2)).isEqualTo(2);
  }

  @Test
  public void shouldThrowWhenCallingGetOrElseTryOnEmptyValueAndTryIsAFailure() {
    assertThatExceptionOfType(Error.class)
        .isThrownBy(
            () ->
                empty()
                    .getOrElseTry(
                        () -> {
                          throw new Error();
                        }));
  }

  @Test
  public void shouldReturnNullWhenGetOrNullOfEmpty() {
    assertThat(empty().getOrNull()).isEqualTo(null);
  }

  @Test
  public void shouldReturnValueWhenGetOrNullOfNonEmpty() {
    assertThat(of(1).getOrNull()).isEqualTo(1);
  }

  @Test
  public void shouldPerformsActionOnEachElement() {
    final int[] consumer = new int[1];
    final FunctionalValue<Integer> value = of(1, 2, 3);
    value.forEach(i -> consumer[0] += i);
    assertThat(consumer[0]).isEqualTo(value.isSingleValued() ? 1 : 6);
  }

  @Test
  public void shouldCalculateIsEmpty() {
    assertThat(empty().isEmpty()).isTrue();
    assertThat(of(1).isEmpty()).isFalse();
  }

  @Test
  public void shouldPeekNil() {
    assertThat(empty().peek(t -> {})).isEqualTo(empty());
  }

  @Test
  public void shouldPeekNonNilPerformingNoAction() {
    assertThat(of(1).peek(t -> {})).isEqualTo(of(1));
  }

  @Test
  public void shouldPeekSingleValuePerformingAnAction() {
    final int[] effect = {0};
    final FunctionalValue<Integer> actual = of(1).peek(i -> effect[0] = i);
    assertThat(actual).isEqualTo(of(1));
    assertThat(effect[0]).isEqualTo(1);
  }

  @Test
  public void shouldPeekNonNilPerformingAnAction() {
    final int[] effect = {0};
    final FunctionalValue<Integer> actual = of(1, 2, 3).peek(i -> effect[0] = i);
    assertThat(actual).isEqualTo(of(1, 2, 3)); // traverses all elements in the lazy case
    assertThat(effect[0]).isEqualTo(getPeekNonNilPerformingAnAction());
  }

  @Test
  public void shouldConvertToOption() {
    assertThat(empty().toOption()).isSameAs(Option.none());
    assertThat(of(1).toOption()).isEqualTo(Option.of(1));
  }

  @Test
  public void shouldConvertToCallResult() {
    assertThat(empty().toCallResult("test")).isEqualTo(CallResult.failure("test"));
    assertThat(empty().toCallResult(() -> "test")).isEqualTo(CallResult.failure("test"));
    assertThat(of(1).toCallResult("test")).isEqualTo(CallResult.success(1));
  }

  @Test
  public void shouldConvertToJavaOptional() {
    assertThat(of(1, 2, 3).toJavaOptional()).isEqualTo(Optional.of(1));
  }

  @Test
  public void shouldBeAwareOfExistingElement() {
    final FunctionalValue<Integer> value = of(1, 2);
    if (value.isSingleValued()) {
      assertThat(value.exists(i -> i == 1)).isTrue();
    } else {
      assertThat(value.exists(i -> i == 2)).isTrue();
    }
  }

  @Test
  public void shouldBeAwareOfNonExistingElement() {
    assertThat(this.<Integer>empty().exists(i -> i == 1)).isFalse();
  }

  @Test
  public void shouldBeAwareOfPropertyThatHoldsForAll() {
    assertThat(of(2, 4).forAll(i -> i % 2 == 0)).isTrue();
  }

  @Test
  public void shouldBeAwareOfPropertyThatNotHoldsForAll() {
    assertThat(of(1, 2).forAll(i -> i % 2 == 0)).isFalse();
  }

  @Test
  public void shouldHaveAReasonableToString() {
    final FunctionalValue<Integer> value = of(1, 2);
    final String actual = value.toString();

    if (value.isSingleValued()) {
      assertThat(actual).contains("1");
    } else {
      assertThat(actual).contains("1", "2");
    }
  }

  @Test
  public void shouldRecognizeSameObject() {
    final FunctionalValue<Integer> v = of(1);
    //noinspection EqualsWithItself
    assertThat(v.equals(v)).isTrue();
  }

  @Test
  public void shouldRecognizeEqualObjects() {
    final FunctionalValue<Integer> v1 = of(1);
    final FunctionalValue<Integer> v2 = of(1);
    assertThat(v1.equals(v2)).isTrue();
  }

  @Test
  public void shouldRecognizeUnequalObjects() {
    final FunctionalValue<Integer> v1 = of(1);
    final FunctionalValue<Integer> v2 = of(2);
    assertThat(v1.equals(v2)).isFalse();
  }
}
