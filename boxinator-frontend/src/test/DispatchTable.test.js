import React from 'react';
import { render } from './render-util';
import DispatchTable from '../components/DispatchTable';

test('empty table displays "No dispatches"', () => {
    const { getByText } = render(<DispatchTable/>);
    const tableEntry = getByText(/No dispatches/i);
    expect(tableEntry).toBeInTheDocument();
})

test('empty table displays zero totals', () => {
    const { getByText, getByTestId } = render(<DispatchTable/>);
    expect(getByText(/Totals/i)).toBeInTheDocument();
    expect(getByTestId('totalWeight').textContent).toBe('0');
    expect(getByTestId('totalCost').textContent).toBe('0');
})

describe('Table with entries', () => {

    const reduxInitialState = {
        dispatches: {
            dispatchesList: [
                { receiver: "Jon", weight: 1, color: "#8DC891", shippingCost: 1.3 },
                { receiver: "习近平", weight: 0.5, color: "#ff0c0c", shippingCost: 2 }
            ],
            totalWeight: 1.5,
            totalCost: 3.3
        }
    }

    test('Table with entries display rows', () => {
        const { getByTestId } = render(<DispatchTable />, {
            initialState: reduxInitialState
        });
        expect(getByTestId('receiver0').textContent).toBe("Jon");
        expect(getByTestId('weight0').textContent).toBe("1");
        expect(getByTestId('colour0').style.backgroundColor).toBe("rgb(141, 200, 145)");
        expect(getByTestId('shippingCost0').textContent).toBe("1.3");

        expect(getByTestId('receiver1').textContent).toBe("习近平");
        expect(getByTestId('weight1').textContent).toBe("0.5");
        expect(getByTestId('colour1').style.backgroundColor).toBe("rgb(255, 12, 12)");
        expect(getByTestId('shippingCost1').textContent).toBe("2");
    })

    test('Table with entries display totals', () => {
        const { getByTestId } = render(<DispatchTable />, {
            initialState: reduxInitialState
        });
        const expectedTotalWeight = reduxInitialState.dispatches.totalWeight.toString();
        expect(getByTestId('totalWeight').textContent).toBe(expectedTotalWeight);
        const expectedTotalCost = reduxInitialState.dispatches.totalCost.toString();
        expect(getByTestId('totalCost').textContent).toBe(expectedTotalCost);
    })
})