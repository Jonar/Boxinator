import { createSlice } from '@reduxjs/toolkit'

export const initialState = {
    dispatchesList: [],
};

const dispatchesSlice = createSlice({
    name: 'dispatches',
    initialState,
    reducers: {
        getDispatchesSuccess: (state, {payload}) => {
            state.dispatchesList = payload;
        },
        getDispatchesFailure: state => {
            state.dispatchesList = [];
        }
    }
});

export const {getDispatchesSuccess, getDispatchesFailure} = dispatchesSlice.actions;

export const dispatchesSelector = state => state.dispatches;

export default dispatchesSlice.reducer;

export function fetchDispatches() {
    return async dispatch => {
        try {
            const response = await fetch('http://localhost:4567/dispatches');
            const data = await response.json();

            dispatch(getDispatchesSuccess(data));
        } catch (error) {
            dispatch(getDispatchesFailure());
            console.log(error);
        }
    }
}