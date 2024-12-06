import { TableContainer, Table, TableHead, TableBody, TableRow, TableCell, CircularProgress } from "@mui/material";

const HistoryTable = ({ rows }) => {

  const lastColumn = (wikirace) => {
    if (wikirace.data.timeToCompletionMilliseconds) {
      return (<b>{wikirace.data.timeToCompletionMilliseconds}</b>);
    }

    if (wikirace.status === 'IN_PROGRESS') {
      return (<CircularProgress size="20px" color="black"/>);
    }

    return (<b style={{color: '#DC2626'}}>Failed</b>);
  }

  return (
    <TableContainer>
      <Table sx={{ minWidth: 300 }} aria-label="Table of Past Wikiraces">
        <TableHead>
          <TableRow>
            <TableCell>Time Started</TableCell>
            <TableCell>Start</TableCell>
            <TableCell>Target</TableCell>
            <TableCell align="right">Time Taken (ms)</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {rows.map((row) => (
            <TableRow key={row.id} sx={{ '&:last-child td, &:last-child th': { border: 0 } }} >
              <TableCell component="th" scope="row">
                {row.data.startTime}
              </TableCell>
              <TableCell>{row.data.start}</TableCell>
              <TableCell>{row.data.target}</TableCell>
              <TableCell align="right">
                {lastColumn(row)}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}

export default HistoryTable;