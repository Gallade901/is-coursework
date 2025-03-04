import React, {useEffect, useMemo, useState} from "react";
import { useTable, usePagination } from 'react-table';
import { useNavigate } from 'react-router-dom';
import Header from "./Header";

const Matches = () => {
    const navigate = useNavigate();
    const [matches, setMatches] = useState([]);
    const [dateFilter, setDateFilter] = useState("");


    useEffect(() => {
        const getMatches = async () => {
            try {
                const response = await fetch(`${process.env.REACT_APP_BASE_URL}/main/getMatches`, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                    }
                });
                const data = await response.json();
                setMatches(data);
            } catch (error) {
                console.error("Ошибка при получении матчей", error);
            }
        }
        getMatches();
    }, []);

    function handleEdit (id) {
        navigate(`/statMatches/${id}`);
    }

    const filteredData = useMemo(() => {
        if (!matches) return [];
        return matches.filter(match =>
            match.date.toLowerCase().includes(dateFilter.toLowerCase())
        );
    }, [matches, dateFilter]);

    const columns = useMemo(() => [
        { Header: 'id', accessor: 'id' },
        { Header: 'location', accessor: 'location'},
        { Header: 'date', accessor: 'date',  Cell: ({ value }) => value?.slice(0, 10) || ''},
        { Header: 'team-1', accessor: 'team1.name' },
        { Header: 'team-2', accessor: 'team2.name' },
        { Header: 'statistics',
            Cell: ({ row }) => (
                <button onClick={() => handleEdit(row.original.id)} className="statistics-button">
                    Смотреть статистику
                </button>
            )}
    ], []);

    const {
        getTableProps,
        getTableBodyProps,
        headerGroups,
        page,
        nextPage,
        previousPage,
        canNextPage,
        canPreviousPage,
        pageOptions,
        state,
        prepareRow,
    } = useTable(
        { columns, data: filteredData, initialState: { pageIndex: 0, pageSize: 15 }},
        usePagination,
    );

    const { pageIndex } = state;

    return (
        <div>
            <Header/>
            <h1>Matches</h1>
            <input
                type="text"
                placeholder="Фильтр по date"
                value={dateFilter}
                onChange={(e) => setDateFilter(e.target.value)}
            />
            <div className="matches-table-wr">
                <table {...getTableProps()} className="matches-table">
                    <thead>
                    {headerGroups.map(headerGroup => (
                        <tr {...headerGroup.getHeaderGroupProps()}>
                            {headerGroup.headers.map(column => (
                                <th {...column.getHeaderProps()}>{column.render('Header')}</th>
                            ))}
                        </tr>
                    ))}
                    </thead>
                    <tbody {...getTableBodyProps()}>
                    {page.map(row => {
                        prepareRow(row);
                        return (
                            <tr {...row.getRowProps()}>
                                {row.cells.map(cell => (
                                    <td {...cell.getCellProps()}>{cell.render('Cell')}</td>
                                ))}
                            </tr>
                        );
                    })}
                    </tbody>
                </table>
            </div>
            <div className="pagination">
                <button onClick={() => previousPage()} disabled={!canPreviousPage}>
                    Previous
                </button>
                <span>Page{' '}
                    <strong>
                    {pageIndex + 1} of {pageOptions.length}
                  </strong>{' '}
                </span>
                <button onClick={() => nextPage()} disabled={!canNextPage}>
                    Next
                </button>
            </div>
        </div>
    );

}


export default Matches;