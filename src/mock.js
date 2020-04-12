//POST /dispatch/
const box1 = {receiver: "Jon", weight: 1, color: "#8DC891", country: "Sweden"};
const box2 = {receiver: "Signe", weight: 2, color: "#b300ff", country: "Sweden"};
const box3 = {receiver: "Shu", weight: 1.5, color: "#ff0000", country: "China"};
const box4 = {receiver: "Ricardo", weight: 0.5, color: "#ffd000", country: "Brazil"};
const box5 = {receiver: "Kim", weight: 1, color: "#ffffff", country: "Australia"};

//GET /dispatches/ if pricing logic is in backend
const dispatches = [
    {receiver: "Jon", weight: 1, color: "#8DC891", shippingCost: 1.3},
    {receiver: "Signe", weight: 2, color: "#b300ff", shippingCost: 2.6},
    {receiver: "Shu", weight: 1.5, color: "#ff0000", shippingCost: 6},
    {receiver: "Ricardo", weight: 0.5, color: "#ffd000", shippingCost: 4.3},
    {receiver: "Kim", weight: 1, color: "#eeeeee", shippingCost: 7.2}
]
//Let BE calculate total price as well?

export default dispatches;

//GET /dispatches/ if pricing logic is in frontend
// [
//     {receiver: "Jon", weight: 1, color: "#8DC891", country: "Sweden"},
//     {receiver: "Signe", weight: 2, color: "#b300ff", country: "Sweden"},
//     {receiver: "Shu", weight: 1.5, color: "#ff0000", country: "China"},
//     {receiver: "Ricardo", weight: 0.5, color: "#ffd000", country: "Brazil"},
//     {receiver: "Kim", weight: 1, color: "#ffffff", country: "Australia"}
// ]