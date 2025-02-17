import {useEffect, useMemo, useState} from "react";
import { useTable, usePagination } from 'react-table';

const Matches = () => {
    const [matches, setMatches] = useState([]);

    useEffect(() => {
        const getMatches = async () => {
            try {
                const response = await fetch(`${process.env.REACT_APP_BASE_URL}/matches`, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                    }
                });
                console.log(response);
            } catch (error) {
                console.error("Ошибка при получении матчей", error);
            }
        }
        getMatches();
    }, []);

    const columns = useMemo(() => [
        { Header: 'id', accessor: 'id' },
        { Header: 'owner', accessor: 'owner'},
        { Header: 'name', accessor: 'name'},
        { Header: 'coordinate-x', accessor: 'coordinateX' },
        { Header: 'coordinate-y', accessor: 'coordinateY' },
        { Header: 'creationDate', accessor: 'creationDate' },
        { Header: 'area', accessor: 'area' },
        { Header: 'numberOfRooms', accessor: 'numberOfRooms'},
        { Header: 'price', accessor: 'price' },
        { Header: 'balcony', accessor: 'balcony', Cell: ({ value }) => value ? 'Есть' : 'Нет' },
        { Header: 'timeToMetroOnFoot', accessor: 'timeToMetroOnFoot' },
        { Header: 'furnish', accessor: 'furnish' },
        { Header: 'view', accessor: 'view' },
        { Header: 'transport', accessor: 'transport' },
        { Header: 'house-name', accessor: 'houseName' },
        { Header: 'house-year', accessor: 'houseYear' },
        { Header: 'house-numberOfFloors', accessor: 'houseNumberOfFloors' },
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
        { columns, data: matches, initialState: { pageIndex: 0, pageSize: 15 }},
        usePagination,
    );

    const { pageIndex } = state;

    return (
        <div>
            <h1>Matches</h1>
            <div className="flats-table-wr">
                <table {...getTableProps()} className="flats-table">
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