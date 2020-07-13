import React from 'react'
import { configureStore } from '@reduxjs/toolkit'
import { render as rtlRender } from '@testing-library/react'
import { Provider } from 'react-redux'
import rootReducer from '../slices/root';

/**
 * Override RTL render function to include Redux store
 * Use default to mirror App or configure for specific test cases
 * */
function render(
  ui,
  {
    initialState = {},
    store = configureStore({reducer: rootReducer, preloadedState: initialState}),
    ...renderOptions
  } = {}
) {
  function Wrapper({ children }) {
    return <Provider store={store}>{children}</Provider>
  }
  return rtlRender(ui, { wrapper: Wrapper, ...renderOptions })
}

// re-export everything
export * from '@testing-library/react'

// override render method
export { render }