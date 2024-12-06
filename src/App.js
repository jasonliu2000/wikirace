import { useState, useEffect } from 'react';
import { Box, Typography, Link } from '@mui/material';

import backgroundStyles from './styles/backgroundStyles';
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
    }

  }

  return (
    <Box
      display="flex"
      flexDirection="column"
      minHeight="100vh"
      sx={{mx: '50px', justifyContent: 'space-between', textAlign: 'center'}}
    >
      <Box sx={{justifyContent: 'flex-start', flexWrap: 'wrap'}} flex="1">

        <Intro/>

        <WikiRaceForm followWikiRace={watchWikiRace}/>

        {wikiRaces.length > 0 && <HistoryTable rows={wikiRaces}/>}

      </Box>

      <Box component="footer">
        <Typography color="gray" sx={backgroundStyles}>
          <i>For more information about Wikiracing, please click&nbsp;
            <Link 
              href="https://en.wikipedia.org/wiki/Wikiracing" 
              target="_blank" 
              rel="noreferrer" 
              underline="none"
            >
              here
            </Link>
          </i>
        </Typography>
      </Box>
    </Box>
  );
}

export default App;
