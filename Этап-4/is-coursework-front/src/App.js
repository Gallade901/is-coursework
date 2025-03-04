import { createBrowserRouter, createRoutesFromElements, Route, RouterProvider } from "react-router-dom";
import './App.css';
import Matches from "./components/Matches";
import StatPlayer from "./components/StatPlayer";
import StatMatch from "./components/StatMatch";
import StatTeam from "./components/StatTeam";

const router = createBrowserRouter(
    createRoutesFromElements(
        <Route path="/">
            <Route index element={<Matches />} />
            <Route path="statMatches/:id" element={<StatMatch/>} />
            <Route path="statPlayer/:id" element={<StatPlayer/>} />
            <Route path="statTeam/:id" element={<StatTeam/>} />
        </Route>
    )
);

const App = () => {
    return (
        <RouterProvider router={router}/>
    );
};

export default App;