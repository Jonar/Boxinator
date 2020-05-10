import React from 'react';

import './App.css';
import AddBox from './AddBox';
import DispatchTable from './DispatchTable';

function App() {
  async function addDispatch(box) {
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
      <AddBox addDispatch={addDispatch}/>
      <DispatchTable />
    </div>
  );
}

export default App;
