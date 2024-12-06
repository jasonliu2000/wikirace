import { TableContainer, Table, TableHead, TableBody, TableRow, TableCell, CircularProgress } from "@mui/material";

const HistoryTable = ({ rows, waitForCompletion }) => {

  const lastColumn = (timeToCompletion, waiting) => {
    if (!waiting && !timeToCompletion) {
      return (<b style={{color: '#DC2626'}}>Failed</b>);
    }

    if (timeToCompletion) {
      return (<b>{timeToCompletion}</b>);
    }

    return (<CircularProgress size="20px" color="black"/>);
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
                {lastColumn(row.data.timeToCompletionMilliseconds, waitForCompletion)}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}

export default HistoryTable;