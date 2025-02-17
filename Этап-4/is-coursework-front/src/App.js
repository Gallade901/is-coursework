import { createBrowserRouter, createRoutesFromElements, Route, RouterProvider } from "react-router-dom";
import './App.css';
import Matches from "./components/Matches";
import StatPlayer from "./components/StatPlayer";

const router = createBrowserRouter(
    createRoutesFromElements(
        <Route path="/">
            <Route index element={<Matches />} />
            <Route path="statPlayer" element={<StatPlayer />} />
        </Route>
    )
);

const App = () => {
    return (
        <RouterProvider router={router}/>
    );
};

export default App;