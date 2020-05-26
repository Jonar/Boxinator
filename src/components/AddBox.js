import React, { useState } from 'react';
import { ChromePicker } from 'react-color'

const matchName = '(\\p{L}+ ?)+(?<! )$'; //Unicode letters. Allow space between words but not at the end.
    //TODO: consider using trim on input instead of lookahead regex to handle trailing space

function AddBox({addDispatch}) {
    const initialState = {
        receiver: '',
        weight: '', //number, initially empty
        color: '',
        country: 'Sweden'
    };
    const initialColour = {
          hex: '#333',
          rgb: {
            r: 51,
            g: 51,
            b: 51,
            a: 1,
          },
          hsl: {
            h: 0,
            s: 0,
            l: .20,
            a: 1,
          },
        }
    const [colour, setColour] = useState();
    const handleColourChange = colour => {
        setColour(colour);
        setBox({...box, color: colour.hex});
    }
    const rgbText = () => colour ? `${colour.rgb.r}, ${colour.rgb.g}, ${colour.rgb.b}`: "";

    const [displayColorPicker, setDisplayColorPicker] = useState(false);
    const toggleColorPicker = () => setDisplayColorPicker(!displayColorPicker);
    const closeColorPicker = () => setDisplayColorPicker(false);
    const popover = {
        position: 'absolute',
        zIndex: '2',
    }
    const cover = { //TODO: Investigate
        position: 'fixed',
        top: '0px',
        right: '0px',
        bottom: '0px',
        left: '0px',
    }

    const [box, setBox] = useState(initialState);
    function handleInput(event) {
        const { name, value } = event.target;
        setBox({ ...box, [name]: value }); //name matches form field
    }
    function handleSubmit(event){
        event.preventDefault(); //avoid URL change
        addDispatch(box);
        setBox(initialState); //TODO: refactor into reset function
        setColour('');
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

            {/* TODO: refactor into weight component */}
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

            {/* TODO: refactor into colour component */}
            <label htmlFor="colour">Box colour</label>
            <input required name="colour" id="colour" type="text"
                placeholder="Click to show colour picker"
                readOnly value={rgbText(colour)}
                onFocus={toggleColorPicker}/>
            {displayColorPicker ?
                <div style={popover}>
                    <div style={cover} onClick={closeColorPicker} />
                    <ChromePicker color={colour}
                        onChange={handleColourChange}
                        onChangeComplete={handleColourChange}
                        disableAlpha={true} />
                </div>
                : null}


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