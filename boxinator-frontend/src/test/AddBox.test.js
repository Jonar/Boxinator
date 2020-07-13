import React from 'react';
import each from 'jest-each';
import { render, fireEvent, act } from './test-util';
import AddBox from '../components/AddBox';

describe('Fill fields', () => {
    each([
        [/Name/i, "Test Testsson"],
        [/Weight/i, "1"]
    ]).test("Fill '%s' input", (fieldLabel, value) => {
        const { getByLabelText } = render(<AddBox />);
        const input = getByLabelText(fieldLabel);
        fireEvent.change(input, { target: { value: value } });
        expect(input.value).toBe(value);
    })

    test('Pick colour', () => {
        const { getByLabelText, getByDisplayValue } = render(<AddBox />);
        const colourField = getByLabelText(/Box colour/i);
        fireEvent.focus(colourField); //opens color picker
        const colourPickerHexField = getByDisplayValue(/#[0-9A-F]{6}/i);
        fireEvent.change(colourPickerHexField, { target: { value: "#FF0000" } });
        expect(colourField.value).toBe("255, 0, 0");
    })

    test('Select country', () => {
        const { getByLabelText } = render(<AddBox />);

        const countrySelect = getByLabelText(/Country/i);
        expect(countrySelect.value).toBe('Sweden');

        fireEvent.change(countrySelect, { target: { value: "China" } });
        expect(countrySelect.value).toBe("China");
    })
})

describe('Verify input validation', () => {

    //Investigate: Test works but prints warning about unmounted component.
    xtest("Error on submit when required fields are blank", async() => {
        const promise = Promise.resolve();
        const form = render(<AddBox />);

        const save = form.getByRole('button', {name: /Submit/i})
        fireEvent.click(save);

        const name = form.getByLabelText(/Name/i);
        expect(name).toBeInvalid();
        await act(()=>promise); //TODO: Try to find an element to wait for instead
    })

    //TODO improvement: More tests covering the input validation. Important; should be the next tests added.
})