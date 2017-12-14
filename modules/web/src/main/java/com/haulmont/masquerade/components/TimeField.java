package com.haulmont.masquerade.components;

import com.haulmont.masquerade.Conditions;

/**
 * TimeField component.
 * <br>
 * Supported conditions:
 * <ul>
 *     <li>{@link Conditions#visible}</li>
 *     <li>{@link Conditions#hidden}</li>
 *     <li>{@link Conditions#enabled}</li>
 *     <li>{@link Conditions#disabled}</li>
 *     <li>{@link Conditions#editable}</li>
 *     <li>{@link Conditions#readonly}</li>
 *     <li>{@link Conditions#required}</li>
 *     <li>{@link Conditions#value(String)}</li>
 *     <li>{@link Conditions#valueContains(String)}</li>
 * </ul>
 */
public interface TimeField extends Field<TimeField> {
    String getValue();
    TimeField setValue(String value);
}