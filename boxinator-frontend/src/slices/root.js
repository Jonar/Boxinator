import {combineReducers} from 'redux';

import dispatchesReducer from './dispatches';

const rootReducer = combineReducers({
    dispatches: dispatchesReducer,
});

export default rootReducer;