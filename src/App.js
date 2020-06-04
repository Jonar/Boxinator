import React from 'react';

import './App.scss';
import AddBox from './components/AddBox';
import DispatchTable from './components/DispatchTable';
import { Switch, Route, Link } from 'react-router-dom';

function App() {
  return (
    <div className="App">
      <nav>
        <Link to="/">Home</Link>{' | '}
        <Link to="/addbox">Add box</Link>{' | '}
        <Link to="/listboxes">List dispatches</Link>
      </nav>
      <Switch>
        <Route exact path="/">
          <AddBox />
          <DispatchTable />
        </Route>
        <Route exact path="/addbox">
          <AddBox />
        </Route>
        <Route exact path="/listboxes">
          <DispatchTable />
        </Route>
      </Switch>
    </div>
  );
}

export default App;
