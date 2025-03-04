import React, {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router-dom";
import Header from "./Header";


const StatMatch = () => {
    const [statMatches, setStatMatches] = useState(null);
    const [statPlayersMatchStat, setStatPlayersMatchStat] = useState([]);
    const [statTeamsMatch, setStatTeamsMatch] = useState(null);
    const [loading, setLoading] = useState(true);
    const [check, setCheck] = useState();
    const { id } = useParams();
    const navigate = useNavigate();

    useEffect(() => {
        const checkMatch  = async () => {
            try {
                setLoading(true);
                const response = await fetch(`${process.env.REACT_APP_BASE_URL}/main/checkMatch/${id}`, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                    }
                });
                const data = await response.json();
                setCheck(data);
                return data;
            } catch (error) {
                console.error("Ошибка при получении матчей", error);
            }
        }
        checkMatch()
            .then((result) => {
                if (result) {
                    const getStatMatches = async () => {
                        try {
                            setLoading(true);
                            const response = await fetch(`${process.env.REACT_APP_BASE_URL}/main/getStatMatches/${id}`, {
                                method: "GET",
                                headers: {
                                    "Content-Type": "application/json",
                                }
                            });
                            const data = await response.json();
                            setStatMatches(data);
                        } catch (error) {
                            console.error("Ошибка при получении матчей", error);
                        }
                    }
                    const getStatTeamsMatch = async () => {
                        try {
                            const response = await fetch(
                                `${process.env.REACT_APP_BASE_URL}/main/getStatTeamsMatch`,
                                {
                                    method: "POST",
                                    headers: {
                                        "Content-Type": "application/json",
                                    },
                                    body: JSON.stringify(id),
                                }
                            );
                            const data = await response.json();
                            setStatTeamsMatch(data);
                        } catch (error) {
                            console.error("Ошибка при получении статистики команд в матче", error);
                        } finally {
                            setLoading(false);
                        }
                    }
                    const getStatPlayersMatch = async () => {
                        try {
                            const response = await fetch(
                                `${process.env.REACT_APP_BASE_URL}/main/getStatPlayersMatch`,
                                {
                                    method: "POST",
                                    headers: {
                                        "Content-Type": "application/json",
                                    },
                                    body: JSON.stringify(id),
                                }
                            );
                            const data = await response.json();
                            setStatPlayersMatchStat(data);
                        } catch (error) {
                            console.error("Ошибка при получении статистики игроков в матче", error);
                        }
                    }
                    if (id) {
                        getStatMatches();
                        getStatPlayersMatch();
                        getStatTeamsMatch();
                    }
                } else {
                    const getStatMatchFuture = async () => {
                        try {
                            setLoading(true);
                            const response = await fetch(`${process.env.REACT_APP_BASE_URL}/predict/getStatMatchFuture/${id}`, {
                                method: "GET",
                                headers: {
                                    "Content-Type": "application/json",
                                }
                            });
                            const data = await response.json();
                            setStatMatches(data);
                        } catch (error) {
                            console.error("Ошибка при получении матчей", error);
                        }
                    }
                    const getStatTeamsMatchFuture = async () => {
                        try {
                            const response = await fetch(
                                `${process.env.REACT_APP_BASE_URL}/predict/getStatTeamsMatchFuture`,
                                {
                                    method: "POST",
                                    headers: {
                                        "Content-Type": "application/json",
                                    },
                                    body: JSON.stringify(id),
                                }
                            );
                            const data = await response.json();
                            setStatTeamsMatch(data);
                        } catch (error) {
                            console.error("Ошибка при получении статистики команд в матче", error);
                        } finally {
                            setLoading(false);
                        }
                    }
                    const getStatPlayersMatchFuture = async () => {
                        try {
                            const response = await fetch(
                                `${process.env.REACT_APP_BASE_URL}/predict/getStatPlayersMatchFuture`,
                                {
                                    method: "POST",
                                    headers: {
                                        "Content-Type": "application/json",
                                    },
                                    body: JSON.stringify(id),
                                }
                            );
                            const data = await response.json();
                            setStatPlayersMatchStat(data);
                        } catch (error) {
                            console.error("Ошибка при получении статистики игроков в матче", error);
                        }
                    }
                    if (id) {
                        getStatMatchFuture();
                        getStatPlayersMatchFuture();
                        getStatTeamsMatchFuture();
                    }
                }
            })
    }, [id]);

    if (loading) {
        return <div className="loading">Загрузка...</div>;
    }

    function handlePlayer (id) {
        navigate(`/statPlayer/${id}`);
    }
    function handleTeam (id) {
        navigate(`/statTeam/${id}`);
    }

    let prefix1 = 0;
    let prefix2 = 1;
    if (statTeamsMatch?.[0].idTeam.name !== statPlayersMatchStat?.teamName1) {
        prefix1 = 1;
        prefix2 = 0;
    }
    return (
        <div>
            <Header/>
            <h1>StatMatch</h1>
            <div className="statMatch">
                {statMatches ? (
                    <div>
                        <span>leadChanges: {statMatches.leadChanges}</span>
                        <span>longestRun: {statMatches.longestRun}</span>
                        <span>timesTied: {statMatches.timesTied}</span>
                        <span>winner: {statMatches.winnersName}</span>
                    </div>
                ) : (
                    <p>...</p>
                )}
            </div>
            <div className="statTeam1">
                {statPlayersMatchStat && statTeamsMatch ? (
                    <div>
                        <div className="teamName">
                            {statPlayersMatchStat?.teamName1}
                        </div>
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
                                <th>Statistics</th>
                            </tr>
                            </thead>
                            <tbody>
                            {statPlayersMatchStat?.statPlayersTeam1?.map((team, index) => (
                                <tr key={team.id || index}>
                                    <td>{team.idPlayer.name}</td>
                                    <td>{team.ast}</td>
                                    <td>{team.blk}</td>
                                    <td>{(team.fg * 100).toFixed(1)}%</td>
                                    <td>{(team.ft * 100).toFixed(1)}%</td>
                                    <td>{team.min}</td>
                                    <td>{team.pts}</td>
                                    <td>{team.reb}</td>
                                    <td>{team.stl}</td>
                                    <td>{(team.threePoints * 100).toFixed(1)}%</td>
                                    <td>{team.tov}</td>
                                    <td>
                                        <button onClick={() => handlePlayer(team?.idPlayer.id)}
                                                className="statistics-button">
                                            Статистика игрока
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            <tr>
                                <td></td>
                                <td>{statTeamsMatch?.[prefix1].ast}</td>
                                <td>{statTeamsMatch?.[prefix1].blk}</td>
                                <td>{(statTeamsMatch?.[prefix1].fg * 100).toFixed(1)}%</td>
                                <td>{(statTeamsMatch?.[prefix1].ft * 100).toFixed(1)}%</td>
                                <td>-</td>
                                <td>{statTeamsMatch?.[prefix1].pts}</td>
                                <td>{statTeamsMatch?.[prefix1].reb}</td>
                                <td>{statTeamsMatch?.[prefix1].stl}</td>
                                <td>{(statTeamsMatch?.[prefix1].threePoints * 100).toFixed(1)}%</td>
                                <td>{statTeamsMatch?.[prefix1].tov}</td>
                                <td>
                                    <button onClick={() => handleTeam(statTeamsMatch?.[prefix1]?.idTeam.id)}
                                            className="statistics-button">
                                        Статистика команды
                                    </button>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                ) : (
                    <p>...</p>
                )}
            </div>
            <div className="statTeam2">
                {statPlayersMatchStat && statTeamsMatch ? (
                    <div>
                    <div className="teamName">
                            {statPlayersMatchStat?.teamName2}
                        </div>
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
                                <th>Statistics</th>
                            </tr>
                            </thead>
                            <tbody>
                            {statPlayersMatchStat?.statPlayersTeam2?.map((team, index) => (
                                <tr key={team.id || index}>
                                    <td>{team.idPlayer.name}</td>
                                    <td>{team.ast}</td>
                                    <td>{team.blk}</td>
                                    <td>{(team.fg * 100).toFixed(1)}%</td>
                                    <td>{(team.ft * 100).toFixed(1)}%</td>
                                    <td>{team.min}</td>
                                    <td>{team.pts}</td>
                                    <td>{team.reb}</td>
                                    <td>{team.stl}</td>
                                    <td>{(team.threePoints * 100).toFixed(1)}%</td>
                                    <td>{team.tov}</td>
                                    <td>
                                        <button onClick={() => handlePlayer(team?.idPlayer.id)}
                                                className="statistics-button">
                                            Статистика игрока
                                        </button>
                                    </td>
                                </tr>
                            ))}
                            <tr>
                                <td></td>
                                <td>{statTeamsMatch?.[prefix2].ast}</td>
                                <td>{statTeamsMatch?.[prefix2].blk}</td>
                                <td>{(statTeamsMatch?.[prefix2].fg * 100).toFixed(1)}%</td>
                                <td>{(statTeamsMatch?.[prefix2].ft * 100).toFixed(1)}%</td>
                                <td>-</td>
                                <td>{statTeamsMatch?.[prefix2].pts}</td>
                                <td>{statTeamsMatch?.[prefix2].reb}</td>
                                <td>{statTeamsMatch?.[prefix2].stl}</td>
                                <td>{(statTeamsMatch?.[prefix2].threePoints * 100).toFixed(1)}%</td>
                                <td>{statTeamsMatch?.[prefix2].tov}</td>
                                <td>
                                    <button onClick={() => handleTeam(statTeamsMatch?.[prefix2]?.idTeam.id)}
                                            className="statistics-button">
                                        Статистика команды
                                    </button>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                ) : (
                    <p>...</p>
                )}
            </div>
        </div>
    );

}


export default StatMatch;