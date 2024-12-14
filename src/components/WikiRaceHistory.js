import { useState } from 'react';
import { TableContainer, Table, TableHead, TableBody, TableRow, TableCell, CircularProgress, IconButton, Collapse, Box, Typography } from '@mui/material';
import { KeyboardArrowUp, KeyboardArrowDown } from '@mui/icons-material';

const WikiRaceRow = ({ wikiRace }) => {
  const [open, setOpen] = useState(false);

  const lastColumn = () => {
    if (wikiRace.data.timeToCompletionMilliseconds) {
      return (<b>{wikiRace.data.timeToCompletionMilliseconds}</b>);
    }

    if (wikiRace.status === 'IN_PROGRESS') {
      return (<CircularProgress size="20px" color="black"/>);
    }

    return (<b style={{color: '#DC2626'}}>Failed</b>);
  }

  const transitionIn = () => {
    return open && wikiRace.data.pathToTarget;
  }

  const pathComponent = (prev, next) => {
    return (
      <>
        <b>{prev}</b>, <b>{next}</b>
      </>
    )
  }

  return (
    <>
      <TableRow key={wikiRace.id} sx={{ '&:last-child td, &:last-child th': { border: 0 } }} >
        <TableCell>
          <IconButton
            aria-label="expand row"
            size="small"
            onClick={() => setOpen(!open)}
          >
            {open ? <KeyboardArrowDown /> : <KeyboardArrowUp />}
          </IconButton>
        </TableCell>
        <TableCell component="th" scope="row">
          {wikiRace.data.startTime}
        </TableCell>
        <TableCell>{wikiRace.data.start}</TableCell>
        <TableCell>{wikiRace.data.target}</TableCell>
        <TableCell align="right">
          {lastColumn(wikiRace)}
        </TableCell>
      </TableRow>
      <TableRow>
        <TableCell style={{ paddingBottom: 0, paddingTop: 0 }} colSpan={5}>
          <Collapse in={transitionIn()} timeout="auto" unmountOnExit>
            <Box sx={{ margin: 1, textAlign: 'center' }}>
              <Typography variant="body2" gutterBottom component="div">
               Path to Target: {wikiRace.data.pathToTarget?.reduce(pathComponent)}
              </Typography>
            </Box>
          </Collapse>
        </TableCell>
      </TableRow>
    </>
  )
} 

const WikiRaceTable = ({ wikiRaces }) => {
  return (
    <TableContainer>
      <Table sx={{ minWidth: 300 }} aria-label="Table of Past Wikiraces">
        <TableHead>
          <TableRow>
            <TableCell />
            <TableCell>Time Started</TableCell>
            <TableCell>Start</TableCell>
            <TableCell>Target</TableCell>
            <TableCell align="right">Time Taken (ms)</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {wikiRaces.map((wikiRace) => <WikiRaceRow wikiRace={wikiRace}/>)}
        </TableBody>
      </Table>
    </TableContainer>
  );
}

export default WikiRaceTable;