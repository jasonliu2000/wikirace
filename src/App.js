import { useState, useEffect } from 'react';
import { Box, Container, Typography, } from '@mui/material';

import './App.css';
import wikiraceServices from './services/wikirace';
import Intro from './components/Intro';
import WikiRaceForm from './components/WikiRaceForm';
import HistoryTable from './components/HistoryTable';

function App() {
  const [wikiRaces, setWikiRaces] = useState([]);
  const [watchNewRace, setWatchNewRace] = useState(false);

  useEffect(() => {
    fetchWikiRaces(); // function called everytime the browser is loaded/reloaded and everytime setWatchNewRace() is called
  }, [watchNewRace]);

  async function fetchWikiRaces() {
    const wikiRaces = await wikiraceServices.getAll();
    console.log(wikiRaces);

    wikiRaces.forEach((race) => {
      const date = new Date(race.data.startTime);
      race.data.startTime = date.toLocaleTimeString();
    })

    setWikiRaces(wikiRaces.reverse());
  }

  function watchWikiRace(id) {
    setWatchNewRace(true);
    pollWikiRaceProgress(id);
  }

  async function pollWikiRaceProgress(id) {
    const polling = new Promise((resolve) => {
      let completed = false;
      setInterval(async () => {
        if (completed) { return }

        const response = await wikiraceServices.get(id);
        console.log(response);
        
        if (response.status === 'COMPLETED') {
          completed = true;
          clearInterval(polling);
          resolve('')
        } 
      }, 500)
    })

    const completed = await polling.catch((err) => {
      console.error(err);
      return 'there was an issue with completing the wikirace';
    });

    setWatchNewRace(false);
    // setNewWikiRaceDisabled(false);
  }

  return (
    <Box
      display="flex"
      flexDirection="column"
      minHeight="100vh"
      sx={{mx: '50px', justifyContent: 'space-between' }}
    >
      <Box sx={{textAlign: 'center', justifyContent: 'flex-start'}} flex="1">

        <Intro />

        <WikiRaceForm followWikiRace={watchWikiRace}
          // newWikiRaceDisabled={newWikiRaceDisabled}
        />

        {wikiRaces.length > 0 && <HistoryTable rows={wikiRaces} />}

      </Box>

      <Box component="footer">
        <Typography>For more information about Wikiracing, please click <a target="_blank" rel="noreferrer" href="https://en.wikipedia.org/wiki/Wikiracing">here</a></Typography>
      </Box>
    </Box>
  );
}

export default App;
