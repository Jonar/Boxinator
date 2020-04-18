import React, {useState} from 'react';
import './App.css';
import AddBox from './AddBox';
import DispatchTable from './DispatchTable';
import mockDispatches from './mock'

function App() {
  const [dispatches, setDispatches] = useState(mockDispatches);
  function addDispatch(box) {
    let shippingCost;
    switch (box.country) {
      case 'Sweden':
        shippingCost = 1.3 * box.weight;
        break;
      case 'China':
        shippingCost = 4 * box.weight;
        break;
      case 'Brazil':
        shippingCost = 8.6 * box.weight;
        break;
      case 'Australia':
        shippingCost = 7.2 * box.weight;
        break;
    }
    const dispatch = {
      ...box, shippingCost: shippingCost //TODO: remove country prop
    };
    setDispatches([...dispatches, dispatch]);
  }
  return (
    <div className="App">
      <AddBox addDispatch={addDispatch}/>
      <DispatchTable dispatches={dispatches} />
    </div>
  );
}

export default App;
