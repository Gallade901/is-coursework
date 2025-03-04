import {useParams} from "react-router-dom";
import React, {useEffect, useState} from "react";
import Header from "./Header";

const StatPlayer = () => {
    const { id } = useParams();
    const [statPlayer, setStatPlayer] = useState();

    useEffect(() => {
        const getStatPlayer = async () => {
            try {
                const response = await fetch(`${process.env.REACT_APP_BASE_URL}/main/getStatPlayer/${id}`, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                    }
                });
                const data = await response.json();
                setStatPlayer(data);
            } catch (error) {
                console.error("Ошибка при получении матчей", error);
            }
        }
        if (id) {
            getStatPlayer();
        }
    }, [id])

    return (
        <div>
            <Header/>
            <h1>Stat player in season</h1>
            <table className="stats-table">
                <thead>
                    <tr>
                        <th>Name</th>
                        <th>AST</th>
                        <th>BLK</th>
                        <th>FG%</th>
                        <th>FT%</th>
                        <th>MIN</th>
                        <th>PTS</th>
                        <th>REB</th>
                        <th>STL</th>
                        <th>3P%</th>
                        <th>TOV</th>
                    </tr>
                </thead>
                <tbody>
                <tr>
                    <td>{statPlayer?.player?.name}</td>
                    <td>{statPlayer?.ast}</td>
                    <td>{statPlayer?.blk}</td>
                    <td>{(statPlayer?.fg * 100).toFixed(1)}%</td>
                    <td>{(statPlayer?.ft * 100).toFixed(1)}%</td>
                    <td>{statPlayer?.min}</td>
                    <td>{statPlayer?.pts}</td>
                    <td>{statPlayer?.reb}</td>
                    <td>{statPlayer?.stl}</td>
                    <td>{(statPlayer?.threePoints * 100).toFixed(1)}%</td>
                    <td>{statPlayer?.tov}</td>
                </tr>
                </tbody>
            </table>
        </div>
    );

}


export default StatPlayer;