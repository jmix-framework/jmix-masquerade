package com.haulmont.masquerade.components.impl.fresh;

import com.codeborne.selenide.SelenideElement;
import com.haulmont.masquerade.components.TextArea;
import org.openqa.selenium.By;

import static com.codeborne.selenide.Condition.enabled;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.haulmont.masquerade.Conditions.editable;

public class TextAreaImpl implements TextArea {
    private final SelenideElement impl;

    public TextAreaImpl(By by) {
        this.impl = $(by);
    }

    @Override
    public void setValue(String value) {
        impl.shouldBe(visible)
                .shouldBe(enabled)
                .shouldBe(editable)
                .setValue(value);
    }

    @Override
    public String getValue() {
        return impl.shouldBe(visible).getValue();
    }

    @Override
    public boolean isEnabled() {
        return is(enabled);
    }

    @Override
    public boolean isEditable() {
        return is(editable);
    }

    @Override
    public SelenideElement delegate() {
        return impl;
    }
}