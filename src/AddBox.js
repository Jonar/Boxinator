import React, { useState } from 'react';

function AddBox({addDispatch}) {
    const initialState = {
        receiver: 'Receiver name',
        weight: 0,
        color: "#8DC891",
        country: 'Sweden'
    };
    const [box, setBox] = useState(initialState);
    function handleInput(event) {
        const { name, value } = event.target;
        setBox({ ...box, [name]: value });
    }
    function handleSubmit(event){
        addDispatch(box);
        event.preventDefault();
    }
    return (
        <form onSubmit={handleSubmit}>
            <label htmlFor="receiver">Name</label>
            <input type="text" id="receiver" name="receiver" value={box.receiver} onChange={handleInput}/>
            <label htmlFor="weight">Weight</label>
            <input type="number" name="weight" id="weight" value={box.weight} onChange={handleInput}/>
            <label htmlFor="color">Box color</label>
            <input type="color" name="color" id="color" value={box.color} onChange={handleInput}/> {/* TODO: Component*/}
            <label htmlFor="country">Country</label>
            <select name="country" id="country" value={box.country} onChange={handleInput}>
                <option value="Sweden">Sweden</option>
                <option value="China">China</option>
                <option value="Brazil">Brazil</option>
                <option value="Australia">Australia</option>
            </select>
            <input type="submit"/>
        </form>
    );
}



export default AddBox;