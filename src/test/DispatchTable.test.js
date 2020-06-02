import React from 'react';
import { render } from './render-util';
import DispatchTable from '../components/DispatchTable';

test('empty table displays "No dispatches"', () => {
    const { getByText } = render(<DispatchTable/>);
    const tableEntry = getByText(/No dispatches/i);
    expect(tableEntry).toBeInTheDocument();
})