import { useState, useEffect } from 'react';
import { Container } from '@mui/material';

import './App.css';
import wikiraceServices from './services/wikirace';
import WikiRaceForm from './components/WikiRaceForm';
import HistoryTable from './components/HistoryTable';

function App() {
  const [wikiRaces, setWikiRaces] = useState([]);
  const [newStart, setNewStart] = useState("");
  const [newTarget, setNewTarget] = useState("");
  const [newWikiRaceDisabled, setNewWikiRaceDisabled] = useState(false);
  const [wikiRaceFailed, setWikiRaceFailed] = useState(false);

  useEffect(() => {
    fetchWikiRaces(); // function called everytime the browser is loaded/reloaded
  }, []);

  async function fetchWikiRaces() {
    const wikiRaces = await wikiraceServices.getAll();
    console.log(wikiRaces);

    wikiRaces.forEach((race) => {
      const date = new Date(race.data.startTime);
      race.data.startTime = date.toLocaleTimeString();
    })

    setWikiRaces(wikiRaces.reverse());
  }

  async function startWikiRace(newWikiRace) {
    try {
      const response = await wikiraceServices.start(newWikiRace);
      if (response.status === 202) {
        setNewWikiRaceDisabled(true);
        cleanupInputs();
        fetchWikiRaces();
      }

      pollWikiRaceProgress(response.headers['location']);

    } catch (error) {
      setWikiRaceFailed(true);
      console.log(error.toJSON());
    }
  }

  async function pollWikiRaceProgress(wikiRaceId) {
    const polling = new Promise((resolve) => {
      let completed = false;
      setInterval(async () => {
        if (completed) { return }

        const response = await wikiraceServices.get(wikiRaceId);
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

    fetchWikiRaces();
    setNewWikiRaceDisabled(false);
  }

  function startButtonClicked(event) {
    event.preventDefault();
    setWikiRaceFailed(false);

    const wikiRaceAttempted = { start: newStart, target: newTarget };
    startWikiRace(wikiRaceAttempted);
  }

  function handleInputChange(event) {
    const changedValue = event.target.value;
    switch (event.target.name) {
      case 'start':
        setNewStart(changedValue);
        break;
      case 'target':
        setNewTarget(changedValue);
        break;
      default:
        console.error('Input field doesn\'t have a name field');
    }
  }

  function cleanupInputs() {
    setNewStart("");
    setNewTarget("");
  }

  return (
    <Container sx={{textAlign: 'center', fontFamily: 'sans-serif'}}>

      <h1>Wikiracing</h1>

      <WikiRaceForm
        startButtonClicked={startButtonClicked}
        newStart={newStart}
        newTarget={newTarget}
        handleInputChange={handleInputChange}
        newWikiRaceDisabled={newWikiRaceDisabled}
      />

      <HistoryTable rows={wikiRaces} />

    </Container>
  );
}

export default App;
