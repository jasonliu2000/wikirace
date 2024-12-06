import { useState, useEffect } from 'react';
import { Box, Typography, } from '@mui/material';

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
    try {
      const races = await wikiraceServices.getAll();
      console.log(races);

      races.forEach((race) => {
        const date = new Date(race.data.startTime);
        race.data.startTime = date.toLocaleTimeString();
      });
  
      setWikiRaces(races.reverse());

    } catch (error) {
      console.error(error);
    }
  }

  function watchWikiRace(id) {
    setWatchNewRace(true);
    pollWikiRaceProgress(id);
  }

  async function pollWikiRaceProgress(id) {
    let interval;
    const polling = new Promise((resolve, reject) => {
      interval = setInterval(async () => {
        try {
          const wikirace = await wikiraceServices.get(id);
          console.log(wikirace);

          if (wikirace.status === 'COMPLETED') {
            clearInterval(interval);
            resolve();
          } 
        } catch (error) {
          // console.error(error);
          clearInterval(interval);
          reject(error);
        }
      }, 500)
    })

    try {
      await polling;
    } catch (error) {
      console.error(error);
    } finally {
      setWatchNewRace(false);
      // setNewWikiRaceDisabled(false);
    }

  }

  return (
    <Box
      display="flex"
      flexDirection="column"
      minHeight="100vh"
      sx={{mx: '50px', justifyContent: 'space-between' }}
    >
      <Box sx={{textAlign: 'center', justifyContent: 'flex-start', flexWrap: 'wrap'}} flex="1">

        <Intro />

        <WikiRaceForm followWikiRace={watchWikiRace}
          // newWikiRaceDisabled={newWikiRaceDisabled}
        />

        {wikiRaces.length > 0 && <HistoryTable rows={wikiRaces} waitForCompletion={watchNewRace} />}

      </Box>

      <Box component="footer">
        <Typography>For more information about Wikiracing, please click <a target="_blank" rel="noreferrer" href="https://en.wikipedia.org/wiki/Wikiracing">here</a></Typography>
      </Box>
    </Box>
  );
}

export default App;
