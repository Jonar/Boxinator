import React, { useState } from 'react';
import { ChromePicker } from 'react-color'
import { fetchDispatches } from '../slices/dispatches';
import { useDispatch } from 'react-redux';

const matchName = '(\\p{L}+ ?)+(?<! )$'; //Unicode letters. Allow space between words but not at the end.
    //Consider using trim on input instead of lookahead regex to handle trailing space

function AddBox() {
    const initialState = {
        receiver: '',
        weight: '',
        color: '',
        country: 'Sweden'
    };
    const [box, setBox] = useState(initialState);
    const [colour, setColour] = useState();
    const dispatch = useDispatch();

    function handleInput(event) {
        const { name, value } = event.target;
        setBox({ ...box, [name]: value }); //name is form field name attribute
    }
    function handleSubmit(event){
        event.preventDefault(); //avoid URL change
        addDispatch(box);
        resetForm();
    }
    function resetForm() {
        setBox(initialState);
        setColour('');
    }

    async function addDispatch(box) {
        try {
          await fetch('http://localhost:4567/box', {
            method: 'POST',
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json'
            },
            body: JSON.stringify(box),
          });
          dispatch(fetchDispatches()); //read back dispatches, re-renders connected components as necessary
        } catch (error) {
          console.log(error);
        }
    }

    return <>
        <h2>Add box</h2>
        <form onSubmit={handleSubmit}>

            <label htmlFor="receiver">Name</label>
            <input type="text" id="receiver" name="receiver" required={true}
                placeholder={'Receiver'} pattern={matchName} maxLength={70}
                title={'Full name. Space can be used between names'}
                value={box.receiver} onChange={handleInput}/>

            <WeightInput box={box} setBox={setBox} handleInput={handleInput} />

            <ColourInput colour={colour} setColour={setColour} box={box} setBox={setBox} />

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
    </>

}

function WeightInput({box, setBox, handleInput}) {
    function checkWeight(event){
        const input = event.target;
        if(input.valueAsNumber < 0) {
            input.setCustomValidity('Negative weight is not permitted');
        } else {
            input.setCustomValidity(''); //value is fine - clear error message
        }
    }
    const resetAndClearOnTimeout = (event) => {
        const input = event.target;
        if (input.valueAsNumber < 0) {
            setBox({ ...box, weight: 0 }); //default to 0
            setTimeout((input) => input.setCustomValidity(''), 2000, input); //error timeout
        }
    }
    return <>
        <label htmlFor="weight">Weight</label>
        <input type="number" name="weight" id="weight" required={true}
            placeholder={'0 kg'} min={0} max={100} step={0.001}
            onInput={checkWeight}
            onInvalid={resetAndClearOnTimeout} //triggers on submit when invalid
            value={box.weight} onChange={handleInput} />
    </>
}

function ColourInput({colour, setColour, box, setBox}) {
    const handleColourChange = colour => {
        setColour(colour);
        setBox({...box, color: colour.hex});
    }
    const rgbText = () => colour ? `${colour.rgb.r}, ${colour.rgb.g}, ${colour.rgb.b}`: "";
    const [displayColourPicker, setDisplayColourPicker] = useState(false);
    const toggleColourPicker = () => setDisplayColourPicker(!displayColourPicker);
    const closeColourPicker = () => setDisplayColourPicker(false);
    const popover = {
        position: 'absolute',
        zIndex: '2',
    }
    const cover = {
        position: 'fixed',
        top: '0px',
        right: '0px',
        bottom: '0px',
        left: '0px',
    }
    return <>
        <label htmlFor="colour">Box colour</label>
        <input required name="colour" id="colour" type="text"
            placeholder="Click to show colour picker"
            readOnly value={rgbText(colour)}
            onFocus={toggleColourPicker} />
        {displayColourPicker ?
            <div style={popover}>
                <div style={cover} onClick={closeColourPicker} />
                <ChromePicker color={colour}
                    onChange={handleColourChange}
                    onChangeComplete={handleColourChange}
                    disableAlpha={true} />
            </div>
            : null}
    </>
}

export default AddBox;