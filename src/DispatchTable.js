import React from 'react';

function DispatchTable({dispatches}) {
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
                {dispatches.length > 0
                    ? dispatches.map((box, index) => ( //TODO: don't use index as key
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
                    )}
            </tbody>
        </table>
    );
}

export default DispatchTable;