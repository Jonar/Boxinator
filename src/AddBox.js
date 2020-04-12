import React, { useState } from 'react';

function AddBox(props) {
    const [box, setBox] = useState([]);
    return (
        <form onSubmit={addBox}>
            <label for="receiver">Name</label>
            <input type="text" id="receiver" name="name"></input>
            <label for="weight">Weight</label>
            <input type="number" name="weight" id="weight"/>
            <label for="color">Box color</label>
            <input type="color" name="color" id="color" /> {/* TODO: Component*/}
            <label for="country">Country</label>
            <select name="country" id="country">
                <option value="Sweden">Sweden</option>
                <option value="China">China</option>
                <option value="Brazil">Brazil</option>
                <option value="Australia">Australia</option>
            </select>
            <input type="submit"/>
        </form>
    );

    function addBox() {
                
    }
}



export default AddBox;