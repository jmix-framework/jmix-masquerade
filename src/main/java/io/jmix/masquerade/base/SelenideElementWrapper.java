/*
 * Copyright (c) 2008-2020 Haulmont.
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
 */

package io.jmix.masquerade.base;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

@SuppressWarnings("unchecked")
public interface SelenideElementWrapper<T> {
    SelenideElement getDelegate();

    default boolean exists() {
        return getDelegate().exists();
    }

    default boolean is(Condition condition) {
        return getDelegate().is(condition);
    }

    default boolean has(Condition condition) {
        return getDelegate().has(condition);
    }

    default T should(Condition... condition) {
        getDelegate().should(condition);
        return (T) this;
    }

    default T should(Condition condition, Duration duration) {
        getDelegate().should(condition, duration);
        return (T) this;
    }

    default T shouldHave(Condition... condition) {
        getDelegate().shouldHave(condition);
        return (T) this;
    }

    default T shouldHave(Condition condition, Duration duration) {
        getDelegate().shouldHave(condition, duration);
        return (T) this;
    }

    default T shouldBe(Condition... condition) {
        getDelegate().shouldBe(condition);
        return (T) this;
    }

    default T shouldBe(Condition condition, Duration duration) {
        getDelegate().shouldBe(condition, duration);
        return (T) this;
    }

    default T shouldNot(Condition... condition) {
        getDelegate().shouldNot(condition);
        return (T) this;
    }

    default T shouldNot(Condition condition, Duration duration) {
        getDelegate().shouldNot(condition, duration);
        return (T) this;
    }

    default T shouldNotHave(Condition... condition) {
        getDelegate().shouldNotHave(condition);
        return (T) this;
    }

    default T shouldNotHave(Condition condition, Duration duration) {
        getDelegate().shouldNotHave(condition, duration);
        return (T) this;
    }

    default T shouldNotBe(Condition... condition) {
        getDelegate().shouldNotBe(condition);
        return (T) this;
    }

    default T shouldNotBe(Condition condition, Duration duration) {
        getDelegate().shouldNotBe(condition, duration);
        return (T) this;
    }

    default String getAttribute(String name) {
        return getDelegate().getAttribute(name);
    }

    default String getCssValue(String propertyName) {
        return getDelegate().getCssValue(propertyName);
    }
}