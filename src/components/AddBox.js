import React, { useState } from 'react';

const matchName = '(\\p{L}+ ?)+(?<! )$'; //Unicode letters. Allow space between words but not at the end.
    //TODO: consider using trim on input instead of lookahead regex to handle trailing space

function AddBox({addDispatch}) {
    const initialState = {
        receiver: '',
        weight: '', //number, initially empty
        color: "#8DC891",
        country: 'Sweden'
    };
    const [box, setBox] = useState(initialState);
    function handleInput(event) {
        const { name, value } = event.target;
        setBox({ ...box, [name]: value }); //name matches form field
    }
    function handleSubmit(event){
        event.preventDefault(); //avoid URL change
        addDispatch(box);
    }
    function checkWeight(event){
        const input = event.target;
        if(input.valueAsNumber < 0) {
            input.setCustomValidity('Negative weight is not permitted');
        } else {
            input.setCustomValidity(''); //value is fine - clear error message
        }
    }
    return (
        <form onSubmit={handleSubmit}>
            <label htmlFor="receiver">Name</label>
            <input type="text" id="receiver" name="receiver" required={true}
                placeholder={'Receiver'} pattern={matchName} title={'Full name. Space can be used between names'}
                value={box.receiver} onChange={handleInput}/>

            <label htmlFor="weight">Weight</label>
            <input type="number" name="weight" id="weight" required={true}
                placeholder={'0 kg'} min={0} step={0.001}
                onInput={checkWeight}
                onInvalid={(event) => {
                    const input = event.target;
                    if (input.valueAsNumber < 0) {
                        setBox({...box, weight: 0}); //default to 0
                        setTimeout((input) => input.setCustomValidity(''), 2000, input); //error timeout
                    }
                }}
                value={box.weight} onChange={handleInput}/>

            <label htmlFor="color">Box color</label>
            <input type="color" name="color" id="color" required={true}
                value={box.color} onChange={handleInput}/> {/* TODO: Component*/}

            <label htmlFor="country">Country</label>
            <select name="country" id="country" required={true}
                value={box.country} onChange={handleInput}>
                <option value="Sweden">Sweden</option>
                <option value="China">China</option>
                <option value="Brazil">Brazil</option>
                <option value="Australia">Australia</option>
            </select>

            <input type="submit" value="Save"/>
        </form>
    );
}



export default AddBox;