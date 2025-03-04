import {useParams} from "react-router-dom";
import React, {useEffect, useState} from "react";
import Header from "./Header";

const StatTeam = () => {
    const { id } = useParams();
    const [statTeam, setStatTeam] = useState();

    useEffect(() => {
        const getStatTeam = async () => {
            try {
                const response = await fetch(`${process.env.REACT_APP_BASE_URL}/main/getStatTeam/${id}`, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                    }
                });
                const data = await response.json();
                setStatTeam(data);
            } catch (error) {
                console.error("Ошибка при получении матчей", error);
            }
        }
        if (id) {
            getStatTeam();
        }
    }, [id])

    return (
        <div>
            <Header/>
            <h1>Stat team in season</h1>
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
                    <td>{statTeam?.teamName}</td>
                    <td>{statTeam?.statTeamSeason?.ast}</td>
                    <td>{statTeam?.statTeamSeason?.blk}</td>
                    <td>{(statTeam?.statTeamSeason?.fg * 100).toFixed(1)}%</td>
                    <td>{(statTeam?.statTeamSeason?.ft * 100).toFixed(1)}%</td>
                    <td>{statTeam?.statTeamSeason?.win}</td>
                    <td>{statTeam?.statTeamSeason?.pts}</td>
                    <td>{statTeam?.statTeamSeason?.reb}</td>
                    <td>{statTeam?.statTeamSeason?.stl}</td>
                    <td>{(statTeam?.statTeamSeason?.threePoints * 100).toFixed(1)}%</td>
                    <td>{statTeam?.statTeamSeason?.tov}</td>
                </tr>
                </tbody>
            </table>
        </div>
    );

}

export default StatTeam;