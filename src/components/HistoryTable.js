import { TableContainer, Table, TableHead, TableBody, TableRow, TableCell } from "@mui/material";

const HistoryTable = ({ rows }) => {
  return (
    <TableContainer>
      <Table sx={{ minWidth: 650 }} aria-label="Table of Past Wikiraces">
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
                <b>{row.data.elapsedTimeMilliseconds}</b>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
}

export default HistoryTable;