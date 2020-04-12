import React, { useState } from 'react';
import dispatches from './mock'

function DispatchTable(props) {
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
                {dispatches.map(box => {
                    return (
                        <tr>
                            <td>{box.receiver}</td>
                            <td>{box.weight}</td>
                            <td style={{backgroundColor: box.color}}></td>
                            <td>{box.shippingCost}</td>
                        </tr>
                    );  
                })}
            </tbody>
        </table>
    );
}

export default DispatchTable;