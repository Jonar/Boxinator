import React from 'react';
import { render } from './test/render-util';
import App from './App';
import { MemoryRouter } from 'react-router-dom';

test('renders app navigation', () => {
  const { getByText } = render(
    <MemoryRouter>
      <App />
    </MemoryRouter>);
  const linkElement = getByText(/Home/i);
  expect(linkElement).toBeInTheDocument();
});
