import React from 'react';

import './App.css';
import AddBox from './components/AddBox';
import DispatchTable from './components/DispatchTable';
import { Switch, Route, Link } from 'react-router-dom';

function App() {
  async function addDispatch(box) { //TODO: Move to AddBox or to redux action
    try {
      const response = await fetch('http://localhost:4567/box', {
        method: 'POST',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(box),
      });
      const data = await response.json();
      console.log('POST /box response:', data);
    } catch (error) {
      console.log(error);
    }
  }
  return (
    <div className="App">
      <nav>
        <Link to="/">Home</Link> |
        <Link to="/addbox"> Add box</Link> |
        <Link to="/listboxes"> List dispatches</Link>
      </nav>
      <Switch>
        <Route exact path="/">
          <AddBox addDispatch={addDispatch} />
          <DispatchTable />
        </Route>
        <Route exact path="/addbox">
          <AddBox addDispatch={addDispatch} />
        </Route>
        <Route exact path="/listboxes">
          <DispatchTable />
        </Route>
      </Switch>
    </div>
  );
}

export default App;
