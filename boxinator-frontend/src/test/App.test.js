import React from 'react';
import { render } from './test-util';
import App from '../App';
import { MemoryRouter } from 'react-router-dom';

test('renders app navigation', () => {
  const { getByText } = render(
    <MemoryRouter>
      <App />
    </MemoryRouter>);
  const linkElement = getByText(/Home/i);
  expect(linkElement).toBeInTheDocument();
});

/**
 * TODO test coverage improvements:
 * [ ] Test that saved boxes appear in the DispatchTable (Using the Home route, where both views are available)
 * [ ] Verify routing by navigating between pages
 * */
