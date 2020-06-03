import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { dispatchesSelector, fetchDispatches } from '../slices/dispatches';

function DispatchTable() {
    const dispatch = useDispatch();
    const {dispatchesList, totalWeight, totalCost} = useSelector(dispatchesSelector);

    useEffect(() => {
        dispatch(fetchDispatches())
    }, [dispatch]);

    function renderDispatchesList() {
        if(dispatchesList) {
            return (dispatchesList.length > 0
                ? dispatchesList.map((box, index) => ( //TODO: don't use index as key
                    <tr key={index}>
                        <td>{box.receiver}</td>
                        <td>{box.weight}</td>
                        <td style={{ backgroundColor: box.color }}></td>
                        <td>{box.shippingCost}</td>
                    </tr>
                )) : (
                    <tr>
                        <td colSpan={4}>No dispatches</td>
                    </tr>
                ));
        } else {
            return <tr><td colSpan={4}>Null dispatches</td></tr>;
        }
    }

    return (
        <table>
            <thead>
                <tr>
                    <th>Receiver</th>
                    <th>Weight</th>
                    <th>Box color</th>
                    <th>Shipping cost</th>
                </tr>
            </thead>
            <tbody>
                {renderDispatchesList()}
            </tbody>
            <tfoot>
                <tr>
                    <th>Totals</th>
                    <th>{totalWeight}</th>
                    <th></th>
                    <th>{totalCost}</th>
                </tr>
            </tfoot>
        </table>
    );
}

export default DispatchTable;