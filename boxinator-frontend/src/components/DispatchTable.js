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
                ? dispatchesList.map((dispatch, index) => (
                    <tr key={index}>
                        <td data-testid={'receiver'+index}>
                            {dispatch.receiver}</td>
                        <td data-testid={'weight'+index}>
                            {dispatch.weight}</td>
                        <td style={{ backgroundColor: dispatch.color }} data-testid={'colour'+index}></td>
                        <td data-testid={'shippingCost'+index}>
                            {dispatch.shippingCost}</td>
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

    return (<>
        <h2>Dispatches</h2>
        <table>
            <thead>
                <tr>
                    <th>Receiver</th>
                    <th>Weight</th>
                    <th>Box colour</th>
                    <th>Shipping cost</th>
                </tr>
            </thead>
            <tbody>
                {renderDispatchesList()}
            </tbody>
            <tfoot>
                <tr>
                    <th>Totals</th>
                    <th data-testid="totalWeight">{totalWeight}</th>
                    <th></th>
                    <th data-testid="totalCost">{totalCost}</th>
                </tr>
            </tfoot>
        </table>
    </>);
}

export default DispatchTable;