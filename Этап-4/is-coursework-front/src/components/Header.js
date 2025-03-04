import React from "react";
import {useNavigate} from "react-router-dom";

const Header = () => {
    const navigate = useNavigate();

    return (
        <header className="header">
            <h1 onClick={() => navigate('/')}>
                NBA REGULAR SEASON 2024-2025
            </h1>
        </header>
    );
};

export default Header;
